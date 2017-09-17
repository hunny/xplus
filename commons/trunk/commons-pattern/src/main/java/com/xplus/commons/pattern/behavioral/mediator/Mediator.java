package com.xplus.commons.pattern.behavioral.mediator;

import java.util.ArrayList;
import java.util.List;

public abstract class Mediator {
	protected final List<Colleague> colleagues = new ArrayList<Colleague>(); // 用于存储同事对象

	// 注册方法，用于增加同事对象
	public void register(Colleague colleague) {
		colleagues.add(colleague);
	}

	// 声明抽象的业务方法
	public abstract void operation();
}
