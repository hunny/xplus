package com.xplus.commons.junit.mockito;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class MockitoJUnitRunnerTest {
	
	@Mock
	private List<String> list;

	@Test
	public void shouldDoSomething() {
		list.add("100");
	}
}
