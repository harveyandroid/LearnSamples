package com.harvey.openat.move;


import com.harvey.openat.move.detect.MoveConfig;

/**
 * 控制
 */
public enum Control {
	STOP(MoveConfig.TYPE_ORDER_STOP),

	FRONT(MoveConfig.TYPE_ORDER_FRONT),

	BACK(MoveConfig.TYPE_ORDER_BACK),

	LEFT(MoveConfig.TYPE_ORDER_LEFT),

	RIGHT(MoveConfig.TYPE_ORDER_RIGHT),

	LEFT_FRONT(MoveConfig.TYPE_ORDER_LEFT_FRONT),

	RIGHT_FRONT(MoveConfig.TYPE_ORDER_RIGHT_FRONT);

	private int value;

	Control(int value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "Control{" + "value=" + value + '}';
	}

	public int intValue() {
		return value;
	}
}
