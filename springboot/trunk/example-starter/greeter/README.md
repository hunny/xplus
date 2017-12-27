# Greeter App

This app takes in the user's name and messages for different times of day as configuration parameters and outptus the greeting messge. For example it will take the name **OkHttp** and the message for morning time as **Good Morning** and output the message **Hello OkHttp, Good Morning**.

## Usage

Create and populate the class `GreetingConfig`, instantiate a `GreeterService` using the `GreetingConfig` and use it get greeting messages:

```java
GreetingConfig greetingConfig = new GreetingConfig();
greetingConfig.put(USER_NAME, "World");
greetingConfig.put(MORNING_MESSAGE, "Good Morning");
greetingConfig.put(AFTERNOON_MESSAGE, "Good Afternoon");
greetingConfig.put(EVENING_MESSAGE, "Good Evening");
greetingConfig.put(NIGHT_MESSAGE, "Good Night");

GreeterService greeterService = new GreeterService(greetingConfig);
greeter.greet();
```