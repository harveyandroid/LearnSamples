package com.harvey.openat.units;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.SparseArray;

/**
 * 行走状态
 */
public class TravelingStatus implements Parcelable {
	public static final TravelingStatus TRAVEL_STATE_ERROR = new TravelingStatus(-1); // 数据异常
	public static final TravelingStatus TRAVEL_STATE_STOP = new TravelingStatus(0); // 停止
	public static final TravelingStatus TRAVEL_STATE_MOVE = new TravelingStatus(1); // 移动
	public static final Creator<TravelingStatus> CREATOR = new Creator<TravelingStatus>() {
		@Override
		public TravelingStatus createFromParcel(Parcel source) {
			return new TravelingStatus(source);
		}

		@Override
		public TravelingStatus[] newArray(int size) {
			return new TravelingStatus[size];
		}
	};
	public static SparseArray<String> Msg;

	static {
		Msg = new SparseArray();
		Msg.put(TRAVEL_STATE_ERROR.getValue(), "数据异常");
		Msg.put(TRAVEL_STATE_STOP.getValue(), "停止");
		Msg.put(TRAVEL_STATE_MOVE.getValue(), "移动");
	}
	private int value;

	public TravelingStatus(int value) {
		this.value = value;
	}
	protected TravelingStatus(Parcel in) {
		this.value = in.readInt();
	}

	public String getMsg() {
		return Msg.get(value, "数据格式不对").toString();
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(this.value);
	}
}
