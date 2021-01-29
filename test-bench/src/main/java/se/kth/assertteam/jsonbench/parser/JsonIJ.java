package se.kth.assertteam.jsonbench.parser;

import cc.plural.jsonij.JSON;
import se.kth.assertteam.jsonbench.JP;

public class JsonIJ implements JP {
	@Override
	public Object parseString(String in) throws Exception {
		return JSON.parse(in);
	}

	@Override
	public String print(Object in) throws Exception {
		return ((JSON) in).toJSON();
	}

	@Override
	public String getName() {
		return "jsonij";
	}

	@Override
	public boolean equivalence(Object a, Object b) {
		if(a == null) return b == null;
		if(a.getClass() != b.getClass()) {
			return false;
		} else if(a.equals(b)) {
			return true;
		} else if(a instanceof JSON.Object) {
			JSON.Object ao, bo;
			ao = (JSON.Object) a;
			bo = (JSON.Object) b;
			if(ao.size() != bo.size()) {
				return false;
			}
			for(Object key: ao.keySet()) {
				if(!bo.containsKey(key)) return false;
				//try {
				if (!equivalence(ao.get(key), bo.get(key))) return false;
				//} catch (JSONException e) {
				//	return false;
				//}
			}
			return true;
		} else if(a instanceof JSON.Array) {
			JSON.Array ao, bo;
			ao = (JSON.Array) a;
			bo = (JSON.Array) b;
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
