package com.xplus.commons.pattern.commandnode;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

//表达式节点类：非终结符表达式
public class ExpressionNode implements Node {
	private List<Node> list = new ArrayList<Node>(); // 定义一个集合用于存储多条命令

	public void interpret(Context context) {
		// 循环处理Context中的标记
		while (true) {
			if (context.currentToken() == null) {
				// 如果已经没有任何标记，则退出解释
				break;
			} else if (context.currentToken().equals("END")) {
				// 如果标记为END，则不解释END并结束本次解释过程，可以继续之后的解释
				context.skipToken("END");
				break;
			} else {
				// 如果为其他标记，则解释标记并将其加入命令集合
				Node commandNode = new CommandNode();
				commandNode.interpret(context);
				list.add(commandNode);
			}
		}
	}

	// 循环执行命令集合中的每一条命令
	public void execute() {
		Iterator<Node> iterator = list.iterator();
		while (iterator.hasNext()) {
			iterator.next().execute();
		}
	}

}
