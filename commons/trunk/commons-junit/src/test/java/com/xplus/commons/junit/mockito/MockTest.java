package com.xplus.commons.junit.mockito;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

/**
 * Mockito
 * 
 * 参考 https://segmentfault.com/a/1190000006746409
 *
 */
public class MockTest {

	/**
	 * 创建 Mock 对象
	 */
	@SuppressWarnings("rawtypes")
	@Test
	public void createMockObject() {
		// 使用 mock 静态方法创建 Mock 对象.
		List mockedList = Mockito.mock(List.class);
		Assert.assertTrue(mockedList instanceof List);

		// mock 方法不仅可以 Mock 接口类, 还可以 Mock 具体的类型.
		ArrayList mockedArrayList = Mockito.mock(ArrayList.class);
		Assert.assertTrue(mockedArrayList instanceof List);
		Assert.assertTrue(mockedArrayList instanceof ArrayList);
	}

	/**
	 * 配置 Mock 对象: 当我们有了一个 Mock 对象后, 我们可以定制它的具体的行为
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test
	public void configMockObject() {
		List mockedList = Mockito.mock(List.class);

		// 我们定制了当调用 mockedList.add("one") 时, 返回 true
		Mockito.when(mockedList.add("one")).thenReturn(true);
		// 当调用 mockedList.size() 时, 返回 1
		Mockito.when(mockedList.size()).thenReturn(1);

		Assert.assertTrue(mockedList.add("one"));
		// 因为我们没有定制 add("two"), 因此返回默认值, 即 false.
		Assert.assertFalse(mockedList.add("two"));
		Assert.assertEquals(mockedList.size(), 1);

		Iterator i = Mockito.mock(Iterator.class);
		Mockito.when(i.next()).thenReturn("Hello,").thenReturn("Mockito!");
		String result = i.next() + " " + i.next();
		// assert
		Assert.assertEquals("Hello, Mockito!", result);
	}

	@SuppressWarnings("rawtypes")
	@Test(expected = NoSuchElementException.class)
	public void testForIOException() throws Exception {
		Iterator i = Mockito.mock(Iterator.class);
		Mockito.when(i.next()).thenReturn("Hello,").thenReturn("Mockito!"); // 1
		String result = i.next() + " " + i.next(); // 2
		Assert.assertEquals("Hello, Mockito!", result);

		Mockito.doThrow(new NoSuchElementException()).when(i).next(); // 3
		i.next(); // 4
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test
	public void testVerify() {
		List mockedList = Mockito.mock(List.class);
		mockedList.add("one");
		mockedList.add("two");
		mockedList.add("three times");
		mockedList.add("three times");
		mockedList.add("three times");
		Mockito.when(mockedList.size()).thenReturn(5);
		Assert.assertEquals(mockedList.size(), 5);

		Mockito.verify(mockedList, Mockito.atLeastOnce()).add("one");
		Mockito.verify(mockedList, Mockito.times(1)).add("two");
		Mockito.verify(mockedList, Mockito.times(3)).add("three times");
		Mockito.verify(mockedList, Mockito.atLeastOnce()).add("three times");
		Mockito.verify(mockedList, Mockito.never()).isEmpty();
	}

	/**
	 * 使用 spy() 部分模拟对象: Mockito 提供的 spy 方法可以包装一个真实的 Java 对象, 并返回一个包装后的新对象.
	 * 若没有特别配置的话, 对这个新对象的所有方法调用, 都会委派给实际的 Java 对象.
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test
	public void testSpy() {
		List list = new LinkedList();
		List spy = Mockito.spy(list);

		// 对 spy.size() 进行定制.
		Mockito.when(spy.size()).thenReturn(100);

		spy.add("one");
		spy.add("two");

		// 因为我们没有对 get(0), get(1) 方法进行定制,
		// 因此这些调用其实是调用的真实对象的方法.
		Assert.assertEquals(spy.get(0), "one");
		Assert.assertEquals(spy.get(1), "two");

		Assert.assertEquals(spy.size(), 100);
		Mockito.when(spy.get(0)).thenReturn("100");
		Assert.assertEquals(spy.get(0), "100");
	}

	/**
	 * 参数捕获: Mockito 允准我们捕获一个 Mock 对象的方法调用所传递的参数
	 * 
	 * <p>
	 * 通过 verify(mockedList).addAll(argument.capture()) 语句来获取 mockedList.addAll
	 * 方法所传递的实参 list.
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test
	public void testCaptureArgument() {
		List<String> list = Arrays.asList("1", "2");
		List mockedList = Mockito.mock(List.class);
		ArgumentCaptor<List> argument = ArgumentCaptor.forClass(List.class);
		mockedList.addAll(list);
		Mockito.verify(mockedList).addAll(argument.capture());

		Assert.assertEquals(2, argument.getValue().size());
		Assert.assertEquals(list, argument.getValue());
	}
}
