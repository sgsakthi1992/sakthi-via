# sakthi-via - Practice Application

`https://sakthi-via.herokuapp.com/swagger-ui.html`

This application provides GET, POST, PUT and DELETE Request methods to manipulate Employee details.

**API's:**
* GET: /api/v1/employees - Retrieve all the employees
* GET: /api/v1/employees/{id} - Get Employee details by Id
* GET: /api/v1/employeesByEmail/{email} - Get Employee Details By Email
* GET: /api/v1/employeesByUsernameOrEmail - Get Employee Details either by Email or Username
* POST: /api/v1/employees - Create Employee
* PUT: /api/v1/employees/{id} - Update Employee Email
* DELETE: /api/v1/employees/{id} - Delete Employee by Id

**Java Version**
* Java 10
* Java 8 streams, lambdas/method references, optionals are used where possible

**Spring**
* Spring Boot 2 with embedded Tomcat
* Spring configurations through annotations - Controller, Service, Repository
* Spring Profiles: dev and prod
* Error handling (@ControllerAdvice, @ExceptionHandler and Custom Exception)
* Spring Data JPA (JpaRepository + Entity class) - Heroku Postgres to store the data

**Additional**
* Validation with JSR303 - Annotations + Custom Annotation (to check Unique Username)
* Slf4j with Logback (logback-spring.xml) - Configured Heroku with Papertrail and logs can be viewed in `https://my.papertrailapp.com/events` 
* Swagger 2 for documentation

**Development and build tools**
* Maven for dependency management
* IntelliJ
* Git `https://github.com/sgsakthi1992/sakthi-via`
* CI & CD: Local Jenkins setup to deploy the code in GIT to Heroku