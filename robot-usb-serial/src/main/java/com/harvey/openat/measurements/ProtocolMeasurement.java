package com.harvey.openat.measurements;

import com.harvey.openat.utils.HexDump;

/**
 * Created by hanhui on 2016-7-22.
 */
public abstract class ProtocolMeasurement extends ProtocolBean {

	protected ProtocolMeasurement() {
	}

	public ProtocolMeasurement(byte[] protocol_source, int length) throws ProtocolException {
		super(protocol_source, length);
		if (!(protocol_begin == getHead() && model_control == getControl() && model_addr == getAddr())) {
			throw new ProtocolException("protocol measurement equals error");
		}
	}

	/**
	 * 发送字符串
	 */
	public byte[] initSend() {
		protocol_length = model_content_length + 8;
		protocol_source = new byte[protocol_length];

		protocol_source[0] = getHead();
		protocol_source[1] = getAddr();
		protocol_source[2] = getControl();
		protocol_source[3] = getTime();
		protocol_source[4] = 0x00;
		protocol_source[5] = (byte) getContentLength();

		System.arraycopy(model_content, 0, protocol_source, 6, model_content_length);

		protocol_source[6 + model_content_length] = HexDump.getChecksum(protocol_source, protocol_length - 2);
		protocol_source[7 + model_content_length] = HexDump.getChecksum_counter(protocol_source, protocol_length - 2);

		return protocol_source;
	}

	public abstract byte getHead();

	public abstract byte getControl();

	public abstract byte getTime();

	public abstract byte getAddr();

	public abstract int getContentLength();
}
