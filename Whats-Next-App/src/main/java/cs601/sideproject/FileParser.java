package cs601.sideproject;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;

import cs601.sideproject.UserInterface.MyPanel;


/**
 * Reads from a input file and stores words in NextWordsHolder. 
 * @author Kei Fukutani
 */
public class FileParser {
	
	private NextWordsHolder nextWordsHolder;
	private volatile boolean finishedReading = false;
	
	
	public FileParser(NextWordsHolder nextWordsHolder) {
		this.nextWordsHolder = nextWordsHolder;
	}
	public boolean getFinishedReading() {
		return finishedReading;
	}
	
	
	/**
     * Reads from a input file and stores words in NextWordsHolder. 
	 * @param fileName  the input file name to be read from
	 * @param nextWordsHolder
	 */
	public void readFile(String fileName) {
		if (fileName.equals("")) {
			return;
		}

		Path inputFilePath = Paths.get(fileName);
		readFileHelper(inputFilePath);
	}
	
	
	/**
	 * Reads from multiple input files and stores words in NextWordsHolder. 
	 * @param files  the files to be read from
	 */
	public void readFile(File[] files) {
		for (File file : files) {
			readFileHelper(file.toPath());
		}
	}
	
	
	/**
	 * Helper method for readFile method. 
	 * Reads from a input file and stores words in NextWordsHolder. 
	 * @param inputFilePath  the path of file to be read from
	 */
	private void readFileHelper(Path inputFilePath) {
		
		Charset charset = StandardCharsets.UTF_8;
		try (BufferedReader reader = Files.newBufferedReader(inputFilePath, charset)) {

			String line;
			TextAnalyzer textAnalyzer = new TextAnalyzer(nextWordsHolder.getWordToCountMap());
			int numObject = 0;

			while ((line = reader.readLine()) != null) {
				
				// Stores data.
				line = line.trim();
				if (!line.trim().equals("")) {
					HashMap<String, HashMap<String, Integer>> wordToCountMap = 
							textAnalyzer.makeNextWordsCountMap(line);
					nextWordsHolder.storeData(wordToCountMap);
				}				
				
				if (numObject % 1000 == 0) {
//					System.out.println(numObject);
				}
				numObject++;
			}
			
			finishedReading = true;
			
		} catch (IOException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
			System.out.println("Bye.");
			System.exit(1);
		}				
	}







	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}
