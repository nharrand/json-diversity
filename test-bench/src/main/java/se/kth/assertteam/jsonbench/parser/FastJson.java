package se.kth.assertteam.jsonbench.parser;

import com.alibaba.fastjson.parser.DefaultJSONParser;
import se.kth.assertteam.jsonbench.JP;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONException;

import com.alibaba.fastjson.JSONArray;

public class FastJson implements JP {
	@Override
	public Object parseString(String in) throws Exception {
		DefaultJSONParser p = new DefaultJSONParser(in);
		return p.parse();
	}

	@Override
	public String print(Object in) throws Exception {
		return in.toString();
	}

	@Override
	public String getName() {
		return "fastjson";
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
			if(ao.keySet().size() != bo.keySet().size()) {
				return false;
			}
			for(String key: ao.keySet()) {
				if(!bo.containsKey(key))
					return false;
				try {
					if (!equivalence(ao.get(key), bo.get(key)))
						return false;
				} catch (JSONException e) {
					return false;
				}
			}
			return true;
		} else if(a instanceof JSONArray) {
			JSONArray ao, bo;
			ao = (JSONArray) a;
			bo = (JSONArray) b;
			if(ao.size() != bo.size()) {
				return false;
			}
			for(int i = 0; i < ao.size(); i++) {
				try {
					if (!equivalence(ao.get(i), bo.get(i)))
						return false;
				} catch (JSONException e) {
					return false;
				}
			}
			return true;
		}
		return false;
	}
}
