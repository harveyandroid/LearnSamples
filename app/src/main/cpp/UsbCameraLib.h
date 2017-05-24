//
// Created by Administrator on 2017/5/22 0022.
//
#include <jni.h>
#include <android/log.h>
#include <android/bitmap.h>

#include <string.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <assert.h>
#include <fcntl.h>              /* low-level i/o */
#include <unistd.h>
#include <errno.h>
#include <malloc.h>

#include <sys/stat.h>
#include <sys/types.h>
#include <sys/time.h>
#include <sys/mman.h>
#include <sys/ioctl.h>
#include <asm/types.h>          /* for videodev2.h */

#include <linux/videodev2.h>
#include <linux/usbdevice_fs.h>

#include <unistd.h>

#define  LOG_TAG    "WebCam"
#define  LOGI(...)  __android_log_print(ANDROID_LOG_INFO,LOG_TAG,__VA_ARGS__)
#define  LOGE(...)  __android_log_print(ANDROID_LOG_ERROR,LOG_TAG,__VA_ARGS__)

#define ERROR_LOCAL -1
#define SUCCESS_LOCAL 0
#define CLEAR(x) memset (&(x), 0, sizeof (x))

//返回摄像头IDlist列表
int getCameraList(int *array);
//打开摄像头
int openCamera(int id);
//获取当前视频设备支持的视频格式
void getCameraFmt(int fd);
int initDevice(int fd,int width,int height);
int initmmap(int fd);
//启动视频采集命令
int startCapturing(int fd);
int readframeonce(int fd);
int readframe(int fd);
int errnoexit(const char *s);
int xioctl(int fd, int request, void *arg);
int initYuv(int width,int height);
void yuv422toyuv420(unsigned char *out, const unsigned char *in, unsigned int width, unsigned int height);
void yuyv422toABGRY(unsigned char *framebuffer,int width,int height);
void yuyv422toRGBA(unsigned char *framebuffer,int width,int height);
int stopCapturing(int fd);
int closeDevice(int fd);
int unInitDevice(void);


struct buffer {
    void *                  start;
    size_t                  length;
};
struct buffer *         buffers;
static unsigned int     n_buffers;
unsigned char *         framebuffer;

int *rgb;
int *ybuf;
int yuv_tbl_ready;
int y1192_tbl[256];
int v1634_tbl[256];
int v833_tbl[256];
int u400_tbl[256];
int u2066_tbl[256];