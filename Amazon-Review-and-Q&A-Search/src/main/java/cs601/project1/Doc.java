package cs601.project1;


/**
 * Super class of both Review and QA class. 
 * 
 * @author Kei Fukutani
 */
public class Doc {
	
	protected int id;
	protected String asin;
	protected String text;
	
	public Doc() {
		
	}
	public Doc(int id) {
		this.id = id;
	}
	public int getId() {
		return id;
	}
	public String getAsin() {
		return asin;
	}
	public String getText() {
		return text;
	}
	
	
	/**
	 * Displays the content of the fields.
	 */
	public String toString() {
		return "id: " + Integer.toString(id) + 
		       " asin: " + getAsin() + 
		       " text; " + getText();
 	}
}
