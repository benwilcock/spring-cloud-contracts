package contracts.fraud

org.springframework.cloud.contract.spec.Contract.make {
    request {
        method 'PUT'
        url '/fraudcheck'
        body("""
    {
      "clientId":"1234567890",
      "loanAmount":4999
    }
    """)
        headers {
            header('Content-Type', 'application/vnd.fraud.v1+json')
        }
    }
    response {
        status 200
        body("""
  {
    "fraudCheckStatus": "OK",
    "rejectionReason": ""
  }
  """)
        headers {
            header('Content-Type': 'application/vnd.fraud.v1+json;charset=UTF-8')
        }
    }
}