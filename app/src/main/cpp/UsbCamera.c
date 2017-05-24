//
// Created by Administrator on 2017/5/22 0022.
//

#include "UsbCamera.h"
#include "UsbCameraLib.h"

/*
 * Class:     com_harvey_learnsamples_UsbCamera
 * Method:    getCameraList
 * Signature: ()[I
 */
JNIEXPORT jintArray JNICALL Java_com_harvey_learnsamples_UsbCamera_getCameraList(JNIEnv * env, jobject obj)
{
    //获取摄像头ID列表
    int array[4];
    int size = getCameraList(array);

    //新建一个长度为size的jintArray数组
    jintArray jarray = (*env)-> NewIntArray(env, size);

    //新建JINT数组
    jint jints[size];
    int i=0;
    for(i=0;i<size;i++)
        jints[i] = array[i];

    //给需要返回的数组赋值
    (*env)->SetIntArrayRegion(env,jarray, 0, size, jints);

    return jarray;
}

/*
 * Class:     com_harvey_learnsamples_UsbCamera
 * Method:    startCamera
 * Signature: (III)I
 */
JNIEXPORT jint JNICALL Java_com_harvey_learnsamples_UsbCamera_openCamera
        (JNIEnv *env, jobject obj, jint videoid, jint fwidth, jint fheight)
{
    width = fwidth;
    height = fheight;
    fd = openCamera(videoid);
    int ret;
    if (fd != ERROR_LOCAL) {
        ret = initDevice(fd, width, height);
        if (ret != ERROR_LOCAL) {
            ret = startCapturing(fd);
            if (ret != SUCCESS_LOCAL) {
            }
        }
    }
    return fd;
}

/*
 * Class:     com_harvey_learnsamples_UsbCamera
 * Method:    readCameraFrame
 * Signature: ([B)I
 */
JNIEXPORT jint JNICALL Java_com_harvey_learnsamples_UsbCamera_readCameraFrame
        (JNIEnv *env, jobject obj, jbyteArray array){
    //取一帧数据
    readframeonce(fd);
    yuv422toyuv420(array,framebuffer,width,height);
//    (*env)->SetByteArrayRegion(env, array, 0, width * height * 2, framebuffer);
    int size = (*env)->GetArrayLength(env, array);
    LOGE("readCameraVideoData %d", size);
    return size;
}

/*
 * Class:     com_harvey_learnsamples_UsbCamera
 * Method:    readCameraFrameRGBA
 * Signature: ([B)I
 */
JNIEXPORT jint JNICALL Java_com_harvey_learnsamples_UsbCamera_readCameraFrameRGBA
        (JNIEnv *env, jobject obj, jbyteArray array){
    //取一帧数据
    readframeonce(fd);
    yuyv422toRGBA(framebuffer,width,height);
    (*env)->SetByteArrayRegion(env, array, 0, width * height * 2, framebuffer);
    int size = (*env)->GetArrayLength(env, array);
    LOGE("readCameraFrameRgb %d", size);
    return size;
}

/*
 * Class:     com_harvey_learnsamples_UsbCamera
 * Method:    readCameraFrameBmp
 * Signature: (Ljava/lang/Object;)V
 */
JNIEXPORT void JNICALL Java_com_harvey_learnsamples_UsbCamera_readCameraFrameBmp
        (JNIEnv *env, jobject obj, jobject bitmap){

    //取一帧数据
    readframeonce(fd);
    yuyv422toABGRY(framebuffer,width,height);

    jboolean bo;

    AndroidBitmapInfo info;
    void *pixels;
    int ret;
    int i;
    int *colors;

    int width = 0;
    int height = 0;

    if ((ret = AndroidBitmap_getInfo(env, bitmap, &info)) < 0) {
        LOGE("AndroidBitmap_getInfo() failed ! error=%d", ret);
        return;
    }

    width = info.width;
    height = info.height;

    if (!rgb || !ybuf) return;

    if (info.format != ANDROID_BITMAP_FORMAT_RGBA_8888) {
        LOGE("Bitmap format is not RGBA_8888 !");
        return;
    }

    if ((ret = AndroidBitmap_lockPixels(env, bitmap, &pixels)) < 0) {
        LOGE("AndroidBitmap_lockPixels() failed ! error=%d", ret);
    }

    colors = (int *) pixels;
    int *lrgb = NULL;
    lrgb = &rgb[0];

    for (i = 0; i < width * height; i++) {
        *colors++ = *lrgb++;
    }

    AndroidBitmap_unlockPixels(env, bitmap);
}

