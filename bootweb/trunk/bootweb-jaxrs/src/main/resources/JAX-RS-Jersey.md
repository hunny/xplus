# Developing RESTful Web Services with JAX-RS (Jersey)

## Introduction
REST stands for REpresentational State Transfer. The REST architectural style describes six constraints. These constraints, applied to the architecture, were originally communicated by Roy Fielding in his doctoral dissertation and defines the basis of RESTful-style.

* The six constraints are: (click on the constraint below to read more)
  - [Uniform Interface](https://stackoverflow.com/a/26049761/6157880)
  - [Stateless](https://stackoverflow.com/a/3105337/6157880)
  - [Cacheable](http://restcookbook.com/Basics/caching/)
  - [Client-Server](https://github.com/brettshollenberger/codecabulary/blob/master/REST%20Constraint%20-%20Client-Server%20Separation.md)
  - [Layered System](http://restfulapi.net/rest-architectural-constraints/#layered-system)
  - [Code on Demand (optional)](http://restfulapi.net/rest-architectural-constraints/#code-on-demand)

[JAX-RS](https://jcp.org/en/jsr/detail?id=339) (JSR-339) as a specification defines a set of Java APIs for the development of Web services built according to the REpresentational State Transfer (REST) architectural style.

[Jersey RESTful](http://jersey.github.io/) Web Services framework is an open source, production quality, framework for developing RESTful Web Services in Java that provides support for JAX-RS APIs and serves as a JAX-RS ([JSR 311](https://www.jcp.org/en/jsr/detail?id=311) & [JSR 339](https://www.jcp.org/en/jsr/detail?id=339)) [Reference Implementation](https://en.wikipedia.org/wiki/Reference_implementation).

## Prerequisites

* To follow along this guide, you should have the following set up:
  - Java Development Kit
  - Maven
  - Spring Boot
* Optional
  - cURL

## Creating Project Template

To create the project structure, we would use the Maven Archetype to generate a project skeleton. In the directory where you want to generate your project, run the following commands:

```
mvn archetype:generate -DgroupId=com.example.bootweb -DartifactId=jaxrs-jersy -DarchetypeArtifactId=maven-archetype-quickstart -DinteractiveMode=false
 ```
Review your selection and hit enter to proceed. At this stage your project is generated in `bootweb-jaxrs`.

## Setting up dependencies

To build a RESTful webservice with Jersey, we must add the Jersey dependencies to our `pom.xml`:

```xml
  <dependencyManagement>
    <dependencies>
      <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-dependencies</artifactId>
        <version>${spring-boot.version}</version>
        <type>pom</type>
        <scope>import</scope>
      </dependency>
  </dependencyManagement>
  <dependencies>
    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-jersey</artifactId>
    </dependency>
  </dependencies>
```

In the snippet above we have specified the `dependencyManagement` so that we can omit the version of `spring-boot-starter-jersey`.

## What we will Create

| HTTP Method | URI | Action |
| --- | --- | --- |
| GET | /api/v1.0/resources/	Retrieve | list of resources |
| GET | /api/v1.0/resources/[resource_id] | Retrieve a resource |
| POST | /api/v1.0/resources/ | Create a new resource |
| PUT | /api/v1.0/resources/[resource_id] | Update an existing resource |
| DELETE | 	/api/v1.0/resources/[resource_id] | Delete a resource |

### GET

The HTTP GET method is used to read (or retrieve) a representation of a resource. In the “happy” (or non-error) path, GET returns a representation in XML or JSON and an HTTP response code of 200 (OK). In an error case, it most often returns a 404 (NOT FOUND) or 400 (BAD REQUEST).
According to the design of the HTTP specification, GET (along with HEAD) requests are used only to read data and not change it. Therefore, when used this way, they are considered safe. That is, they can be called without risk of data modification or corruption — calling it once has the same effect as calling it 10 times, or none at all. Additionally, GET (and HEAD) is idempotent, which means that making multiple identical requests ends up having the same result as a single request.
Do not expose unsafe operations via GET — it should never modify any resources on the server.

* Examples:
  - GET http://www.example.com/resources/1
  - GET http://www.example.com/resources/1/items

### POST

The POST verb is most-often utilized to create new resources. In particular, it’s used to create subordinate resources. That is, subordinate to some other (e.g. parent) resource. In other words, when creating a new resource, POST to the parent and the service takes care of associating the new resource with the parent, assigning an ID (new resource URI), etc.
On successful creation, return HTTP status 201, returning a Location header with a link to the newly-created resource with the 201 HTTP status.
POST is neither safe nor idempotent. It is therefore recommended for non-idempotent resource requests. Making two identical POST requests will most-likely result in two resources containing the same information.

* Examples:
  - POST http://www.example.com/resources
  - POST http://www.example.com/resources/1/items

### PUT

PUT is most-often utilized for update capabilities, PUT-ing to a known resource URI with the request body containing the newly-updated representation of the original resource.
However, PUT can also be used to create a resource in the case where the resource ID is chosen by the client instead of by the server. In other words, if the PUT is to a URI that contains the value of a non-existent resource ID. Again, the request body contains a resource representation. Many feel this is convoluted and confusing. Consequently, this method of creation should be used sparingly, if at all.
Alternatively, use POST to create new resources and provide the client-defined ID in the body representation—presumably to a URI that doesn’t include the ID of the resource (see POST above). On successful update, return 200 (or 204 if not returning any content in the body) from a PUT. If using PUT for create, return HTTP status 201 on successful creation. A body in the response is optional — providing one consumes more bandwidth. It is not necessary to return a link via a Location header in the creation case since the client already set the resource ID.
PUT is not a safe operation, in that it modifies (or creates) state on the server, but it is idempotent. In other words, if you create or update a resource using PUT and then make that same call again, the resource is still there and still has the same state as it did with the first call.
If, for instance, calling PUT on a resource increments a counter within the resource, the call is no longer idempotent. Sometimes that happens and it may be enough to document that the call is not idempotent. However, it’s recommended to keep PUT requests idempotent. It is strongly recommended to use POST for non-idempotent requests.

* Examples:
  - PUT http://www.example.com/resources/1
  - PUT http://www.example.com/resources/1/items/1

### DELETE

DELETE is pretty easy to understand. It is used to delete a resource identified by a URI.
On successful deletion, return HTTP status 200 (OK) along with a response body, perhaps the representation of the deleted item (often demands too much bandwidth), or a wrapped response. Either that or return HTTP status 204 (NO CONTENT) with no response body. In other words, a 204 status with no body, or the JSEND-style response and HTTP status 200 are the recommended responses.
HTTP-spec-wise, DELETE operations are idempotent. If you DELETE a resource, it’s removed. Repeatedly calling DELETE on that resource ends up the same: the resource is gone. If calling DELETE say, decrements a counter (within the resource), the DELETE call is no longer idempotent. As mentioned previously, usage statistics and measurements may be updated while still considering the service idempotent as long as no resource data is changed. Using POST for non-idempotent resource requests is recommended.
There is a caveat about DELETE idempotence, however. Calling DELETE on a resource a second time will often return a 404 (NOT FOUND) since it was already removed and therefore is no longer findable. This, by some opinions, makes DELETE operations no longer idempotent, however, the end-state of the resource is the same. Returning a 404 is acceptable and communicates accurately the status of the call.

* Examples:
  - DELETE http://www.example.com/resources/1
  - DELETE http://www.example.com/resources/1/items

## Building Resources

### We will create a POJO to represent our REST resource.

```java
package com.example.bootweb.jaxrs.jersy;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Resource implements Serializable {

  private static final long serialVersionUID = 4144444660085631303L;
  
  private Long id;
  private String description;
  private LocalDateTime createdTime;
  private LocalDateTime modifiedTime;

  public Resource(Long id, //
      String description, //
      LocalDateTime createdTime, //
      LocalDateTime modifiedTime) {
    super();
    this.id = id;
    this.description = description;
    this.createdTime = createdTime;
    this.modifiedTime = modifiedTime;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public LocalDateTime getCreatedTime() {
    return createdTime;
  }

  public void setCreatedTime(LocalDateTime createdTime) {
    this.createdTime = createdTime;
  }

  public LocalDateTime getModifiedTime() {
    return modifiedTime;
  }

  public void setModifiedTime(LocalDateTime modifiedTime) {
    this.modifiedTime = modifiedTime;
  }

}
```

### We will declare a API for our REST resource.

```java
package com.example.bootweb.jaxrs.api;

import java.util.List;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.example.bootweb.jaxrs.jersy.Resource;

@Path("/v1.0/resources")
@Produces({ MediaType.APPLICATION_JSON})
public interface ResourceService {
  
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Path("/hello")
  Map<String,Object> hello();

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  List<Resource> getResources();

  @GET
  @Path("/{id: [0-9]+}")
  @Produces(MediaType.APPLICATION_JSON)
  Resource getResource(@PathParam("id") Long id);

  @POST
  @Consumes({ MediaType.APPLICATION_JSON})
  Response createResource(Resource resource);

  @PUT
  @Path("{id: [0-9]+}")
  @Consumes({ MediaType.APPLICATION_JSON})
  Response updateResource(@PathParam("id") Long id, Resource resource);

  @DELETE
  @Path("{id: [0-9]+}")
  @Consumes({ MediaType.APPLICATION_JSON})
  Response deleteResource(@PathParam("id") Long id);
  
}
```

### We will implement a API for our REST resource.

```java
package com.example.bootweb.jaxrs.impl;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import org.springframework.stereotype.Component;

import com.example.bootweb.jaxrs.jersy.Resource;
import com.example.bootweb.jaxrs.jersy.api.ResourceService;

@Component
public class ResourceServiceImpl implements ResourceService {

  private static List<Resource> resources = null;

  static {
    resources = new ArrayList<>();
    resources.add(new Resource(1L, "Resource One", LocalDateTime.now(), null));
    resources.add(new Resource(2L, "Resource Two", LocalDateTime.now(), null));
    resources.add(new Resource(3L, "Resource Three", LocalDateTime.now(), null));
    resources.add(new Resource(4L, "Resource Four", LocalDateTime.now(), null));
    resources.add(new Resource(5L, "Resource Five", LocalDateTime.now(), null));
    resources.add(new Resource(6L, "Resource Six", LocalDateTime.now(), null));
    resources.add(new Resource(7L, "Resource Seven", LocalDateTime.now(), null));
    resources.add(new Resource(8L, "Resource Eight", LocalDateTime.now(), null));
    resources.add(new Resource(9L, "Resource Nine", LocalDateTime.now(), null));
    resources.add(new Resource(10L, "Resource Ten", LocalDateTime.now(), null));
  }

  @Override
  public Map<String, Object> hello() {
    Map<String, Object> map = new HashMap<String, Object>();
    map.put("code", "1");
    map.put("codeMsg", "success");
    return map;
  }

  @Override
  public List<Resource> getResources() {
    return resources;
  }

  @Override
  public Resource getResource(Long id) {
    Resource resource = new Resource(id, null, null, null);

    int index = Collections.binarySearch(resources, resource, Comparator.comparing(Resource::getId));

    if (index >= 0)
      return resources.get(index);
    else
      throw new WebApplicationException(Response.Status.NOT_FOUND);
  }

  @Override
  public Response createResource(Resource resource) {
    if (Objects.isNull(resource.getId()))
      throw new WebApplicationException(Response.Status.BAD_REQUEST);

    int index = Collections.binarySearch(resources, resource, Comparator.comparing(Resource::getId));

    if (index < 0) {
      resource.setCreatedTime(LocalDateTime.now());
      resources.add(resource);
      return Response.status(Response.Status.CREATED)
          .location(URI.create(String.format("/api/v1.0/resources/%s", resource.getId()))).build();
    } else
      throw new WebApplicationException(Response.Status.CONFLICT);
  }

  @Override
  public Response updateResource(Long id, Resource resource) {
    resource.setId(id);
    int index = Collections.binarySearch(resources, resource, Comparator.comparing(Resource::getId));

    if (index >= 0) {
      Resource updatedResource = resources.get(index);
      updatedResource.setModifiedTime(LocalDateTime.now());
      updatedResource.setDescription(resource.getDescription());
      resources.set(index, updatedResource);
      return Response.status(Response.Status.NO_CONTENT).build();
    } else
      throw new WebApplicationException(Response.Status.NOT_FOUND);
  }

  @Override
  public Response deleteResource(Long id) {
    Resource resource = new Resource(id, null, null, null);
    int index = Collections.binarySearch(resources, resource, Comparator.comparing(Resource::getId));
    if (index >= 0) {
      resources.remove(index);
      return Response.status(Response.Status.NO_CONTENT).build();
    } else
      throw new WebApplicationException(Response.Status.NOT_FOUND);
  }
}
```

### We will configure a Jersey enviroment for our REST resource.

* option one with profile `spring.profiles.active=JerseyConfig`

```java
package com.example.bootweb.jaxrs.jersy.config;

import javax.ws.rs.ApplicationPath;

import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@ApplicationPath("/api")
@Profile("JerseyConfig")
public class JerseyConfig extends ResourceConfig {

  public JerseyConfig() {
    //配置restful package.
    packages("com.example.bootweb.jaxrs.api");
  }
  
}
```

* option two with profile `spring.profiles.active=JerseyConfigServlet`

```java
package com.example.bootweb.jaxrs.jersy.configbean;

import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.context.annotation.Profile;

@Profile("JerseyConfigServlet")
public class JerseyConfigServlet extends ResourceConfig {

  public JerseyConfigServlet() {
    // 配置restful package.
    packages("com.example.bootweb.jaxrs.api");
  }
  
}
```

```java
package com.example.bootweb.jaxrs.jersy.configbean;

import org.glassfish.jersey.servlet.ServletContainer;
import org.glassfish.jersey.servlet.ServletProperties;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("JerseyConfigServlet")
public class JerseyConfigServletBean {
  @Bean
  public ServletRegistrationBean jerseyServlet() {
    ServletRegistrationBean registration = new ServletRegistrationBean( //
        new ServletContainer(), "/api-servlet/*");
    // our rest resources will be available in the path /api/*
    registration.addInitParameter(ServletProperties.JAXRS_APPLICATION_CLASS, //
        JerseyConfigServlet.class.getName());
    return registration;
  }
}
```

### We will create a Spring Boot Application for our REST resource.

```java
package com.example.bootweb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {
  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }
}
```

## Run

* Option one run with profile `spring.profiles.active=JerseyConfig`
  - Check result: `http://localhost:8080/api/v1.0/resources/2`
* Option two run with profile `spring.profiles.active=JerseyConfigServlet`
  - Check result: `http://localhost:8080/api-servlet/v1.0/resources/2`

* Result:

```json
{
  "id": 2, 
  "description": "Resource Two", 
  "createdTime": {
    "dayOfYear": 322, 
    "dayOfWeek": "SATURDAY", 
    "month": "NOVEMBER", 
    "dayOfMonth": 18, 
    "year": 2017, 
    "monthValue": 11, 
    "hour": 13, 
    "minute": 58, 
    "second": 48, 
    "nano": 52000000, 
    "chronology": {
      "id": "ISO", 
      "calendarType": "iso8601"
    }
  }, 
  "modifiedTime": null
}
```