package com.harvey.openat.measurements;

/**
 * Created by hanhui on 2017/8/4 0004 15:47 移动距离
 */
public class CRM_Traveling_Distance extends ProtocolMeasurement {
	// 头
	public static final byte BEGIN = (byte) 0xAA;
	// 地址（u8）
	public static final byte ADDR = 0x01;
	// 命令（u8）
	public static final byte CONTROL = 0x12;
	// 时间戳（u8）
	public static final int time = 0;
	// 数据数量
	public static final int length = 0;
	// 数据类型
	public static final String CODE_NAME = BEGIN + "." + ADDR + "." + CONTROL;

	public CRM_Traveling_Distance() {
		model_content_length = 0;
		model_content = new byte[0];
		initSend();
	}

	public CRM_Traveling_Distance(byte[] protocol_source, int length) throws ProtocolException {
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
