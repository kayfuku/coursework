package cs601.project1;


/**
 * To run some tests for this project.
 * 
 * @author Kei Fukutani
 */
public class DriverProj1 {

	public static void main(String[] args) {
//		AmazonSearch.getFileName(args);
				
		// Inverted index. 
//		InvertedIndex reviewII = new InvertedIndex();
//		InvertedIndex qaII = new InvertedIndex();
//
//		AmazonSearch.getReviewData(reviewFileName, reviewII);
//		AmazonSearch.getQAData(qaFileName, qaII);
		
		
		
		
		
		
		// Test string indexOf method. 
//		String text = "dog1 do dog aaa.";
//		String text = "aaaaaa";
//		String term = "aaaa";
//		String term = "aaaaaa";
//		String term = "do";
//		int count = 0;
//		count = countPartial(text, term);
//		System.out.println(count);
//		String text5= text.substring(5);
//		System.out.println(text5);  // a
//		String text6 = text.substring(6);
//		System.out.println(text6.equals(""));  // true
//		String text7 = text.substring(7);
//		System.out.println(text7);  // Exception

		// Test string contains method. 
//		String string = "abc";
//		System.out.println(string.contains("a"));  // true
//		System.out.println(string.contains("a c"));  // false
//		System.out.println(string.contains("abc"));  // true
		
		// Test string preprocessing. 
//		String sentence = "The cat and the dog  ran. ab(cd) ab-77 but, g**1";
//		String[] words = sentence.split(" ");
//		String word;
//		for (int i = 0; i < words.length; i++) {
//			word = words[i].trim().toLowerCase();
//			// Replaces non-alphanumeric characters. (https://code.i-harness.com/ja/q/1b8cce)
//			word = word.replaceAll("[^A-Za-z0-9]", "");
//			words[i] = word;
//		}
//		System.out.println(Arrays.toString(words));
		
		// Test find method. 
//		AmazonSearch.find("1466736038", reviewII, qaII);
//		AmazonSearch.find("B00LORXVUE", reviewII, qaII);
		
		// Test interted index for find cmd. 
//		reviewII.displayAllAsinToEntryIds();
//		qaII.displayAllAsinToEntryIds();
		
		// Test reviewMaster. 
//		reviewMaster.displayALl();
//		ArrayList<Integer> list = new ArrayList<>();
//		list.addAll(reviewMaster.keySet());
//		Collections.sort(list);
//		for (Integer e : list) {
//			System.out.println(e);
//		}
		
		// Test reviewMaster and qaMaster. 
//		System.out.println(AmazonSearch.reviewMaster.get(190000));
//		System.out.println(AmazonSearch.qaMaster.get(80000));
//		System.out.println(AmazonSearch.reviewMaster.size());
//		System.out.println(AmazonSearch.qaMaster.size());
		
		

//		String command;
//		Scanner in = new Scanner(System.in); 
//		do {
//			System.out.print("Enter a command or 'exit': ");
//
//			command = in.nextLine();
//			switch (command) {
//			case "find":
//				System.out.println("Finding... ");
//				break;
//			case "reviewsearch":
//				System.out.println("Review searching... ");
//				break;
//			case "qasearch":
//				System.out.println("QA searching... ");
//				break;
//			case "reviewpartialsearch":
//				System.out.println("Partial review searching... ");
//				break;
//			case "qapartialsearch":
//				System.out.println("Partial qa searching... ");
//				break;
//			default:
//				break;
//			}
//
//		} while (!command.equals("exit"));
//
//		System.out.println("Bye.");
//		in.close();
//		System.exit(1);


		System.out.println();
		System.out.println("done.");
	}

	
	
	
//	private static int countPartial(String text, String term) {
//		int count = 0;
//		int index = 0;
//		while (text.length() > 0) {
//			index = text.indexOf(term);
//			if (index == -1) {
//				return count;
//			} else {
//				count++;
//				text = text.substring(index + 1);
//			}			
//			System.out.println("count: " + count + " index: " + index);
//		}
//		return count;
//	}

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
