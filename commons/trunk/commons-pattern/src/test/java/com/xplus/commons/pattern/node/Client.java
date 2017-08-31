package com.xplus.commons.pattern.node;

public class Client {
	
	public static void main(String args[]) {
		String instruction = "up move 5 and down run 10 and left move 5 and right run 30 and down move 15";
		InstructionHandler handler = new InstructionHandler();
		handler.handle(instruction);
		String outString;
		outString = handler.output();
		System.out.println(outString);
	}
	
}
