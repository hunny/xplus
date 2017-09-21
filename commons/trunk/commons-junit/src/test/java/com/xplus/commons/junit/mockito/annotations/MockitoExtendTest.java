package com.xplus.commons.junit.mockito.annotations;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;

public class MockitoExtendTest extends MockitoAnnotationBaseTest {

	@SuppressWarnings("rawtypes")
	@Mock
	private List list;

	@SuppressWarnings("rawtypes")
	@Spy
	private List spy = new ArrayList();

	@SuppressWarnings("unchecked")
	@Before
	public void setup() {
		Mockito.when(list.get(0)).thenReturn("OK");

		Mockito.when(spy.size()).thenReturn(100);
		spy.add("one");
	}

	@Test
	public void testMock() {
		Assert.assertEquals("相同", list.get(0), "OK");
	}

	@Test(expected = IndexOutOfBoundsException.class)
	public void testSpy() {
		Assert.assertEquals(spy.get(0), "one");
		Assert.assertEquals(spy.size(), 100);
		spy.get(1);// throw IndexOutOfBoundsException
	}

}
