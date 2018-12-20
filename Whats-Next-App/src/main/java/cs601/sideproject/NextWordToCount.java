package cs601.sideproject;


/**
 * When queried, in order to sort the result by the count, 
 * this is used to hold the next word and its count. 
 * @author Kei Fukutani
 */
public class NextWordToCount implements Comparable<NextWordToCount> {
	
	private String nextWord;
	private int count;
	private double probability;

	
	public NextWordToCount(String nextWord, int count) {
		this.nextWord = nextWord;
		this.count = count;
	}
	public String getNextWord() {
		return nextWord;
	}
	public int getCount() {
		return count;
	}
	public double getProbability() {
		return probability;
	}
	public void setProbability(double probability) {
		this.probability = probability;
	}
		
	
	/**
	 * Increments the count. 
	 */
	public void incrementCount() {
		this.count++;
	}
	
	
	/**
	 * Determines which object is bigger, equal to, or smaller. 
	 * First compares by count. If the count is equal, then this compares by next word. 
	 * When this object being sorted, it is going to be in descending order, and 
	 * if the count is equal, then sorted by next word in an ascending order. 
	 * @param NextWordToCount  this class's object to be compared
	 */
	@Override
	public int compareTo(NextWordToCount o) {
		if (this.count != o.count) {
			return o.count - this.count;
		}
		return this.nextWord.compareTo(o.nextWord);
	}
	
	
	/**
	 * Displays the content of the fields.
	 */
	@Override
	public String toString() {
		return "nextWord: " + nextWord + "\n" +
		       "count: " + count + "\n";
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}
