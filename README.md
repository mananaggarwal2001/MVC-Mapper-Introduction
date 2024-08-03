### Spring MVC Customer Application
[![CircleCI](https://dl.circleci.com/status-badge/img/gh/mananaggarwal2001/MVC-Mapper-Introduction/tree/master.svg?style=svg)](https://dl.circleci.com/status-badge/redirect/gh/mananaggarwal2001/MVC-Mapper-Introduction/tree/master)
[![codecov](https://codecov.io/gh/mananaggarwal2001/MVC-Mapper-Introduction/graph/badge.svg?token=OY7XCCY8LN)](https://codecov.io/gh/mananaggarwal2001/MVC-Mapper-Introduction)
- This application is made using the mapStruct which is used to convert the Entity classes to the DTO or vice versa is possible.
- This is pure application that is made using the spring boot and spring MVC.
- This application passes all the tests and run properly on the system.
#### New Concepts
- How to Use Mapper for mapping the DTO with the entity class
- How to get Response using the response Entity.
- How to convert the POJO class to the JSON manually using the Jackson class which is `ObjectMapper Class`. We can use this class for converting any class into the JSON using this class and it's also support the jackson binding.
- `@RestController` is the annotation which provides the combination of both which is controller and the Response Body but in controller we are not given the response body.
- `given()` is same as the `when()` just the difference is `when()` syntax uses the realtime objects and given() uses the behavioural Driven syntax.
- I learned about one more concept which is `swagger UI`, which is used for showing the REST API's in the web. It also helps in doing the refactoring of Web API and also help in testing the Web API's also.
- XJC library is used for creating the java coding files from the XML configuration file.
- `Spring RestDocs` plugin is used for creating the documentation for the controllers used in the project of the spring boot.
- `ASCII doctor` maven plugin is used for combine all the final document results into the single documentation that will be provided to us.
- I learned this new concepts that how to generate the documentation using the restdocs template for the constraint that is provided to the variables in the CustomerDTO classes.
#### New Dependencies
```angular2html
- MapStruct for mapping classes automatically when the project is building
- H2 Database for storing the data temporary when the application is in the running instance.
- Swagger UI for showing the API in website and for testing API's also.
- XML Jackson dependency for converting the POJO classes to the XML jackson and represent infront of the users.
```