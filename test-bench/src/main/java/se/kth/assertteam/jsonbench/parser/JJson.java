package se.kth.assertteam.jsonbench.parser;

import de.grobmeier.jjson.JSONArray;
import de.grobmeier.jjson.JSONObject;
import de.grobmeier.jjson.JSONValue;
import de.grobmeier.jjson.convert.JSONDecoder;
import se.kth.assertteam.jsonbench.JP;

public class JJson implements JP {
	@Override
	public Object parseString(String in) throws Exception {
		JSONDecoder d = new JSONDecoder(in);
		JSONValue v = d.decode();
		return v;
	}

	@Override
	public String print(Object in) throws Exception {
		return ((JSONValue) in).toJSON();
	}

	@Override
	public String getName() {
		return "jjson";
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
			if(ao.getValue().keySet().size() != bo.getValue().keySet().size()) {
				return false;
			}
			for(String key: ao.getValue().keySet()) {
				if(!bo.getValue().containsKey(key)) return false;
				//try {
				if (!equivalence(ao.getValue().get(key), bo.getValue().get(key))) return false;
				//} catch (JSONException e) {
				//	return false;
				//}
			}
			return true;
		} else if(a instanceof JSONArray) {
			JSONArray ao, bo;
			ao = (JSONArray) a;
			bo = (JSONArray) b;
			if(ao.getValue().size() != bo.getValue().size()) {
				return false;
			}
			for(int i = 0; i < ao.getValue().size(); i++) {
				//try {
				if (!equivalence(ao.getValue().get(i), bo.getValue().get(i))) return false;
				//} catch (JSONException e) {
				//	return false;
				//}
			}
			return true;
		} else if(a instanceof JSONValue) {
			JSONValue ao, bo;
			ao = (JSONValue) a;
			bo = (JSONValue) b;
			return ao.toJSON().equals(bo.toJSON());

		}
		return false;
	}
}
