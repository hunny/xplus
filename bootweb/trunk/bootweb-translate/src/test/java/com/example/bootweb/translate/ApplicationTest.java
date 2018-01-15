package com.example.bootweb.translate;

import org.junit.Test;

import com.example.bootweb.translate.google.tk.Tk;
import com.example.bootweb.translate.google.tk.Tk0;
import com.example.bootweb.translate.google.tk.Tk1;

import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class ApplicationTest extends TestCase {
  /**
   * Create the test case
   *
   * @param testName
   *          name of the test case
   */
  public ApplicationTest(String testName) {
    super(testName);
  }

  /**
   * @return the suite of tests being tested
   */
  public static junit.framework.Test suite() {
    return new TestSuite(ApplicationTest.class);
  }

  /**
   * Rigourous Test :-)
   */
  public void testApp() {
    assertTrue(true);
  }
  
  @Test
  public void testTk() {
    Tk tk0 = new Tk0();
    Tk tk1 = new Tk1();
    assertTrue(tk0.calc("Hello World!").equals(tk1.calc("Hello World!")));
  }
}
