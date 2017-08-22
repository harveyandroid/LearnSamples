package com.harvey.openat.units;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 单位，厘米
 */
public class Centimeter implements Parcelable {
	public static final String TYPE_STRING	= "cm";
	int							value;

	public Centimeter(int value) {
		this.value = value;
	}

	public int intValue() {
		return value;
	}

	protected Centimeter(Parcel in) {
		value = in.readInt();
	}

	public static final Creator<Centimeter> CREATOR = new Creator<Centimeter>() {
		@Override
		public Centimeter createFromParcel(Parcel in) {
			return new Centimeter(in);
		}

		@Override
		public Centimeter[] newArray(int size) {
			return new Centimeter[size];
		}
	};

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
		return "Centimeter{" + "value=" + value + '}';
	}
}
