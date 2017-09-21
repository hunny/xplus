package com.xplus.commons.junit;

import junit.framework.JUnit4TestAdapter;
import junit.framework.Test;
import junit.framework.TestSuite;

public class JTestSuit1 {

	public static Test suite() {
		TestSuite suite = new TestSuite("Test for package1");
		suite.addTest(new JUnit4TestAdapter(JunitBaseTest.class));
		suite.addTest(new JUnit4TestAdapter(AssertTests.class));
		return suite;
	}
}
