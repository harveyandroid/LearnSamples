package com.harvey.openat;

import android.app.Application;

import com.harvey.openat.measurements.CRM_Traveling_Distance;
import com.harvey.openat.measurements.CRM_Traveling_Fast1;
import com.harvey.openat.units.Centimeter;
import com.harvey.openat.units.Doppler;
import com.harvey.openat.units.Humidity;
import com.harvey.openat.units.Luminance;
import com.harvey.openat.units.PowerAutoStatus;
import com.harvey.openat.units.Temperature;

import java.util.List;


/**
 * Created by hanhui on 2017/8/4 0004 09:07
 */

public final class RobotManager {
	static RobotManager instance = null;
	final String TAG = this.getClass().getSimpleName();
	final SerialPortCentre serialPortCentre;
	final SensorReadManager sensorReadManager;
	Application mContext;
	private RobotManager() {
		// 传感器读取数据管理
		sensorReadManager = new SensorReadManager();
		serialPortCentre = new SerialPortCentre(sensorReadManager);
	}

	public static RobotManager getInstance() {
		if (instance == null) {
			synchronized (RobotManager.class) {
				if (instance == null) {
					instance = new RobotManager();
				}
			}
		}
		return instance;
	}

	public boolean init(Application context) {
		this.mContext = context;
		return serialPortCentre.openSerialPort(context);
	}

	/**
	 * 获取超声波数据组
	 *
	 * @return
	 */
	public List<Centimeter> getUltrasonic_Waves() {
		return sensorReadManager.getUltrasonic_Waves();
	}

	/**
	 * 获取亮度数据
	 *
	 * @return
	 */
	public Luminance getLuminance() {
		return sensorReadManager.getLuminances();
	}

	/**
	 * 获取湿度数据
	 *
	 * @return
	 */
	public Humidity getHumidity() {
		return sensorReadManager.getHumidity();
	}

	/**
	 * 获取人体感应数据
	 *
	 * @return
	 */
	public Doppler getDopplerValue() {
		return sensorReadManager.getDoppler();
	}

	/**
	 * 获取温度数据
	 *
	 * @return
	 */
	public Temperature getTemperature() {
		return sensorReadManager.getTemperatures();
	}

	/**
	 * 获取自动充电状态
	 *
	 * @return
	 */
	public PowerAutoStatus getPowerAutoStatus() {
		return sensorReadManager.getPowerAutoStatus();
	}

	/**
	 * 获取红外传感器状态
	 *
	 * @return 8组数据，分别对应 0前左 1前右 2前左下 3前右下 4后左 5后右 6后左下 7后右下
	 */
	public boolean[] getInfraredStatus() {
		return sensorReadManager.getInfraredStatus();
	}

	/**
	 * 控制移动
	 *
	 * @param linear_speed
	 *            线速度（单位m/s）
	 * @param angular_speed
	 *            角速度（单位rad/s）
	 * @return
	 */
	public void controlTraveling(float linear_speed, float angular_speed) {
		CRM_Traveling_Fast1 crm_traveling = new CRM_Traveling_Fast1(linear_speed, angular_speed);
		serialPortCentre.writeAsyncChassis(crm_traveling.protocol_source);
	}

	/**
	 * 获取移动的距离
	 *
	 */
	public void obtainDistance() {
		CRM_Traveling_Distance traveling_distance = new CRM_Traveling_Distance();
		serialPortCentre.writeAsyncChassis(traveling_distance.protocol_source);
	}

	public void destroy() {
		serialPortCentre.destroy();
		instance = null;
	}

}
