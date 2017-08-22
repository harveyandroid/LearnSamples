package com.harvey.openat.units;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.SparseArray;

/**
 * 多普勒传感器
 */

public class Doppler implements Parcelable {
	public static final Creator<Humidity> CREATOR = new Creator<Humidity>() {
		@Override
		public Humidity createFromParcel(Parcel in) {
			return new Humidity(in);
		}

		@Override
		public Humidity[] newArray(int size) {
			return new Humidity[size];
		}
	};
	public static SparseArray<String> Msg;

	public static Doppler DOPPLER_STATE_NO = new Doppler(0); // 没有感应到移动导体
	public static Doppler DOPPLER_STATE_YES = new Doppler(1); // 有感应到移动导体

	static {
		Msg = new SparseArray();
		Msg.put(DOPPLER_STATE_NO.intValue(), "没有感应到移动导体");
		Msg.put(DOPPLER_STATE_YES.intValue(), "有感应到移动导体");
	}

	int value;

	public Doppler() {
		this.value = 0;
	}

	public Doppler(int value) {
		this.value = value;
	}

	protected Doppler(Parcel in) {
		value = in.readInt();
	}

	public String getMsg() {
		return Msg.get(value, "数据格式不对").toString();
	}

	public int intValue() {
		return value;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel parcel, int i) {
		parcel.writeInt(value);
	}

	@Override
	public String toString() {
		return "Doppler{" +
				"value=" + value +
				'}';
	}
}