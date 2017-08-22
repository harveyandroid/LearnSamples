package com.harvey.openat.io;

import android.util.Log;


import com.harvey.openat.SensorReadManager;
import com.harvey.openat.driver.UsbSerialPort;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

/**
 * * Created by hanhui on 2017/3/27 0027 11:43
 * 
 * 传感器串口读写管理
 */
public class SensorSerialIO extends AbstractSerialIO {
	final byte[] SENSOR_ORDER = {0x41};// 请求传感器数据指令
	SensorReadManager readManager;
	long TIME_PERIOD = TimeUnit.SECONDS.toMillis(2);
	Timer timer;
	TimerTask timerTask = new TimerTask() {

		@Override
		public void run() {
			writeAsync(SENSOR_ORDER);
		}
	};
	public SensorSerialIO(UsbSerialPort driver) {
		super(driver);
	}

	public void setReadManager(SensorReadManager readManager) {
		this.readManager = readManager;
	}
	@Override
	public void readData(byte[] data) {
		Log.d(getLogTag(), "传感器数据：" + new String(data));
		readManager.addTable(new String(data));
	}

	public void startPeriodAsking() {
		timer = new Timer();
		timer.schedule(timerTask, 10, TIME_PERIOD);
	}

	@Override
	public synchronized void stop() {
		super.stop();
		if (timer != null) {
			timerTask.cancel();
			timer.cancel();
		}
	}

	@Override
	public String getLogTag() {
		return "SensorSerial";
	}
}
