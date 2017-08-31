package com.xplus.commons.pattern.node;

//距离解释：终结符表达式
public class DistanceNode implements Node {

	private String distance;
	
	public DistanceNode(String distance) {
		this.distance = distance;
	}
	
	//距离表达式的解释操作
	@Override
	public String interpret() {
		return this.distance;
	}

}
