package com.xplus.commons.junit.parameterized;

public class EvenNumberChecker {

	/**
	 * Is input number even.
	 * 
	 * @param i
	 *          input number
	 * @return <code>true</code> if input is even number; otherwise return false
	 */
	public boolean isEven(int i) {
		if (i % 2 == 0) {
			return true;
		} else {
			return false;
		}
	}

}
