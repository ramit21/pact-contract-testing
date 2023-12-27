# pact-contract-testing
Contract testing between microservices using Pact

## What is contract testing
Microservices integrate as per a contract, which is an agreed 
payload structure that a producer app would return to the consumer app. But if the producer
 app changes the data model keys, it breaks the contract. This doesn't get caught in unit 
testing as both applications are testing in isolation. When the bug is found in integration testing
the cost of fixing the bug is a lot more than if it was found in unit testing itself.

As per the "testing pyramid", unit test cases are at the bottom, integration tests are in the middle, and
end-to-end tests are at the top. ie. We should have many numbers of unit test cases, followed by a few
integration tests and a few end-to-end tests. Most of the issues should be caught at the unit
test level itself. This is where contract testing helps.

Using Pact, the consumer app writes a pact test and defines what fields it expects in the
response from the producer app (it may not cover all data points in the producer response, and 
only checks for the ones that it consumes). Test cases are then written around this contract,
and this generates a 'PACT file'. It is a JSON file that covers all details about the contract 
 and all tests written around it. This PACT file is then shared with the producer app, which
 then verifies the contract. If the contract is broken, (eg. the producer changed the data model 
 field salary to sal), the unit test case will fail. Hence the producer would know at the unit testing
 stage itself that it is breaking the agreed contract. 
 
 When you run the unit test case on the consumer side, PACT creates a pact server as per the configurations
  and helps in mocking the API response when the code hits the point of the API call.


## Project Setup
Import two maven spring boot applications -consumer and producer, and run them.

Note that both projects use JDK 11, to be compatible with JUnit 5 and Pact v4.

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
You may now switch the applications off, and run PactConsumerTest.java as JUnit test.
Once the test is executed, the pact json contract will be created under the target/pact folder.
Take this file and keep it in the producer project under src/main/java/pacts 
(one such generated file is already present in this project; you may directly run the
verification test as mentioned next).

Now run PactProviderTest.java as JUnit test case, and this would then verify the contract.
Now try changing salary to sal in Employee dto in the producer, and rerun the test case.
This time it will show you unit test contract failure.

If ou do not want to load entire spring context in the test case, then follow the approach of mocking the
controller using static class at provider side as given below. 

https://docs.pact.io/implementation_guides/jvm/provider/junit5spring

And on consumer side, simply remove @SpringBootApplication/@Autowired annotations, and create 
the calling client's object directly and then call the fetch method on it.

## Important files and configurations

The producer app returns all employees' data. The consumer app returns data of the department,
which is the department ID being passed in API, and the total salary of all employees
in that department. For this purpose, the consumer app makes a rest call to the producer
app to fetch all employees and then sums up the salaries.

Pact setup: notice the junit5 and Pact dependencies in pom.xmls. For junit5 to work,
 you need to exclude any junit 4 dependencies. See the exclusions for the spring-boot-starter-test.
 
See **PactConsumerTest.java** on configuring a PACT contract and a test using junit5 annotations.
 Note how pact test cases point to mocked pact response methods, and how we set the base URL 
 in the controller as per the mock pact server being launched when the test is run.
 This way, you can mock different responses from the server, like 500/404 errors as well,
 and accordingly write your test cases. All these tests will be captured in the pact contract file, 
 which will then be verified at the producer side.
 
When we run the above test case, it generates a JSON file under target/pacts which contains
all information on the contract. This file is then shared with the producer app to verify the contract.

**PactProviderTest.java** is the unit test on the producer side that verifies the contract.
@LocalServerPort is used to capture the random port used to run the mock pact server when 
 running this Junit test. This class also has @State method hooks in case you want to perform some operations before verifying 
the contract. The Provider name here should match the one given in the consumer side test case.

 **@State**
 
Also note the builder.given() takes a state as an argument in the consumer test. 
You can then use this state name in the producer test case along with @State. 
 For eg, if the state is that a certain employee  does not exist, and the test
 case on the provider side is written around that, then using @State at the producer side, 
 you can create a state method where you would remove that particular employee
 so that contract verification would pass.
 
## pactflow.io

In this POC, we are manually copying and pasting the pact file from the consumer to the provider app.
In real-world projects, different microservices are managed by different teams.
Hence such sharing is difficult. A better solution is to use Pact flow.
When you create an account on pactflow.io, you will get a project URL and a token to connect.
On the producer side, configure the pact provider plugin with the URL and token details (shown as commented code).
Then run the command 'mvn pact:publish' which would push the pact file onto the pact flow project.

On the provider side, instead of giving a path to the pact folder, you give the PactBroker url along 
with the token (as shown in the commented code of PactProviderTest.java. This test case 
would then fetch the file from pact flow and validate it.


