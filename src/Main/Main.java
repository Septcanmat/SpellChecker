/*	SpellChecker
 *	Written by Matthew Schmeiser, April 2017
 *	For Jean-Francois St-Amand, Lockheed Martin
 *	As part of job application process
 */

package Main;

import GUI.MainFrame;
import Model.SpellChecker;

public class Main {

	public static void main(String[] args) {
		SpellChecker spellChecker = new SpellChecker();
		MainFrame mainFrame = new MainFrame(spellChecker);
		mainFrame.pack();
		mainFrame.setVisible(true);
	}

}
