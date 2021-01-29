package se.kth.assertteam.jsonbench.parser;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import se.kth.assertteam.jsonbench.JP;

public class GsonParser implements JP {
	JsonParser parser = new JsonParser();
	//Gson gson = new Gson();
	@Override
	public Object parseString(String in) throws Exception {
		//return JsonParser.parseString(in);
		//return gson.fromJson(in, JsonElement.class);
		return parser.parse(in);
	}

	@Override
	public String print(Object in) throws Exception {
		return in.toString();
	}

	@Override
	public String getName() {
		return "gson";
	}

	@Override
	public boolean equivalence(Object a, Object b) {
		if(a == null) return b == null;
		if(a.getClass() != b.getClass()) {
			return false;
		} else if(a.equals(b)) {
			return true;
		} else if(a instanceof JsonObject) {
			JsonObject ao, bo;
			ao = (JsonObject) a;
			bo = (JsonObject) b;
			if(ao.size() != bo.size()) {
				return false;
			}
			for(String key: ao.keySet()) {
				if(!bo.has(key)) return false;
				if(!equivalence(ao.get(key), bo.get(key))) return false;
			}
			return true;
		} else if(a instanceof JsonArray) {
			JsonArray ao, bo;
			ao = (JsonArray) a;
			bo = (JsonArray) b;
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
