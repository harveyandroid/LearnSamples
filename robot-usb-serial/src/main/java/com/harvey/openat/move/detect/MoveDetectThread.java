package com.harvey.openat.move.detect;

/**
 * Created by hanhui on 2017/2/21 0021 16:34
 */
public class MoveDetectThread extends Thread {
	private long straightMoveTime;
	private volatile boolean setToStopped = false;
	private MoveDetectListener detectListener;
	private volatile boolean skipDetect = false;

	public MoveDetectThread() {
		this.setToStopped = false;
	}

	public void setDetectListener(MoveDetectListener listener) {
		this.detectListener = listener;
	}

	public void clearListener() {
		this.detectListener = null;
	}

	public void stopDetecting() {
		setToStopped = true;
		clearListener();
		skipDetect = false;
	}

	public void setSkipDetect(boolean isSkip) {
		this.skipDetect = isSkip;
	}

	public boolean isStop() {
		return setToStopped;
	}

	public void setMoveTime() {
		this.straightMoveTime = System.currentTimeMillis();
	}

	@Override
	public void run() {
		while (!this.setToStopped) {
			try {
				if (System.currentTimeMillis() - straightMoveTime > DetectConfig.WAIT_ORDER_DELAY_TIME) {
					if (detectListener != null && !skipDetect) {
						detectListener.onMoveTimeOut();
					}
				}
				Thread.sleep(DetectConfig.DETECT_MOVE_DELAY_TIME);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 移动监测监听器
	 */
	public interface MoveDetectListener {
		void onMoveTimeOut();
	}

}
