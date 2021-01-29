package se.kth.assertteam.jsonbench.parser;

import org.apache.johnzon.core.JsonProviderImpl;
import org.apache.johnzon.core.JsonGeneratorFactoryImpl;
import se.kth.assertteam.jsonbench.JP;

import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonValue;
import javax.json.stream.JsonGenerator;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.HashMap;

public class Johnzon implements JP {
	@Override
	public Object parseString(String in) throws Exception {
		JsonProviderImpl provider = new JsonProviderImpl();
		return provider.createParser(new StringReader(in)).getValue();
	}

	@Override
	public String print(Object in) throws Exception {
		JsonGeneratorFactoryImpl gen = new JsonGeneratorFactoryImpl(new HashMap<>());
		StringWriter w = new StringWriter();
		JsonGenerator g = gen.createGenerator(w);
		g.write((JsonValue) in);
		g.flush();
		return w.toString();
	}

	@Override
	public String getName() {
		return "johnzon";
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
			for(Object ok: ao.keySet()) {
				String key = ok.toString();
				if(!bo.containsKey(key)) return false;
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
