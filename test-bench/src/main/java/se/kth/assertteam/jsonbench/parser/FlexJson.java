package se.kth.assertteam.jsonbench.parser;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;
import se.kth.assertteam.jsonbench.JP;

import java.util.List;
import java.util.Map;

public class FlexJson implements JP {
	@Override
	public Object parseString(String in) throws Exception {
		JSONDeserializer deserializer = new JSONDeserializer();

		return deserializer.deserialize(in);
	}

	@Override
	public String print(Object in) throws Exception {
		JSONSerializer serializer = new JSONSerializer();
		return serializer.deepSerialize(in);
	}

	@Override
	public String getName() {
		return "flex-json";
	}

	@Override
	public boolean equivalence(Object a, Object b) {
		if(a == null) return b == null;
		if(a.getClass() != b.getClass()) {
			return false;
		} else if(a.equals(b)) {
			return true;
		} else if(a instanceof Map) {
			Map ao, bo;
			ao = (Map) a;
			bo = (Map) b;
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
