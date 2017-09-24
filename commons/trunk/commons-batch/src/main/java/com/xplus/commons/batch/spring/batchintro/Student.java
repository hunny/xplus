package com.xplus.commons.batch.spring.batchintro;

import java.io.Serializable;

public class Student implements Serializable {
	
	private static final long serialVersionUID = -1814536947545870104L;
	
	private String firstName;
	private String lastName;
	private String email;
  private int age;

  public Student() {
  }

  public Student(String firstName, String lastName, String email, int age) {
      this.firstName = firstName;
      this.lastName = lastName;
      this.email = email;
      this.age = age;
  }

  public String getFirstName() {
      return firstName;
  }

  public void setFirstName(String firstName) {
      this.firstName = firstName;
  }

  public String getLastName() {
      return lastName;
  }

  public void setLastName(String lastName) {
      this.lastName = lastName;
  }

  public String getEmail() {
      return email;
  }

  public void setEmail(String email) {
      this.email = email;
  }

  public int getAge() {
      return age;
  }

  public void setAge(int age) {
      this.age = age;
  }
}
