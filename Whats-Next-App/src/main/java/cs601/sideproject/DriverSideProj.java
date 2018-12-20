package cs601.sideproject;

public class DriverSideProj {
	
	

	public static void main(String[] args) {
		
		
		String inputFile = "";
		
		NextWordsHolder nextWordsHolder = new NextWordsHolder();
		FileParser fileParser = new FileParser(nextWordsHolder);
		
		fileParser.readFile(inputFile);
		
		

	}

}
