## 注解和断言的作用
JUnit 的注解。
@Test (expected = Exception.class)       表示预期会抛出Exception.class 的异常
@Ignore 含义是“某些方法尚未完成，暂不参与此次测试”。这样的话测试结果就会提示你有几个测试被忽略，而不是失败。一旦你完成了相应函数，只需要把@Ignore注解删去，就可以进行正常的测试。
@Test(timeout=100)       表示预期方法执行不会超过 100 毫秒，控制死循环
@Before 表示该方法在每一个测试方法之前运行，可以使用该方法进行初始化之类的操作
@After 表示该方法在每一个测试方法之后运行，可以使用该方法进行释放资源，回收内存之类的操
@BeforeClass  表示该方法只执行一次，并且在所有方法之前执行。一般可以使用该方法进行数据库连接操作，注意该注解运用在静态方法。
@AfterClass    表示该方法只执行一次，并且在所有方法之后执行。一般可以使用该方法进行数据库连接关闭操作，注意该注解运用在静态方法。

TestSuite
如果你须有多个测试单元，可以合并成一个测试套件进行测试，况且在一个项目中，只写一个测试类是不可能的，我们会写出很多很多个测试类。可是这些测试类必须一个一个的执行，也是比较麻烦的事情。鉴于此， JUnit 为我们提供了打包测试的功能，将所有需要运行的测试类集中起来，一次性的运行完毕，大大的方便了我们的测试工作。并且可以按照指定的顺序执行所有的测试类。下面的代码示例创建了一个测试套件来执行两个测试单元。如果你要添加其他的测试单元可以使用语句 @Suite.SuiteClasses 进行注解。
```
import org.junit.runner.RunWith;  
import org.junit.runners.Suite;  
import org.junit.runners.Suite.SuiteClasses;  
  
@RunWith( Suite.class )  
@SuiteClasses( { JUnit1Test.class, StringUtilTest.class } )  
public class JSuit {  
  
}
```
TestSuite 测试包类——多个测试的组合 TestSuite 类负责组装多个 Test Cases。待测得类中可能包括了对被测类的多个测试，而 TestSuit 负责收集这些测试，使我们可以在一个测试中，完成全部的对被测类的多个测试。 TestSuite 类实现了 Test 接口，且可以包含其它的 TestSuites。它可以处理加入Test 时的所有抛出的异常。
TestResult 结果类集合了任意测试累加结果，通过 TestResult 实例传递个每个测试的 Run() 方法。TestResult 在执行 TestCase 是如果失败会异常抛出 
TestListener 接口是个事件监听规约，可供 TestRunner 类使用。它通知 listener 的对象相关事件，方法包括测试开始 startTest(Test test)，测试结束 endTest(Test test),错误，增加异常 addError(Test test, Throwable t) 和增加失败 addFailure(Test test, AssertionFailedError t) 。TestFailure 失败类是个“失败”状况的收集类，解释每次测试执行过程中出现的异常情况，其 toString() 方法返回“失败”状况的简要描述。

## 断言核心方法
assertArrayEquals(expecteds, actuals)	查看两个数组是否相等。
assertEquals(expected, actual)	查看两个对象是否相等。类似于字符串比较使用的equals()方法
assertNotEquals(first, second)	查看两个对象是否不相等。
assertNull(object)	查看对象是否为空。
assertNotNull(object)	查看对象是否不为空。
assertSame(expected, actual)	查看两个对象的引用是否相等。类似于使用“==”比较两个对象
assertNotSame(unexpected, actual)	查看两个对象的引用是否不相等。类似于使用“!=”比较两个对象
assertTrue(condition)	查看运行结果是否为true。
assertFalse(condition)	查看运行结果是否为false。
assertThat(actual, matcher)	查看实际值是否满足指定的条件
fail()	让测试失败

## 核心——注解
说明
@Before	初始化方法
@After	释放资源
@Test	测试方法，在这里可以测试期望异常和超时时间
@Ignore	忽略的测试方法
@BeforeClass	针对所有测试，只执行一次，且必须为static void
@AfterClass	针对所有测试，只执行一次，且必须为static void
@RunWith	指定测试类使用某个运行器
@Parameters	指定测试类的测试数据集合
@Rule	允许灵活添加或重新定义测试类中的每个测试方法的行为
@FixMethodOrder	指定测试方法的执行顺序

## 执行顺序
一个测试类单元测试的执行顺序为：
@BeforeClass –> @Before –> @Test –> @After –> @AfterClass
每一个测试方法的调用顺序为：
@Before –> @Test –> @After