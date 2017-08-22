package com.harvey.openat.measurements;

import com.harvey.openat.utils.HexDump;

/**
 * 移动指令 Created by hanhui on 2017/8/4 0004 15:47
 */

public class CRM_Traveling_Fast1 extends ProtocolMeasurement {
	// 头
	public static final byte BEGIN = (byte) 0xAA;
	// 地址（u8）
	public static final byte ADDR = 0x01;
	// 命令（u8）
	public static final byte CONTROL = 0x11;
	// 时间戳（u8）
	public static final int time = 0;
	// 数据数量
	public static final int length = 2;
	// 数据类型
	public static final String CODE_NAME = BEGIN + "." + ADDR + "." + CONTROL;

	public CRM_Traveling_Fast1(float linear_speed, float angular_speed) {
		model_content_length = 8;
		model_content = new byte[model_content_length];
		byte[] linear = HexDump.floatToByteArray(linear_speed);
		byte[] angular = HexDump.floatToByteArray(angular_speed);
		System.arraycopy(linear, 0, model_content, 0, 4);
		System.arraycopy(angular, 0, model_content, 4, 4);
		initSend();
	}

	public CRM_Traveling_Fast1(byte[] protocol_source, int length) throws ProtocolException {
		super(protocol_source, length);
	}

	@Override
	public byte getHead() {
		return BEGIN;
	}

	@Override
	public byte getControl() {
		return CONTROL;
	}

	@Override
	public byte getTime() {
		return time;
	}

	@Override
	public byte getAddr() {
		return ADDR;
	}

	@Override
	public int getContentLength() {
		return length;
	}

}
