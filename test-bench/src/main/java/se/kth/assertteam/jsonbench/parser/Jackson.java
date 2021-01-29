package se.kth.assertteam.jsonbench.parser;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import se.kth.assertteam.jsonbench.JP;

import java.util.Iterator;

public class Jackson implements JP {
	ObjectMapper mapper = new ObjectMapper();
	@Override
	public Object parseString(String in) throws Exception {
		return mapper.readTree(in);
	}

	@Override
	public String print(Object in) throws Exception {
		return mapper.writeValueAsString(in);
	}

	@Override
	public String getName() {
		return "jackson-databind";
	}

	@Override
	public boolean equivalence(Object a, Object b) {
		if(a == null) return b == null;
		if(a.getClass() != b.getClass()) {
			return false;
		} else if(a.equals(b)) {
			return true;
		} else if(a instanceof ObjectNode) {
			ObjectNode ao, bo;
			ao = (ObjectNode) a;
			bo = (ObjectNode) b;

			Iterator<String> it = ao.fieldNames();
			/*if(ao. != bo.keySet().size()) {
				return false;
			}*/
			while(it.hasNext()) {
				String key = it.next();
				if(bo.get(key) == null) return false;
				try {
					if (!equivalence(ao.get(key), bo.get(key))) return false;
				} catch (Exception e) {
					return false;
				}
			}
			Iterator<String> itb = ao.fieldNames();
			while(itb.hasNext()) {
				String key = itb.next();
				if(ao.get(key) == null) return false;
			}
			return true;
		} else if(a instanceof ArrayNode) {
			ArrayNode ao, bo;
			ao = (ArrayNode) a;
			bo = (ArrayNode) b;
			if(ao.size() != bo.size()) {
				return false;
			}
			for(int i = 0; i < ao.size(); i++) {
				try {
					if (!equivalence(ao.get(i), bo.get(i))) return false;
				} catch (Exception e) {
					return false;
				}
			}
			return true;
		}
		return false;
	}
}
