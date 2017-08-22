package com.harvey.openat.units;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 单位，温度°C
 */
public class Temperature implements Parcelable {
	public static final String TYPE_STRING = "°C";
	public static final Creator<Temperature> CREATOR = new Creator<Temperature>() {
		@Override
		public Temperature createFromParcel(Parcel in) {
			return new Temperature(in);
		}

		@Override
		public Temperature[] newArray(int size) {
			return new Temperature[size];
		}
	};

	int value;

	public Temperature() {
		this.value = 0;
	}
	public Temperature(int value) {
		this.value = value;
	}

	protected Temperature(Parcel in) {
		value = in.readInt();
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
		return "Temperature{" +
				"value=" + value +
				'}';
	}
}
