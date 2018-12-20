package cs601.project1;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;


/**
 * Reads from a specified file, parses lines in the file, and loads the data 
 * in the data structure. 
 * 
 * @author Kei Fukutani
 */
public class AmazonSearchFileParser {
	
	
	/**
	 * Stores data from a file in a master table and inverted index.
	 * @param fileName  the file name to be used
	 * @param invertedIndex  the inverted index to be populated
	 * @param doc  the document to be stored
	 */
//	public void readFile(String fileName, InvertedIndex invertedIndex, Doc doc) {
	public void readFile(String fileName, InvertedIndex invertedIndex, int flag) {

	    Charset charset = StandardCharsets.ISO_8859_1;
		Path inputFile = Paths.get(fileName);
		Gson gson = new Gson();

		try (BufferedReader reader = Files.newBufferedReader(inputFile, charset)) {

			String line;
			TextAnalyzer textAnalyzer = new TextAnalyzer();
			int numObject = 0;
			while ((line = reader.readLine()) != null) {
				try {
					Doc doc = new Doc();
					if (flag == 1) {
					    doc = (Review) gson.fromJson(line, Review.class);
				    } else if (flag == 2) {
					    doc = (QA) gson.fromJson(line, QA.class);
				    }
	
					
//					if (doc instanceof Review) {
//						doc = gson.fromJson(line, Review.class);
//					} else {
//						doc = gson.fromJson(line, QA.class);
//					}
					
				    // Stores data in the master table and the inverted index. 
					invertedIndex.storeData(doc, textAnalyzer);
										

				} catch (JsonSyntaxException jse) {
					System.out.println("skipping... " + line);
				}

				if (numObject % 10000 == 0) {
					System.out.println(numObject);
				}
				numObject++;
			}
			
		} catch (IOException e) {
			System.out.println(e.getMessage() + ", not found.");
			System.out.println("Bye.");
			System.exit(1);
		}				
	}
	
	
	

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}
