# Bootweb Jersey Using Demo project.

## RESTful Web Services in Java.

Developing RESTful Web services that seamlessly support exposing your data in a variety of representation media types and abstract away the low-level details of the client-server communication is not an easy task without a good toolkit. In order to simplify development of RESTful Web services and their clients in Java, a standard and portable JAX-RS API has been designed. Jersey RESTful Web Services framework is open source, production quality, framework for developing RESTful Web Services in Java that provides support for JAX-RS APIs and serves as a JAX-RS (JSR 311 & JSR 339) Reference Implementation.

开发REST风格的Web服务，可以无缝地支持在各种表示媒体类型中公开数据，并且抽象出客户端 - 服务器通信的底层细节，如果没有好的工具包，这不是一件容易的事情。 为了简化RESTful Web服务及其Java客户端的开发，设计了一个标准的可移植的JAX-RS API。 Jersey REST风格的Web服务框架是开放源码，生产质量的框架，用于开发Java中的REST风格的Web服务，为JAX-RS API提供支持，并作为JAX-RS（JSR 311和JSR 339）参考实现。

Jersey framework is more than the JAX-RS Reference Implementation. Jersey provides it’s own API that extend the JAX-RS toolkit with additional features and utilities to further simplify RESTful service and client development. Jersey also exposes numerous extension SPIs so that developers may extend Jersey to best suit their needs.

Jersey框架不仅仅是JAX-RS参考实现。 Jersey提供了自己的API来扩展JAX-RS工具包的附加功能和实用程序，以进一步简化RESTful服务和客户端开发。 Jersey还公开了大量的扩展SPI，以便开发者可以将Jersey扩展到最适合他们的需求。

Goals of Jersey project can be summarized in the following points:

Jersey项目的目标可以归纳为以下几点：

 - Track the JAX-RS API and provide regular releases of production quality Reference Implementations that ships with GlassFish;跟踪JAX-RS API并定期发布GlassFish附带的生产质量参考实现;
 - Provide APIs to extend Jersey & Build a community of users and developers; and finally提供API来扩展Jersey和构建一个用户和开发人员社区; 
 - Make it easy to build RESTful Web services utilising Java and the Java Virtual Machine.使用Java和Java虚拟机可以轻松构建RESTful Web服务。