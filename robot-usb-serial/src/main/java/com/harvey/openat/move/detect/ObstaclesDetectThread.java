package com.harvey.openat.move.detect;

import android.util.Log;

import com.harvey.openat.RobotManager;
import com.harvey.openat.units.Centimeter;

import java.util.List;


/**
 * 障碍物检测线程 Created by hanhui on 2016/11/3 0003 17:16
 */
public class ObstaclesDetectThread extends Thread {
	private final static int DETECT_DELAY = 200;
	private final String TAG = "ObstaclesDetectThread";
	private volatile boolean setToStopped = false;
	private OnObstaclesDetectListener obstaclesDetectListener;
	private boolean[] infraredStatus;
	private List<Centimeter> centimeterList;

	private int leftFrontWaveDistance;
	private int rightFrontWaveDistance;
	private int frontWaveDistance;
	private int backWaveDistance;
	private int leftWaveDistance;
	private int rightWaveDistance;

	private boolean isRobotMoveSlow = true;
	private boolean isFrontHaveObstacles = false;
	private boolean isFrontHollow = false;
	private boolean isBackHaveObstacles = false;
	private boolean isBackHollow = false;
	private boolean isLeftHaveObstacles = false;
	private boolean isRightHaveObstacles = false;

	private boolean isDetectInfrared = true;
	private boolean isDetectUltrasonicWaves = true;

	public ObstaclesDetectThread(OnObstaclesDetectListener listener) {
		this.setToStopped = false;
		this.obstaclesDetectListener = listener;
	}
	public ObstaclesDetectThread() {
		this.setToStopped = false;
	}

	private void initDefaultParams() {
		isRobotMoveSlow = true;
		isFrontHaveObstacles = false;
		isFrontHollow = false;
		isBackHaveObstacles = false;
		isBackHollow = false;
		isLeftHaveObstacles = false;
		isRightHaveObstacles = false;

		infraredStatus = null;
		if (centimeterList != null) {
			centimeterList.clear();
			centimeterList = null;
		}
	}

	/**
	 * 是否红外检测
	 *
	 * @param isDetect
	 */
	public void setDetectInfrared(boolean isDetect) {
		this.isDetectInfrared = isDetect;
		initDefaultParams();
	}

	/**
	 * 是否超声波检测
	 *
	 * @param isDetect
	 */
	public void setDetectUltrasonicWaves(boolean isDetect) {
		this.isDetectUltrasonicWaves = isDetect;
		initDefaultParams();
	}

	public void setOnObstaclesDetectListener(OnObstaclesDetectListener l) {
		this.obstaclesDetectListener = l;
	}

	public void clearListener() {
		this.obstaclesDetectListener = null;
	}

	private void detectUltrasonicWaves() {
		centimeterList = RobotManager.getInstance().getUltrasonic_Waves();
		if (centimeterList == null || centimeterList.size() != 8)
			return;
		leftFrontWaveDistance = centimeterList.get(DetectConfig.LEFT_FRONT_WAVE_POSITION).intValue();
		rightFrontWaveDistance = centimeterList.get(DetectConfig.RIGHT_FRONT_WAVE_POSITION).intValue();
		frontWaveDistance = centimeterList.get(DetectConfig.FRONT_WAVE_POSITION).intValue();
		leftWaveDistance = centimeterList.get(DetectConfig.LEFT_WAVE_POSITION).intValue();
		rightWaveDistance = centimeterList.get(DetectConfig.RIGHT_WAVE_POSITION).intValue();
		backWaveDistance = centimeterList.get(DetectConfig.BACK_WAVE_POSITION).intValue();
		Log.i(TAG, "超声波：" + leftFrontWaveDistance + "," + rightFrontWaveDistance + "," + frontWaveDistance + ","
				+ backWaveDistance + "," + leftWaveDistance + "," + rightWaveDistance);
		isRobotMoveSlow = frontWaveDistance < DetectConfig.SLOW_DOWN_MIN_WAVE_DETECTION_DISTANCE;
		isFrontHaveObstacles = frontWaveDistance < DetectConfig.MIN_FRONT_WAVE_DETECTION_DISTANCE
				|| (leftFrontWaveDistance != 0
						&& leftFrontWaveDistance < DetectConfig.MIN_LEFT_FRONT_WAVE_DETECTION_DISTANCE)
				|| (rightFrontWaveDistance != 0
						&& rightFrontWaveDistance < DetectConfig.MIN_RIGHT_FRONT_WAVE_DETECTION_DISTANCE);
		isBackHaveObstacles = backWaveDistance < DetectConfig.MIN_BACK_WAVE_DETECTION_DISTANCE;
		isLeftHaveObstacles = leftWaveDistance < DetectConfig.MIN_AROUND_WAVE_DETECTION_DISTANCE;
		isRightHaveObstacles = rightWaveDistance < DetectConfig.MIN_AROUND_WAVE_DETECTION_DISTANCE;
	}

	private void detectInfraredStatus() {
		// 获取红外传感器状态8组数据，分别对应0前左1前右2后右3后左4前左下5前右下6后右下7后左下
		infraredStatus = RobotManager.getInstance().getInfraredStatus();
		if (infraredStatus == null || infraredStatus.length != 8)
			return;
		isFrontHaveObstacles = isFrontHaveObstacles || infraredStatus[0] || infraredStatus[1];
		isFrontHollow = infraredStatus[4] || infraredStatus[5];
		isBackHaveObstacles = isBackHaveObstacles || infraredStatus[2] || infraredStatus[3];
		isBackHollow = infraredStatus[6] || infraredStatus[7];
	}
	public boolean isStop() {
		return setToStopped;
	}
	public void stopDetecting() {
		this.setToStopped = true;
		clearListener();
	}

	@Override
	public void run() {
		super.run();
		while (!this.setToStopped) {
			try {
				if (isDetectUltrasonicWaves)
					detectUltrasonicWaves();
				if (isDetectInfrared)
					detectInfraredStatus();
				if (obstaclesDetectListener != null) {
					obstaclesDetectListener.onDetectResult(isRobotMoveSlow, isFrontHaveObstacles, isFrontHollow,
							isBackHaveObstacles, isBackHollow, isLeftHaveObstacles, isRightHaveObstacles);
				}
				sleep(DETECT_DELAY);
			} catch (Exception e) {
				e.printStackTrace();
				if (obstaclesDetectListener != null) {
					obstaclesDetectListener.onDetectFailure(e);
				}
			}
		}
	}

	public interface OnObstaclesDetectListener {

		void onDetectResult(boolean isRobotMoveSlow, boolean isFrontHaveObstacles, boolean isFrontHollow,
                            boolean isBackHaveObstacles, boolean isBackHollow, boolean isLeftHaveObstacles,
                            boolean isRightHaveObstacles);

		void onDetectFailure(Exception e);

	}

}
