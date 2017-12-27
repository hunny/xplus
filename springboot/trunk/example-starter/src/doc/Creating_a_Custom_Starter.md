# Creating a Custom Starter

* To create a custom starter we need to write the following components:
  - An auto-configure class for our library along with a properties class for custom configuration.
  - A starter pom to bring in the dependencies of the library and the autoconfigure project.

For demonstration, we have created a simple greeting library that will take in a greeting message for different times of day as configuration parameters and output the greeting message. We will also create a sample Spring Boot application to demonstrate the usage of our autoconfigure and starter modules.
  
## The Autoconfigure Module

We’ll call our auto configure module `greeter-spring-boot-autoconfigure`. This module will have two main classes i.e. `GreeterProperties` which will enable setting custom properties through `application.properties` file and `GreeterAutoConfiguartion` which will create the beans for greeter library.

Let’s look at the code for both the classes:

