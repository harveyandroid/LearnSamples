//
// Created by Administrator on 2017/5/22 0022.
//

#include "UsbCameraLib.h"

//返回摄像头IDlist列表
int getCameraList(int *array){
    //
    char *dev = "/dev/video%d";
    char dev_name[16];

    int size = 0;
    int i = 0;
    for(i=0; i<4; i++)
    {
        sprintf(dev_name, dev, i);
        //文件是否存在
        if(access(dev_name,F_OK)==0) {
            array[size] = i;
            size++;
        }
    }

    return size;
}

//打开摄像头
int openCamera(int id){
    //
    char *dev = "/dev/video%d";
    char dev_name[16];
    sprintf(dev_name, dev, id);

    //
    int fd = open(dev_name, O_RDWR | O_NONBLOCK, 0);
    if (-1 == fd) {
        LOGE("Cannot open '%s'", dev_name);
        return ERROR_LOCAL;
    }
    LOGE("open '%s' success", dev_name);
    return fd;
}

//获取当前视频设备支持的视频格式
void getCameraFmt(int fd){
    struct v4l2_fmtdesc fmt;
    CLEAR (fmt);
    fmt.index = 0;
    fmt.type = V4L2_BUF_TYPE_VIDEO_CAPTURE;
    int ret;
    while ((ret = xioctl(fd, VIDIOC_ENUM_FMT, &fmt)) == 0)
    {
        fmt.index++;
        LOGE("{ pixelformat = ''%c%c%c%c'', description = ''%s'' }\n",
             fmt.pixelformat & 0xFF, (fmt.pixelformat >> 8) & 0xFF, (fmt.pixelformat >> 16) & 0xFF,(fmt.pixelformat >> 24) & 0xFF, fmt.description);
    }
}

//
int initDevice(int fd,int width,int height) {
    //设备支持的操作模式
    struct v4l2_capability cap;
    //视频裁剪和缩放功能信息
    struct v4l2_cropcap cropcap;
    struct v4l2_crop crop;
    struct v4l2_format fmt;
    unsigned int min;

    //查询当前driver是否合乎规范
    if (-1 == xioctl(fd, VIDIOC_QUERYCAP, &cap)) {
        if (EINVAL == errno) {
            LOGE("%d is no V4L2 device", fd);
            return ERROR_LOCAL;
        } else {
            return errnoexit("VIDIOC_QUERYCAP");
        }
    }

    if (!(cap.capabilities & V4L2_CAP_VIDEO_CAPTURE)) {
        LOGE("%d is no video capture device", fd);
        return ERROR_LOCAL;
    }

    if (!(cap.capabilities & V4L2_CAP_STREAMING)) {
        LOGE("%d does not support streaming i/o", fd);
        return ERROR_LOCAL;
    }

    //视频裁剪和缩放功能信息
    CLEAR (cropcap);
    cropcap.type = V4L2_BUF_TYPE_VIDEO_CAPTURE;
    if (0 == xioctl(fd, VIDIOC_CROPCAP, &cropcap)) {
        crop.type = V4L2_BUF_TYPE_VIDEO_CAPTURE;
        crop.c = cropcap.defrect;
        //设置裁剪区域
        if (-1 == xioctl(fd, VIDIOC_S_CROP, &crop)) {
            LOGE("%d VIDIOC_S_CROP ERROR", fd);
        }
    } else {
        LOGE("%d VIDIOC_CROPCAP ERROR", fd);
    }

    //设置视频设备的视频数据格式，例如设置视频图像数据的长、宽，图像格式(JPEG、YUYV格式);
    CLEAR (fmt);
    fmt.type = V4L2_BUF_TYPE_VIDEO_CAPTURE;
    //分辨率
    fmt.fmt.pix.width = width;
    fmt.fmt.pix.height = height;
    //设置视频格式
    fmt.fmt.pix.pixelformat = V4L2_PIX_FMT_YUYV;
    fmt.fmt.pix.field = V4L2_FIELD_INTERLACED;
    if (-1 == xioctl(fd, VIDIOC_S_FMT, &fmt)){
        return errnoexit("VIDIOC_S_FMT");
    }
    LOGE("init '%d' success width=%d,height=%d", fd,width,height);

    initYuv(width,height);

    /* set framerate */
    struct v4l2_streamparm* setfps;
    setfps=(struct v4l2_streamparm *) calloc(1, sizeof(struct v4l2_streamparm));
    memset(setfps, 0, sizeof(struct v4l2_streamparm));
    setfps->type = V4L2_BUF_TYPE_VIDEO_CAPTURE;
    setfps->parm.capture.timeperframe.numerator=1;
    setfps->parm.capture.timeperframe.denominator=30;
    int ret = xioctl(fd, VIDIOC_S_PARM, setfps);
    if(ret == -1) {
        LOGE("Unable to set frame rate");
    }
    ret = xioctl(fd, VIDIOC_G_PARM, setfps);
    if(ret == 0) {
        if (setfps->parm.capture.timeperframe.numerator != 1 ||
            setfps->parm.capture.timeperframe.denominator != 30) {
            LOGE("  Frame rate:   %d",setfps->parm.capture.timeperframe.denominator);
        }
        else {
            LOGE("  Frame rate:   %d fps", 30);
        }
    }
    else {
        LOGE("Unable to read out current frame rate");
    }

    return initmmap(fd);
}

