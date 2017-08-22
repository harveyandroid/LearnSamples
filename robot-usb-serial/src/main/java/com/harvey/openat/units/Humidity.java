package com.harvey.openat.units;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 湿度
 */
public class Humidity implements Parcelable {
	public static final String TYPE_STRING = "rh";
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

	int value;

	public Humidity() {
		this.value = 0;
	}

	public Humidity(int value) {
		this.value = value;
	}

	protected Humidity(Parcel in) {
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
		return "Humidity{" + "value=" + value + '}';
	}

}
