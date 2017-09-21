package com.xplus.commons.junit.mockito.annotations;

import org.junit.Before;
import org.mockito.MockitoAnnotations;

public class MockitoAnnotationBaseTest {
	@Before
	public void initMocks() {
		MockitoAnnotations.initMocks(this);
	}
}
