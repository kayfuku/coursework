package cs601.project1;


/**
 * Holds id of document. Also, this holds frequency of word in a document in count field. 
 * This is used in InvertedIndex object to indicate which documents a given word appears in,
 * and how many times the word appears. 
 * 
 * @author Kei Fukutani
 */
public class DocId implements Comparable<DocId> {
	
	private int id;
	private int count;
	
	
	public DocId(int id, int count) {
		super();
		this.id = id;
		this.count = count;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	
	
	/**
	 * Determines which object is bigger, equal to, or smaller. 
	 * First compares by count. If the count is equal, then this compares by id. 
	 * When this docId object being put in the TreeSet, it is going to be sorted by 
	 * count in descending order, and if the count is equal, then sorted by id in
	 * ascending order. 
	 * @param docId  this class's object to be compared
	 */
	@Override
	public int compareTo(DocId docId) {
		if (this.count != docId.count) {
			// To get sorted in descending order. 
			return docId.count - this.count;
		}
		return this.id - docId.id;
	}
	
	
	/**
	 * Displays the content of the fields.
	 */
	@Override
	public String toString() {
		return "id: " + this.id + " count: " + this.count;
	}
}
