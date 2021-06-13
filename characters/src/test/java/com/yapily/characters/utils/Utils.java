package com.yapily.characters.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class Utils {

	public static String readFile(String file) {
		String strCurrentLine;
		StringBuilder builder = new StringBuilder();
		File fl = new File("");
		try (BufferedReader objReader = new BufferedReader(new FileReader(fl.getAbsolutePath() + "/src/test/resources/testfiles/" + file))) {
			while ((strCurrentLine = objReader.readLine()) != null) {
				builder.append(strCurrentLine);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return builder.toString();
	}
}
