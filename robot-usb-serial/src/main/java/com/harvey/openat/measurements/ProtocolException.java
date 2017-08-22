package com.harvey.openat.measurements;

import android.util.Log;

/**
 * Created by hanhui on 2017/3/29 0029 10:18
 */

public class ProtocolException extends Exception {
	private String LOG = "ProtocolException";

	public ProtocolException() {
		super();
	}

	public ProtocolException(String message) {
		super(message);
		Log.e(LOG, message);
	}
}
