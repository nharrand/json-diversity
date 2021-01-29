package se.kth.assertteam.jsonbench.parser;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.JSONParser;
import se.kth.assertteam.jsonbench.JP;

public class JsonSimple implements JP {
	JSONParser parser = new JSONParser();
	@Override
	public Object parseString(String in) throws Exception {
		return parser.parse(in);
	}

	@Override
	public String print(Object in) throws Exception {
		return JSONValue.toJSONString(in);
	}

	@Override
	public String getName() {
		return "json-simple";
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
			if(ao.size() != bo.size()) {
				return false;
			}
			for(Object ok: ao.keySet()) {
				String key = ok.toString();
				if(!bo.containsKey(key)) return false;
				if(!equivalence(ao.get(key), bo.get(key))) return false;
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
				if (!equivalence(ao.get(i), bo.get(i))) return false;
			}
			return true;
		}
		return false;
	}

	public static void main(String[] args) {
		JP parser = new JsonSimple();
		System.out.println("hello");
	}
}
