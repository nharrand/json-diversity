package se.kth.assertteam.jsonbench.parser;

import org.yuanheng.cookjson.TextJsonGenerator;
import org.yuanheng.cookjson.TextJsonParser;
import org.yuanheng.cookjson.value.CookJsonArray;
import org.yuanheng.cookjson.value.CookJsonObject;
import se.kth.assertteam.jsonbench.JP;
import javax.json.JsonValue;

import java.io.StringReader;
import java.io.StringWriter;

public class CookJson implements JP {
	@Override
	public Object parseString(String in) throws Exception {
		TextJsonParser p = new TextJsonParser(new StringReader(in));
		p.next();
		return p.getValue();
	}

	@Override
	public String print(Object in) throws Exception {
		StringWriter a = new StringWriter();
		TextJsonGenerator g = new TextJsonGenerator(a);
		g.write((JsonValue) in);
		g.flush();
		return a.toString();
	}

	@Override
	public String getName() {
		return "cookjson";
	}

	@Override
	public boolean equivalence(Object a, Object b) {
		if(a == null) return b == null;
		if(a.getClass() != b.getClass()) {
			return false;
		} else if(a.equals(b)) {
			return true;
		} else if(a instanceof CookJsonObject) {
			CookJsonObject ao, bo;
			ao = (CookJsonObject) a;
			bo = (CookJsonObject) b;
			if(ao.keySet().size() != bo.keySet().size()) {
				return false;
			}
			for(String key: ao.keySet()) {
				if(!bo.containsKey(key)) return false;
				//try {
					if (!equivalence(ao.get(key), bo.get(key))) return false;
				//} catch (JSONException e) {
				//	return false;
				//}
			}
			return true;
		} else if(a instanceof CookJsonArray) {
			CookJsonArray ao, bo;
			ao = (CookJsonArray) a;
			bo = (CookJsonArray) b;
			if(ao.size() != bo.size()) {
				return false;
			}
			for(int i = 0; i < ao.size(); i++) {
				//try {
					if (!equivalence(ao.get(i), bo.get(i))) return false;
				//} catch (JSONException e) {
				//	return false;
				//}
			}
			return true;
		} else if(a instanceof JsonValue) {
			JsonValue ao, bo;
			ao = (JsonValue) a;
			bo = (JsonValue) b;
			if(!ao.getValueType().equals(bo.getValueType())) return false;
			return ao.toString().equals(bo.toString());

		}
		return false;
	}
}
