package com.xplus.commons.junit;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

/**
 * 执行顺序 一个测试类单元测试的执行顺序为：
 * 
 * @BeforeClass –> @Before –> @Test –> @After –> @AfterClass
 * 
 * 每一个测试方法的调用顺序为：
 * @Before –> @Test –> @After
 * 
 */
public class JunitBaseTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		System.out.println("All Test BeforeClass================");
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		System.out.println("All Test AfterClass=================");
	}

	@Before
	public void before() {
		System.out.println("Test method Before");
	}

	@After
	public void after() {
		System.out.println("Test method After");
	}

	@Test
	public void testTest() {
		System.out.println("测试用例。");
	}

	@Test
	@Ignore
	public void testIgnore() {
		System.out.println("测试忽略用例。");
	}

	@Test(timeout = 100)
	public void testTimeout1() throws Exception {
		System.out.println("测试用例最多执行时长。");
		Thread.sleep(500);
		System.out.println("这句测试用例语句不会输出。");
	}

	@Test(timeout = 100)
	public void testTimeout2() throws Exception {
		System.out.println("测试用例最多执行时长。");
		Thread.sleep(50);
		System.out.println("测试用例最多执行时长结束。");
	}

	@Test(expected = Exception.class)
	public void testException() throws Exception {
		System.out.println("测试用例抛出异常。");
		throw new Exception("异常测试。");
	}

}
