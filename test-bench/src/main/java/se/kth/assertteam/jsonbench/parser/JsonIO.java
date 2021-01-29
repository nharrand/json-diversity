package se.kth.assertteam.jsonbench.parser;

import com.cedarsoftware.util.io.JsonObject;
import com.cedarsoftware.util.io.JsonReader;
import com.cedarsoftware.util.io.JsonWriter;
import se.kth.assertteam.jsonbench.JP;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JsonIO implements JP {

	public static Map customPrintArgs;
	public static Map customReadArgs;

	static {
		customPrintArgs = new HashMap();
		customPrintArgs.put(JsonWriter.TYPE, false);
		customReadArgs = new HashMap();
		customReadArgs.put(JsonReader.USE_MAPS, true);
	}

	@Override
	public Object parseString(String in) throws Exception {
		return JsonReader.jsonToJava(in, customReadArgs);
	}

	@Override
	public String print(Object in) throws Exception {
		return JsonWriter.objectToJson(in, customPrintArgs);
	}

	@Override
	public String getName() {
		return "json-io";
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
			if(ao.keySet().size() != bo.keySet().size()) {
				return false;
			}
			for(Object key: ao.keySet()) {
				if(!bo.containsKey(key)) return false;
				if (!equivalence(ao.get(key), bo.get(key))) return false;
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
		} else if(a.getClass().isArray()) {
			Object[] ao, bo;
			ao = (Object[]) a;
			bo = (Object[]) b;
			if(ao.length != bo.length) {
				return false;
			}
			for(int i = 0; i < ao.length; i++) {
				if (!equivalence(ao[i], bo[i])) return false;
			}
			return true;
		}
		return false;
	}
}
