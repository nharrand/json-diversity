package se.kth.assertteam.jsonbench.parser;

import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonValue;
import jakarta.json.stream.JsonGenerator;
import jakarta.json.stream.JsonParser;
import se.kth.assertteam.jsonbench.JP;


import java.io.StringReader;
import java.io.StringWriter;

import org.glassfish.json.JsonProviderImpl;

public class JsonP implements JP {
	@Override
	public Object parseString(String in) throws Exception {
		JsonProviderImpl provider = new JsonProviderImpl();
		JsonParser parser = provider.createParser(new StringReader(in));
		parser.next();
		return parser.getValue();
	}

	@Override
	public String print(Object in) throws Exception {
		StringWriter w = new StringWriter();
		JsonProviderImpl provider = new JsonProviderImpl();
		JsonGenerator generator = provider.createGenerator(w);
		generator.write((JsonValue) in);
		generator.flush();
		return w.toString();
	}

	@Override
	public String getName() {
		return "jsonp";
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
