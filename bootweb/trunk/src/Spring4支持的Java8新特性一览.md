# Spring 4支持的Java 8新特性一览

有众多新特性和函数库的Java 8发布之后，Spring 4.x已经支持其中的大部分。有些Java 8的新特性对Spring无影响，可以直接使用，但另有些新特性需要Spring的支持。本文将带您浏览Spring 4.0和4.1已经支持的Java 8新特性。

## Spring 4支持Java 6、7和8

Java 8编译器编译过的代码生成的.class文件需要在Java 8或以上的Java虚拟机上运行。由于Spring对反射机制和ASM、CGLIB等字节码操作函数库的重度使用，必须确保这些函数库能理解Java 8生成的新class文件。因此Spring将ASM、CGLIB等函数库通过jar jar(https://code.google.com/p/jarjar/)嵌入Spring框架中，这样Spring就可以同时支持Java6、7和8的字节码代码而不会触发运行时错误。

Spring框架本身是由Java 8编译器编译的，编译时使用的是生成Java 6字节码的编译命令选项。因此你可以Java6、7或者8来编译运行Spring 4.x的应用。

## Spring和Java 8的Lambda表达式

Java 8的设计者想保证它是向下兼容的，以使其lambda表达式能在旧版本的代码编译器中使用。向下兼容通过定义函数式接口概念实现。

基本上，Java 8的设计者分析了现有的Java代码体系，注意到很多Java程序员用只有一个方法的接口来表示方法的思想。以下就是JDK和Spring中只有一个方法的接口的例子，也就是所谓的“函数式接口”。

JDK里的函数式接口：

```java
public interface Runnable {
    public abstract void run();}

public interface Comparable<T> {
    public int compareTo(T o);}
```

Spring框架里的函数式接口：

```java
public interface ConnectionCallback<T> {
  T doInConnection(Connection con) throws SQLException, DataAccessException;}

public interface RowMapper<T>{
  T mapRow(ResultSet rs, int rowNum) throws SQLException;}
```

在Java 8里，任何函数式接口作为方法的参数传入或者作为方法返回值的场合，都可以用lambda表达式代替。例如，Spring的JdbcTemplate类里有一个方法定义如下：

```java
public <T> List<T> query(String sql, RowMapper<T> rowMapper)
  throws DataAccessException
```

这个查询方法的第二个参数需要RowMapper接口的一个实例。在Java 8中我们可以写一个lambda表达式作为第二个参数的值传进去。

别把代码写成这样：

```java
jdbcTemplate.query("SELECT * from products", new RowMapper<Product>(){
  @Override
  public Product mapRow(ResultSet rs, int rowNum) throws SQLException {
    Integer id = rs.getInt("id");
    String description = rs.getString("description");
    Integer quantity = rs.getInt("quantity");
    BigDecimal price = rs.getBigDecimal("price");
    Date availability = rs.getDate("available_date");

    Product product = new Product();
    product.setId(id);
    product.setDescription(description);
    product.setQuantity(quantity);
    product.setPrice(price);
    product.setAvailability(availability);

    return product;
  }});
```

我们这么写：

```java
jdbcTemplate.query("SELECT * from queries.products", (rs, rowNum) -> {
    Integer id = rs.getInt("id");
    String description = rs.getString("description");
    Integer quantity = rs.getInt("quantity");
    BigDecimal price = rs.getBigDecimal("price");
    Date availability = rs.getDate("available_date");

    Product product = new Product();
    product.setId(id);
    product.setDescription(description);
    product.setQuantity(quantity);
    product.setPrice(price);
    product.setAvailability(availability);

    return product;});
```

我们注意到Java 8中这段代码使用了lambda表达式，这比之前的版本中使用匿名内部类的方式紧凑、简洁得多。

涵盖Java 8中函数式接口的所有细节超出了本文的范畴，我们强烈建议您从别处详细学习函数式接口。本文想要传达的关键点在于Java 8的lambda表达式能传到那些用Java 7或更早的JDK编译的、接受函数式接口作为参数的方法中。

Spring的代码里有很多函数式接口，因此lambda表达式很容易与Spring结合使用。即便Spring框架本身被编译成Java 6的.class文件格式，你仍然可以用Java 8的lambda表达式编写应用代码、用Java 8编译器编译、并且在Java 8虚拟机上运行，你的应用可以正常工作。

总之，因为Spring框架早在Java 8正式给函数式接口下定义之前就已经实际使用了函数式接口，因此在Spring里使用lambda表达式非常容易。

## Spring 4和Java 8的时间与日期API

Java开发者们一直痛恨java.util.Date类的设计缺陷，终于，Java 8带来了全新的日期与时间API，解决了那些久被诟病的问题。这个新的日期与时间API值得用一整篇文章的篇幅来讲述，因此我们在本文不会详述其细节，而是重点关注新的java.time包中引入的众多新类，如LocalDate、LocalTime和 LocalDateTime。

Spring有一个数据转换框架，它可以使字符串和Java数据类型相互转换。Spring 4升级了这个转换框架以支持Java 8日期与时间API里的那些类。因此你的代码可以这样写：

```java
@RestController
public class ExampleController {

  @RequestMapping("/date/{localDate}")
  public String get(@DateTimeFormat(iso = ISO.DATE) LocalDate localDate)
  {
    return localDate.toString();
  }}
```

上面的例子中，get方法的参数是Java 8的LocalDate类型，Spring 4能接受一个字符串参数例如2014-02-01并将它转换成Java 8 LocalDate的实例。

要注意的是Spring通常会与其它一些库一起使用实现特定功能，比如与Hibernate一起实现数据持久化，与Jackson一起实现Java对象和JSON的互相转换。

虽然Spring 4支持Java 8的日期与时间库，这并不表示第三方框架如Hibernate和Jackson等也能支持它。到本文发表时，Hibernate JIRA里仍有一个开放状态的请求HHH-8844要求在Hibernate里支持Java 8日期与时间API。

## Spring 4与重复注解
Java 8增加了对重复注解的支持，Spring 4也同样支持。特殊的是，Spring 4支持对注解@Scheduled和@PropertySource的重复。例如，请注意如下代码片段中对@PropertySource注解的重复使用：

```java
@Configuration
@ComponentScan
@EnableAutoConfiguration
@PropertySource("classpath:/example1.properties")
@PropertySource("classpath:/example2.properties")public class Application {

        @Autowired
        private Environment env;

        @Bean
        public JdbcTemplate template(DataSource datasource) {
                System.out.println(env.getProperty("test.prop1"));
                System.out.println(env.getProperty("test.prop2"));
                return new JdbcTemplate(datasource);
        }

        public static void main(String[] args) {
                SpringApplication.run(Application.class, args);
        }}
```

## Java 8的Optional<>与Spring 4.1
忘记检查空值引用是应用代码中一类常见的bug来源。消除NullPointerExceptions的方式之一是确保方法总是返回一个非空值。例如如下方法：

```java
public interface CustomerRepository extends CrudRepository<Customer, Long> {
   /**
    * returns the customer for the specified id or
    * null if the value is not found
   */
   public Customer findCustomerById(String id);}
```

用如下有缺陷的代码来调用CustomerRepository ：

```java
Customer customer = customerRepository.findCustomerById(“123”);
customer.getName(); // 得到空指针错误
```

这段代码的正确写法应该是：

```java
Customer customer = customerRepository.findCustomerById(“123”);if(customer != null) {
  customer.getName(); // 避免空指针错误
}
```

理想状态下，如果我们没有检查某个值能否为空，我们希望编译器及时发现。java.util.Optional类让我们可以像这样写接口：

```java
public interface CustomerRepository extends CrudRepository<Customer, Long> {
  public Optional<Customer> findCustomerById(String id);}
```
这样一来，这段代码的有缺陷版本不会被编译，开发者必须显式地检查这个Optional类型对象是否有值，代码如下：

```java
Optional<Customer> optional = 
customerRepository.findCustomerById(“123”);if(optional.isPresent()) {
   Customer customer = optional.get();
   customer.getName();}
```

所以Optional的关键点在于确保开发者不用查阅Javadoc就能知道某个方法可以返回null，或者可以把一个null值传给某方法。编译器和方法签名有助于开发者明确知道某个值是Optional类型。关于Optional类思想的详细描述请参考这里。

Spring 4.1有两种方式支持Java Optional。Spring的@Autowired注解有一个属性"required"，使用之后我们可以把如下代码：

```java
@Service
public class MyService {

    @Autowired(required=false)
    OtherService otherService;

    public doSomething() {
      if(otherService != null) {
        // use other service
      }
   }}
```

替换成：

```java
public class MyService {

    @Autowired
    Optional<OtherService> otherService;

    public doSomething() {
      otherService.ifPresent( s ->  {
        // use s to do something
      });
    }}
```

另一个能用Optional的地方是Spring MVC框架，可以用于表示某个处理方法的参数是可选的。例如：

```java
@RequestMapping(“/accounts/{accountId}”,requestMethod=RequestMethod.POST)
void update(Optional<String> accountId, @RequestBody Account account)
```

这段代码会告诉Spring其accountId是可选参数。

总之，Java 8的Optional类通过减少空指针错误相关的缺陷简化了代码编写，同时Spring能很好地支持Java 8的Optional类。

## 参数名发现机制
Java 8支持在编译后的代码中保留方法的参数名。这意味着Spring 4可以从方法中提取参数名，从而使SpringMVC代码更为简洁。例如：

```java
@RequestMapping("/accounts/{id}")public Account getAccount(@PathVariable("id") String id)
```

可以改写为：

```java
@RequestMapping("/accounts/{id}")public Account getAccount(@PathVariable String id)
```

可以看到我们把`@PathVariable(“id”)`替换成@PathVariable，因为Spring 4能从编译后的Java 8代码中获取参数名——id。只要在编译时指定了–parameters标记，Java 8编译器就会把参数名写入.class文件中。在Java 8发布之前，Spring也可以从使用-debug选项编译之后的代码中提取出参数名。

在Java 7及之前的版本中，-debug选项不会保留抽象方法的参数名。这会导致Spring Data这类基于Java接口自动生成其资源库实现的工程就会出现问题。比如接口如下：

```java
interface CustomerRepository extends CrudRepository<Customer, Long> {
  @Query("select c from Customer c where c.lastname = :lastname")
  List<Customer> findByLastname(@Param("lastname") String lastname);}
```

我们能看到findByLastname仍然需要@Param(“lastname”)，这是因为findByLastname是个抽象方法，而在Java 7及之前的版本里就算用了-debug选项也不会保留其参数名。而在Java 8中，使用–parameters选项后，Spring Data就能自动找到抽象方法的参数名，我们可以把上例中的接口改写成：

```java
interface CustomerRepository extends CrudRepository<Customer, Long> {
  @Query("select c from Customer c where c.lastname = :lastname")
  List<Customer> findByLastname(String lastname);}
```

这里我们已经不再需要@Param(“lastname”)，让代码更简洁且易于阅读。所以使用Java 8编译代码时加上–parameters标记是个好方法。

## 总结
Spring 4支持Java 6、7和8，开发者可以随意使用Java 6、7或8来编写自己的应用代码。如果使用的是Java 8，那么只要有函数式接口的地方就可以使用lambda表达式，使代码更为简洁易读。

Java 8对某些库做了改进，比如新的java.time包和Optional类，Optional类使得用Spring编写的代码更加简单明了。

最后，用–parameters选项编译Java 8代码会在编译时保留方法的参数名，使得开发者得以编写更为紧凑的Spring MVC方法和Spring Data查询方法。

如果你已经准备在项目中使用Java 8，你会发现Spring 4是个很好地利用了Java 8新特性的出色框架。