# Intro to Spring Boot Starters

## Reference

Intro to Spring Boot Starters, click [Here](http://www.baeldung.com/spring-boot-starters). [More about guides](http://www.baeldung.com/tag/spring-boot/)

## Overview

Dependency management is a critical aspects of any complex project. And doing this manually is less than ideal; the more time you spent on it the less time you have on the other important aspects of the project.

Spring Boot starters were built to address exactly this problem. Starter POMs are a set of convenient dependency descriptors that you can include in your application. You get a one-stop-shop for all the Spring and related technology that you need, without having to hunt through sample code and copy paste loads of dependency descriptors.

We have more than 30 Boot starters available – let’s see some of them in the following sections.

## The Web Starter

First, let’s look at developing the REST service; we can use libraries like Spring MVC, Tomcat and Jackson – a lot of dependencies for a single application.

Spring Boot starters can help to reduce the number of manually added dependencies just by adding one dependency. So instead of manually specifying the dependencies just add one starter as in the following example:

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>
```

Now we can create a REST controller. For the sake of simplicity we won’t use the database and focus on the REST controller:

```java
@RestController
public class GenericEntityController {
    private List<GenericEntity> entityList = new ArrayList<>();
 
    @RequestMapping("/entity/all")
    public List<GenericEntity> findAll() {
        return entityList;
    }
 
    @RequestMapping(value = "/entity", method = RequestMethod.POST)
    public GenericEntity addEntity(GenericEntity entity) {
        entityList.add(entity);
        return entity;
    }
 
    @RequestMapping("/entity/findby/{id}")
    public GenericEntity findById(@PathVariable Long id) {
        return entityList.stream().
                 filter(entity -> entity.getId().equals(id)).
                   findFirst().get();
    }
}
```

he GenericEntity is a simple bean with id of type Long and value of type String.

That’s it – with the application running, you can access http://localhost:8080/springbootapp/entity/all and check the controller is working.

We have created a REST application with quite minimal configuration.

## The Test Starter

For testing we usually use the following set of libraries: Spring Test, JUnit, Hamcrest and Mockito. We can include all of these libraries manually, but Spring Boot starter can be used to automatically include these libraries in the following way:

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-test</artifactId>
    <scope>test</scope>
</dependency>
```

Notice that you don’t need to specify the version number of an artifact. Spring Boot will figure out what version to use – all you need to specify is the version of `spring-boot-starter-parent` artifact. If later on you need to upgrade the Boot library and dependencies, just upgrade the Boot version in one place and it will take care of the rest.

Let’s actually test the controller we created in the previous example.

* There are two ways to test the controller:
  - Using the mock environment
  - Using the embedded Servlet container (like Tomcat or Jetty)

In this example we’ll use a mock environment:

```java
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
public class SpringBootApplicationTest {
    @Autowired
    private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;
 
    @Before
    public void setupMockMvc() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }
 
    @Test
    public void givenRequestHasBeenMade_whenMeetsAllOfGivenConditions_thenCorrect()
      throws Exception { 
        MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
        MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));
        mockMvc.perform(MockMvcRequestBuilders.get("/entity/all")).
        andExpect(MockMvcResultMatchers.status().isOk()).
        andExpect(MockMvcResultMatchers.content().contentType(contentType)).
        andExpect(jsonPath("$", hasSize(4))); 
    } 
}
```

What is important here is that `@WebAppConfiguration` annotation and MockMVC are part of the spring-test module, hasSize is a Hamcrest matcher, and `@Before` is a JUnit annotation. These are all available by importing one this one starter dependency.

## The Data JPA Starter

Most web applications have some sort of persistence – and that’s quite often JPA.

Instead of defining all of the associated dependencies manually – let’s go with the starter instead:

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>
<dependency>
    <groupId>com.h2database</groupId>
    <artifactId>h2</artifactId>
    <scope>runtime</scope>
</dependency>
```

Notice that out of box we have automatic support for at least the following databases: H2, Derby and Hsqldb. In our example we’ll use H2.

Now let’s create the repository for our entity:

```java
public interface GenericEntityRepository extends JpaRepository<GenericEntity, Long> {}
```

Time to test the code. Here is the JUnit test:

```java
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
public class SpringBootJPATest {
     
    @Autowired
    private GenericEntityRepository genericEntityRepository;
 
    @Test
    public void givenGenericEntityRepository_whenSaveAndRetreiveEntity_thenOK() {
        GenericEntity genericEntity = 
          genericEntityRepository.save(new GenericEntity("test"));
        GenericEntity foundedEntity = 
          genericEntityRepository.findOne(genericEntity.getId());
         
        assertNotNull(foundedEntity);
        assertEquals(genericEntity.getValue(), foundedEntity.getValue());
    }
}
```

We didn’t spend time on specifying the database vendor, URL connection and credentials. No extra configuration is necessary as we’re benefiting from the solid Boot defaults; but of course all of these details can still be configured if necessary.

## The Mail Starter

A very common task in enterprise development is sending email; and dealing directly with Java Mail API usually can be difficult.

Spring Boot starter hides this complexity – mail dependencies can be specified in the following way:

```java
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-mail</artifactId>
</dependency>
```

Now we can directly use the JavaMailSender, so let’s write some tests.

For the testing purpose we need a simple SMTP server. In this example we’ll use Wiser. This is how we can include it in our POM:

```xml
<dependency>
    <groupId>org.subethamail</groupId>
    <artifactId>subethasmtp</artifactId>
    <version>3.1.7</version>
    <scope>test</scope>
</dependency>
```

The latest version of Wiser can be found on [Maven central repository](http://search.maven.org/#search%7Cga%7C1%7Csubethasmtp).

Here is the source code for the test:

```java
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
public class SpringBootMailTest {
    @Autowired
    private JavaMailSender javaMailSender;
 
    private Wiser wiser;
 
    private String userTo = "user2@localhost";
    private String userFrom = "user1@localhost";
    private String subject = "Test subject";
    private String textMail = "Text subject mail";
 
    @Before
    public void setUp() throws Exception {
        final int TEST_PORT = 25;
        wiser = new Wiser(TEST_PORT);
        wiser.start();
    }
 
    @After
    public void tearDown() throws Exception {
        wiser.stop();
    }
 
    @Test
    public void givenMail_whenSendAndReceived_thenCorrect() throws Exception {
        SimpleMailMessage message = composeEmailMessage();
        javaMailSender.send(message);
        List<WiserMessage> messages = wiser.getMessages();
 
        assertThat(messages, hasSize(1));
        WiserMessage wiserMessage = messages.get(0);
        assertEquals(userFrom, wiserMessage.getEnvelopeSender());
        assertEquals(userTo, wiserMessage.getEnvelopeReceiver());
        assertEquals(subject, getSubject(wiserMessage));
        assertEquals(textMail, getMessage(wiserMessage));
    }
 
    private String getMessage(WiserMessage wiserMessage)
      throws MessagingException, IOException {
        return wiserMessage.getMimeMessage().getContent().toString().trim();
    }
 
    private String getSubject(WiserMessage wiserMessage) throws MessagingException {
        return wiserMessage.getMimeMessage().getSubject();
    }
 
    private SimpleMailMessage composeEmailMessage() {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(userTo);
        mailMessage.setReplyTo(userFrom);
        mailMessage.setFrom(userFrom);
        mailMessage.setSubject(subject);
        mailMessage.setText(textMail);
        return mailMessage;
    }
}
```

In the test, the `@Before` and `@After` methods are in charge of starting and stopping the mail server.

Notice that we’re wiring in the `JavaMailSender` bean – the bean was automatically created by Spring Boot.

Just like any other defaults in Boot, the email settings for the JavaMailSender can be customized in application.properties:

```
spring.mail.host=localhost
spring.mail.port=25
spring.mail.properties.mail.smtp.auth=false
```

So we configured the mail server on localhost:25 and we didn’t require authentication.

## Conclusion
In this article we have given an overview of Starters, explained why we need them and provided examples on how to use them in your projects.

* Let’s recap the benefits of using Spring Boot starters:
  - increase pom manageability
  - production ready, tested & supported dependency configurations
  - decrease the overall configuration time for the project
Actual list of starters can be found [here](https://github.com/spring-projects/spring-boot/tree/master/spring-boot-project/spring-boot-starters). 


