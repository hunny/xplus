/**
 * 
 */
package com.xplus.commons.pattern.creational.factorymethod;

import java.io.File;

import com.xplus.commons.pattern.XmlLoadFactory;

/**
 * @author hunnyhu
 *
 */
public class Client {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Factory factory = null;
		Product product = null;
		factory = new ConcreteFactoryA();
		product = factory.factoryMethod();
		product.productMethod();
		
		factory = new ConcreteFactoryB();
		product = factory.factoryMethod();
		product.productMethod();
		
		System.out.println("从配置文件中读取类配置：");
		File xml = new File(Client.class.getResource("").getPath() + "/config.xml");
		
		factory = XmlLoadFactory.getBean(xml);
		product = factory.factoryMethod();
		product.productMethod();
	}

}
