/**
 * 
 */
package com.xplus.commons.pattern.creational.simple.factory.category1;

/**
 * 图表
 * 
 * @author hunnyhu
 *
 */
public class Chart {

	private String type;

	public Chart(Object[][] data, String type) {
		this.type = type;
		if ("histogram".equalsIgnoreCase(type)) {
			// 初始化柱状图
		} else if ("pie".equalsIgnoreCase(type)) {
			// 初始化饼状图
		} else if ("line".equalsIgnoreCase(type)) {
			// 初始化拆线图
		}
	}
	
	public void display() {
		if ("histogram".equalsIgnoreCase(type)) {
			// 显示柱状图
		} else if ("pie".equalsIgnoreCase(type)) {
			// 显示饼状图
		} else if ("line".equalsIgnoreCase(type)) {
			// 显示拆线图
		}
	}
}
