# pact-contract-testing
Contract testing between microservices using Pact




## Project Setup
Import two maven spring boot applciations -consumer and producer, and run them.

Note that both projects use JDK 11, as we are using Junit 5 in them which requires JKD to be v11.

Make sure applications are up:

Consumer:
```
http://localhost:8100
http://localhost:8100/getAllEmployees
```

Producer:
```
http://localhost:8000
http://localhost:8000/getTotalSalaryByDeptId/1
```





