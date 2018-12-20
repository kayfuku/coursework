package cs601.project1;


/**
 * Represents each question and answer document. 
 * Every time instantiated, the instance gets an unique id. 
 * 
 * @author Kei Fukutani
 */
public class QA extends Doc {
	
	// To get unique id.
	private static int counter = 0;
	
	private String questionType;
	private String answerTime;
	private long unixTime;
	private String question;
	private String answerType;
	private String answer;
	
	
	/**
	 * Constructor that generates unique id for each instance.
	 */
	public QA() {
		super(counter);
		counter++;
	}
	private String getQuestion() {
		return question;
	}
	private String getAnswer() {
		return answer;
	}
	
	
	/**
	 * Gets text of question and answer. 
	 */
	@Override
	public String getText() {
		return getQuestion() + " " + getAnswer();
	}


	/**
	 * Displays the content of qa object.
	 */
	public String toString() {
		return "qaID: " + getId() + 
			   " questionType: " + questionType +
			   " asin: " + getAsin() + 
			   " answerTime: " + answerTime + 
			   " unixTime: " + Long.toString(unixTime) + 
			   " question: " + question + 
			   " answerType: " + answerType + 
			   " answer: " + answer;
	}
}
