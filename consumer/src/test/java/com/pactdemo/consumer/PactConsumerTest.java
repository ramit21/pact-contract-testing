package com.pactdemo.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pactdemo.consumer.controller.DepartmentController;
import com.pactdemo.consumer.model.Department;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import au.com.dius.pact.consumer.MockServer;
import au.com.dius.pact.consumer.dsl.PactDslJsonArray;
import au.com.dius.pact.consumer.dsl.PactDslJsonBody;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.consumer.junit5.PactConsumerTestExt;
import au.com.dius.pact.consumer.junit5.PactTestFor;
import au.com.dius.pact.core.model.RequestResponsePact;
import au.com.dius.pact.core.model.annotations.Pact;

@SpringBootTest
@ExtendWith(PactConsumerTestExt.class)
@PactTestFor(providerName = "DepartmentCatalogue")
public class PactConsumerTest {

    @Autowired
    private DepartmentController departmentController;

    @Pact(consumer="EmployeeCatalogue")
    public RequestResponsePact PactAllEmployees(PactDslWithProvider builder)
    {
        return builder.given("employee exists")
                .uponReceiving("fetching all employee details")
                .path("/getAllEmployees")
                .willRespondWith()
                .status(200)
                .body(PactDslJsonArray.arrayMinLike(3) //return 3 responses in the array
                        .stringType("name") //values are optional, mostly which only test the contract, when left blank, dummy values are created
                        .integerType("salary", 10)
                        .integerType("departmentId", 1)
                        .closeObject()).toPact();
    }

    @Test
    @PactTestFor(pactMethod="PactAllEmployees",port = "9999")
    public void testAllSalariesSum(MockServer mockServer) throws JsonMappingException, JsonProcessingException {

        String expectedJson ="{\"departmentId\":1,\"totalEmployeeSalary\":30}";
        departmentController.setBaseUrl(mockServer.getUrl());

        Department dept = departmentController.getTotalSalaryBydept(1L);
        ObjectMapper obj = new ObjectMapper();
        String jsonActual = obj.writeValueAsString(dept);
        System.out.println("jsonActual : "+ jsonActual);
        Assertions.assertEquals(expectedJson, jsonActual);

    }

}
