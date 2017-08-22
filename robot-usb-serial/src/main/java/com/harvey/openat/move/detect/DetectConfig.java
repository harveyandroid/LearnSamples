package com.harvey.openat.move.detect;

public class DetectConfig {
	// 超声波在检测数据中的位置
	public final static int LEFT_FRONT_WAVE_POSITION = 2;
	public final static int RIGHT_FRONT_WAVE_POSITION = 3;
	public final static int FRONT_WAVE_POSITION = 0;
	public final static int RIGHT_WAVE_POSITION = 1;
	public final static int LEFT_WAVE_POSITION =2;
	public final static int BACK_WAVE_POSITION = 3;

	public static final long WAIT_ORDER_DELAY_TIME = 400;// 等待指令时间
	public static final long DETECT_MOVE_DELAY_TIME = 200;// 发送指令的延迟

	public final static int SLOW_DOWN_MIN_WAVE_DETECTION_DISTANCE = 2000;// 需要减速行驶的超声波检测最小距离单位mm
	public final static int MIN_FRONT_WAVE_DETECTION_DISTANCE = 550;// 超声波检测前的最小距离单位mm
	public final static int MIN_LEFT_FRONT_WAVE_DETECTION_DISTANCE = 300;// 超声波检测左前的最小距离单位mm
	public final static int MIN_RIGHT_FRONT_WAVE_DETECTION_DISTANCE = 300;// 超声波检测右前的最小距离单位mm
	public final static int MIN_BACK_WAVE_DETECTION_DISTANCE = 500;// 超声波检测后的最小距离单位mm
	public final static int MIN_AROUND_WAVE_DETECTION_DISTANCE = 300;// 超声波检测左右的最小距离单位mm
}
