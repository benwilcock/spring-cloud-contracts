package io.pivotalservices.fraud;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.core.Is.is;
import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * This test class shows how to use Spring Rest Docs to document service calls based on real testing code.
 * Documentation was here: http://docs.spring.io/spring-restdocs/docs/current/reference/html5/#getting-started
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SpringRestDocsTest {

    @Rule
    public JUnitRestDocumentation restDocumentation =
            new JUnitRestDocumentation("build/generated-snippets");

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @Before
    public void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context)
                .apply(documentationConfiguration(this.restDocumentation))
                .build();
    }

    /**
     * This method illustrates how the mockMvc framework can be used to perform tests and also how the
     * same framework is extended by Spring Rest Docs in order to add documentation to the project based on
     * the code, requests and responses present in the test case.
     * @throws Exception
     */
    @Test
    public void shouldRejectWhenAmountIsTooHigh() throws Exception{

        // Given
        FraudCheck check = new FraudCheck("1234567890", new BigDecimal(5001));

        // When
        this.mockMvc.perform(
                put("/fraudcheck")
                    .contentType("application/vnd.fraud.v1+json")
                    .content(asJsonString(check))
                )

        // Then

                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", containsString("application/vnd.fraud.v1+json")))
                .andExpect(jsonPath("$.resultText", is("Amount too high")))
                .andExpect(jsonPath("$.fraudCheckStatus", is("FRAUD")))

        // Finally - Write the RestDocs

                .andDo(document("index",
                        requestHeaders(
                                headerWithName("Content-Type").description("application/vnd.fraud.v1+json")
                        ),

                        requestFields(
                                fieldWithPath("clientId").description("The ID of the customer."),
                                fieldWithPath("loanAmount").description("The amount they have requested to borrow.")),

                        responseHeaders(
                                headerWithName("Content-Type").description("application/vnd.fraud.v1+json")
                        ),

                        responseFields(
                                fieldWithPath("fraudCheckStatus").description("The fraud check STATUS CODE.").type(JsonFieldType.STRING),
                                fieldWithPath("resultText").description("The human readable description of the fraud check STATUS CODE.").type(JsonFieldType.STRING))
                        )
                )
        ;

    }

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
