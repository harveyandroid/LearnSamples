package com.harvey.myapplication;

import android.content.Context;
import android.graphics.ImageFormat;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.media.MediaRecorder;
import android.util.Log;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by hanhui on 2016/8/8 0008 11:00
 */
public class LocalCamera implements Camera.PreviewCallback, SurfaceHolder.Callback {
    // 当前摄像头数据回调的类型
    public final static int FACE_VIDEO_TYPE = 1;// 人脸识别
    public final static int REMOTE_VIDEO_TYPE = 2;// 远程控制
    final static String TAG = "LocalCamera";
    /**
     * 外置摄像头id
     */
    public static int CAMERA_DEFAULT_ID = 0;
    public static int CAMERA_OTHER_ID = 1;
    public static int DEFAULT_CAMERA_WIDTH = 1280;
    public static int DEFAULT_CAMERA_HEIGHT = 720;
    static LocalCamera LocalCameraHelper;
    public int cameraWidth = DEFAULT_CAMERA_WIDTH;
    public int cameraHeight = DEFAULT_CAMERA_HEIGHT;
    boolean openCamera = false;
    SparseArray<PreviewBytesCallback> callbackArray = new SparseArray<>();
    Camera mCamera = null;
    int mCameraOrientation = 0;
    int currentCameraId = CAMERA_DEFAULT_ID;
    SurfaceView cameraSurfaceView = null;
    WindowManager mWindowManager = null;
    WindowManager.LayoutParams wmParams = null;
    MediaRecorder mMediaRecorder;

    private LocalCamera() {
    }

    public static LocalCamera createCamera() {
        if (LocalCameraHelper == null) {
            synchronized (LocalCamera.class) {
                if (LocalCameraHelper == null) {
                    LocalCameraHelper = new LocalCamera();
                }
            }
        }
        return LocalCameraHelper;
    }

    /**
     * 添加视频数据回调
     *
     * @param videoType
     * @param callback
     */
    public void addPreviewCallback(int videoType, PreviewBytesCallback callback) {
        if (videoType == REMOTE_VIDEO_TYPE) {
            showPreviewDisplay(false);
        } else {
            showPreviewDisplay(true);
        }
        callbackArray.put(videoType, callback);
    }

    public void removePreviewCallback(int videoType) {
        callbackArray.remove(videoType);
    }

    /**
     * 创建悬浮窗口
     *
     * @param context
     */
    private void createFloatView(Context context) {
        mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        wmParams = new WindowManager.LayoutParams();
        wmParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY; // 设置window
        wmParams.format = PixelFormat.TRANSPARENT;
        wmParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS;
        wmParams.gravity = Gravity.CENTER;
        // wmParams.width = 1;
        // wmParams.height = 1;
        wmParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        wmParams.height = WindowManager.LayoutParams.MATCH_PARENT;
        cameraSurfaceView = new SurfaceView(context);
        cameraSurfaceView.getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        cameraSurfaceView.getHolder().addCallback(this);
        Log.d(TAG, "--createFloatView--");
    }

    /**
     * 通过传入的宽高算出最接近于宽高值的相机大小
     */
    private Camera.Size getBestPreviewSize(Camera.Parameters camPara, final int width, final int height) {
        List<Camera.Size> allSupportedSize = camPara.getSupportedPreviewSizes();
        List<Camera.Size> widthLargerSize = new ArrayList<>();
        for (Camera.Size tmpSize : allSupportedSize) {
            Log.i(TAG, "设置摄像头分辨率" + tmpSize.width + "*" + tmpSize.height);
            if (tmpSize.width > tmpSize.height) {
                widthLargerSize.add(tmpSize);
            }
        }
        Collections.sort(widthLargerSize, new Comparator<Camera.Size>() {
            @Override
            public int compare(Camera.Size lhs, Camera.Size rhs) {
                int off_one = Math.abs(lhs.width * lhs.height - width * height);
                int off_two = Math.abs(rhs.width * rhs.height - width * height);
                return off_one - off_two;
            }
        });
        return widthLargerSize.get(0);
    }

    public boolean isOpen() {
        return openCamera;
    }

    /**
     * 默认初始化
     * <p>
     * 创建悬浮窗口
     * </p>
     *
     * @param context
     */
    public void initCamera(Context context) {
        initPreviewSize();
        createFloatView(context);
    }

    /**
     * 打开摄像头显示画面
     *
     * @param surfaceView
     */
    public boolean openCamera(SurfaceView surfaceView) {
        cameraSurfaceView = surfaceView;
        cameraSurfaceView.getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        cameraSurfaceView.getHolder().addCallback(this);
        return openCamera();
    }

