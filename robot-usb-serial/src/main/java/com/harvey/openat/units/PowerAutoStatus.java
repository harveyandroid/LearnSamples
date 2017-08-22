package com.harvey.openat.units;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.SparseArray;

/**
 * 自动充电状态
 */
public class PowerAutoStatus implements Parcelable {

	public static final int PWR_STATE_NORMAL = 0; // 正常工作状态
	public static final int PWR_STATE_PILE_L_R = 1; // 充电桩左右连接
	public static final int PWR_STATE_PILE_L = 2; // 充电桩左连接
	public static final int PWR_STATE_PILE_R = 3; // 充电桩右连接
	public static final Creator<PowerAutoStatus> CREATOR = new Creator<PowerAutoStatus>() {
		@Override
		public PowerAutoStatus createFromParcel(Parcel source) {
			return new PowerAutoStatus(source);
		}

		@Override
		public PowerAutoStatus[] newArray(int size) {
			return new PowerAutoStatus[size];
		}
	};
	public static SparseArray<String> Msg;

	static {
		Msg = new SparseArray<>();
		Msg.put(PWR_STATE_PILE_L_R, "充电桩左右连接");
		Msg.put(PWR_STATE_PILE_L, "充电桩左连接");
		Msg.put(PWR_STATE_PILE_R, "充电桩右连接");
		Msg.put(PWR_STATE_NORMAL, "正常工作状态");
	}

	private int value;

	public PowerAutoStatus() {
		this.value = 0;
	}
	public PowerAutoStatus(int value) {
		this.value = value;
	}

	protected PowerAutoStatus(Parcel in) {
		this.value = in.readInt();
	}

	public int intValue() {
		return value;
	}

	public String getMsg() {
		return Msg.get(value, "数据格式不对").toString();
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(this.value);
	}

	@Override
	public String toString() {
		return "PowerAutoStatus{" + "状态=" + getMsg() + '}';
	}
}
