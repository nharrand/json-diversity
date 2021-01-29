package se.kth.assertteam.jsonbench.parser;

import com.progsbase.libraries.JSON.JSONObjectReader;
import com.progsbase.libraries.JSON.JSONObjectWriter;
import se.kth.assertteam.jsonbench.JP;

import java.util.List;
import java.util.Map;

public class ProgsBaseJson implements JP {
	@Override
	public Object parseString(String in) throws Exception {
		return JSONObjectReader.readJSON(in);
	}

	@Override
	public String print(Object in) throws Exception {
		return JSONObjectWriter.writeJSON(in);
	}

	@Override
	public String getName() {
		return "progbase-json";
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
			if(ao.size() != bo.size()) {
				return false;
			}
			for(Object ok: ao.keySet()) {
				String key = ok.toString();
				if(!bo.containsKey(key)) return false;
				if(!equivalence(ao.get(key), bo.get(key))) return false;
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
		}
		return false;
	}
}
