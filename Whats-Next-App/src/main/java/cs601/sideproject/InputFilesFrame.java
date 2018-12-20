package cs601.sideproject;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import cs601.sideproject.UserInterface.MyPanel;


/**
 * To display what books read (what input files are), this object 
 * shows up next to the main window. Also, holds inner class MyInputFilesPanel. 
 * @author Kei Fukutani
 */
public class InputFilesFrame implements Runnable {
	
	private File[] files;
	private MyPanel myPanel;
	
	
	public InputFilesFrame(File[] files, MyPanel myPanel) {
		this.files = files;		
		this.myPanel = myPanel;
	}
	
	
	/**
	 * When user choose the input files and press the "open" button, 
	 * this will be called. 
	 */
	@Override
	public void run() {
		
		JFrame jFrame = new JFrame("Input Books");
		jFrame.setBounds(900, 100, 400, 700);
		
		MyInputFilesPanel panel = new MyInputFilesPanel();
		panel.setLayout(null);
		panel.setPreferredSize(new Dimension(400, 700));
		
		jFrame.getContentPane().add(panel);
		jFrame.pack();
		jFrame.setVisible(true);
	}
	
	
	/**
	 * Provides window view that display the file names to be read. 
	 */
	class MyInputFilesPanel extends JPanel implements ActionListener {
		
		private static final long serialVersionUID = 1L;
		private TextArea filesTextArea;
		private JButton readBtn;
		
		
		public MyInputFilesPanel() {
			initialize();
			displayFiles();
		}
		
		
		/**
		 * Initialize the components. 
		 */
		public void initialize() {
			filesTextArea = new TextArea(20, 5);
			filesTextArea.setBounds(20, 20, 360, 360);
			filesTextArea.setFont(new Font(null, Font.PLAIN, 16));

			readBtn = new JButton("Read");
			readBtn.setBounds(20, 400, 100, 30);
			readBtn.setFont(new Font(null, Font.PLAIN, 16));
			readBtn.addActionListener(this);
			
			add(filesTextArea);
			add(readBtn);
		}
		
		
		/**
		 * Displays file names that user choose. 
		 */
		private void displayFiles() {
			StringBuffer sb = new StringBuffer();
			for (File file : files) {
				sb.append(file.getName());
				sb.append("\n");
			}
			filesTextArea.setText(sb.toString());
		}
		
		
		/**
		 * When user press the "Read" button, this will be called. 
		 */
		@Override
		public void actionPerformed(ActionEvent e) {
			readBtn.setEnabled(false);
			myPanel.readFiles(files);
		}
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}
