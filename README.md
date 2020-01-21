# sakthi-via - Practice Application

`https://sakthi-via.herokuapp.com/swagger-ui.html`

This application provides,
* GET, POST, PUT and DELETE Request methods to manipulate Employee details.
* GET methods to check the countries, currencies and latest currency rate conversion.
* Currency rate daily alerts via Email.

**API's:**

Employee:
* GET: /api/v1/employees - Retrieve all the employees
* GET: /api/v1/employees/{id} - Get Employee details by Id
* GET: /api/v1/employeesByEmail/{email} - Get Employee Details By Email
* GET: /api/v1/employeesByUsernameOrEmail - Get Employee Details either by Email or Username
* POST: /api/v1/employees - Create Employee
* POST: /api/v1/registerForRates - Register for scheduled mail alerts
* PUT: /api/v1/employees/{id} - Update Employee Email
* DELETE: /api/v1/employees/{id} - Delete Employee by Id

CurrencyConverter:
* GET: /api/v1/countries - To get the countries and their currency codes
* GET: /api/v1/countries/{code} - To get the country for currency code
* GET: /api/v1/rates?base={code} - To retrieve the latest currency rates
* GET: /api/v1/highestAndLowestCurrencyRates?base={code} - To get the highest and lowest currencies for the base currency

**Java Version**
* Java 11
* Java 8 streams, lambdas/method references, optionals are used where possible

**Spring**
* Spring Boot 2 with embedded Tomcat
* Spring configurations through annotations - Controller, Service, Repository, Component
* Spring Profiles: dev, test and prod
* Error handling (@ControllerAdvice, @ExceptionHandler and Custom Exception)
* Spring Data JPA (JpaRepository + Entity class) - Heroku Postgres to store the data
* Spring Mail with Thymeleaf
* Spring Scheduler
* Spring Cache - Simple cache

**Additional**
* Validation with JSR303 - Annotations + Custom Annotation (to check Unique Username)
* Slf4j with Logback (logback-spring.xml) - Configured Heroku with Papertrail and logs can be viewed in `https://my.papertrailapp.com/events` 
* Swagger 2 for documentation
* Circuit Breaking - Netflix Hystrix

**Development and build tools**
* Maven for dependency management
* IntelliJ
* Git `https://github.com/sgsakthi1992/sakthi-via`
* CI & CD: TravisCI to deploy the code in GIT to Heroku (Only if Code Coverage rule is met). Runs SonarQube and uploads the result to configured SonarCloud

**Testing**
* Unit and Integration tests
* Mocking with Mockito
* Hamcrest, AssertJ for assertions
* @SpringBootTest and MockMvc in integration tests with in-memory h2 database
* GreenMail to test Mail integration test
* WireMock to test external API integration test
* JaCoCo for code coverage, configured in maven plugin
* SonarQube(SonarCloud.io), CheckStyle(configured in maven plugin) for code quality

**System Architecture**

![alt text](https://raw.githubusercontent.com/sgsakthi1992/sakthi-via/master/plantUml/system_diagram.png)