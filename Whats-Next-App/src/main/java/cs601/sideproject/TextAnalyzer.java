package cs601.sideproject;

import java.util.HashMap;


/**
 * Preprocesses text data before storing data in NextWordsHolder. 
 * @author Kei Fukutani
 */
public class TextAnalyzer {
	
    private HashMap<String, HashMap<String, Integer>> wordToCountMap;
	private String lastWord = null;
	
	
	public TextAnalyzer(HashMap<String, HashMap<String, Integer>> wordToCountMap) {
		this.wordToCountMap = wordToCountMap;
	}
	
	
	/**
	 * Splits the line, counts the number of the next words, and make the wordToCountMap. 
	 * Also, it handles with the last word of the line. 
	 * @param line  the input text
	 * @return
	 */
	public HashMap<String, HashMap<String, Integer>> makeNextWordsCountMap(String line) {
		
		String[] words = preprocessText(line);
		
		if (lastWord != null) {
			countNextWords(lastWord, words[0]);
		}
		
		String word = "";
		String nextWord = "";
		for (int i = 0; i < words.length - 1; i++) {
			word = words[i];
			nextWord = words[i + 1];
			
			countNextWords(word, nextWord);
		}
		
		lastWord = words[words.length - 1];
		
		return wordToCountMap;
	}
	
	
	/**
	 * For each word, count the number of its next word, and stores it in 
	 * the nextWordToCount map. The map is put in another map as value, where 
	 * each word as key. 
	 * @param word  one word
	 * @param nextWord  the next word following the word
	 */
	private void countNextWords(String word, String nextWord) {
		
		if (!wordToCountMap.containsKey(word)) {
			HashMap<String, Integer> nextWordToCount = new HashMap<>();
			nextWordToCount.put(nextWord, 1);
			wordToCountMap.put(word, nextWordToCount);
		} else {
			HashMap<String, Integer> nextWordToCount = wordToCountMap.get(word);
			if (!nextWordToCount.containsKey(nextWord)) {
				nextWordToCount.put(nextWord, 1);
			} else {
				nextWordToCount.put(nextWord, nextWordToCount.get(nextWord) + 1);
			}
			wordToCountMap.put(word, nextWordToCount);
		}
	}
	

	/**
	 * Splits the given text by whitespace, removes all non-alphanumeric characters and 
	 * converts the string to lower case. 
	 * @param text  the text to be preprocessed
	 * @return preprocessed text
	 */
	private String[] preprocessText(String text) {
		String[] words = text.split("\\s+");
		String word;
		for (int i = 0; i < words.length; i++) {
			// Removes non-alphanumeric characters. 
			word = words[i].replaceAll("[^A-Za-z0-9]", "");
			word = word.toLowerCase();
			words[i] = word;
		}
		return words;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}