//Buffer的申请和数据的抓取
int initmmap(int fd) {
    //帧缓冲
    struct v4l2_requestbuffers req;
    CLEAR (req);
    req.count = 4;                              //缓存数量,也就是说在缓存队列里保持多少张照片
    req.type = V4L2_BUF_TYPE_VIDEO_CAPTURE;     //数据流类型,必须永远是V4L2_BUF_TYPE_VIDEO_CAPTURE
    req.memory = V4L2_MEMORY_MMAP;              //mmap方式
    //请求V4L2驱动分配视频缓冲区(申请V4L2视频驱动分配内存)
    if (-1 == xioctl(fd, VIDIOC_REQBUFS, &req)) {
        if (EINVAL == errno) {
            LOGE("%d does not support memory mapping", fd);
            return ERROR_LOCAL;
        } else {
            return errnoexit("VIDIOC_REQBUFS");
        }
    }
    if (req.count < 2) {
        LOGE("Insufficient buffer memory on %d", fd);
        return ERROR_LOCAL;
    }

    //开辟buffers内存空间,帧缓冲对应大小
    buffers = calloc(req.count, sizeof(*buffers));
    if (!buffers) {
        LOGE("Out of memory");
        return ERROR_LOCAL;
    }

    //在通过调用VIDIOC_QUERYBUF取得内核空间的缓冲区信息后，接着调用mmap函数把内核空间缓冲区映射到用户空间
    for (n_buffers = 0; n_buffers < req.count; ++n_buffers) {
        //内存区信息
        struct v4l2_buffer buf;
        CLEAR (buf);
        buf.type = V4L2_BUF_TYPE_VIDEO_CAPTURE;
        buf.memory = V4L2_MEMORY_MMAP;
        buf.index = n_buffers;
        //查询已经分配的V4L2的视频缓冲区的相关信息，包括视频缓冲区的使用状态、在内核空间的偏移地址、缓冲区长度等。
        if (-1 == xioctl(fd, VIDIOC_QUERYBUF, &buf))
            return errnoexit("VIDIOC_QUERYBUF");

        // 把内核空间缓冲区映射到用户空间缓冲区
        buffers[n_buffers].length = buf.length;
        buffers[n_buffers].start = mmap(NULL,
                                        buf.length,
                                        PROT_READ | PROT_WRITE,
                                        MAP_SHARED,
                                        fd, buf.m.offset);

        if (MAP_FAILED == buffers[n_buffers].start)
            return errnoexit("mmap");
    }
    return SUCCESS_LOCAL;
}

int initYuv(int width,int height){
    rgb = (int *) malloc(sizeof(int) * (width * height));
    ybuf = (int *) malloc(sizeof(int) * (width * height));
}

void yuyv422toABGRY(unsigned char *framebuffer,int width,int height) {

    int frameSize = width * height * 2;

    int i;

    if ((!rgb || !ybuf)) {
        return;
    }
    int *lrgb;
    int *lybuf;

    lrgb = &rgb[0];
    lybuf = &ybuf[0];

    if (yuv_tbl_ready == 0) {
        for (i = 0; i < 256; i++) {
            y1192_tbl[i] = 1192 * (i - 16);
            if (y1192_tbl[i] < 0) {
                y1192_tbl[i] = 0;
            }

            v1634_tbl[i] = 1634 * (i - 128);
            v833_tbl[i] = 833 * (i - 128);
            u400_tbl[i] = 400 * (i - 128);
            u2066_tbl[i] = 2066 * (i - 128);
        }
        yuv_tbl_ready = 1;
    }

    for (i = 0; i < frameSize; i += 4) {
        unsigned char y1, y2, u, v;
        y1 = framebuffer[i];
        u = framebuffer[i + 1];
        y2 = framebuffer[i + 2];
        v = framebuffer[i + 3];

        int y1192_1 = y1192_tbl[y1];
        int r1 = (y1192_1 + v1634_tbl[v]) >> 10;
        int g1 = (y1192_1 - v833_tbl[v] - u400_tbl[u]) >> 10;
        int b1 = (y1192_1 + u2066_tbl[u]) >> 10;

        int y1192_2 = y1192_tbl[y2];
        int r2 = (y1192_2 + v1634_tbl[v]) >> 10;
        int g2 = (y1192_2 - v833_tbl[v] - u400_tbl[u]) >> 10;
        int b2 = (y1192_2 + u2066_tbl[u]) >> 10;

        r1 = r1 > 255 ? 255 : r1 < 0 ? 0 : r1;
        g1 = g1 > 255 ? 255 : g1 < 0 ? 0 : g1;
        b1 = b1 > 255 ? 255 : b1 < 0 ? 0 : b1;
        r2 = r2 > 255 ? 255 : r2 < 0 ? 0 : r2;
        g2 = g2 > 255 ? 255 : g2 < 0 ? 0 : g2;
        b2 = b2 > 255 ? 255 : b2 < 0 ? 0 : b2;

        *lrgb++ = 0xff000000 | b1 << 16 | g1 << 8 | r1;
        *lrgb++ = 0xff000000 | b2 << 16 | g2 << 8 | r2;

        if (lybuf != NULL) {
            *lybuf++ = y1;
            *lybuf++ = y2;
        }
    }

}

