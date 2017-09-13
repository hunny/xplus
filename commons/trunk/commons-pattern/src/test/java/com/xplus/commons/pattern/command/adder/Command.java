package com.xplus.commons.pattern.command.adder;

public interface Command {

	int execute(int value); // 声明命令执行方法execute()

	int undo(); // 声明撤销方法undo()
}
