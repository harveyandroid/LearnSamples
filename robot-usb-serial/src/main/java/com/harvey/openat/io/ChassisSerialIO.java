package com.harvey.openat.io;

import android.util.Log;

import com.harvey.openat.utils.HexDump;
import com.harvey.openat.driver.UsbSerialPort;
import com.harvey.openat.measurements.CRM_Traveling_Distance;
import com.harvey.openat.measurements.CRM_Traveling_Fast1;
import com.harvey.openat.measurements.ProtocolBean;
import com.harvey.openat.measurements.ProtocolException;


/**
 * * Created by hanhui on 2017/3/27 0027 11:43
 * 
 * 底盘串口读写管理
 */
public class ChassisSerialIO extends AbstractSerialIO {

	public ChassisSerialIO(UsbSerialPort driver) {
		super(driver);
	}

	@Override
	public void readData(byte[] data) {
		try {
			ProtocolBean protocolBean = new ProtocolBean(data, data.length);
			String codeName = protocolBean.getCodeName();
			switch (codeName) {
				case CRM_Traveling_Fast1.CODE_NAME :
					break;
				case CRM_Traveling_Distance.CODE_NAME :
					Log.d(getLogTag(), "距离返回数据：" + HexDump.byteArrayToFloat(protocolBean.model_content));
					break;
			}
		} catch (ProtocolException e) {
			e.printStackTrace();
		}
	}

	@Override
	public synchronized void stop() {
		super.stop();
	}

	@Override
	public String getLogTag() {
		return "ChassisSerial";
	}
}
