package cs601.sideproject;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;


/**
 * Main class for What's Next App. 
 * @author Kei Fukutani
 */
public class WhatsNextApp {
	
	private static String CONFIGURATION_FILE = "config/whats_next_app_config.properties";
	private static String PATH_FILE_CHOOSER_OPEN;
	private static NextWordsHolder nextWordsHolder;
	private static FileParser fileParser;

	
	public static String getPathFileChooserOpen() {
		return PATH_FILE_CHOOSER_OPEN;
	}
	
	
	/**
	 * Main logic for What's Next App. 
	 * Gets input file names and reads the files. 
	 * @param args
	 */
	public static void main(String[] args) {
		
		nextWordsHolder = new NextWordsHolder();
		fileParser = new FileParser(nextWordsHolder);
		
		String[] files = getConfigInfo();
		
		Thread uiThread = new Thread(new UserInterface(fileParser, nextWordsHolder));
		uiThread.start();
		
	}

	
	/**
	 * Reads files one by one. 
	 * @param files
	 */
	public static void readFiles(String[] files) {
		for (String inputFile : files) {
			fileParser.readFile(inputFile);
		}	
	}
	
	
	/**
	 * Reads a configuration file and gets input file names. 
	 */
	private static String[] getConfigInfo() {
		Properties config = new Properties();
		String[] files = null;
		
		try (FileInputStream input = new FileInputStream(CONFIGURATION_FILE)) {
			
			config.load(input);
			
			PATH_FILE_CHOOSER_OPEN = config.getProperty("pathFileChooserOpen");
			files = config.getProperty("fileList").split(",");
			
		} catch (IOException e) {
			System.out.println(e.getMessage() + ", not found.");
			System.out.println("Isn't that config/whats_next_app_config.properties?");
			System.out.println("Bye.");
			System.exit(1);
		}
		
		return files;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}
