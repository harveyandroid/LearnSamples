package com.harvey.openat.move;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.harvey.openat.move.detect.MoveConfig;


/**
 * Created by hanhui on 2017/8/8 0008 14:58
 */

public final class HandlerPoster extends Handler {
	static final long SLOW_STOP_DELAY_TIME = 200;// 缓慢停止延迟时间
	static final long MOVE_DELAY_TIME = 200;// 移动延迟时间毫秒要大于耗时时间
	static final long SEND_ORDER_TIME = 140;// 发送指令耗时时间
	static final float ANGLE_ERROR_MULTIPLE = 7.8f;// 角速度转机器人旋转角度误差倍数
	final ChassisMoveControl moveControl;

	HandlerPoster(Looper looper, ChassisMoveControl moveControl) {
		super(looper);
		this.moveControl = moveControl;
	}

	/**
	 * 获取当前移动延迟所移动的距离
	 * 
	 * @return
	 */
	public float getMoveDistance() {
		return moveControl.currentSpeed * (MOVE_DELAY_TIME - SEND_ORDER_TIME) / 1000;
	}

	/**
	 * 获取当前移动延迟所移动的角度
	 *
	 * @return
	 */
	public float getMoveAngle() {
		return MoveConfig.DEFAULT_TURN_SPEED * ANGLE_ERROR_MULTIPLE * (MOVE_DELAY_TIME) / 1000f;
	}

	@Override
	public void handleMessage(Message msg) {
		switch (msg.what) {
			case MoveConfig.TYPE_ORDER_SLOW_STOP :
				final float currentSpeed = moveControl.currentSpeed - MoveConfig.DEFAULT_SUBTRACT_SPEED;
				if (currentSpeed < MoveConfig.DEFAULT_SLOW_MIN_SPEED) {
					moveControl.controlTraveling(0);
				} else {
					moveControl.controlTraveling(currentSpeed);
					sendEmptyMessageDelayed(MoveConfig.TYPE_ORDER_SLOW_STOP, SLOW_STOP_DELAY_TIME);
				}
				break;
			case MoveConfig.TYPE_ORDER_FRONT :
				if ((float) msg.obj > 0) {
					moveControl.controlFrontMove();
					Message msgOrder = Message.obtain();
					msgOrder.what = MoveConfig.TYPE_ORDER_FRONT;
					msgOrder.obj = (float) msg.obj - getMoveDistance();
					sendMessageDelayed(msgOrder, MOVE_DELAY_TIME);
				} else {
					moveControl.controlTraveling(0);
				}

				break;
			case MoveConfig.TYPE_ORDER_BACK :
				if ((float) msg.obj > 0) {
					moveControl.controlBackMove();
					Message msgOrder = Message.obtain();
					msgOrder.what = MoveConfig.TYPE_ORDER_BACK;
					msgOrder.obj = (float) msg.obj - getMoveDistance();
					sendMessageDelayed(msgOrder, MOVE_DELAY_TIME);
				} else {
					moveControl.controlTraveling(0);
				}
				break;
			case MoveConfig.TYPE_ORDER_LEFT :
				if ((float) msg.obj > 0) {
					moveControl.controlLeftMove();
					Message msgOrder = Message.obtain();
					msgOrder.what = MoveConfig.TYPE_ORDER_LEFT;
					msgOrder.obj = (float) msg.obj - getMoveAngle();
					sendMessageDelayed(msgOrder, MOVE_DELAY_TIME);
				} else {
					moveControl.controlTraveling(0);
				}
				break;

			case MoveConfig.TYPE_ORDER_RIGHT :
				if ((float) msg.obj > 0) {
					moveControl.controlRightMove();
					Message msgOrder = Message.obtain();
					msgOrder.what = MoveConfig.TYPE_ORDER_RIGHT;
					msgOrder.obj = (float) msg.obj - getMoveAngle();
					sendMessageDelayed(msgOrder, MOVE_DELAY_TIME);
				} else {
					moveControl.controlTraveling(0);
				}
				break;
		}
		super.handleMessage(msg);
	}
}
