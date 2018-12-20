package experiments;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class Panel extends JPanel {
	
	private JLabel label;
	
	
	public Panel() {
		label = new JLabel("Enter a word: ");
		add(label);
		
		
		setPreferredSize(new Dimension(800, 600));
		setBackground(Color.WHITE);
	}
	

}
