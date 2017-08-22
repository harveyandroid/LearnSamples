package com.harvey.openat.measurements;

import android.text.TextUtils;

import com.harvey.openat.measurements.ProtocolException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

/**
 * 校验数据格式 Created by hanhui on 2017/3/29 0029 10:18
 */

public class SensorProtocolBean {
	// 数据头
	public final static String BEGIN = "{\"sensor\":{";
	// 数据结尾
	public final static String EDN = "}]}}\r\n";
	// JSON数据头
	public final static String HEADER_KEY = "sensor";
	// JSON数据 SOC 型传感器
	public final static String SOC_KEY = "soc";
	// 亮度传感器标签
	public final static String LGT_KEY = "lgt";
	// 温度传感器标签
	public final static String TMP_KEY = "tmp";
	// 湿度传感 器标签
	public final static String HUM_KEY = "hum";
	// 超声波 0 标签
	public final static String UCW0_KEY = "ucw0";
	// 超声波 1 标签
	public final static String UCW1_KEY = "ucw1";
	// 超声波 2 标签
	public final static String UCW2_KEY = "ucw2";
	// 超声波 3 标签
	public final static String UCW3_KEY = "ucw3";
	// 前置避障传感器标签
	public final static String OBS0_KEY = "obs0";
	// 后置避障传感器标签
	public final static String OBS1_KEY = "obs1";
	// 充电对接传感器标签
	public final static String CHG_KEY = "chg";
	// 人体感应传感器标签
	public final static String DPL_KEY = "dpl";
	// 亮度传感器
	public int lgt;
	// 温度传感器
	public int tmp;
	// 湿度传感 器
	public int hum;
	// 超声波 0
	public int ucw0;
	// 超声波 1
	public int ucw1;
	// 超声波 2
	public int ucw2;
	// 超声波 3
	public int ucw3;
	// 前置避障传感器
	public int obs0;
	// 后置避障传感器
	public int obs1;
	// 充电对接传感器
	public int chg;
	// 人体感应传感器
	public int dpl;

	protected SensorProtocolBean() {
	}

	public SensorProtocolBean(String protocolSource) throws ProtocolException {

		if (TextUtils.isEmpty(protocolSource)) {
			return;
		}
		if (!protocolSource.startsWith(BEGIN)) {
			throw new ProtocolException("protocol error begin " + protocolSource);
		}
		if (!protocolSource.endsWith(EDN)) {
			throw new ProtocolException("protocol error end " + protocolSource);
		}
		try {
			JSONObject jsonObject = new JSONObject(protocolSource);
			// SOC 型传感器+地址约定
			JSONObject sensorObject = jsonObject.optJSONObject(HEADER_KEY);
			String socAddressKey = getSocAddressKey(sensorObject);
			if (TextUtils.isEmpty(socAddressKey)) {
				throw new ProtocolException("protocol socAddressKey null " + protocolSource);
			}
			parseJsonArray(sensorObject.optJSONArray(socAddressKey));
		} catch (JSONException e) {
			throw new ProtocolException("protocol json exception " + protocolSource);
		}
	}

	private void parseJsonArray(JSONArray socArray) throws JSONException {
		if (socArray == null)
			return;
		int length = socArray.length();
		for (int i = 0; i < length; i++) {
			JSONObject object = socArray.optJSONObject(i);
			if (object.has(LGT_KEY)) {
				lgt = object.optInt(LGT_KEY);
				continue;
			}
			if (object.has(TMP_KEY)) {
				tmp = object.optInt(TMP_KEY);
				continue;
			}
			if (object.has(LGT_KEY)) {
				lgt = object.optInt(LGT_KEY);
				continue;
			}
			if (object.has(HUM_KEY)) {
				hum = object.optInt(HUM_KEY);
				continue;
			}
			if (object.has(UCW0_KEY)) {
				ucw0 = object.optInt(UCW0_KEY);
				continue;
			}
			if (object.has(UCW1_KEY)) {
				ucw1 = object.optInt(UCW1_KEY);
				continue;
			}
			if (object.has(UCW2_KEY)) {
				ucw2 = object.optInt(UCW2_KEY);
				continue;
			}
			if (object.has(UCW3_KEY)) {
				ucw3 = object.optInt(UCW3_KEY);
				continue;
			}
			if (object.has(OBS0_KEY)) {
				obs0 = object.optInt(OBS0_KEY);
				continue;
			}
			if (object.has(OBS1_KEY)) {
				obs1 = object.optInt(OBS1_KEY);
				continue;
			}
			if (object.has(CHG_KEY)) {
				chg = object.optInt(CHG_KEY);
				continue;
			}
			if (object.has(DPL_KEY)) {
				dpl = object.optInt(DPL_KEY);
				continue;
			}
		}

	}
	/**
	 * 获取模块类型+地址约定
	 * <p/>
	 * SOC 型传感器；地址范围 0~7(包括7)
	 *
	 * @return
	 */
	private String getSocAddressKey(JSONObject jsonObject) {
		if (jsonObject == null)
			return null;
		for (int i = 0; i < 8; i++) {
			if (jsonObject.has(SOC_KEY + i)) {
				return SOC_KEY + i;
			}
		}
		return null;
	}

	public boolean[] getInfraredStatus() {
		boolean[] frontInfrared = getFrontInfraredStatus();
		boolean[] backInfrared = getBackInfraredStatus();
		boolean[] infrared = Arrays.copyOf(frontInfrared, frontInfrared.length + backInfrared.length);
		System.arraycopy(backInfrared, 0, infrared, frontInfrared.length, backInfrared.length);
		return infrared;
	}

	private boolean[] getFrontInfraredStatus() {

		int obs01 = obs0 / 8;// 右地
		int obs02 = (obs0 - obs01 * 8) / 4;// 左地
		int obs03 = (obs0 - obs01 * 8 - obs02 * 4) / 2;// 右侧
		int obs04 = obs0 - obs01 * 8 - obs02 * 4 - obs03 * 2;// 左侧
		return new boolean[]{obs04 == 0, obs03 == 0, obs02 == 1, obs01 == 1};
	}

	private boolean[] getBackInfraredStatus() {
		int obs01 = obs1 / 8;// 右地
		int obs02 = (obs1 - obs01 * 8) / 4;// 左地
		int obs03 = (obs1 - obs01 * 8 - obs02 * 4) / 2;// 右侧
		int obs04 = obs1 - obs01 * 8 - obs02 * 4 - obs03 * 2;// 左侧
		return new boolean[]{obs04 == 0, obs03 == 0, obs02 == 1, obs01 == 1};
	}

	@Override
	public String toString() {
		return "SensorProtocolBean{" + "lgt=" + lgt + ", tmp=" + tmp + ", hum=" + hum + ", ucw0=" + ucw0 + ", ucw1="
				+ ucw1 + ", ucw2=" + ucw2 + ", ucw3=" + ucw3 + ", obs0=" + obs0 + ", obs1=" + obs1 + ", chg=" + chg
				+ ", dpl=" + dpl + '}';
	}
}