void yuyv422toRGBA(unsigned char *framebuffer,int width,int height) {

    int frameSize = width * height * 2;

    int i;

    if ((!rgb || !ybuf)) {
        return;
    }
    int *lrgb;
    int *lybuf;

    lrgb = &rgb[0];
    lybuf = &ybuf[0];

    if (yuv_tbl_ready == 0) {
        for (i = 0; i < 256; i++) {
            y1192_tbl[i] = 1192 * (i - 16);
            if (y1192_tbl[i] < 0) {
                y1192_tbl[i] = 0;
            }

            v1634_tbl[i] = 1634 * (i - 128);
            v833_tbl[i] = 833 * (i - 128);
            u400_tbl[i] = 400 * (i - 128);
            u2066_tbl[i] = 2066 * (i - 128);
        }
        yuv_tbl_ready = 1;
    }

    for (i = 0; i < frameSize; i += 4) {
        unsigned char y1, y2, u, v;
        y1 = framebuffer[i];
        u = framebuffer[i + 1];
        y2 = framebuffer[i + 2];
        v = framebuffer[i + 3];

        int y1192_1 = y1192_tbl[y1];
        int r1 = (y1192_1 + v1634_tbl[v]) >> 10;
        int g1 = (y1192_1 - v833_tbl[v] - u400_tbl[u]) >> 10;
        int b1 = (y1192_1 + u2066_tbl[u]) >> 10;

        int y1192_2 = y1192_tbl[y2];
        int r2 = (y1192_2 + v1634_tbl[v]) >> 10;
        int g2 = (y1192_2 - v833_tbl[v] - u400_tbl[u]) >> 10;
        int b2 = (y1192_2 + u2066_tbl[u]) >> 10;

        r1 = r1 > 255 ? 255 : r1 < 0 ? 0 : r1;
        g1 = g1 > 255 ? 255 : g1 < 0 ? 0 : g1;
        b1 = b1 > 255 ? 255 : b1 < 0 ? 0 : b1;
        r2 = r2 > 255 ? 255 : r2 < 0 ? 0 : r2;
        g2 = g2 > 255 ? 255 : g2 < 0 ? 0 : g2;
        b2 = b2 > 255 ? 255 : b2 < 0 ? 0 : b2;

        *lrgb++ = 0xff000000 | b1 << 16 | g1 << 8 | r1;
        *lrgb++ = 0xff000000 | b2 << 16 | g2 << 8 | r2;

        if (lybuf != NULL) {
            *lybuf++ = y1;
            *lybuf++ = y2;
        }
    }

}

void yuv422toyuv420(unsigned char *out, const unsigned char *in, unsigned int width, unsigned int height){
    unsigned char *y = out;
    unsigned char *u = out + width*height;
    unsigned char *v = out + width*height + width*height/4;

    unsigned int i,j;
    unsigned int base_h;
    unsigned int is_y = 1, is_u = 1;
    unsigned int y_index = 0, u_index = 0, v_index = 0;

    unsigned long yuv422_length = 2 * width * height;

    //序列为YU YV YU YV，一个yuv422帧的长度 width * height * 2 个字节
    //丢弃偶数行 u v

    for(i=0; i<yuv422_length; i+=2){
        *(y+y_index) = *(in+i);
        y_index++;
    }

    for(i=0; i<height; i+=2){
        base_h = i*width*2;
        for(j=base_h+1; j<base_h+width*2; j+=2){
            if(is_u){
                *(u+u_index) = *(in+j);
                u_index++;
                is_u = 0;
            }
            else{
                *(v+v_index) = *(in+j);
                v_index++;
                is_u = 1;
            }
        }
    }
}

