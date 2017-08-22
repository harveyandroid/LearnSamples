package com.harvey.openat.units;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 单位，亮度
 */
public class Luminance implements Parcelable {
	public static final String TYPE_STRING = "lm";
	public static final Creator<Luminance> CREATOR = new Creator<Luminance>() {
		@Override
		public Luminance createFromParcel(Parcel in) {
			return new Luminance(in);
		}

		@Override
		public Luminance[] newArray(int size) {
			return new Luminance[size];
		}
	};
	int value;

	public Luminance() {
		this.value = 0;
	}
	public Luminance(int value) {
		this.value = value;
	}

	protected Luminance(Parcel in) {
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
		return "Luminance{" + "value=" + value + '}';
	}
}
