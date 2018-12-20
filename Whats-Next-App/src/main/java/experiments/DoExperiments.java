package experiments;

import java.awt.Dimension;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;

import javax.swing.JFrame;


public class DoExperiments {
	
	private static HashMap<String, TreeSet<NextWordToCount>> wordToCountMap = new HashMap<>();


	public static void main(String[] args) {
		
		
		JFrame jFrame = new JFrame("What's Next App");
		jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		Panel panel = new Panel();
		panel.setPreferredSize(new Dimension(800, 600));
		
		jFrame.getContentPane().add(panel);
		jFrame.pack();
		jFrame.setVisible(true);
		
		
		
		
		
//		NextWordToCount nextWordToCount1 = new NextWordToCount("b", 1);
//		NextWordToCount nextWordToCount2 = new NextWordToCount("c", 3);
//		NextWordToCount nextWordToCount3 = new NextWordToCount("d", 5);
//		NextWordToCount nextWordToCount4 = new NextWordToCount("e", 2);
//		NextWordToCount nextWordToCount5 = new NextWordToCount("b", 5);
//		
//		TreeSet<NextWordToCount> treeSet = new TreeSet<>();
//		treeSet.add(nextWordToCount1);
//		treeSet.add(nextWordToCount2);
//		treeSet.add(nextWordToCount3);
//		treeSet.add(nextWordToCount4);
//		treeSet.add(nextWordToCount5);
//		
//		wordToCountMap.put("a", treeSet);
//		
//		
//		
//		displayAllMapContent();
		

		System.out.println("done.");
	}
	
	
	
	public static void displayAllMapContent() {
		for (Map.Entry<String, TreeSet<NextWordToCount>> e : wordToCountMap.entrySet()) {
			System.out.println("word: " + e.getKey() + " ");
			for (NextWordToCount nextWordToCount : e.getValue()) {
				System.out.println(nextWordToCount + " ");
			}
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}
