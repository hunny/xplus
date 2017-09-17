package com.xplus.commons.pattern.behavioral.mediator.demo;

/**
 * 按钮类：具体同事类
 */
public class Button extends Component {

	@Override
	public void update() {
		System.out.println("按钮不产生交互");
	}

}
