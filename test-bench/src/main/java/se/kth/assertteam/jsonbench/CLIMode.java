package se.kth.assertteam.jsonbench;

import se.kth.assertteam.jsonbench.parser.JJson;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static se.kth.assertteam.jsonbench.Bench.testCorrectJson;
import static se.kth.assertteam.jsonbench.Bench.testIncorrectJson;

public class CLIMode {

	public static void main(String[] args) {
		if(args.length < 1) {
			System.err.println("usage: java -cp jar se.kth.assertteam.jsonbench.CLIMode file");
		}
		String file = args[0];

		JP parser = new JJson();
		//String category = "errored";
		String category = "undefined";

		File input = new File("../data/bench/" + category + "/" + file);
		File dir = new File("results");
		File output = new File(dir,parser.getName() + "_" + category + "_results.csv");

		ResultKind r = testCorrectJson(input, parser);
		//ResultKind r = testIncorrectJson(input, parser);

		try {
			if(!output.exists()) {
				output.createNewFile();
				Files.write(output.toPath(), "Parser,Category,File,Result\n".getBytes(), StandardOpenOption.APPEND);
			}
			Files.write(output.toPath(), (parser.getName() + "," + category + "," + file +  ","  + r + "\n").getBytes(), StandardOpenOption.APPEND);

		} catch (IOException e) {
			e.printStackTrace();
		}


	}
}
