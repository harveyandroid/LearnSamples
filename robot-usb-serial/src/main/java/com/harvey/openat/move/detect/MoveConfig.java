package com.harvey.openat.move.detect;

public class MoveConfig {
	/**
	 * 移动的指令 1前 2后 3左 4右 5左前 6右前
	 */
	public static final int TYPE_ORDER_SLOW_STOP = -2;
	public static final int TYPE_ORDER_STOP = -1;
	public static final int TYPE_ORDER_FRONT = 1;
	public static final int TYPE_ORDER_BACK = 2;
	public static final int TYPE_ORDER_LEFT = 3;
	public static final int TYPE_ORDER_RIGHT = 4;
	public static final int TYPE_ORDER_LEFT_FRONT = 5;
	public static final int TYPE_ORDER_RIGHT_FRONT = 6;

	public final static float DEFAULT_FAST_SPEED = 0.7f;// 默认快速移动的速度（单位m/s）

	public final static float DEFAULT_TURN_SPEED = 0.05f;// 默认转弯的速度（单位rad/s）

	public final static float DEFAULT_SLOW_MAX_SPEED = 0.5f;// 默认缓慢的最大速度（单位m/s）

	public final static float DEFAULT_SLOW_MIN_SPEED = 0.3f;// 默认慢移动最小速度（起步）（单位m/s）

	public final static float DEFAULT_ADD_SPEED = 0.2f;// 递增的差量（单位m/s）

	public final static float DEFAULT_SUBTRACT_SPEED = 0.3f;// 递减的差量（单位m/s）

	public final static int DEFAULT_MIX_SPEED = 20;// 默认最小移动速度
	public final static int DEFAULT_MAX_SPEED = 80;// 默认最大移动速度

	public final static float DEFAULT_HIGH_SPEED = 1.0f;// 默认高速
	public final static float DEFAULT_MEDIUM_SPEED = 0.7f;// 默认中速
	public final static float DEFAULT_LOW_SPEED = 0.5f;// 默认低速
}
