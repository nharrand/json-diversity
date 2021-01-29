package se.kth.assertteam.jsonbench.parser;

import net.sf.corn.converter.json.JsTypeComplex;
import net.sf.corn.converter.json.JsTypeList;
import net.sf.corn.converter.json.JsTypeObject;
import net.sf.corn.converter.json.JsonStringParser;
import se.kth.assertteam.jsonbench.JP;

public class Corn implements JP {
	@Override
	public Object parseString(String in) throws Exception {
		return JsonStringParser.parseJsonString(in);
	}

	@Override
	public String print(Object in) throws Exception {
		return ((JsTypeObject) in).toJsonString();
	}

	@Override
	public String getName() {
		return "corn";
	}

	@Override
	public boolean equivalence(Object a, Object b) {
		if(a == null) return b == null;
		if(a.getClass() != b.getClass()) {
			return false;
		} else if(a.equals(b)) {
			return true;
		} else if(a instanceof JsTypeComplex) {
			JsTypeComplex ao, bo;
			ao = (JsTypeComplex) a;
			bo = (JsTypeComplex) b;
			if(ao.size() != bo.size()) {
				return false;
			}
			for(String key: ao.keys()) {
				if(!bo.has(key)) return false;
				//try {
				if (!equivalence(ao.get(key), bo.get(key))) return false;
				//} catch (JSONException e) {
				//	return false;
				//}
			}
			return true;
		} else if(a instanceof JsTypeList) {
			JsTypeList ao, bo;
			ao = (JsTypeList) a;
			bo = (JsTypeList) b;
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
		}
		return false;
	}
}
