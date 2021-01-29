package se.kth.assertteam.jsonbench.parser;

import org.kordamp.json.JSONArray;
import org.kordamp.json.JSONObject;
import org.kordamp.json.JSONSerializer;
import org.kordamp.json.JSONString;
import se.kth.assertteam.jsonbench.JP;

import java.io.StringWriter;
import java.util.List;

public class JsonLib implements JP {

	private char firstNonWhitChar(String str) {
		return str.trim().charAt(0);
	}

	@Override
	public Object parseString(String in) throws Exception {

		char first = firstNonWhitChar(in);
		if (first == '{') {
			return JSONObject.fromObject(in);
		} else if (first == '[') {
			return JSONArray.fromObject(in);
		}
		throw new Exception("ParseException");
	}

	@Override
	public String print(Object in) throws Exception {
		StringWriter w = new StringWriter();
		JSONSerializer.toJSON(in).write(w);
		return w.toString();
	}

	@Override
	public String getName() {
		return "json-lib";
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
		} else if(a instanceof List) {
			List ao, bo;
			ao = (List) a;
			bo = (List) b;
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
}
