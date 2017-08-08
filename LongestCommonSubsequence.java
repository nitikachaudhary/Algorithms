package com.project1;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

/******* References: Professor Shi Li's Dynamic Programming Slides *******/
public class LongestCommonSubsequence {

	public static void main(String[] args) {
		
		String inputFile = "input.txt";
		String outputFile = "output.txt";

		try {
			//Reading Input File
			BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(inputFile)));
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFile)));
			
			//Reading each line from input file
			String string1 = reader.readLine();
        	String string2 = reader.readLine();
        	
        	//initializing 2D array to save the length of common subsequence as we continue iterating the strings
			int[][] opt = new int[string1.length() + 1][string2.length() + 1];
			
			//initializing 2D array to save the common subsequence as we continue iterating the strings
			String[][] pi = new String[string1.length() + 1][string2.length() + 1];
			
			for (int i = 0; i <= string2.length(); i++) {
				opt[0][i] = 0;
			}
			
			//iterating string to set the two 2D arrays
			for (int i = 1; i <= string1.length(); i++) {
				opt[i][0] = 0;
				for (int j = 1; j <= string2.length(); j++) {
					if (string1.charAt(i - 1) == string2.charAt(j - 1)) {
						opt[i][j] = opt[i - 1][j - 1] + 1;
						pi[i][j] = "diagonal";
					} else if (opt[i][j - 1] >= opt[i - 1][j]) {
						opt[i][j] = opt[i][j - 1];
						pi[i][j] = "left";
					} else {
						opt[i][j] = opt[i - 1][j];
						pi[i][j] = "top";
					}
				}
			}

			int i = string1.length();
			int j = string2.length();
			
			//finding the common subsequence
			String commonSequence = "";
			while (i > 0 && j > 0) {
				if (pi[i][j].equals("diagonal")) {
					commonSequence += string1.charAt(i - 1);
					i--;
					j--;
				} else if (pi[i][j].equals("top")) {
					i--;
				} else {
					j--;
				}
			}

			//Writing the output to the file
			writer.write(commonSequence.length() + "\r\n");
			writer.write(commonSequence);
			writer.flush();
			writer.close();
		} catch (FileNotFoundException e) {
			System.out.println(e.getMessage());
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}

}
