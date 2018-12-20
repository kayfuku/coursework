package cs601.sideproject;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
 * Provides user interface. This runs in a separate thread from 
 * the main thread. Also, holds inner class MyPanel. 
 * @author Kei Fukutani
 */
public class UserInterface implements Runnable {

	private FileParser fileParser;
	private NextWordsHolder nextWordsHolder;
	private MyPanel myPanel;
	private static Logger logger = LogManager.getLogger();


	public UserInterface(FileParser fileParser, NextWordsHolder nextWordsHolder) {
		this.fileParser = fileParser;
		this.nextWordsHolder = nextWordsHolder;
	}


	/**
	 * Creates MyPanel object that has text field, button, and labels. 
	 */
	@Override
	public void run() {

		JFrame jFrame = new JFrame("What's Next App");
		jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jFrame.setBounds(200, 100, 700, 700);

		myPanel = new MyPanel();
		myPanel.setLayout(null);
		myPanel.setPreferredSize(new Dimension(700, 700));

		jFrame.getContentPane().add(myPanel);
		jFrame.pack();
		jFrame.setVisible(true);		
	}


	/**
	 * This panel provides user interface, such as text field, button, label to 
	 * display results. 
	 */
	public class MyPanel extends JPanel implements ActionListener {

		private static final long serialVersionUID = 1L;
		private JLabel lblReading, lblEnterAWord;
		private JButton btnSubmit, btnSelectBooks;
		private JTextField textField;
		private TextArea txtAreaResult;
		private File[] files;
		private final char[] SPINNER = {'|', '/', '-', '\\'};
		private int indexOfSpinner = 0;


		public MyPanel() {
			initialize();
		}


		/**
		 * Initialize the panel components. 
		 */
		private void initialize() {
			btnSelectBooks = new JButton("Select Books");
			lblReading = new JLabel("");
			lblEnterAWord = new JLabel("Enter a word: ");
			textField = new JTextField("", 20);
			btnSubmit = new JButton("Submit");
			txtAreaResult = new TextArea(20, 5);

			btnSelectBooks.setBounds(20, 20, 200, 30);
			btnSelectBooks.setFont(new Font(null, Font.PLAIN, 16));
			btnSelectBooks.addActionListener(this);
			lblReading.setBounds(20, 60, 400, 20);
			lblReading.setFont(new Font(null, Font.PLAIN, 16));
			lblEnterAWord.setBounds(20, 100, 400, 20);
			lblEnterAWord.setFont(new Font(null, Font.PLAIN, 20));
			textField.setBounds(20, 140, 200, 30);
			textField.setFont(new Font(null, Font.PLAIN, 16));
			btnSubmit.setBounds(240, 140, 100, 30);
			btnSubmit.setFont(new Font(null, Font.PLAIN, 16));
			btnSubmit.addActionListener(this);
			txtAreaResult.setBounds(20, 190, 600, 400);
			txtAreaResult.setFont(new Font(null, Font.PLAIN, 16));

			add(btnSelectBooks);
			add(lblReading);
			add(lblEnterAWord);
			add(textField);
			add(btnSubmit);
			add(txtAreaResult);

			setPreferredSize(new Dimension(800, 600));
			setBackground(Color.WHITE);
		}


		/**
		 * When the "Select Books" button is pressed, call the selectBooks method. 
		 * When the "Submit" button is pressed, call the getResult method. 
		 */
		@Override
		public void actionPerformed(ActionEvent e) {

			String cmd = e.getActionCommand();

			switch (cmd) {
			case "Select Books":
				btnSelectBooks.setEnabled(false);
				selectBooks();
				break;
			case "Submit":
				getResult();
				break;
			default:
				break;
			}		

		}


		/**
		 * Invokes the file chooser and once files chosen, show up another JFrame object
		 * that runs in a separate thread. 
		 */
		private void selectBooks() {
			
			String path = WhatsNextApp.getPathFileChooserOpen();
			JFileChooser jFileChooser = new JFileChooser(new File(path));
			jFileChooser.setMultiSelectionEnabled(true);

			int selected = jFileChooser.showOpenDialog(this);
			if (selected == JFileChooser.APPROVE_OPTION) {
				files = jFileChooser.getSelectedFiles();

				Thread inputFilesThread = new Thread(new InputFilesFrame(files, myPanel));
				inputFilesThread.start();
			} else if (selected == JFileChooser.CANCEL_OPTION) {
				btnSelectBooks.setEnabled(true);
			}
		}
		
		
		/**
		 * This is called from InputFilesFrame#actionPerformed method. 
		 * When user press the "Read" button, this will be called. 
		 * Tried to display something like progress indicator.
		 * @param files
		 */
		void readFiles(File[] files) {
			
			fileParser.readFile(files);	
			
			lblReading.setText("Finished reading " + files.length + " book" + 
			                   ((files.length <= 1) ? "" : "s") + ".");

			
//			Thread tReadFile = new Thread(new Runnable() {
//
//				@Override
//				public void run() {
//					fileParser.readFile(files);	
//
//				}
//			});
//			tReadFile.start();
						
			
//			Thread indicatorThread = new Thread(new Runnable() {
//			SwingUtilities.invokeLater(new Runnable() {
//			
//				@Override
//				public void run() {
////					logger.trace("run() in readFiles() starts.");
//					
//					logger.trace(fileParser.getFinishedReading());
//					while (!fileParser.getFinishedReading()) {
//						
//						runIndicator();
//						
//						try {
//							Thread.sleep(10);
//						} catch (InterruptedException e) {
//							e.printStackTrace();
//						}
//					}	
//					
//					lblReading.setText("Finished reading " + files.length + " books.");
//				}
//
//				private void runIndicator() {
//					logger.trace("runIndicator() starts.");
//
//					lblReading.setText("Reading books... " + SPINNER[indexOfSpinner % 4]);
//					indexOfSpinner++;
//				}
//			});
//			indicatorThread.start();
			
//			lblReading.setText("Reading books... ");

			
			
		}


		/**
		 * When user press the "Submit" button, this will be called to display the result. 
		 */
		private void getResult() {

			String word = textField.getText().trim().toLowerCase();

			if (!word.equals("")) {
				String result = nextWordsHolder.buildResult(word, 10);

				result += "\n\nTotal number of next words for word '" + word + "': " + nextWordsHolder.getSum() + "\n";
				result += "\nTotal number of words as keys: " + nextWordsHolder.getWordToCountMapSize() + "\n";

				txtAreaResult.setText(result);		
			}

		}

	}






































}
