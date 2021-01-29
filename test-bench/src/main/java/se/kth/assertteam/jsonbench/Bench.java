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
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

public class Bench {
	static int TIMEOUT = 15;
	static boolean log = true;

	public static void main(String[] args) throws IOException {

		/*JP orgJson = new OrgJSON();
		test(orgJson);

		JP gson = new GsonParser();
		test(gson);

		JP simple = new JsonSimple();
		test(simple);

		JP jackson = new Jackson();
		test(jackson);

		JP cookJson = new CookJson();
		test(cookJson);

		JP jsonIO = new JsonIO();
		test(jsonIO);

		JP jsonLib = new JsonLib();
		test(jsonLib);

		JP jsonutil = new JsonUtil();
		test(jsonutil);

		JP klaxon = new KlaxonP();
		test(klaxon);

		JP mjson = new MJson();
		test(mjson);

		JP flexjson = new FlexJson();
		test(flexjson);

		JP corn = new Corn();
		test(corn);

		JP johnzon = new Johnzon();
		test(johnzon);

		JP genson = new GensonP();
		test(genson);

		JP progbase = new ProgsBaseJson();
		test(progbase);

		JP jsonP = new JsonP();
		test(jsonP);

		JP sojo = new SOJO();
		test(sojo);

		JP argo = new Argo();
		test(argo);

		JP fastjson = new FastJson();
		test(fastjson);*/

		JP jjson = new JJson();
		test(jjson);

		//JP jsonij = new JsonIJ();
		//test(jsonij);

	}

	public static void test(JP parser) throws IOException {
		File correct = new File("../data/bench/correct");
		File errored = new File("../data/bench/errored");
		File undefined = new File("../data/bench/undefined");
		System.out.println("Start testing " + parser.getName());
		printResults(parser, "correct", testAllCorrectJson(correct, parser));
		printResults(parser, "errored", testAllIncorrectJson(errored, parser));
		printResults(parser, "undefined", testAllCorrectJson(undefined, parser));

	}

	public static void printResults(JP parser, String category, Map<String,ResultKind> results) throws IOException {
		File dir = new File("results");
		File output = new File(dir,parser.getName() + "_" + category + "_results.csv");
		System.out.println("Print result from " + parser.getName() + " to " + output.getName());
		if(output.exists()) {
			output.delete();
		}
		output.createNewFile();
		try {
			Files.write(output.toPath(), "Parser,Category,File,Result\n".getBytes(), StandardOpenOption.APPEND);
		} catch (IOException e) {
			e.printStackTrace();
		}
		results.forEach((k,v) -> {
			String line = parser.getName() + "," + category + "," + k + "," + v + "\n";
			try {
				Files.write(output.toPath(), line.getBytes(), StandardOpenOption.APPEND);
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
	}

	public static List<File> findFiles(String dir, String suffix) throws IOException {
		List<File> files = new ArrayList<>();

		Files.walk(Paths.get(dir))
				.filter(Files::isRegularFile)
				.forEach((f)->{
					String file = f.toString();
					if( file.endsWith(suffix))
						files.add(new File(file));
				});

		return files;
	}

	public static String readFile(File f) throws IOException {
		try {
			return Files.lines(f.toPath(), Charset.forName("UTF-8")).collect(Collectors.joining("\n"));
		} catch (Exception e) {
			System.err.println("Failed with UTF-8");
		}
		return Files.lines(f.toPath(), Charset.forName("UTF-16")).collect(Collectors.joining("\n"));
	}

	public static Map<String,ResultKind> testAllCorrectJson(File inDir, JP parser) throws IOException {
		int i = 0;
		ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
		Map<String,ResultKind> results = new HashMap<>();
		for(File f: findFiles(inDir.getAbsolutePath(),".json")) {
			if(log) System.out.println("[" + parser.getName() + "] " + (i++) + " " + f.getName());

			Callable<ResultKind> task = () -> testCorrectJson(f, parser);
			Future<ResultKind> future = executor.submit(task);
			ResultKind r = ResultKind.CRASH;
			try {
				r = future.get(TIMEOUT, TimeUnit.SECONDS);
			} catch (InterruptedException | ExecutionException | TimeoutException e) {
				System.err.println("Timout for " + f.getName());
			}
			results.put(f.getName(), r);
		}
		executor.shutdown();
		return results;
	}

	public static Map<String,ResultKind> testAllIncorrectJson(File inDir, JP parser) throws IOException {
		int i = 0;
		ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
		Map<String,ResultKind> results = new HashMap<>();
		for(File f: findFiles(inDir.getAbsolutePath(),".json")) {
			if(log) System.out.println("[" + parser.getName() + "] " + (i++) + " " + f.getName());

			Callable<ResultKind> task = () -> testIncorrectJson(f, parser);
			Future<ResultKind> future = executor.submit(task);
			ResultKind r = ResultKind.CRASH;
			try {
				r = future.get(TIMEOUT, TimeUnit.SECONDS);
			} catch (InterruptedException | ExecutionException | TimeoutException e) {
				System.err.println("Timout for " + f.getName());
			}
			results.put(f.getName(), r);
		}
		executor.shutdown();
		return results;
	}

	public static ResultKind testCorrectJson(File f, JP parser)  {
		String jsonIn = null;
		try {
			jsonIn = readFile(f);
		} catch (Exception e) {
			return ResultKind.FILE_ERROR;
		}
		Object jsonObject = null;
		String jsonOut;
		try {
			try {
				jsonObject = parser.parseString(jsonIn);
				if(jsonObject == null && !jsonIn.equals("null"))
					return ResultKind.NULL_OBJECT;
			} catch (Exception e) {
				return ResultKind.PARSE_EXCEPTION;
			}
			try {
				jsonOut = parser.print(jsonObject);
				if(jsonOut.equalsIgnoreCase(jsonIn)) {
					return ResultKind.OK;
				}
				if(parser.equivalence(jsonObject,parser.parseString(jsonOut))) {
					return ResultKind.EQUIVALENT_OBJECT;
				} else {
					return ResultKind.NON_EQUIVALENT_OBJECT;
				}
			} catch (Exception e) {
				return ResultKind.PRINT_EXCEPTION;
			}
		} catch (Error e) {
			return ResultKind.CRASH;
		}
	}

	public static ResultKind testIncorrectJson(File f, JP parser)  {
		String jsonIn;
		try {
			jsonIn = readFile(f);
		} catch (Exception e) {
			return ResultKind.FILE_ERROR;
		}
		try {
			try {
				try {
					Object jsonObject = parser.parseString(jsonIn);
					if (jsonObject != null)
						return ResultKind.UNEXPECTED_OBJECT;
					else
						return ResultKind.NULL_OBJECT;
				} catch (Exception e) {
					return ResultKind.PARSE_EXCEPTION;
				}
			} catch (Error e) {
				return ResultKind.CRASH;
			}
		} catch (Exception e) {
			return ResultKind.CRASH;
		}
	}
}
