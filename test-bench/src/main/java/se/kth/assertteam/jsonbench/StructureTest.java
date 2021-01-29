package se.kth.assertteam.jsonbench;

import org.apache.commons.io.FileUtils;
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
import se.kth.assertteam.jsonbench.parser.MJson;
import se.kth.assertteam.jsonbench.parser.OrgJSON;
import se.kth.assertteam.jsonbench.parser.ProgsBaseJson;
import se.kth.assertteam.jsonbench.parser.SOJO;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import static se.kth.assertteam.jsonbench.Bench.findFiles;
import static se.kth.assertteam.jsonbench.Bench.readFile;

public class StructureTest {

	public static void main(String[] args) throws Exception {

		/*System.out.println(
				"{\"short_min\":" + Short.MIN_VALUE + "," +
				"\"short_max\":" + Short.MAX_VALUE + "," +
				"\"integer_min\":" + Integer.MIN_VALUE + "," +
				"\"integer_max\":" + Integer.MAX_VALUE + "," +
						"\"long_min\":" + Long.MIN_VALUE + "," +
						"\"long_max\":" + Long.MAX_VALUE + "," +
						"\"float_min_normal\":" + Float.MIN_NORMAL + "," +
						"\"float_max\":" + Float.MAX_VALUE + "," +
						"\"float_min_value\":" + Float.MIN_VALUE + "," +
						"\"double_min_normal\":" + Double.MIN_NORMAL + "," +
						"\"double_max\":" + Double.MAX_VALUE + "," +
						"\"double_min_value\":" + Double.MIN_VALUE + "," +
						"\"bigdecimal\":" + BigDecimal.valueOf(Double.MIN_VALUE).divide(BigDecimal.valueOf(10.0)) + "," +
						"\"biginteger\":" + BigInteger.valueOf(Long.MAX_VALUE).multiply(BigInteger.valueOf(Long.MAX_VALUE))
						+ "}"
		);*/






		List<JP> parsers = new ArrayList<>();
		/*parsers.add(new Argo());
		parsers.add(new CookJson());
		parsers.add(new Corn());
		parsers.add(new FastJson());
		parsers.add(new FlexJson());
		parsers.add(new GensonP());
		parsers.add(new GsonParser());
		parsers.add(new Jackson());*/
		//parsers.add(new JJson());
		/*parsers.add(new Johnzon());
		parsers.add(new JsonIJ());
		parsers.add(new JsonIO());
		parsers.add(new JsonLib());
		parsers.add(new JsonP());
		parsers.add(new JsonSimple());
		parsers.add(new JsonUtil());
		parsers.add(new MJson());
		parsers.add(new OrgJSON());
		parsers.add(new ProgsBaseJson());*/
		parsers.add(new SOJO());

		/*
		File test = new File("./src/main/resources/test.json");
		String jsonIn = readFile(test);
		File testN = new File("./src/main/resources/numbers.json");
		String jsonInN = readFile(testN);
		File testN2 = new File("./src/main/resources/numbers_2.json");
		String jsonInN2 = readFile(testN2);*/

		File testDir = new File("./src/main/resources/types");
		List<File> jsonTypes = findFiles(testDir.getAbsolutePath(),".json");

		StringBuilder sb = new StringBuilder();
		sb.append("Library");
		for(File f: jsonTypes) {
			sb.append("," + f.getName().replace(".json", ""));
		}
		sb.append("\n");


		for(JP p: parsers) {
			/*try {
				Object o = p.parseString(jsonIn);
				System.out.println(p.getName() + ":");
			} catch (Exception e) {
				System.err.println(p.getName());
			}
			try {
				Object o = p.parseString(jsonInN);
				System.out.println(p.getName() + ":");
			} catch (Exception e) {
				System.err.println(p.getName());
			}
			try {
				Object o = p.parseString(jsonInN2);
				System.out.println(p.getName() + ":");
			} catch (Exception e) {
				System.err.println(p.getName());
			}*/

			sb.append(p.getName());
			for(File f: jsonTypes) {
				String jsonf = readFile(f);
				try {
					Object o = p.parseString(jsonf);

					Method getter = null;
					Method getter2 = null;
					Object res = null;
					Object fval = null;
					try {
						getter = o.getClass().getMethod("get", String.class);
					} catch (NoSuchMethodException me) {
					}
					if(getter == null) {
						try {
							getter = o.getClass().getMethod("get", Object.class);
						} catch (NoSuchMethodException me) {
							Field field = o.getClass().getDeclaredField("object");
							field.setAccessible(true);
							fval = field.get(o);
							try {
								getter2 = fval.getClass().getMethod("get", String.class);
							} catch (NoSuchMethodException mee) {
							}
							if(getter2 == null) {
								getter2 = fval.getClass().getMethod("get", Object.class);
							}
						}
					}

					if(getter != null) {
						res = getter.invoke(o, "val");
					} else if (getter2 != null) {
						res = getter2.invoke(fval, "val");
					}
					if(res != null) {
						sb.append("," + res.getClass());
						System.out.println(p.getName() + " -> " + f.getName() + ": " + res.getClass());
					} else {
						System.err.println(p.getName() + " -> " + f.getName() + ": " + "fail");
					}

				} catch (Exception e) {
					System.err.println(p.getName() + " -> " + f.getName() + ": " + "fail to parse");
				}
			}
			sb.append("\n");
		}

		FileUtils.write(new File("types.csv"), sb.toString(), Charset.defaultCharset());
	}
}
