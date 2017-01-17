package com.example.test.util;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 一个简易的json-hashmap转换和格式化工具
 * 
 * @author ZengJ
 */
public class Hashon {

	/** json字符串转为hashmap */
	public HashMap<String, Object> fromJson(String jsonStr) {
		try {
			return fromJson(new JSONObject(jsonStr));
		} catch (Throwable t) {
			t.printStackTrace();
		}
		return new HashMap<String, Object>();
	}

	private HashMap<String, Object> fromJson(JSONObject json)
			throws JSONException {
		HashMap<String, Object> map = new HashMap<String, Object>();
		@SuppressWarnings("unchecked")
		Iterator<String> iKey = json.keys();
		while (iKey.hasNext()) {
			String key = iKey.next();
			Object value = json.opt(key);
			if (value == JSONObject.NULL) {
				value = null;
			}
			if (value != null) {
				if (value instanceof JSONObject) {
					value = fromJson((JSONObject) value);
				} else if (value instanceof JSONArray) {
					value = fromJson((JSONArray) value);
				}
				map.put(key, value);
			}
		}
		return map;
	}

	private ArrayList<Object> fromJson(JSONArray array) throws JSONException {
		ArrayList<Object> list = new ArrayList<Object>();
		for (int i = 0, size = array.length(); i < size; i++) {
			Object value = array.opt(i);
			if (value instanceof JSONObject) {
				value = fromJson((JSONObject) value);
			} else if (value instanceof JSONArray) {
				value = fromJson((JSONArray) value);
			}
			list.add(value);
		}
		return list;
	}

	/** hashmap转为json字符串 */
	public String fromHashMap(HashMap<String, Object> map) {
		try {
			return getJSONObject(map).toString();
		} catch (Throwable t) {
			t.printStackTrace();
		}
		return "";
	}

	@SuppressWarnings("unchecked")
	private JSONObject getJSONObject(HashMap<String, Object> map)
			throws JSONException {
		JSONObject json = new JSONObject();
		for (Entry<String, Object> entry : map.entrySet()) {
			Object value = entry.getValue();
			if (value instanceof HashMap<?, ?>) {
				value = getJSONObject((HashMap<String, Object>) value);
			} else if (value instanceof ArrayList<?>) {
				value = getJSONArray((ArrayList<Object>) value);
			}
			json.put(entry.getKey(), value);
		}
		return json;
	}

	@SuppressWarnings("unchecked")
	private JSONArray getJSONArray(ArrayList<Object> list) throws JSONException {
		JSONArray array = new JSONArray();
		for (Object value : list) {
			if (value instanceof HashMap<?, ?>) {
				value = getJSONObject((HashMap<String, Object>) value);
			} else if (value instanceof ArrayList<?>) {
				value = getJSONArray((ArrayList<Object>) value);
			}
			array.put(value);
		}
		return array;
	}

	/** 格式化给定json串 */
	public String format(String jsonStr) {
		try {
			return format("", fromJson(jsonStr));
		} catch (Throwable t) {
			t.printStackTrace();
		}
		return "";
	}

	@SuppressWarnings("unchecked")
	private String format(String sepStr, HashMap<String, Object> map) {
		StringBuffer sb = new StringBuffer();
		sb.append("{\n");
		String mySepStr = sepStr + "\t";
		int i = 0;
		for (Entry<String, Object> entry : map.entrySet()) {
			if (i > 0) {
				sb.append(",\n");
			}
			sb.append(mySepStr).append('\"').append(entry.getKey())
					.append("\":");
			Object value = entry.getValue();
			if (value instanceof HashMap<?, ?>) {
				sb.append(format(mySepStr, (HashMap<String, Object>) value));
			} else if (value instanceof ArrayList<?>) {
				sb.append(format(mySepStr, (ArrayList<Object>) value));
			} else if (value instanceof String) {
				sb.append('\"').append(value).append('\"');
			} else {
				sb.append(value);
			}
			i++;
		}
		sb.append('\n').append(sepStr).append('}');
		return sb.toString();
	}

	@SuppressWarnings("unchecked")
	private String format(String sepStr, ArrayList<Object> list) {
		StringBuffer sb = new StringBuffer();
		sb.append("[\n");
		String mySepStr = sepStr + "\t";
		int i = 0;
		for (Object value : list) {
			if (i > 0) {
				sb.append(",\n");
			}
			sb.append(mySepStr);
			if (value instanceof HashMap<?, ?>) {
				sb.append(format(mySepStr, (HashMap<String, Object>) value));
			} else if (value instanceof ArrayList<?>) {
				sb.append(format(mySepStr, (ArrayList<Object>) value));
			} else if (value instanceof String) {
				sb.append('\"').append(value).append('\"');
			} else {
				sb.append(value);
			}
			i++;
		}
		sb.append('\n').append(sepStr).append(']');
		return sb.toString();
	}

}
