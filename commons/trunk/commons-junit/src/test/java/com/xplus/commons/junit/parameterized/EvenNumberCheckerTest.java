package com.xplus.commons.junit.parameterized;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

//Step 1  
@RunWith(Parameterized.class)
public class EvenNumberCheckerTest {

	// Step 2: variables to be used in test method of Step 5
	private int inputNumber;
	private boolean isEven;

	// Step 3: parameterized constructor
	public EvenNumberCheckerTest(int inputNumber, boolean isEven) {
		super();
		this.inputNumber = inputNumber;
		this.isEven = isEven;
	}

	// Step 4: data set of variable values
	@Parameters
	public static Collection<Object[]> data() {
		Object[][] data = new Object[][] { //
			{ 2, true }, //
			{ 5, false }, //
			{ 10, false } };
		return Arrays.asList(data);

	}

	@Test
	public void test() {
		System.out.println("inputNumber: " + inputNumber + "; isEven: " + isEven);
		EvenNumberChecker evenNumberChecker = new EvenNumberChecker();
		// Step 5: use variables in test code
		boolean actualResult = evenNumberChecker.isEven(inputNumber);
		Assert.assertEquals(isEven, actualResult);
	}
}
