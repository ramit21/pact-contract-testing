# pact-contract-testing
Contract testing between microservices using Pact

## What is contract testing
Microservices integrate with each other as per a contract, which is basically an agreed 
payload structure that a producer app would return to the consumer app. But if the producer
 app changes the data model keys, it breaks the contract. This doesn't get caught in unit 
testing as both applications are testing in isolation. When the bug is found in integration testing
the cost of fixing the bug is lot more than if it was found in unit testing itself.

As per "testing pyramid", unit test cases are at bottom, integration tests in middle, and
end to end tests at the top. ie. we should have many numbers of unit test cases, followed by few
integration tests and little end to end tests. Most of the issues should be caught at unit
test level itself. This is where contract testing helps.

Using Pact, the consumer app writes a pact test, and defines what fields it expects in the
response from the producer app (it may not cover all data points in producer response, and 
only check for the ones which it consumes). Test cases are then written around this contract,
and this generates a 'PACT file'. It is a json file which covers all details about the contract 
 and all tests written around it. This PACT file is then shared with the consumer app, which
 then verfies the contract. If the contract is broken, (eg. producer changed data mdoel 
 field salary to sal), the unit test case would fail. Hence the producer would know at unit testing
 stage itself that it is breaking the agreed contract. 
 
 When you run the unit test case on consumer side, PACT creates a pact server as per the configurations
  and helps in mocking the API response when the code hits the point of api call.


## Project Setup
Import two maven spring boot applciations -consumer and producer, and run them.

Note that both projects use JDK 11, to be compatible with Junit 5 and Pact v4.

Make sure applications are up:

Consumer:
```
http://localhost:8100
http://localhost:8100/getAllEmployees
```

Producer:
```
http://localhost:8080
http://localhost:8080/getTotalSalaryByDeptId/1
```
You may now switch the applications off, and run PactConsumerTest.java as junit test.
Once test is executed, pact json contract will be created under target/pact folder.
Take this file and keep it in the producer project under src/main/java/pacts 
(one such generated file is already present in this project, hence you may directly run the
verification test as mentioned next).
Now run <> as junit test case, and this would then verify the contract.
Now try changing salary to sal in Employee dto in the producer, and run the test case again.
This time it will show you unit test contract failure.


## Important files and configurations

Producer app returns all employees data. Consumer app returns data of department,
which is the department id being passed in api, and the total salary of all employees
in that department. For this purpose the consumer app makes a rest call to the producer
app to fetch all employees, and then sums up the salaries.

Pact setup: notice the junit5 and Pact dependencies in pom.xmls. For junit5 to work,
 you need to exclude any junit 4 dependencies. See the exclusions for spring-boot-starter-test.
 
See **PactConsumerTest.java** on how to configure a PACT contract and a test using junit5 annotations.
 Note how pact test cases point to mocked pact response methods, and how we set the base url 
 in the controller as per the mock pact server being launched when the test is run.
 This way, you can mock different responses from the server, like 500/404 errors as well,
 and accoridngly write your test cases. All these tests will be captured in pact contract file, 
 which would then be verified at the producer side.
 
When we run above test case, it generates a json file under target/pacts which cotnains
all information on the contract. This file is then shared with the producer app to verify the contract.

**PactProviderTest.java** is the unit test on the producer side that verifies the contract.
@LocalServerPort is used to capture the random port used to run mock pact server when 
 running this junit test. This class also has @State method hooks in case you want to perform some operations before veriffying 
the contract. The Provider name here should match the one given in the consumer side test case.

 **@State**
 
Also note the builder.given() takes a state as an argument in consumer test. 
You can then use this state name in the producer test case along with @State. 
 For eg, if the state is that certain employee  does not exist, and the test
 case on provider side is written around that, then using @State at the producer side, 
 you can create state method where you would remove that particular employee
 so that contract verfication would pass.
 



