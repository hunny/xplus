package com.xplus.commons.pattern.behavioral.state.onoff;

public interface State {
	
	void on(Switch s);

	void off(Switch s);
}