//启动视频采集命令
int startCapturing(int fd) {
    unsigned int i;
    enum v4l2_buf_type type;

    for (i = 0; i < n_buffers; ++i) {
        struct v4l2_buffer buf;
        CLEAR (buf);
        buf.type = V4L2_BUF_TYPE_VIDEO_CAPTURE;
        buf.memory = V4L2_MEMORY_MMAP;
        buf.index = i;

        //投放一个空的视频缓冲区到视频缓冲区输入队列中
        if (-1 == xioctl(fd, VIDIOC_QBUF, &buf))
            return errnoexit("VIDIOC_QBUF");
    }

    // 启动视频采集命令，应用程序调用VIDIOC_STREAMON启动视频采集命令后，
    // 视频设备驱动程序开始采集视频数据，并把采集到的视频数据保存到视频驱动的视频缓冲区中。
    type = V4L2_BUF_TYPE_VIDEO_CAPTURE;
    if (-1 == xioctl(fd, VIDIOC_STREAMON, &type))
        return errnoexit("VIDIOC_STREAMON");

    return SUCCESS_LOCAL;
}


int readframeonce(int fd) {
    for (; ;) {
        fd_set fds;
        struct timeval tv;
        //将set清零使集合中不含任何fd
        FD_ZERO(&fds);
        //将fd加入set集合
        FD_SET(fd, &fds);
        //指定了select调用在返回前等待多长时间
        tv.tv_sec = 2;
        tv.tv_usec = 0;
        int r = select(fd + 1, &fds, NULL, NULL, &tv);
        //描述符集清0
        if (-1 == r) {
            if (EINTR == errno)
                continue;
            return errnoexit("select");
        }
        //超时
        if (0 == r) {
            LOGE("select timeout");
            return ERROR_LOCAL;
        }
        //成功
        if (readframe(fd) == 1)
            break;
    }
    return SUCCESS_LOCAL;
}

int readframe(int fd) {
    struct v4l2_buffer buf;
    unsigned int i;
    CLEAR (buf);
    buf.type = V4L2_BUF_TYPE_VIDEO_CAPTURE;
    buf.memory = V4L2_MEMORY_MMAP;
    //从视频缓冲区的输出队列中取得一个已经保存有一帧视频数据的视频缓冲区
    if (-1 == xioctl(fd, VIDIOC_DQBUF, &buf)) {
        switch (errno) {
            case EAGAIN:
                return 0;
            case EIO:
            default:
                return errnoexit("VIDIOC_DQBUF");
        }
    }
    //函数执行成功后，相应的内核视频缓冲区中保存有当前拍摄到的视频数据，应用程序可以通过访问用户空间来读取该视频数据。
    // 在调用ioctl-VIDIOC_REQBUFS时，建立了count个Buffer。所以，这里index的有效范围是：0到count-1.
    framebuffer = (unsigned char *)buffers[buf.index].start;

    //投放一个空的视频缓冲区到视频缓冲区输入队列中
    //指令(指定)的视频缓冲区进入视频输入队列，在启动视频设备拍摄图像时，相应的视频数据被保存到视频输入队列相应的视频缓冲区中
    if (-1 == xioctl(fd, VIDIOC_QBUF, &buf))
        return errnoexit("VIDIOC_QBUF");

    return 1;
}

int stopCapturing(int fd) {
    enum v4l2_buf_type type;
    type = V4L2_BUF_TYPE_VIDEO_CAPTURE;

    if (-1 == xioctl(fd, VIDIOC_STREAMOFF, &type))
        return errnoexit("VIDIOC_STREAMOFF");

    return SUCCESS_LOCAL;

}


int unInitDevice(void) {
    unsigned int i;

    for (i = 0; i < n_buffers; ++i)
        if (-1 == munmap(buffers[i].start, buffers[i].length))
            return errnoexit("munmap");

    free(buffers);

    return SUCCESS_LOCAL;
}

int closeDevice(int fd) {
    if (-1 == close(fd)) {
        fd = -1;
        return errnoexit("close");
    }

    fd = -1;
    return SUCCESS_LOCAL;
}

int setConfig(int fd,int type,int value){
    struct v4l2_control control_s;
    CLEAR (control_s);
    control_s.id    = type;
    control_s.value     = value;
    if (-1 == xioctl(fd, VIDIOC_S_CTRL, &control_s)) {
        return errnoexit("VIDIOC_S_CTRL");
    }

    LOGE("VIDIOC_S_CTRL success! %d",value);
    return SUCCESS_LOCAL;
}


int errnoexit(const char *s) {
    LOGE("%s error %d, %s", s, errno, strerror(errno));
    return ERROR_LOCAL;
}

int xioctl(int fd, int request, void *arg) {
    int r;
    do r = ioctl(fd, request, arg);
    while (-1 == r && EINTR == errno);
    return r;
}
