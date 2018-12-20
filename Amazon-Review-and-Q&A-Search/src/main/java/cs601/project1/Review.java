package cs601.project1;

import java.util.Arrays;


/**
 * Represents each review document. 
 * Every time instantiated, the instance gets an unique id. 
 * 
 * @author Kei Fukutani
 */
public class Review extends Doc {
	
	// To get unique id.
	private static int counter = 0;
	
	private String reviewerID;
	private String reviewerName;
	private int[] helpful;
	private String reviewText;
	private double overall;
	private String summary;
	private long unixReviewTime;
	private String reviewTime;
	
	
	/**
	 * Constructor that generates unique id for each instance. 
	 */
	public Review() {		
		super(counter);
		counter++;
	}
		
	
	/**
	 * Gets text of reviewText. 
	 */
	@Override
	public String getText() {
		return reviewText;
	}


	/**
	 * Displays the content of review object.
	 */
	@Override
	public String toString() {
		return "reviewID: " + getId() + 
			   " reviewerID: " + reviewerID +
			   " asin: " + getAsin() + 
			   " reviewerName: " + reviewerName + 
			   " helpful: " + Arrays.toString(helpful) + 
			   " reviewText: " + reviewText + 
			   " overall: " + Double.toString(overall) + 
			   " summary: " + summary + 
			   " unixReviewTime: " + Long.toString(unixReviewTime) + 
			   " reviewTime: " + reviewTime;
	}
	
}
