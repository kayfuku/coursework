package cs601.project1;

import java.util.HashMap;


/**
 * Deals with text processing. 
 * This implements methods that tokenize text data, preprocess the text, and 
 * count the occurrence of words that appear in the text. 
 * 
 * @author Kei Fukutani
 */
public class TextAnalyzer {
	
	
	/**
	 * For each document, preprocesses text data and counts the occurrence 
	 * of words that appear in the text. This creates a map that maps words in a document to 
	 * DocId object which has 'id' field and 'count' field.  
	 * @param doc  the document to be analyzed
	 * @return the map that maps words to DocId object 
	 */
	public HashMap<String, DocId> countWordsForExactMatch(Doc doc) {
		int id = doc.getId();
		String text = doc.getText();
		HashMap<String, DocId> wordToCount = new HashMap<>();

		String[] words = preprocessText(text);

		for (String word : words) {
			if (!wordToCount.containsKey(word)) {
				DocId docId = new DocId(id, 1);
				wordToCount.put(word, docId);
			} else {
				DocId docId = wordToCount.get(word);
				docId.setCount(docId.getCount() + 1);
			}
		}
		
		return wordToCount;
	}
	
	
	/**
	 * Splits the given text by whitespace, removes all non-alphanumeric characters and 
	 * converts the string to lower case. 
	 * @param text  the text to be preprocessed
	 * @return preprocessed text
	 */
	private String[] preprocessText(String text) {
		String[] words = text.split(" ");
		String word;
		for (int i = 0; i < words.length; i++) {
			// Removes non-alphanumeric characters. (https://code.i-harness.com/ja/q/1b8cce)
			word = words[i].replaceAll("[^A-Za-z0-9]", "");
			word = word.toLowerCase();
			words[i] = word;
		}
		return words;
	}
	
}
