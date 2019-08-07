package demo;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;

public class Main {

	public static void main(String[] args) {
		String filepath = "test.c";
//		String filepath = "c.txt";

		InputStreamReader reader = null;
		try {
			System.setOut(new PrintStream(new BufferedOutputStream(new FileOutputStream(new File("out.txt")))));
			reader = new InputStreamReader(new FileInputStream(new File(filepath)));
		} catch (FileNotFoundException e) {
			System.err.println(e.getMessage());
			System.exit(0);
		}
		LALRParser lalrParser = new LALRParser(reader);
		Node root = lalrParser.getRoot();
		if (root != null)
			Node.print(root, null);
		System.out.flush();
	}
}