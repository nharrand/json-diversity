package se.kth.assertteam.jsonbench.parser;

import argo.format.CompactJsonFormatter;
import argo.jdom.JdomParser;
import argo.jdom.JsonNode;
import argo.jdom.JsonStringNode;
import se.kth.assertteam.jsonbench.JP;

import java.util.List;
import java.util.Map;

public class Argo implements JP {
	@Override
	public Object parseString(String in) throws Exception {
		JdomParser parser = new JdomParser();
		return parser.parse(in);
	}

	@Override
	public String print(Object in) throws Exception {
		CompactJsonFormatter printer = new CompactJsonFormatter();
		return printer.format((JsonNode) in);
	}

	@Override
	public String getName() {
		return "json-argo";
	}

	@Override
	public boolean equivalence(Object a, Object b) {
		if(a == null) return b == null;
		if(a.getClass() != b.getClass()) {
			return false;
		} else if(a.equals(b)) {
			return true;
		} else if(a instanceof JsonNode) {
			JsonNode ao, bo;
			ao = (JsonNode) a;
			bo = (JsonNode) b;
			if(ao.hasFields()) {
				Map<JsonStringNode, JsonNode> fieldsA = ao.getFields();
				if(!bo.hasFields()) return false;
				Map<JsonStringNode, JsonNode> fieldsB = bo.getFields();
				if(fieldsA.size() != fieldsB.size()) {
					return false;
				}
				for(JsonStringNode key: fieldsA.keySet()) {
					if(!fieldsB.containsKey(key)) return false;
					if(!equivalence(fieldsA.get(key), fieldsB.get(key))) return false;
				}
				return true;
			} else if(ao.hasElements()) {
				List<JsonNode> elementsA = ao.getElements();
				if(!bo.hasElements()) return false;
				List<JsonNode> elementsB = bo.getElements();
				if(elementsA.size() != elementsB.size()) {
					return false;
				}
				for(int i = 0; i < elementsA.size(); i++) {
					if (!equivalence(elementsA.get(i), elementsB.get(i))) return false;
				}
				return true;

			} else {
				return ao.toString().equals(bo.toString());
			}
		}
		return false;
	}
}
