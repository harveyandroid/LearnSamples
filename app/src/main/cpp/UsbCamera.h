/* DO NOT EDIT THIS FILE - it is machine generated */
#include <jni.h>
/* Header for class com_harvey_learnsamples_UsbCamera */

#ifndef _Included_UsbCamera
#define _Included_UsbCamera
#ifdef __cplusplus
extern "C" {
#endif

int fd = 0;
int width = 0;
int height = 0;
/*
 * Class:     com_harvey_learnsamples_UsbCamera
 * Method:    getCameraList
 * Signature: ()[I
 */
JNIEXPORT jintArray JNICALL Java_com_harvey_learnsamples_UsbCamera_getCameraList
  (JNIEnv *, jobject);

/*
 * Class:     com_harvey_learnsamples_UsbCamera
 * Method:    startCamera
 * Signature: (III)I
 */
JNIEXPORT jint JNICALL Java_com_harvey_learnsamples_UsbCamera_openCamera
  (JNIEnv *, jobject, jint, jint, jint);

/*
 * Class:     com_harvey_learnsamples_UsbCamera
 * Method:    readCameraFrame
 * Signature: ([B)I
 */
JNIEXPORT jint JNICALL Java_com_harvey_learnsamples_UsbCamera_readCameraFrame
  (JNIEnv *, jobject, jbyteArray);

/*
 * Class:     com_harvey_learnsamples_UsbCamera
 * Method:    readCameraFrameBmp
 * Signature: (Ljava/lang/Object;)V
 */
JNIEXPORT void JNICALL Java_com_harvey_learnsamples_UsbCamera_readCameraFrameBmp
  (JNIEnv *, jobject, jobject);

/*
 * Class:     com_harvey_learnsamples_UsbCamera
 * Method:    readCameraFrameRGBA
 * Signature: ([B)I
 */
JNIEXPORT jint JNICALL Java_com_harvey_learnsamples_UsbCamera_readCameraFrameRGBA
(JNIEnv *, jobject, jbyteArray);


/*
 * Class:     com_harvey_learnsamples_UsbCamera
 * Method:    stopCamera
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_com_harvey_learnsamples_UsbCamera_stopCamera
  (JNIEnv *, jobject);

/*
 * Class:     com_harvey_learnsamples_UsbCamera
 * Method:    destoryCamera
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_com_harvey_learnsamples_UsbCamera_destoryCamera
  (JNIEnv *, jobject);

///*
// * Class:     com_harvey_learnsamples_UsbCamera
// * Method:    setBrightness
// * Signature: (I)I
// */
//JNIEXPORT jint JNICALL Java_com_harvey_learnsamples_UsbCamera_setBrightness
//  (JNIEnv *, jobject, jint);
//
///*
// * Class:     com_harvey_learnsamples_UsbCamera
// * Method:    setContrast
// * Signature: (I)I
// */
//JNIEXPORT jint JNICALL Java_com_harvey_learnsamples_UsbCamera_setContrast
//  (JNIEnv *, jobject, jint);
//
///*
// * Class:     com_harvey_learnsamples_UsbCamera
// * Method:    setSaturation
// * Signature: (I)I
// */
//JNIEXPORT jint JNICALL Java_com_harvey_learnsamples_UsbCamera_setSaturation
//  (JNIEnv *, jobject, jint);
//
///*
// * Class:     com_harvey_learnsamples_UsbCamera
// * Method:    setHue
// * Signature: (I)I
// */
//JNIEXPORT jint JNICALL Java_com_harvey_learnsamples_UsbCamera_setHue
//  (JNIEnv *, jobject, jint);
//
///*
// * Class:     com_harvey_learnsamples_UsbCamera
// * Method:    setSharpness
// * Signature: (I)I
// */
//JNIEXPORT jint JNICALL Java_com_harvey_learnsamples_UsbCamera_setSharpness
//  (JNIEnv *, jobject, jint);
//
///*
// * Class:     com_harvey_learnsamples_UsbCamera
// * Method:    setGain
// * Signature: (I)I
// */
//JNIEXPORT jint JNICALL Java_com_harvey_learnsamples_UsbCamera_setGain
//  (JNIEnv *, jobject, jint);
//
///*
// * Class:     com_harvey_learnsamples_UsbCamera
// * Method:    setGamma
// * Signature: (I)I
// */
//JNIEXPORT jint JNICALL Java_com_harvey_learnsamples_UsbCamera_setGamma
//  (JNIEnv *, jobject, jint);
//
///*
// * Class:     com_harvey_learnsamples_UsbCamera
// * Method:    setAutoExposure
// * Signature: (I)I
// */
//JNIEXPORT jint JNICALL Java_com_harvey_learnsamples_UsbCamera_setAutoExposure
//  (JNIEnv *, jobject, jint);
//
///*
// * Class:     com_harvey_learnsamples_UsbCamera
// * Method:    setExposure
// * Signature: (I)I
// */
//JNIEXPORT jint JNICALL Java_com_harvey_learnsamples_UsbCamera_setExposure
//  (JNIEnv *, jobject, jint);
//
///*
// * Class:     com_harvey_learnsamples_UsbCamera
// * Method:    setWhiteBalanceAuto
// * Signature: (I)I
// */
//JNIEXPORT jint JNICALL Java_com_harvey_learnsamples_UsbCamera_setWhiteBalanceAuto
//  (JNIEnv *, jobject, jint);
//
///*
// * Class:     com_harvey_learnsamples_UsbCamera
// * Method:    setWhiteBalance
// * Signature: (I)I
// */
//JNIEXPORT jint JNICALL Java_com_harvey_learnsamples_UsbCamera_setWhiteBalance
//  (JNIEnv *, jobject, jint);
//
///*
// * Class:     com_harvey_learnsamples_UsbCamera
// * Method:    setFocusingAuto
// * Signature: (I)I
// */
//JNIEXPORT jint JNICALL Java_com_harvey_learnsamples_UsbCamera_setFocusingAuto
//  (JNIEnv *, jobject, jint);
//
///*
// * Class:     com_harvey_learnsamples_UsbCamera
// * Method:    setFocusing
// * Signature: (I)I
// */
//JNIEXPORT jint JNICALL Java_com_harvey_learnsamples_UsbCamera_setFocusing
//  (JNIEnv *, jobject, jint);

#ifdef __cplusplus
}
#endif
#endif
