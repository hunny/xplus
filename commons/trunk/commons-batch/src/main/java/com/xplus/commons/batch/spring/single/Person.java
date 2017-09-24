package com.xplus.commons.batch.spring.single;

import java.io.Serializable;

import javax.validation.constraints.Size;

/**
 * 1. 领域模型类。<br>
 * 2. 读取文件的数据源person.csv。<br>
 * 3. 输出文件的数据库脚本person.sql。<br>
 *
 */
public class Person implements Serializable {

	private static final long serialVersionUID = -6076714054593205350L;

	@Size(max = 4, min = 2) // 使用JSR-303注解来校验数据。
	private String name;
	private int age;
	private String nation;
	private String address;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getNation() {
		return nation;
	}

	public void setNation(String nation) {
		this.nation = nation;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	@Override
	public String toString() {
		return "Person [name=" + name + ", age=" + age + ", nation=" + nation + ", address=" + address + "]";
	}

}