/*
 * Class:     com_harvey_learnsamples_UsbCamera
 * Method:    stopCamera
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_com_harvey_learnsamples_UsbCamera_stopCamera
        (JNIEnv *env, jobject obj){
    stopCapturing(fd);

    unInitDevice();

    closeDevice(fd);

    if (rgb) free(rgb);
    if (ybuf) free(ybuf);

    fd = -1;
}


///*
// * Class:     com_harvey_learnsamples_UsbCamera
// * Method:    setBrightness
// * Signature: (I)I
// */
//JNIEXPORT jint JNICALL Java_com_harvey_learnsamples_UsbCamera_setBrightness
//        (JNIEnv *env, jobject obj, jint value){
//    return setConfig(fd,V4L2_CID_BRIGHTNESS,value);
//}
//
///*
// * Class:     com_harvey_learnsamples_UsbCamera
// * Method:    setContrast
// * Signature: (I)I
// */
//JNIEXPORT jint JNICALL Java_com_harvey_learnsamples_UsbCamera_setContrast
//        (JNIEnv *env, jobject obj, jint value){
//    return setConfig(fd,V4L2_CID_CONTRAST,value);
//}
///*
// * Class:     com_harvey_learnsamples_UsbCamera
// * Method:    setSaturation
// * Signature: (I)I
// */
//JNIEXPORT jint JNICALL Java_com_harvey_learnsamples_UsbCamera_setSaturation
//        (JNIEnv *env, jobject obj, jint value){
//    return setConfig(fd,V4L2_CID_SATURATION,value);
//}
///*
// * Class:     com_harvey_learnsamples_UsbCamera
// * Method:    setHue
// * Signature: (I)I
// */
//JNIEXPORT jint JNICALL Java_com_harvey_learnsamples_UsbCamera_setHue
//        (JNIEnv *env, jobject obj, jint value){
//    return setConfig(fd,V4L2_CID_HUE,value);
//}
///*
// * Class:     com_harvey_learnsamples_UsbCamera
// * Method:    setSharpness
// * Signature: (I)I
// */
//JNIEXPORT jint JNICALL Java_com_harvey_learnsamples_UsbCamera_setSharpness
//        (JNIEnv *env, jobject obj, jint value){
//    return setConfig(fd,V4L2_CID_SHARPNESS,value);
//}
///*
// * Class:     com_harvey_learnsamples_UsbCamera
// * Method:    setGain
// * Signature: (I)I
// */
//JNIEXPORT jint JNICALL Java_com_harvey_learnsamples_UsbCamera_setGain
//        (JNIEnv *env, jobject obj, jint value){
//    return setConfig(fd,V4L2_CID_GAIN,value);
//}
///*
// * Class:     com_harvey_learnsamples_UsbCamera
// * Method:    setGamma
// * Signature: (I)I
// */
//JNIEXPORT jint JNICALL Java_com_harvey_learnsamples_UsbCamera_setGamma
//        (JNIEnv *env, jobject obj, jint value){
//    return setConfig(fd,V4L2_CID_GAMMA,value);
//}
///*
// * Class:     com_harvey_learnsamples_UsbCamera
// * Method:    setAutoExposure
// * Signature: (I)I
// */
//JNIEXPORT jint JNICALL Java_com_harvey_learnsamples_UsbCamera_setAutoExposure
//        (JNIEnv *env, jobject obj, jint value){
//    if(value == 1)
//        return setConfig(fd,V4L2_CID_EXPOSURE_AUTO,V4L2_EXPOSURE_APERTURE_PRIORITY);
//    else
//        return setConfig(fd,V4L2_CID_EXPOSURE_AUTO,V4L2_EXPOSURE_MANUAL);
//}
///*
// * Class:     com_harvey_learnsamples_UsbCamera
// * Method:    setExposure
// * Signature: (I)I
// */
//JNIEXPORT jint JNICALL Java_com_harvey_learnsamples_UsbCamera_setExposure
//        (JNIEnv *env, jobject obj, jint value){
//    return setConfig(fd,V4L2_CID_EXPOSURE_ABSOLUTE,value);
//}
///*
// * Class:     com_harvey_learnsamples_UsbCamera
// * Method:    setWhiteBalanceAuto
// * Signature: (I)I
// */
//JNIEXPORT jint JNICALL Java_com_harvey_learnsamples_UsbCamera_setWhiteBalanceAuto
//        (JNIEnv *env, jobject obj, jint value){
//    return setConfig(fd,V4L2_CID_AUTO_WHITE_BALANCE,value);
//}
///*
// * Class:     com_harvey_learnsamples_UsbCamera
// * Method:    setWhiteBalance
// * Signature: (I)I
// */
//JNIEXPORT jint JNICALL Java_com_harvey_learnsamples_UsbCamera_setWhiteBalance
//        (JNIEnv *env, jobject obj, jint value){
//    return setConfig(fd,V4L2_CID_WHITE_BALANCE_TEMPERATURE,value);
//}
///*
// * Class:     com_harvey_learnsamples_UsbCamera
// * Method:    setFocusingAuto
// * Signature: (I)I
// */
//JNIEXPORT jint JNICALL Java_com_harvey_learnsamples_UsbCamera_setFocusingAuto
//        (JNIEnv *env, jobject obj, jint value){
//    return setConfig(fd,V4L2_CID_FOCUS_AUTO,value);
//}
///*
// * Class:     com_harvey_learnsamples_UsbCamera
// * Method:    setFocusing
// * Signature: (I)I
// */
//JNIEXPORT jint JNICALL Java_com_harvey_learnsamples_UsbCamera_setFocusing
//        (JNIEnv *env, jobject obj, jint value){
//    return setConfig(fd,V4L2_CID_FOCUS_ABSOLUTE,value);
//}