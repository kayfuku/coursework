package experiments;



public class NextWordToCount implements Comparable<NextWordToCount> {
	
	private String nextWord;
	private int count;

	
	public NextWordToCount(String nextWord, int count) {
		this.nextWord = nextWord;
		this.count = count;
	}
	
	
	public void incrementCount() {
		this.count++;
	}
	
	
	@Override
	public int compareTo(NextWordToCount o) {
		if (this.count != o.count) {
			return o.count - this.count;
		}
		return o.nextWord.compareTo(this.nextWord);
	}
	
	
	public String toString() {
		return "nextWord: " + nextWord + "\n" +
		       "count: " + count + "\n";
	}
	
	
	
	

}
