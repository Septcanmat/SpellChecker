/*	SpellChecker
 *	Written by Matthew Schmeiser, April 2017
 *	For Jean-Francois St-Amand, Lockheed Martin
 *	As part of job application process
 */

package GUI;

import java.util.HashSet;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class DictionaryFrame {

	private static final int TextAreaColumns = 12;
	private static final int TextAreaRows = 40;
	private JFrame frame;

	public DictionaryFrame(HashSet<String> dictionary) {
		frame = new JFrame("Dictionary");
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setResizable(false);
		
		JPanel dictionaryPanel = dictionaryPanel(dictionaryString(dictionary));
		
		frame.add(dictionaryPanel);
	}

	private String dictionaryString(HashSet<String> dictionary) {
		StringBuilder builder = new StringBuilder();
		
		for(String word : dictionary){
			builder.append(word);
			builder.append(System.lineSeparator());
		}
		
		return builder.toString();
	}


	private JPanel dictionaryPanel(String text) {
		JPanel dictionaryPanel = new JPanel();
		JTextArea dictionaryArea = new JTextArea(TextAreaRows, TextAreaColumns);
		dictionaryArea.setEditable(false);
		dictionaryArea.setText(text);
		JScrollPane dictionaryPane = new JScrollPane(dictionaryArea);
		dictionaryPanel.add(dictionaryPane);
		return dictionaryPanel;
	}

	public void pack() {
		frame.pack();
	}

	public void setVisible(boolean b) {
		frame.setVisible(b);
	}

}