    /**
     * 初始化分辨率
     */
    public void initPreviewSize() {
        currentCameraId = CAMERA_DEFAULT_ID;
        cameraWidth = DEFAULT_CAMERA_WIDTH;
        cameraHeight = DEFAULT_CAMERA_HEIGHT;
    }

    public void showPreviewDisplay(boolean isShow) {
        Log.i(TAG, "--showPreviewDisplay--" + isShow);
        if (cameraSurfaceView == null)
            return;
        if (mWindowManager == null)
            return;
        if (isShow) {
            if (cameraSurfaceView.isAttachedToWindow()) {
                mWindowManager.removeView(cameraSurfaceView);
            }
            mWindowManager.addView(cameraSurfaceView, wmParams);
            cameraSurfaceView.setVisibility(View.VISIBLE);
        } else {
            if (cameraSurfaceView.isAttachedToWindow()) {
                mWindowManager.removeView(cameraSurfaceView);
            }
            cameraSurfaceView.setVisibility(View.GONE);
        }
    }

    public boolean openCamera() {
        if (openCamera)
            return openCamera;
        if (getNumberOfCameras() < currentCameraId + 1) {
            return false;
        }
        try {
            mCamera = Camera.open(currentCameraId);
            initCameraParameters();
            mCamera.startPreview();
            mCamera.setPreviewCallback(this);
            openCamera = true;
            // startMediaRecorder(getMediaRecorderFilePath());
        } catch (RuntimeException e) {
            Log.e(TAG, "open camera error" + e);
            closeCamera();
        }
        Log.i(TAG, "openCamera");
        return openCamera;
    }

    /**
     * 释放录像
     */
    public void releaseMediaRecorder() {
        if (mMediaRecorder != null) {
            mMediaRecorder.reset();
            mMediaRecorder.release();
            mMediaRecorder = null;
            mCamera.lock();
            Log.d(TAG, "mMediaRecorder.reset");
        }
    }

    public int getNumberOfCameras() {
        Log.i(TAG, "摄像头数量" + Camera.getNumberOfCameras());
        return Camera.getNumberOfCameras();
    }

    private void initCameraParameters() throws RuntimeException {
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        Camera.getCameraInfo(currentCameraId, cameraInfo);
        Camera.Parameters params = mCamera.getParameters();
        Camera.Size bestPreviewSize = getBestPreviewSize(params, cameraWidth, cameraHeight);
        cameraWidth = bestPreviewSize.width;
        cameraHeight = bestPreviewSize.height;
        params.setPreviewSize(cameraWidth, cameraHeight);
        Log.i(TAG, "---PreviewSize---" + cameraWidth + "," + cameraHeight);
        params.setPreviewFormat(ImageFormat.NV21);
        mCamera.setParameters(params);
        Log.i(TAG, "allocate:  camera orientation=" + cameraInfo.orientation + ", facing=" + cameraInfo.facing);
        mCameraOrientation = getCameraDisplayOrientation(cameraInfo);
        mCamera.setDisplayOrientation(mCameraOrientation);
        Log.i(TAG, "allocate:  camera orientation=" + mCameraOrientation + ", facing=" + cameraInfo.facing);
    }

