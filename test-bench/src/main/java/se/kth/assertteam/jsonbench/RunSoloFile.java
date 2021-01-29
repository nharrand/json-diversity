package se.kth.assertteam.jsonbench;

import se.kth.assertteam.jsonbench.parser.Argo;
import se.kth.assertteam.jsonbench.parser.CookJson;
import se.kth.assertteam.jsonbench.parser.Corn;
import se.kth.assertteam.jsonbench.parser.FastJson;
import se.kth.assertteam.jsonbench.parser.FlexJson;
import se.kth.assertteam.jsonbench.parser.GensonP;
import se.kth.assertteam.jsonbench.parser.GsonParser;
import se.kth.assertteam.jsonbench.parser.JJson;
import se.kth.assertteam.jsonbench.parser.Jackson;
import se.kth.assertteam.jsonbench.parser.Johnzon;
import se.kth.assertteam.jsonbench.parser.JsonIJ;
import se.kth.assertteam.jsonbench.parser.JsonIO;
import se.kth.assertteam.jsonbench.parser.JsonLib;
import se.kth.assertteam.jsonbench.parser.JsonP;
import se.kth.assertteam.jsonbench.parser.JsonSimple;
import se.kth.assertteam.jsonbench.parser.JsonUtil;
import se.kth.assertteam.jsonbench.parser.KlaxonP;
import se.kth.assertteam.jsonbench.parser.MJson;
import se.kth.assertteam.jsonbench.parser.OrgJSON;
import se.kth.assertteam.jsonbench.parser.ProgsBaseJson;
import se.kth.assertteam.jsonbench.parser.SOJO;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static se.kth.assertteam.jsonbench.Bench.test;
import static se.kth.assertteam.jsonbench.Bench.testCorrectJson;

public class RunSoloFile {

	public static void main(String[] args) throws IOException {
		File correct = new File("../data/bench/correct");
		File errored = new File("../data/bench/errored");
		File undefined = new File("../data/bench/undefined");

		//File toTest = new File(correct, "y_structure_string_empty.json");
		//File toTest = new File(correct, "org-json-pass1.json");
		//File toTest = new File(correct, "y_structure_lonely_string.json");
		//File toTest = new File(correct, "y_string_space.json");
		//File toTest = new File(correct, "pass01.json");
		//File toTest = new File(correct, "y_structure_lonely_false.json");
		//File toTest = new File(undefined, "i_number_huge_exp.json");
		//JP parser = new FastJson();
		//File toTest = new File(correct, "y_number_int_with_exp.json");
		File toTest = new File(correct, "simple-object.json");

		File grr = new File(undefined, "string_2_invalid_codepoints.json");

		JP parser = new OrgJSON();
		ResultKind r = testCorrectJson(toTest, parser);


		System.out.println("Result: " + r.toString());
		//JP parser = new FlexJson();
		/*List<JP> parsers = new ArrayList<>();
		parsers.add(new Argo());
		parsers.add(new CookJson());
		parsers.add(new Corn());
		parsers.add(new FastJson());
		parsers.add(new FlexJson());
		parsers.add(new GensonP());
		parsers.add(new GsonParser());
		parsers.add(new Jackson());
		parsers.add(new JJson());
		parsers.add(new Johnzon());
		parsers.add(new JsonIJ());
		parsers.add(new JsonIO());
		parsers.add(new JsonLib());
		parsers.add(new JsonP());
		parsers.add(new JsonSimple());
		parsers.add(new JsonUtil());
		parsers.add(new MJson());
		parsers.add(new OrgJSON());
		parsers.add(new ProgsBaseJson());
		parsers.add(new SOJO());






		for(JP parser: parsers) {
			ResultKind r = testCorrectJson(toTest, parser);


			System.out.println("Result: " + r.toString());
		}*/
	}
}
