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
		factory = new ConcreteFactoryA();
		produce(factory);
		
		factory = new ConcreteFactoryB();
		produce(factory);
		
		System.out.println("从配置文件中读取类配置：");
		File xml = new File(Client.class.getResource("").getPath() + "/config.xml");
		
		factory = XmlLoadFactory.getBean(xml);
		produce(factory);
	}
	
	public static void produce(Factory factory) {
		System.out.println("工厂准备生产产品======");
		Product product = null;
		product = factory.factoryMethod();
		product.productMethod();
	}

}
