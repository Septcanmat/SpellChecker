/*	SpellChecker
 *	Written by Matthew Schmeiser, April 2017
 *	For Jean-Francois St-Amand, Lockheed Martin
 *	As part of job application process
 */

package GUI;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.util.Iterator;
import java.util.Map.Entry;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import Model.SpellChecker;

public class CorrectionsFrame {

	private static final String CANCEL = "Cancel";
	private static final String IGNORE = "Ignore";
	private static final String CORRECT_SPELLING = "Correct Spelling";

	private JFrame frame;
	private MainFrame mainFrame;
	private SpellChecker spellChecker;
	private CorrectionsFrameListener listener;
	private Iterator<Entry<Integer, String>> iterator;
	private Entry<Integer, String> currentEntry;
	private JTextField misspellingField;
	private JTextField correctionField;

	public CorrectionsFrame(SpellChecker spellCheckerIn, MainFrame mainFrameIn) {
		spellChecker = spellCheckerIn;
		mainFrame = mainFrameIn;
		frame = new JFrame("Misspelling");
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setResizable(false);

		listener = new CorrectionsFrameListener();

		JPanel correctionsPanel = correctionsPanel();

		frame.add(correctionsPanel);

		spellChecker.findMisspellings();
		iterator = spellChecker.misspellings.entrySet().iterator();
		iterate();
	}

	private JPanel correctionsPanel() {
		JPanel correctionsPanel = new JPanel();
		correctionsPanel.setLayout(new GridBagLayout());

		JLabel misspellingLabel = new JLabel("Unkown Word:");
		correctionsPanel.add(misspellingLabel, misspellingLabelGBC());

		JLabel newSpellingLabel = new JLabel("New Spelling:");
		correctionsPanel.add(newSpellingLabel, newSpellingLabelGBC());

		misspellingField = new JTextField();
		misspellingField.setEditable(false);
		correctionsPanel.add(misspellingField, misspellingFieldGBC());

		correctionField = new JTextField();
		correctionsPanel.add(correctionField, correctionFieldGBC());

		JButton cancelButton = cancelButton();
		correctionsPanel.add(cancelButton, cancelButtonGBC());

		JButton ignoreButton = ignoreButton();
		correctionsPanel.add(ignoreButton, ignoreButtonGBC());

		JButton correctSpellingButton = correctSpellingButton();
		correctionsPanel.add(correctSpellingButton, correctSpellingButtonGBC());

		return correctionsPanel;
	}

	private JButton cancelButton() {
		JButton cancelButton = new JButton(CANCEL);
		cancelButton.setActionCommand(CANCEL);
		cancelButton.addActionListener(listener);
		return cancelButton;
	}

	private JButton ignoreButton() {
		JButton ignoreButton = new JButton(IGNORE);
		ignoreButton.setActionCommand(IGNORE);
		ignoreButton.addActionListener(listener);
		return ignoreButton;
	}
	
	private JButton correctSpellingButton() {
		JButton correctSpellingButton = new JButton(CORRECT_SPELLING);
		correctSpellingButton.setActionCommand(CORRECT_SPELLING);
		correctSpellingButton.addActionListener(listener);
		return correctSpellingButton;
	}

	private GridBagConstraints misspellingFieldGBC() {
		GridBagConstraints gbc;
		gbc = new GridBagConstraints();
		gbc.gridx = 1;
		gbc.gridy = 0;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridwidth = 2;
		return gbc;
	}

	private GridBagConstraints newSpellingLabelGBC() {
		GridBagConstraints gbc;
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 1;
		return gbc;
	}

	private GridBagConstraints correctSpellingButtonGBC() {
		GridBagConstraints gbc;
		gbc = new GridBagConstraints();
		gbc.gridx = 2;
		gbc.gridy = 2;
		return gbc;
	}

	private GridBagConstraints cancelButtonGBC() {
		GridBagConstraints gbc;
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.anchor = GridBagConstraints.LINE_START;
		return gbc;
	}

	private GridBagConstraints ignoreButtonGBC() {
		GridBagConstraints gbc;
		gbc = new GridBagConstraints();
		gbc.gridx = 1;
		gbc.gridy = 2;
		return gbc;
	}

	private GridBagConstraints correctionFieldGBC() {
		GridBagConstraints gbc;
		gbc = new GridBagConstraints();
		gbc.gridx = 1;
		gbc.gridy = 1;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridwidth = 2;
		return gbc;
	}

	private GridBagConstraints misspellingLabelGBC() {
		GridBagConstraints gbc;
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		return gbc;
	}

	public void pack() {
		frame.pack();
	}

	public void setVisible(boolean b) {
		frame.setVisible(b);
	}

	public void cancel() {
		frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
	}

	public void ignore() {
		iterate();
	}

	public void correctSpelling() {
		if (!correctionField.getText().isEmpty()) {
			spellChecker.replaceSpelling(correctionField.getText(), currentEntry.getKey());
			iterate();
		}
	}

	private void iterate() {
		if (iterator.hasNext()) {
			currentEntry = iterator.next();
			updateFields();
		} else {			
			//Confirm that the list has finished populating rather than potentially
			//having processed all entries before the executor finished execution
			if(!spellChecker.executorFinished()){
				try {
					spellChecker.waitOnExecutor();
				} catch (InterruptedException e) {
					System.err.println("Error: Timed Out Waiting for Spell Check.");
					e.printStackTrace();
					System.exit(-1);					
				}
			}
			frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
			mainFrame.spellingsChecked();
		}
	}

	private void updateFields() {
		misspellingField.setText(currentEntry.getValue());
		correctionField.setText("");
	}

	private class CorrectionsFrameListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			switch (e.getActionCommand()) {
			case CANCEL:
				CorrectionsFrame.this.cancel();
				break;
			case IGNORE:
				CorrectionsFrame.this.ignore();
				break;
			case CORRECT_SPELLING:
				CorrectionsFrame.this.correctSpelling();
				break;
			default:
				System.err.println("Corrections Frame ActionEvent with unrecognized ActionCommand");
				System.err.println(e.getActionCommand());
				System.exit(-1);
			}
		}

	}
}
