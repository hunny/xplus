package com.xplus.commons.pattern.creational.singleton;

import com.xplus.commons.pattern.creational.singleton.iodh.Singleton;

public class Client {

	public static void main(String args[]) {
		// 使用IoDH的方式，产生单例
		Singleton s1, s2;
		s1 = Singleton.getInstance();
		s2 = Singleton.getInstance();
		System.out.println(s1 == s2);
	}

}
