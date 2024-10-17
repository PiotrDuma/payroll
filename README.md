# payroll
Payroll is an implementation of payment system in Java/Spring enviroment based on requirements' stories, UML diagrams and design patterns dercribed by Robert C. Martin in one of his book [[1]](#book-reference). A large amount of abstraction, polymorphism and SOLID priciples make system's architecture more flexible for extensions and keep code clean and simple. The design, originally written in C++, required a few modifications to be adjusted into Spring web application. The changes are described in the [modifications'](#modifications) section.

## Release info:

### SNAPSHOT 1.0

+ implementation of domain models and system's methods;
+ unit tests;
+ extension of domain classes with value objects;
+ custom exceptions and global exception handler;

### SNAPSHOT 1.1

+ infrastructure implementation based on Spring JPA/Hibernate;
+ integration tests;
+ application profiles and database configurations.
  

## Run project

There's two different approaches to run payroll application. It depends on acive profile. The "prod" profile requires connection to the PostgreSQL database server and configured application-prod.properties file. The "test" profile contains simple in memory H2 database, which is implemented to run integration tests and it can be also used as a standalone application instace, however in this case all data will be lost when application's process is terminated.

Make sure you have installed [git](https://git-scm.com/), [Java17 JDK](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html) and [Maven](https://maven.apache.org/download.cgi). They must be recognized in your system variables.

### A. 'prod' profile

Before application run, database must be initialized on [PostgreSQL server](https://www.postgresql.org/), it can be done via pgAdmin4 application.

1. Download project from repository 
```
git clone https://github.com/PiotrDuma/payroll.git
```
2. Set database connection in application-prod.properties file. It can be found on the path: 'payroll\src\main\resources\application-prod.properties'
```
spring.datasource.url=jdbc:postgresql://localhost:5433/payroll
spring.datasource.username=postgres
spring.datasource.password=admin
```
3. Move to project folder
```
cd payroll
```
4. Build project with Maven
```
mvn clean install 
```
5. Run application in your command window
```
java -jar -Dspring.profiles.active=prod target/payroll-SNAPSHOT_1.1.jar
```

### B. 'test' profile

Running applicaiton with test profile also availables to inspect database on [/h2-console](http://localhost:8080/h2-console) endpoint. 

1. Download project from repository 
```
git clone https://github.com/PiotrDuma/payroll.git
```
2. Move to project folder
```
cd payroll
```
3. Build project with Maven
```
mvn clean install 
```
4. Run application in your command window
```
java -jar -Dspring.profiles.active=test target/payroll-SNAPSHOT_1.1.jar
```

## Modifications

The main changes were caused by differences between C++ and Java. The Spring weeb application architecture required to provide higher lever of encapsulation, new design patterns and implementation of infrastructure and controller layers. 

+ avoidance of public classes, lack of pointers and global variables forced to change Transaction command interface to provide ID identifiers or final objects;
+ JPA/Hibernate infrastructure forced changes in Employee interfaces' references. Associated methods entities had to be related by public abstract classes to initialize entities;
+ Union Affiliation has been excluded from Employee and implemented as independent domain with ability to create many instances and changing dues using different entry point;
+ Payday Transaction is a new service aggregating required elements across domains;
+ every Change Employee Transaction is placed in separated class provided by factory; 


## Biblography
<a name="book-reference">1</a>  Martin, Robert C. Agile Software Development, Principles, Patterns, and Practices. Essex, Pearson, 2014, pp. 193–250, ISBN 9781292025940.
