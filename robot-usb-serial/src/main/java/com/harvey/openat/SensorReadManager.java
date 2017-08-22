package com.harvey.openat;



import com.harvey.openat.measurements.ProtocolException;
import com.harvey.openat.measurements.SensorProtocolBean;
import com.harvey.openat.units.Centimeter;
import com.harvey.openat.units.Doppler;
import com.harvey.openat.units.Humidity;
import com.harvey.openat.units.Luminance;
import com.harvey.openat.units.PowerAutoStatus;
import com.harvey.openat.units.Temperature;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hanhui on 2017/3/28 0028 18:16
 */

public class SensorReadManager {
	// 亮度
	Luminance luminances = new Luminance();
	// 温度
	Temperature temperatures = new Temperature();
	// 湿度
	Humidity humidity = new Humidity();
	// 超声波数据
	List<Centimeter> Ultrasonic_Waves = new ArrayList<>();
	// 前后置避障传感器数据
	boolean[] infraredStatus = new boolean[]{};
	// 充电桩对接传感器
	PowerAutoStatus powerAutoStatus = new PowerAutoStatus();
	// 人体感应
	Doppler doppler = new Doppler();
	public SensorReadManager() {

	}
	public synchronized void addTable(String data) {
		try {
			SensorProtocolBean protocolBean = new SensorProtocolBean(data);
			Ultrasonic_Waves.clear();
			Ultrasonic_Waves.add(new Centimeter(protocolBean.ucw0));
			Ultrasonic_Waves.add(new Centimeter(protocolBean.ucw1));
			Ultrasonic_Waves.add(new Centimeter(protocolBean.ucw2));
			Ultrasonic_Waves.add(new Centimeter(protocolBean.ucw3));
			temperatures = new Temperature(protocolBean.tmp);
			luminances = new Luminance(protocolBean.lgt);
			humidity = new Humidity(protocolBean.hum);
			infraredStatus = protocolBean.getInfraredStatus();
			powerAutoStatus = new PowerAutoStatus(protocolBean.chg);
			doppler = new Doppler(protocolBean.dpl);
		} catch (ProtocolException e) {
			 e.printStackTrace();
		}
	}

	public Luminance getLuminances() {
		return luminances;
	}

	public Temperature getTemperatures() {
		return temperatures;
	}

	public Humidity getHumidity() {
		return humidity;
	}

	public List<Centimeter> getUltrasonic_Waves() {
		return Ultrasonic_Waves;
	}

	public boolean[] getInfraredStatus() {
		return infraredStatus;
	}

	public PowerAutoStatus getPowerAutoStatus() {
		return powerAutoStatus;
	}

	public Doppler getDoppler() {
		return doppler;
	}
}
