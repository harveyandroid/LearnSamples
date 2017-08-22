package com.harvey.openat.move;

import android.os.Looper;
import android.util.Log;

import com.harvey.openat.RobotManager;
import com.harvey.openat.move.detect.MoveConfig;
import com.harvey.openat.move.detect.MoveDetectThread;
import com.harvey.openat.move.detect.ObstaclesDetectThread;


/**
 * <p>
 * 注：一个指令底盘电机移动2s
 * </p>
 * Created by hanhui on 2017/8/8 0008 10:51
 */

public class ChassisMoveControl
        implements
        ObstaclesDetectThread.OnObstaclesDetectListener,
        MoveDetectThread.MoveDetectListener {
    final String TAG = this.getClass().getSimpleName();
    final HandlerPoster moveHandler = new HandlerPoster(Looper.getMainLooper(), this);
    volatile float currentSpeed;// 单位m/s
    float robotSpeed = MoveConfig.DEFAULT_FAST_SPEED;// 单位m/s默认快速度
    ObstaclesDetectThread obstaclesDetectThread;
    MoveDetectThread moveDetectThread;
    Control currentDirection = Control.FRONT;// 记录机器人走的方向(默认向前)
    boolean barrierDetect = false;
    boolean rightBarrier = false;
    boolean leftBarrier = false;
    boolean frontBarrier = false;
    boolean backBarrier = false;
    boolean needSlow = false;

    public ChassisMoveControl() {

    }

    public void init() {
        // setBarrierDetected(true);
        moveDetectThread = new MoveDetectThread();
        moveDetectThread.setDetectListener(this);
        moveDetectThread.start();
    }

    /**
     * 设置障碍物检测
     *
     * @param isDetect
     */
    public void setBarrierDetected(boolean isDetect) {
        this.barrierDetect = isDetect;
        if (barrierDetect) {
            if (obstaclesDetectThread != null) {
                obstaclesDetectThread.stopDetecting();
            }
            obstaclesDetectThread = new ObstaclesDetectThread(this);
            obstaclesDetectThread.start();
        } else {
            if (obstaclesDetectThread != null)
                obstaclesDetectThread.stopDetecting();
        }
    }

    /**
     * 控制移动
     *
     * @param control 前、后、左、右
     * @return
     */
    public void controlTraveling(Control control) {
        Log.i(TAG, "---控制移动---" + control);
        moveDetectThread.setSkipDetect(false);
        moveDetectThread.setMoveTime();
        switch (control.intValue()) {
            case MoveConfig.TYPE_ORDER_FRONT:
                controlFrontMove();
                break;
            case MoveConfig.TYPE_ORDER_BACK:
                controlBackMove();
                break;
            case MoveConfig.TYPE_ORDER_LEFT:
                controlLeftMove();
                break;
            case MoveConfig.TYPE_ORDER_RIGHT:
                controlRightMove();
                break;
            case MoveConfig.TYPE_ORDER_LEFT_FRONT:
                controlLeftFrontMove();
                break;
            case MoveConfig.TYPE_ORDER_RIGHT_FRONT:
                controlRightFrontMove();
                break;
            case MoveConfig.TYPE_ORDER_STOP:
            default:
                controlRobotStop();
                break;
        }
    }

    /**
     * 控制移动指定的距离
     *
     * @param control 移动方向
     * @param size    移动大小(单位cm或度°)
     */
    public void controlTraveling(Control control, int size) {
        Log.i(TAG, "---控制移动---" + control + ",size=" + size);
        moveDetectThread.setSkipDetect(true);
        moveHandler.removeCallbacksAndMessages(null);
        float data = 0.0f;
        switch (control.intValue()) {
            case MoveConfig.TYPE_ORDER_FRONT:
            case MoveConfig.TYPE_ORDER_BACK:
                data = (float) size / 100f;
                break;
            case MoveConfig.TYPE_ORDER_LEFT:
            case MoveConfig.TYPE_ORDER_RIGHT:
                data = (float) (size * Math.PI) / 180f;
                break;
        }
        Log.i(TAG, "---控制移动-距离或者角度--=" + data);
        moveHandler.sendMessage(moveHandler.obtainMessage(control.intValue(), data));
    }

    /**
     * 控制前移
     */
    public void controlFrontMove() {
        Log.i(TAG, "-向前--");
        if (!frontBarrier || (!barrierDetect)) {
            currentDirection = Control.FRONT;
            controlTraveling(verifyFrontSpeed());
        }
    }

    public void controlBackMove() {
        Log.i(TAG, "-向后--");
        if (!backBarrier || (!barrierDetect)) {
            currentDirection = Control.BACK;
            controlTraveling(-MoveConfig.DEFAULT_SLOW_MAX_SPEED);
        }
    }

    public void controlLeftMove() {
        Log.i(TAG, "-向左--");
        if (!leftBarrier || (!barrierDetect)) {
            currentDirection = Control.LEFT;
            controlTraveling(0, -MoveConfig.DEFAULT_TURN_SPEED);

        }
    }

    public void controlRightMove() {
        Log.i(TAG, "-向右--");
        if (!rightBarrier || (!barrierDetect)) {
            currentDirection = Control.RIGHT;
            controlTraveling(0, MoveConfig.DEFAULT_TURN_SPEED);
        }
    }

    public void controlLeftFrontMove() {
        Log.i(TAG, "-向左前--");
        if ((!leftBarrier && !frontBarrier) || (!barrierDetect)) {
            currentDirection = Control.LEFT_FRONT;
            controlTraveling(0.15f, -0.1f);
        }
    }

    public void controlRightFrontMove() {
        Log.i(TAG, "-向右前--");
        if ((!rightBarrier && !frontBarrier) || (!barrierDetect)) {
            currentDirection = Control.RIGHT_FRONT;
            controlTraveling(0.15f, 0.1f);
        }
    }

    public float verifyFrontSpeed() {
        if (currentSpeed < MoveConfig.DEFAULT_SLOW_MIN_SPEED) {
            return MoveConfig.DEFAULT_SLOW_MIN_SPEED;
        }
        if (currentSpeed < MoveConfig.DEFAULT_SLOW_MAX_SPEED
                && currentSpeed + MoveConfig.DEFAULT_ADD_SPEED < MoveConfig.DEFAULT_SLOW_MAX_SPEED) {
            return currentSpeed + MoveConfig.DEFAULT_ADD_SPEED;
        }
        if (needSlow) {
            return MoveConfig.DEFAULT_SLOW_MAX_SPEED;
        }
        if (robotSpeed < MoveConfig.DEFAULT_SLOW_MAX_SPEED) {
            return MoveConfig.DEFAULT_SLOW_MAX_SPEED;
        } else {
            return robotSpeed;
        }
    }

    /**
     * 设置速度
     *
     * @param speed 速度
     */
    public void setRobotSpeed(float speed) {
        robotSpeed = Math.abs(speed);
        if (robotSpeed < MoveConfig.DEFAULT_LOW_SPEED) {
            robotSpeed = MoveConfig.DEFAULT_LOW_SPEED;
        }
        if (robotSpeed > MoveConfig.DEFAULT_HIGH_SPEED) {
            robotSpeed = MoveConfig.DEFAULT_HIGH_SPEED;
        }
    }

    /**
     * 控制直走移动
     *
     * @param linear_speed 线速度（单位m/s）
     * @return
     */
    public void controlTraveling(float linear_speed) {
        this.controlTraveling(linear_speed, 0);
    }

    /**
     * 控制移动
     *
     * @param linear_speed  线速度（单位m/s）
     * @param angular_speed 角速度（单位rad/s）
     * @return
     */
    public void controlTraveling(float linear_speed, float angular_speed) {
        Log.e(TAG, "控制移动：线速度" + linear_speed + "，角速度" + angular_speed);
        currentSpeed = Math.max(Math.abs(linear_speed), Math.abs(angular_speed));
        RobotManager.getInstance().controlTraveling(linear_speed, angular_speed);
    }

    public void setHighSpeed() {
        this.setRobotSpeed(MoveConfig.DEFAULT_HIGH_SPEED);

    }

    public void setMediumSpeed() {
        this.setRobotSpeed(MoveConfig.DEFAULT_MEDIUM_SPEED);
    }

    public void setLowSpeed() {
        this.setRobotSpeed(MoveConfig.DEFAULT_LOW_SPEED);
    }

    /**
     * 控制机器人停止
     */
    public void controlRobotStop() {
        if (currentSpeed == 0)
            return;
        // 移动检测跳过回调
        moveDetectThread.setSkipDetect(true);
        controlTraveling(0);
        if (currentDirection == Control.FRONT) {
            // 缓停
            Log.e(TAG, "controlRobotStop--缓停");
            moveHandler.sendEmptyMessage(MoveConfig.TYPE_ORDER_SLOW_STOP);
        } else {
            // 立即停止
            Log.e(TAG, "controlRobotStop--立即停止");
            controlTraveling(0);
        }
    }

    @Override
    public void onDetectResult(boolean isRobotMoveSlow, boolean isFrontHaveObstacles, boolean isFrontHollow,
                               boolean isBackHaveObstacles, boolean isBackHollow, boolean isLeftHaveObstacles,
                               boolean isRightHaveObstacles) {
        needSlow = isRobotMoveSlow;
        frontBarrier = isFrontHaveObstacles | isFrontHollow;
        backBarrier = isBackHaveObstacles | isBackHollow;
        leftBarrier = isLeftHaveObstacles;
        rightBarrier = isRightHaveObstacles;
        int order = 0;
        if (frontBarrier)
            order += Math.pow(2, 3);
        if (backBarrier)
            order += Math.pow(2, 2);
        if (leftBarrier)
            order += Math.pow(2, 1);
        if (rightBarrier)
            order += Math.pow(2, 0);
    }

    @Override
    public void onDetectFailure(Exception e) {
        Log.i(TAG, "---onDetectFailure--" + e.getMessage());
    }

    @Override
    public void onMoveTimeOut() {
        Log.e(TAG, "--onMoveTimeOut--");
        controlRobotStop();
    }

    /**
     * 销毁
     */
    public void destroy() {
        moveHandler.removeCallbacksAndMessages(null);
        controlRobotStop();
        if (obstaclesDetectThread != null) {
            moveDetectThread.stopDetecting();
        }
        if (obstaclesDetectThread != null)
            obstaclesDetectThread.stopDetecting();
    }
}
