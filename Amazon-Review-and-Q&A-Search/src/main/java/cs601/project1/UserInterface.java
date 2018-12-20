package cs601.project1;

import java.util.ArrayList;
import java.util.Scanner;


/**
 * Accepts a command from user and displays the results according to the command. 
 * Provides the functions, such as find, reviewsearch, qasearch, reviewpartialsearch, and 
 * qapartialsearch. 
 * 
 * @author Kei Fukutani
 */
public class UserInterface {
	
	private InvertedIndex reviewII;
	private InvertedIndex qaII;
	
	
	/**
	 * Constructor that takes master tables and inverted indices to use for the functions. 
	 * @param reviewII  the inverted index data structure for review document
	 * @param qaII  the inverted index data structure for qa document 
	 */
    public UserInterface(InvertedIndex reviewII, InvertedIndex qaII) {
	    super();
		this.reviewII = reviewII;
		this.qaII = qaII;
	}


	/**
	 * Accepts a command that user inputs and processes according to it. 
	 */
	public void execute() {
		String[] command;
		Scanner in = new Scanner(System.in);

		while (true) {
			System.out.print("Enter a command: ");
			command = in.nextLine().toLowerCase().split(" ");

			if (!command[0].equals("exit") && command.length != 2) {
				System.out.println("Invalid command or arguments.");
				showHelp();
				continue;
			}
			switch (command[0]) {
			case "find":
				displayResultForFind(command[1]);
				break;
			case "reviewsearch":
				displayResultForExactMatch(command[1], reviewII);
				break;
			case "qasearch":
				displayResultForExactMatch(command[1], qaII);
				break;
			case "reviewpartialsearch":
				displayResultForPartialMatch(command[1], reviewII);
				break;
			case "qapartialsearch":
				displayResultForPartialMatch(command[1], qaII);
				break;
			case "exit":
				System.out.println("Bye.");
				in.close();
				System.exit(0);
			default:
				System.out.println("Invalid command.");
				showHelp();
			}
		} 
	}


	/**
	 * Displays all the documents that have a given ASIN (Amazon Standard Identification Number). 
	 * @param asin  the ASIN to be used as a key
	 */
	private void displayResultForFind(String asin) {	
		boolean b1 = displayResultForFindHelper(asin, reviewII);
		boolean b2 = displayResultForFindHelper(asin, qaII);
		
		// No result found both in review documents and question and answer documents. 
		if (!b1 && !b2) {
			System.out.println("No result found, or invalid asin.");
		}
	}
	/**
	 * Helps retrieve the documents using InvertedIndex object. 
	 * @param asin  the ASIN to be used as a key
	 * @param invertedIndex  the inverted index to be used
	 * @return true if result found, false if no result found
	 */
	private boolean displayResultForFindHelper(String asin, InvertedIndex invertedIndex) {
		ArrayList<Doc> docList = invertedIndex.find(asin);
		
		if (docList != null) {
			for (Doc doc : docList) {
				System.out.println(doc);
			}
			return true;
		}
		
		return false;
	}
	
	
	/**
	 * Displays all the documents that have a given term(exact match).  
	 * @param term  the term to be used as a key
	 * @param invertedIndex  the inverted index to be used
	 */
	private void displayResultForExactMatch(String term, InvertedIndex invertedIndex) {
		ArrayList<Doc> docList = invertedIndex.searchForExactMatch(term);
		if (docList != null) {
			for (Doc doc : docList) {
				System.out.println(doc);
			}
		} else {
			System.out.println("No result found.");
		}
	}


	/**
	 * Displays all the documents that contain a given term(partial match). 
	 * @param term  the term to be used for search
	 * @param invertedIndex  the inverted index to be used
	 */
	private void displayResultForPartialMatch(String term, InvertedIndex invertedIndex) {
		ArrayList<Doc> docList = invertedIndex.searchForPartialMatch(term);
		if (docList != null) {
			for (Doc doc : docList) {
				System.out.println(doc);
			}
		} else {
			System.out.println("No result found.");
		}		
	}
	

	/**
	 * Shows help instruction. 
	 */
	private void showHelp() {
		System.out.println("--- USAGE ---");
		System.out.println("find <asin>: Find documents by ASIN.");
		System.out.println("reviewsearch <term>: Find review documents by term (exact match).");
		System.out.println("qasearch <term>: Find question and answer documents by term (exact match).");
		System.out.println("reviewpartialsearch <term>: Find review documents by term (partial match).");
		System.out.println("qapartialsearch <term>: Find question and answer documents by term (partial match).");
		System.out.println("exit: Exit program.");
	}




	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	




}
