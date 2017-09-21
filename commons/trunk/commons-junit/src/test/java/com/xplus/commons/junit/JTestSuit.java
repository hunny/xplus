package com.xplus.commons.junit;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 * 测试集合。
 * @author hunnyhu
 *
 */
@RunWith(Suite.class)
@SuiteClasses({ JunitBaseTest.class, AssertTests.class })
public class JTestSuit {

	@Test
	public void testSuit() {
		System.out.println("测试一个测试单元集合。");
		Assert.fail();
		System.out.println("测试一个测试单元集合，该方法是不会被测试的。");
	}
	
}
