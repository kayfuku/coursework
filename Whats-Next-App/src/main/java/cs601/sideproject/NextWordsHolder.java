package cs601.sideproject;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;


/**
 * This holds word, next word following the word, and the number of times the next word
 * follows the word(count). 
 * @author Kei Fukutani
 */
public class NextWordsHolder {
	
	// Key: word, Value: (Key: next word, Value: count) 
	private HashMap<String, HashMap<String, Integer>> wordToCountMap;
	private int sum;
	
	
	public NextWordsHolder() {
		this.wordToCountMap = new HashMap<>();
		this.sum = 0;
	}
	public HashMap<String, HashMap<String, Integer>> getWordToCountMap() {
		return wordToCountMap;
	}
	public int getSum() {
		return sum;
	}
	
	
	/**
	 * Gets the size of wordToCountMap. 
	 * @return size
	 */
	public int getWordToCountMapSize() {
		return wordToCountMap.size();
	}
	
	
	/**
	 * Takes the wordToCountMap built up. 
	 * @param line
	 * @param textAnalyzer
	 */
	public void storeData(HashMap<String, HashMap<String, Integer>> wordToCountMap) {
		this.wordToCountMap = wordToCountMap;
	}
	
	
	/**
	 * Builds the result list using StringBuffer to display it on the JLabel object. 
	 * @param word  the key to be used
	 * @param top  the number of rows of the result to be displayed
	 * @return string
	 */
	public synchronized String buildResult(String word, int top) {
		StringBuffer sb = new StringBuffer();
		sb.append("word: \tnext word: \tcount: \tprobability: \n");
		sb.append(word + "\n");
		
		LinkedList<NextWordToCount> list = getSortedResultList(word);
		if (list.isEmpty()) {
			return "No result found.";
		}
		
		int i = 0;
		for (NextWordToCount nextWordToCount : list) {
			if (i == top) { 
				break; 
			}
			nextWordToCount.setProbability((double) nextWordToCount.getCount() * 100 / sum);
			sb.append("\t" + nextWordToCount.getNextWord() + "\t" + nextWordToCount.getCount());
			sb.append("\t" + String.format("%.1f", nextWordToCount.getProbability()) + " %");
			sb.append("\n");
			i++;
		}
		
		return sb.toString();
	}
	

	/**
	 * Gets a list of next words and counts, sorts the list by the count. 
	 * @param word  the key to be used
	 * @return list  the sorted result list
	 */
	private synchronized LinkedList<NextWordToCount> getSortedResultList(String word) {	
		LinkedList<NextWordToCount> list = new LinkedList<>();
		String nextWord = "";
		int count = 0;
	    sum = 0;

		HashMap<String, Integer> nextWordToCountMap = wordToCountMap.get(word);
		if (nextWordToCountMap == null) {
			return list;
		}
		
		for (Map.Entry<String, Integer> entry : nextWordToCountMap.entrySet()) {
			nextWord = entry.getKey();
			count = entry.getValue();
			sum += count;
			list.add(new NextWordToCount(nextWord, count));		
		}
		
		Collections.sort(list);
		return list;
	}
	
	
	/**
	 * For testing, displays the result of query. 
	 * @param word
	 * @param top
	 */
	public synchronized void displayResult(String word, int top) {
		System.out.println("word: " + "\tnext word: " + "\tcount: " + "\tprobability: ");
		System.out.println(word);
		
		LinkedList<NextWordToCount> list = getSortedResultList(word);
		
		int i = 0;
		for (NextWordToCount nextWordToCount : list) {
			if (i == top) { 
				break; 
			}
			nextWordToCount.setProbability((double) nextWordToCount.getCount() * 100 / sum);
			System.out.print("\t" + nextWordToCount.getNextWord() + "\t\t" + nextWordToCount.getCount());
			System.out.println("\t" + String.format("%.1f", nextWordToCount.getProbability()) + " %");
			i++;
		}
	}
	
	
	/**
	 * For testing, displays the content of the wordToCountMap. 
	 * @param wordLimit  the number of words to be displayed
	 * @param nextWordLimit  the number of next words to be displayed
	 */
	public void displayWordToCountMap(int wordLimit, int nextWordLimit) {
		System.out.println("word: " + "\tnext word: " + "\tcount: ");

		int i = 0;
		for (Map.Entry<String, HashMap<String, Integer>> entryWordToCountMap : wordToCountMap.entrySet()) {
			if (i == wordLimit) { 
				break; 
			}
			
			System.out.println(entryWordToCountMap.getKey());
			
			int j = 0;
			for (Map.Entry<String, Integer> entryNextWordToCount : entryWordToCountMap.getValue().entrySet()) {
				if (j == nextWordLimit) { 
					break; 
				}
				
				System.out.println("\t" + entryNextWordToCount.getKey() + "\t\t" + entryNextWordToCount.getValue());
				
				j++;
			}
			
			i++;
		}
	}
	

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}
