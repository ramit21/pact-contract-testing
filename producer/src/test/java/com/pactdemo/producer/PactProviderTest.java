package com.pactdemo.producer;

import au.com.dius.pact.provider.junit5.HttpTestTarget;
import au.com.dius.pact.provider.junit5.PactVerificationContext;
import au.com.dius.pact.provider.junit5.PactVerificationInvocationContextProvider;
import au.com.dius.pact.provider.junitsupport.Provider;
import au.com.dius.pact.provider.junitsupport.State;
import au.com.dius.pact.provider.junitsupport.StateChangeAction;
import au.com.dius.pact.provider.junitsupport.loader.PactFolder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestTemplate;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import java.util.Map;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Provider("EmployeeCatalogue")
@PactFolder("pacts")// path where pact json is kept
//@PactBroker(url="https://<my-account>.pactflow.io/",
//authentication= @PactBrokerAuth(token="<token name"))

public class PactProviderTest {

    @LocalServerPort
    public int port;

    @BeforeEach
    public void setup(PactVerificationContext context) {
        context.setTarget(new HttpTestTarget("localhost", port));
    }

    //Verify the contract file here
    @TestTemplate
    @ExtendWith(PactVerificationInvocationContextProvider.class)
    public void pactVerificationTest(PactVerificationContext context) {
        context.verifyInteraction();
    }

    @State(value = "employee exists", action = StateChangeAction.SETUP)
    public void employeeExist() {
        System.out.println("Setup: This state is mentioned in contract, hence this method will be called");
    }

    @State(value = "employee abc does not exist", action = StateChangeAction.SETUP)
    public void coursesExistTearDown() {
        System.out.println("Setup: Delete an employee if contract does not expect it to be present");
    }

    @State(value = "employee abc does not exist", action = StateChangeAction.TEARDOWN)
    public void appiumCourseExist() {
        System.out.println("Setup: Add the employee back on completion of test run");
    }
}

