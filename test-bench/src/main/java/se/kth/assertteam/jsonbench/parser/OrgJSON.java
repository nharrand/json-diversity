package se.kth.assertteam.jsonbench.parser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import se.kth.assertteam.jsonbench.JP;

import java.util.Iterator;

public class OrgJSON implements JP {

	private char firstNonWhitChar(String str) {
		return str.trim().charAt(0);
	}

	@Override
	public Object parseString(String in) throws Exception {
		char first = firstNonWhitChar(in);
		if (first == '{') {
			return new JSONObject(in);
		} else if (first == '[') {
			return new JSONArray(in);
		}
		return JSONObject.stringToValue(in);
	}

	@Override
	public String print(Object in) throws Exception {
		return in.toString();
	}

	@Override
	public String getName() {
		return "json";
	}

	@Override
	public boolean equivalence(Object a, Object b) {
		if(a == null) return b == null;
		if(a.getClass() != b.getClass()) {
			return false;
		} else if(a.equals(b)) {
			return true;
		} else if(a instanceof JSONObject) {
			JSONObject ao, bo;
			ao = (JSONObject) a;
			bo = (JSONObject) b;
			if(ao.length() != bo.length()) {
				return false;
			}
			Iterator<String> it = ao.keys();
			while (it.hasNext()) {
				String key = it.next();
				if(!bo.has(key)) return false;
				try {
					if (!equivalence(ao.get(key), bo.get(key))) return false;
				} catch (JSONException e) {
					return false;
				}
			}
			return true;
		} else if(a instanceof JSONArray) {
			JSONArray ao, bo;
			ao = (JSONArray) a;
			bo = (JSONArray) b;
			if(ao.length() != bo.length()) {
				return false;
			}
			for(int i = 0; i < ao.length(); i++) {
				try {
					if (!equivalence(ao.get(i), bo.get(i))) return false;
				} catch (JSONException e) {
					return false;
				}
			}
			return true;
		}
		return false;
	}
}
