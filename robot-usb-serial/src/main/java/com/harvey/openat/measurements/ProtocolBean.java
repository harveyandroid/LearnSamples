package com.harvey.openat.measurements;

import android.util.Log;

import com.harvey.openat.utils.HexDump;

/**
 * Created by hanhui on 2016-7-22.
 */
public class ProtocolBean {

	// 原始报文
	public byte[] protocol_source;
	public int protocol_length; // 原始报文长度
	// 原始报文内容
	public byte protocol_begin; // 起始帧
	public byte protocol_checksum; // 校验和原码
	public byte protocol_checksum_counter; // 校验和反码
	// 内容帧
	public byte model_addr; // 模块地址
	public byte model_control; // 操作命令
	// 内容帧
	public byte[] model_content; // 数据内容
	public int model_content_length; // 数据内容长度

	protected ProtocolBean() {

	}

	public ProtocolBean(byte[] protocol_source, int length) throws ProtocolException {
		// 检验长度是否正确
		protocol_length = ((int) protocol_source[5] & 0xff) * 4 + 8;
		if (protocol_length != length || protocol_source[4] != 0) {
			Log.d("uarterror", "protocol error length protocol_length->" + protocol_length + " length->" + length);
			Log.d("uarterror", HexDump.toHexString(protocol_source, length));
			throw new ProtocolException(
					"protocol error length protocol_length->" + protocol_length + " length->" + length);
		}

		// 检验校验和是否正确
		if (protocol_source[protocol_length - 2] != HexDump.getChecksum(protocol_source, protocol_length - 2)) {
			Log.d("uarterror", "protocol error checksum");
			Log.d("uarterror", HexDump.toHexString(protocol_source, length));
			throw new ProtocolException("protocol error checksum");
		}

		if (protocol_source[protocol_length - 1] != HexDump.getChecksum_counter(protocol_source, protocol_length - 2)) {

			Log.d("uarterror", "protocol error checksum_counter");
			Log.d("uarterror", HexDump.toHexString(protocol_source, length));
			throw new ProtocolException("protocol error checksum_counter");
		}

		// 原始报文
		this.protocol_source = protocol_source;
		this.protocol_length = length; // 原始报文长度
		// 原始报文内容
		model_addr = protocol_source[1];
		model_control = protocol_source[2];

		this.protocol_begin = protocol_source[0]; // 起始帧
		this.protocol_checksum = protocol_source[length - 2]; // 校验和原码
		this.protocol_checksum_counter = protocol_source[length - 1]; // 校验和反码

		// 数据内容
		this.model_content_length = protocol_source[5] * 4; // 数据内容长度每个数据4个字节
		this.model_content = new byte[this.model_content_length];
		for (int i = 0; i < this.model_content_length; i++) {
			this.model_content[i] = this.protocol_source[i + 6];
		}
	}
	public String getCodeName() {
		return protocol_begin + "." + model_addr + "." + model_control;
	}
	// 转换为16进制字符串
	public String toHexString() {
		StringBuffer sb = new StringBuffer(protocol_length);
		String sTemp;

		for (int i = 0; i < protocol_length; i++) {
			sTemp = Integer.toHexString(0xFF & protocol_source[i]);
			if (sTemp.length() < 2)
				sb.append(0);
			sb.append(sTemp.toUpperCase() + " ");
		}
		sb.append("\n");
		return sb.toString();
	}

}
