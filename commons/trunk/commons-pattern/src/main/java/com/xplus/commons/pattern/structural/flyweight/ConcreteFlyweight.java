package com.xplus.commons.pattern.structural.flyweight;

public class ConcreteFlyweight implements Flyweight {
	// 内部状态intrinsicState作为成员变量，同一个享元对象其内部状态是一致的
	private String intrinsicState;

	public ConcreteFlyweight(String intrinsicState) {
		this.intrinsicState = intrinsicState;
	}

	// 外部状态extrinsicState在使用时由外部设置，不保存在享元对象中，
	// 即使是同一个对象，在每一次调用时也可以传入不同的外部状态
	public void operation(String extrinsicState) {
		System.out.println(intrinsicState + ":" + extrinsicState);
	}
}