    public int getCameraDisplayOrientation(Camera.CameraInfo cameraInfo) {
        int result = 0;
        try {
            if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                result = (cameraInfo.orientation) % 360;
                result = (360 - result) % 360; // compensate the mirror
            } else { // back-facing
                result = (cameraInfo.orientation + 360) % 360;
            }
        } catch (Exception ex) {

        }
        return result;
    }

    public void setSceneMode(String value) {
        if (mCamera != null) {
            Camera.Parameters params = mCamera.getParameters();
            params.setSceneMode(value);
            mCamera.setParameters(params);
        }

    }

    public void setColorEffect(String value) {
        if (mCamera != null) {
            Camera.Parameters params = mCamera.getParameters();
            params.setColorEffect(value);
            mCamera.setParameters(params);
        }

    }

    public void setExposureCompensation(int value) {
        if (mCamera != null) {
            Camera.Parameters params = mCamera.getParameters();
            params.setExposureCompensation(value);
            mCamera.setParameters(params);
        }

    }

    public void setWhiteBalance(String value) {
        if (mCamera != null) {
            Camera.Parameters params = mCamera.getParameters();
            params.setWhiteBalance(value);
            mCamera.setParameters(params);
        }

    }

    public void setAntibanding(String value) {
        if (mCamera != null) {
            Camera.Parameters params = mCamera.getParameters();
            params.setAntibanding(value);
            mCamera.setParameters(params);
        }

    }

    public void setFlashMode(String value) {
        if (mCamera != null) {
            Camera.Parameters params = mCamera.getParameters();
            params.setFlashMode(value);
            mCamera.setParameters(params);
        }

    }

    public void setFocusMode(String value) {
        if (mCamera != null) {
            Camera.Parameters params = mCamera.getParameters();
            params.setFocusMode(value);
            mCamera.setParameters(params);
        }

    }

    public void setZoom(int value) {
        if (mCamera != null) {
            Camera.Parameters params = mCamera.getParameters();
            params.setZoom(value);
            mCamera.setParameters(params);
        }

    }

    // /**
    // * 获取视频旋转角度(适用手机测试)
    // *
    // * @return
    // */
    // public int getSurfaceRotation() {
    // if (currentCameraId == CAMERA_DEFAULT_ID) {
    // return 360 - mCameraOrientation - 180;
    // } else {
    // return 360 - mCameraOrientation;
    // }
    // }
    public int getRotation() {
        return mCameraOrientation;
    }

    /**
     * 获取视频旋转角度(适用板子测试)
     *
     * @return
     */
    public int getSurfaceRotation() {
        if (currentCameraId == CAMERA_DEFAULT_ID) {
            return 90;
        } else {
            return 180;
        }
    }

    // 切换摄像头
    public boolean switchCamera() {
        boolean isSuccess = false;
        // 必须3个摄像头才可以切换
        if (getNumberOfCameras() < 3) {
            return isSuccess;
        }
        currentCameraId = (currentCameraId == CAMERA_DEFAULT_ID)
                ? getNumberOfCameras() - 1
                : CAMERA_DEFAULT_ID;
        closeCamera();
        isSuccess = openCamera();
        return isSuccess;
    }

    public boolean setCameraPreviewSize(int width, int height) {
        this.cameraWidth = width;
        this.cameraHeight = height;
        Log.d(TAG, "--setCameraPreviewSize--" + width + "," + height);
        closeCamera();
        return openCamera();
    }

    /**
     * 是否包含远程控制类型
     *
     * @return
     */
    private boolean isContainRemoteType() {
        int index = callbackArray.indexOfKey(REMOTE_VIDEO_TYPE);
        return index < 0 ? false : true;
    }

    public void stopCamera() {
        // 如果正在远程控制，就不停止（防止人脸识别和远程控制冲突）
        // 结束远程控制的时候请先removePreviewCallback再stopCamera
        if (isContainRemoteType())
            return;
        closeCamera();
        Log.d(TAG, "--stopCamera--");

    }

    private void closeCamera() {
        showPreviewDisplay(false);
        if (mCamera != null) {
            mCamera.stopPreview();
            mCamera.setPreviewCallback(null);
            mCamera.release();
            mCamera = null;
        }
        openCamera = false;
        Log.d(TAG, "--closeCamera--");

    }

    public void destroyCamera() {
        this.stopCamera();
        callbackArray.clear();
        if (mWindowManager != null) {
            if (cameraSurfaceView != null && cameraSurfaceView.isAttachedToWindow()) {
                mWindowManager.removeView(cameraSurfaceView);
                cameraSurfaceView.getHolder().getSurface().release();
            }
            cameraSurfaceView = null;
        }
        Log.d(TAG, "--destroyCamera--");
        // stopMediaRecorder();
    }

    @Override
    public void onPreviewFrame(byte[] data, Camera camera) {
        if (data == null)
            return;
        // Log.i(TAG, "onPreviewFrame" + data.length);
        int size = callbackArray.size();
        for (int i = 0; i < size; i++) {
            PreviewBytesCallback callback = callbackArray.valueAt(i);
            callback.onPreviewFrame(data, data.length);
        }
    }

    public void setCameraBrightness(int value) {
    }

    public void setCameraContrast(int value) {
    }

    public void setCameraSaturation(int value) {
    }

    public void setCameraHue(int value) {
    }

    public void setCameraSharpness(int value) {
    }

    public void setCameraGain(int value) {
    }

    public void setCameraGamma(int value) {

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Log.d(TAG, "--surfaceCreated--");
        try {
            if (mCamera != null) {
                mCamera.setPreviewDisplay(cameraSurfaceView.getHolder());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Log.d(TAG, "--surfaceChanged--");
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.d(TAG, "--surfaceDestroyed--");
    }

    public interface PreviewBytesCallback {

        void onPreviewFrame(byte[] frameBytes, int length);
    }
}
