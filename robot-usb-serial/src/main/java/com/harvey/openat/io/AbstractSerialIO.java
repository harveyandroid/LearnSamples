package com.harvey.openat.io;

import android.hardware.usb.UsbRequest;
import android.util.Log;


import com.harvey.openat.utils.HexDump;
import com.harvey.openat.driver.UsbSerialPort;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * 串口读写
 */
public abstract class AbstractSerialIO implements Runnable {

	private static final boolean DEBUG = true;
	private static final long TIME_WAIT_RECORDING = 10;
	private static final int READ_WAIT_MILLIS = 200;
	private static final int BUFF_SIZE = 4096;
	private static final int QUEUE_SIZE = 50;
	// 队列
	final BlockingQueue<byte[]> uartQueue = new ArrayBlockingQueue<>(QUEUE_SIZE);
	private final UsbSerialPort serialPort;
	private final ByteBuffer mReadBuffer = ByteBuffer.allocate(BUFF_SIZE);
	long writeSystime = 0;
	long readSystime = 0;
	// Synchronized by 'this'
	private State mState = State.STOPPED;
	/**
	 * Creates a new instance with the provided listener.
	 */
	public AbstractSerialIO(UsbSerialPort driver) {
		serialPort = driver;
	}

	public void writeAsync(byte[] data) {
		uartQueue.offer(data);
	}

	public synchronized void stop() {
		if (getState() == State.RUNNING) {
			Log.i(getLogTag(), "Stop requested");
			mState = State.STOPPING;
		}
	}

	public abstract String getLogTag();

	private synchronized State getState() {
		return mState;
	}

	/**
	 * Continuously services the read and write buffers until {@link #stop()} is
	 * called, or until a driver exception is raised.
	 *
	 * NOTE(mikey): Uses inefficient read/write-with-timeout. TODO(mikey): Read
	 * asynchronously with {@link UsbRequest#queue(ByteBuffer, int)}
	 */
	@Override
	public void run() {
		synchronized (this) {
			if (getState() != State.STOPPED) {
				throw new IllegalStateException("Already running.");
			}
			mState = State.RUNNING;
		}
		Log.i(getLogTag(), "Running ..");
		try {
			while (true) {
				if (getState() != State.RUNNING) {
					Log.i(getLogTag(), "Stopping mState=" + getState());
					break;
				}
				stepQueue();
			}
		} catch (Exception e) {
			Log.w(getLogTag(), "Run ending due to exception: " + e.getMessage(), e);
		} finally {
			synchronized (this) {
				mState = State.STOPPED;
				Log.i(getLogTag(), "Stopped.");
			}
		}
	}

	private void stepQueue() throws IOException, InterruptedException {

		int len = serialPort.read(mReadBuffer.array(), READ_WAIT_MILLIS);
		if (len > 0) {
			final byte[] data = new byte[len];
			mReadBuffer.get(data, 0, len);
			readData(data);
			mReadBuffer.clear();
			if (DEBUG) {
				Log.d(getLogTag(), "Read data len=" + len);
				Log.e(getLogTag(), "Read data time space：" + (System.currentTimeMillis() - readSystime));
			}
		}
		readSystime = System.currentTimeMillis();
		// 空队列，继续循环
		if (uartQueue.size() == 0)
			return;
		// 队列取出元素
		byte[] outData = uartQueue.poll(TIME_WAIT_RECORDING, TimeUnit.MILLISECONDS);
		// 空元素继续循环
		if (outData == null)
			return;
		serialPort.write(outData, READ_WAIT_MILLIS);
		if (DEBUG) {
			Log.e(getLogTag(), "write data：" + HexDump.toHexString(outData));
			Log.e(getLogTag(), "write data time space：" + (System.currentTimeMillis() - writeSystime));
		}
		writeSystime = System.currentTimeMillis();
	}

	public abstract void readData(byte[] data);

	private enum State {
		STOPPED, RUNNING, STOPPING
	}
}
