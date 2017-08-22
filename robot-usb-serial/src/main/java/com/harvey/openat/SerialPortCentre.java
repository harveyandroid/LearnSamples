package com.harvey.openat;

import android.app.Application;
import android.content.Context;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbManager;
import android.util.Log;


import com.harvey.openat.driver.Ch34xSerialDriver;
import com.harvey.openat.driver.UsbSerialDriver;
import com.harvey.openat.driver.UsbSerialPort;
import com.harvey.openat.driver.UsbSerialProber;
import com.harvey.openat.io.ChassisSerialIO;
import com.harvey.openat.io.SensorSerialIO;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by hanhui on 2017/8/8 0008 10:31
 */

public class SerialPortCentre {
	final static String TAG = "SerialPortCentre";
	static UsbManager mUsbManager;
	final ExecutorService mExecutor = Executors.newFixedThreadPool(2);
	final SensorReadManager sensorReadManager;
	Ch34xSerialDriver.Ch340SerialPort sensorSerialPort;
	Ch34xSerialDriver.Ch340SerialPort chassisSerialPort;
	SensorSerialIO sensorSerialIO;
	ChassisSerialIO chassisSerialIO;
	public SerialPortCentre(SensorReadManager sensorReadManager) {
		this.sensorReadManager = sensorReadManager;
	}

	/**
	 * 获取usb串口的数量
	 * 
	 * @param context
	 * @return
	 */
	public static List<UsbSerialPort> getUsbSerialPorts(Application context) {
		mUsbManager = (UsbManager) context.getSystemService(Context.USB_SERVICE);
		final List<UsbSerialDriver> drivers = UsbSerialProber.getDefaultProber().findAllDrivers(mUsbManager);
		final List<UsbSerialPort> serialPorts = new ArrayList<>();
		for (final UsbSerialDriver driver : drivers) {
			final List<UsbSerialPort> ports = driver.getPorts();
			serialPorts.addAll(ports);
		}
		Log.e(TAG, "usb串口数量=" + serialPorts.size());
		return serialPorts;
	}

	public boolean openSerialPort(Application context) {
		final List<UsbSerialPort> serialPorts = getUsbSerialPorts(context);
		if (serialPorts.size() != 2)
			return false;
		// 位置顺序固定
		sensorSerialPort = (Ch34xSerialDriver.Ch340SerialPort) serialPorts.get(0);
		chassisSerialPort = (Ch34xSerialDriver.Ch340SerialPort) serialPorts.get(1);
		try {
			Log.e(TAG, "打开传感器usb串口 ..." + sensorSerialPort.getDriver().getDevice().toString());
			UsbDeviceConnection sensorConnection = mUsbManager.openDevice(sensorSerialPort.getDriver().getDevice());
			sensorSerialPort.open(sensorConnection);
			sensorSerialPort.setParameters(115200, 8, UsbSerialPort.STOPBITS_1, UsbSerialPort.PARITY_NONE);
			Log.e(TAG, "打开底盘usb串口 ..." + chassisSerialPort.getDriver().getDevice().toString());
			UsbDeviceConnection chassisConnection = mUsbManager.openDevice(chassisSerialPort.getDriver().getDevice());
			chassisSerialPort.open(chassisConnection);
			chassisSerialPort.setParameters(115200, 8, UsbSerialPort.STOPBITS_1, UsbSerialPort.PARITY_NONE);
			// 传感器串口
			sensorSerialIO = new SensorSerialIO(sensorSerialPort);
			sensorSerialIO.setReadManager(sensorReadManager);
			sensorSerialIO.startPeriodAsking();
			mExecutor.submit(sensorSerialIO);
			// 底盘串口
			chassisSerialIO = new ChassisSerialIO(chassisSerialPort);
			mExecutor.submit(chassisSerialIO);
			return true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	public void writeAsyncChassis(byte[] protocol_source) {
		if (chassisSerialIO != null) {
			chassisSerialIO.writeAsync(protocol_source);
		} else {
			Log.e(TAG, " 底盘usb串口为空");
		}
	}

	public void writeAsyncSensor(byte[] protocol_source) {
		if (sensorSerialIO != null) {
			sensorSerialIO.writeAsync(protocol_source);
		} else {
			Log.e(TAG, " 传感器usb串口为空");
		}
	}

	public void destroy() {
		mExecutor.shutdown();
		if (sensorSerialIO != null) {
			sensorSerialIO.stop();
			sensorSerialIO = null;
		}

		if (sensorSerialPort != null) {
			try {
				sensorSerialPort.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			sensorSerialPort = null;
		}

		if (chassisSerialIO != null) {
			chassisSerialIO.stop();
			chassisSerialIO = null;
		}

		if (chassisSerialPort != null) {
			try {
				chassisSerialPort.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			chassisSerialPort = null;
		}
	}
}
