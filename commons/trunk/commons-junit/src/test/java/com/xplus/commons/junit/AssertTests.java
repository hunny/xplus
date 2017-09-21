package com.xplus.commons.junit;

import java.util.Arrays;

import org.hamcrest.CoreMatchers;
import org.hamcrest.core.CombinableMatcher;
import org.junit.Assert;
import org.junit.Test;

public class AssertTests {

	@Test
	public void testAssertArrayEquals() {
		byte[] expected = "trial".getBytes();
		byte[] actual = "trial".getBytes();
		Assert.assertArrayEquals("failure - byte arrays not same", //
				expected, actual);
	}

	@Test
	public void testAssertEquals() {
		Assert.assertEquals("failure - strings not same", 5l, 5l);
	}

	@Test
	public void testAssertFalse() {
		Assert.assertFalse("failure - should be false", false);
	}

	@Test
	public void testAssertNotNull() {
		Assert.assertNotNull("should not be null", new Object());
	}

	@Test
	public void testAssertNotSame() {
		Assert.assertNotSame("should not be same Object", //
				new Object(), new Object());
	}

	@Test
	public void testAssertNull() {
		Assert.assertNull("should be null", null);
	}

	@Test
	public void testAssertSame() {
		Integer aNumber = Integer.valueOf(768);
		Assert.assertSame("should be same", aNumber, aNumber);
	}

	// JUnit Matchers assertThat
	@Test
	public void testAssertThatBothContainsString() {
		Assert.assertThat("albumen", //
				CoreMatchers.both(CoreMatchers.containsString("a")) //
						.and(CoreMatchers.containsString("b")));
	}

	@Test
	public void testAssertThathasItemsContainsString() {
		Assert.assertThat(Arrays.asList("one", "two", "three"), //
				CoreMatchers.hasItems("one", "three"));
	}

	@Test
	public void testAssertThatEveryItemContainsString() {
		Assert.assertThat(Arrays.asList(new String[] { "fun", "ban", "net" }), //
				CoreMatchers.everyItem(CoreMatchers.containsString("n")));
	}

	// Core Hamcrest Matchers with assertThat
	@Test
	public void testAssertThatHamcrestCoreMatchers() {
		Assert.assertThat("good", CoreMatchers.allOf(CoreMatchers.equalTo("good"), CoreMatchers.startsWith("good")));
		Assert.assertThat("good",
				CoreMatchers.not(CoreMatchers.allOf(CoreMatchers.equalTo("bad"), CoreMatchers.equalTo("good"))));
		Assert.assertThat("good", CoreMatchers.anyOf(CoreMatchers.equalTo("bad"), CoreMatchers.equalTo("good")));
		Assert.assertThat(7,
				CoreMatchers.not(CombinableMatcher.<Integer>either(CoreMatchers.equalTo(3)).or(CoreMatchers.equalTo(4))));
		Assert.assertThat(new Object(), CoreMatchers.not(CoreMatchers.sameInstance(new Object())));
	}
}
