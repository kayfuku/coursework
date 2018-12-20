package cs601.project1;

/**
 * AmazonSearch is a driver class. 
 * Reads files specified in command line, takes user command, retrieves the data, and 
 * displays the result.
 * 
 * @author Kei Fukutani
 */
public class AmazonSearch {
	
	private static String reviewFileName = "";
	private static String qaFileName = "";
	
	
	/**
	 * Main logic to take file names, build the data structure, and store data. 
	 * @param args  the arguments to be input 
	 */
	public static void main(String[] args) {
		
		getFileName(args);

		// For testing with much smaller data.
		reviewFileName = "testForReview.json";
		qaFileName = "testForQA.json";

		AmazonSearchFileParser amazonSearchFileParser = new AmazonSearchFileParser();
		
		// Creates two InvertedIndex instances. (Assignment statement)
		InvertedIndex reviewII = new InvertedIndex();
		InvertedIndex qaII = new InvertedIndex();

		// Stores data in the inverted indices.
//		Doc review = new Review();
//		Doc qa = new QA();
//		amazonSearchFileParser.readFile(reviewFileName, reviewII, review);
//		amazonSearchFileParser.readFile(qaFileName, qaII, qa);
		int flag = 1;
		amazonSearchFileParser.readFile(reviewFileName, reviewII, flag);
		flag = 2;
		amazonSearchFileParser.readFile(qaFileName, qaII, flag);
		
		UserInterface userInterface = new UserInterface(reviewII, qaII);
		userInterface.execute();
		
		
		System.out.println("done.");
	}

	
	/**
	 * Gets file name from command line arguments. 
	 * @param args  the arguments to be input
	 */
	private static void getFileName(String[] args) {
		if (args.length == 4 && args[0].equals("-reviews") && args[2].equals("-qa")) {
			reviewFileName = args[1];
			qaFileName = args[3];
		} else {
			System.out.println("Invalid input.");
			System.out.println(
					"Usage: java cs601.project1.AmazonSearch" + " -reviews <review_file_name> -qa <qa_file_name>");
			System.out.println("Example: java cs601.project1.AmazonSeacrch"
					+ " -reviews reviews_Cell_Phones_and_Accessories_5.json -qa qa_Cell_Phones_and_Accessories.json");
			System.exit(1);
		}
	}

	


	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}
