package main;

import java.io.*;
import java.util.ArrayList;
import java.util.regex.*;

import org.apache.commons.io.IOUtils;

public class regex_test {
	public static void main(String[] args){
		Pattern quoteResultStartPat = Pattern.compile("<li><div class='mw-search-result-heading'>");
				//+ "\\.*</li>");
		Pattern quoteResultEndPat = Pattern.compile("</li>");
				/*Pattern.compile("\\u003Cli\\u003E"
				+ "(\\u003Cdiv class=\\'mw-search-result-heading\\'\\u003E.*)"
				+ "\\u003C\\\\li\\u003E");*/
		try {
			String inputString = IOUtils.toString(new FileReader("src/main/test.html"));
			//System.out.println(inputString);
			ArrayList<String> results = Wikiquote_window.findResults(inputString, quoteResultStartPat, quoteResultEndPat);
			System.out.println(results);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
