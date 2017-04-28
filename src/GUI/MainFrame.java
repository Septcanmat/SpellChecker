/*	SpellChecker
 *	Written by Matthew Schmeiser, April 2017
 *	For Jean-Francois St-Amand, Lockheed Martin
 *	As part of job application process
 */

package GUI;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;

import Model.SpellChecker;

public class MainFrame {

	private static final FileNameExtensionFilter TextFileFilter = new FileNameExtensionFilter("Text", "txt");
	private static final String CLOSE = "Close";
	private static final String LOAD_FILES = "Load Files";
	private static final String SELECT_TEXT_FILE = "Select Text File";
	private static final String SELECT_DICTIONARY_FILE = "Select Dictionary File";
	private static final String SAVE = "Save";
	private static final String CHECK_SPELLING = "Check Spelling";
	private static final String VIEW_DICTIONARY = "View Dictionary";

	private final Dimension FrameMinSize = new Dimension(614, 530);
	private final Dimension SelectFilesPanelMaxSize = new Dimension(800, 89);
	private final Dimension SelectFilesPanelMinSize = new Dimension(428, 89);
	private JFrame frame;
	private MainFrameListener listener;
	private JFileChooser dictionaryChooser;
	private JFileChooser textChooser;
	private JFileChooser textSaver;
	private JTextField dictionaryFilePathField;
	private JTextField inputTextFilePathField;
	private SpellChecker spellChecker;
	private boolean filesLoaded;
	private JTextArea inputTextArea;
	private JLabel textUpdatedLabel;
	private File dictionaryFile;
	private File inputTextFile;

	public MainFrame(SpellChecker sc) {
		boolean showBorders = false; // *******************

		spellChecker = sc;

		frame = new JFrame("MSchmeiser Spell Checker");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		filesLoaded = false;

		listener = new MainFrameListener();

		JPanel mainPanel = mainPanel(showBorders);
		frame.add(mainPanel);
		frame.setMinimumSize(FrameMinSize);
	}

	public void pack() {
		frame.pack();
	}

	public void setVisible(boolean b) {
		frame.setVisible(b);
	}

	private JPanel mainPanel(boolean showBorders) {
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		if (showBorders)
			mainPanel.setBorder(BorderFactory.createLineBorder(Color.black)); // *******************
		mainPanel.add(Box.createRigidArea(new Dimension(0, 5)));

		JPanel selectFilesPanel = selectFilesPanel(showBorders);
		mainPanel.add(selectFilesPanel);
		mainPanel.add(Box.createRigidArea(new Dimension(0, 5)));

		JPanel contentPanel = contentPanel(showBorders);
		mainPanel.add(contentPanel);

		JPanel closeButtonPanel = closeButtonPanel();
		mainPanel.add(closeButtonPanel);
		return mainPanel;
	}
	
	private JPanel selectFilesPanel(boolean showBorders) {
		JPanel selectFilesPanel = new JPanel();
		selectFilesPanel.setLayout(new GridBagLayout());
		selectFilesPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
		if (showBorders)
			selectFilesPanel.setBorder(BorderFactory.createLineBorder(Color.blue)); // *******************
		selectFilesPanel.setMinimumSize(SelectFilesPanelMinSize);
		selectFilesPanel.setMaximumSize(SelectFilesPanelMaxSize);
		
		JButton selectDictionaryButton = new JButton(SELECT_DICTIONARY_FILE);
		selectDictionaryButton.setActionCommand(SELECT_DICTIONARY_FILE);
		selectDictionaryButton.addActionListener(listener);
		selectFilesPanel.add(selectDictionaryButton, selectDictionaryButtonConstraints());
		
		dictionaryFilePathField = dictionaryFilePathField();
		selectFilesPanel.add(dictionaryFilePathField, dictionaryFilePathFieldConstraints());
		
		JButton selectTextButton = new JButton(SELECT_TEXT_FILE);
		selectTextButton.setActionCommand(SELECT_TEXT_FILE);
		selectTextButton.addActionListener(listener);
		selectFilesPanel.add(selectTextButton, selectTextButtonConstraints());
		
		inputTextFilePathField = inputTextFilePathField();
		selectFilesPanel.add(inputTextFilePathField, inputTextFilePathFieldConstraints());
		
		JButton loadButton = new JButton(LOAD_FILES);
		loadButton.setActionCommand(LOAD_FILES);
		loadButton.addActionListener(listener);
		selectFilesPanel.add(loadButton, loadButtongConstraints());
		return selectFilesPanel;
	}
	
	private JPanel contentPanel(boolean showBorders) {
		JPanel contentPanel = new JPanel();
		contentPanel.setLayout(new GridBagLayout());
		contentPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
		if (showBorders)
			contentPanel.setBorder(BorderFactory.createLineBorder(Color.red)); // *******************
		
		JLabel inputLabel = new JLabel("Text");
		contentPanel.add(inputLabel, inputLabelConstraints());
		
		JButton viewDictionaryButton = new JButton(VIEW_DICTIONARY);
		viewDictionaryButton.setActionCommand(VIEW_DICTIONARY);
		viewDictionaryButton.addActionListener(listener);
		contentPanel.add(viewDictionaryButton, viewDictionaryButtonConstraints());
		
		inputTextArea = new JTextArea(20, 50);
		inputTextArea.setEditable(false);
		JScrollPane inputPane = new JScrollPane(inputTextArea);
		contentPanel.add(inputPane, inputPaneConstraints());
		
		JButton checkSpellingButton = new JButton(CHECK_SPELLING);
		checkSpellingButton.setActionCommand(CHECK_SPELLING);
		checkSpellingButton.addActionListener(listener);
		contentPanel.add(checkSpellingButton, checkSpellingButtonConstraints());
		
		textUpdatedLabel = new JLabel();
		contentPanel.add(textUpdatedLabel, textUpdatedLabelConstraints());
		
		JButton saveButton = new JButton(SAVE);
		saveButton.setActionCommand(SAVE);
		saveButton.addActionListener(listener);
		contentPanel.add(saveButton, saveButtonConstraints());
		
		return contentPanel;
	}

	private JPanel closeButtonPanel() {
		JPanel closeButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
		closeButtonPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
		JButton closeButton = new JButton(CLOSE);
		closeButton.setActionCommand(CLOSE);
		closeButton.addActionListener(listener);
		closeButtonPanel.add(closeButton);
		return closeButtonPanel;
	}

	private JTextField dictionaryFilePathField() {
		JTextField dictionaryFilePathField = new JTextField();
		dictionaryFilePathField.setColumns(20);
		dictionaryFilePathField.setEditable(false);
		return dictionaryFilePathField;
	}

	private JTextField inputTextFilePathField() {
		JTextField inputTextFilePathField = new JTextField();
		inputTextFilePathField.setColumns(20);
		inputTextFilePathField.setEditable(false);
		return inputTextFilePathField;
	}

	private GridBagConstraints saveButtonConstraints() {
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 1;
		gbc.gridy = 2;
		gbc.weightx = 0;
		gbc.weighty = 0;
		gbc.anchor = GridBagConstraints.LINE_END;
		return gbc;
	}

	private GridBagConstraints textUpdatedLabelConstraints() {
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 1;
		gbc.gridy = 2;
		gbc.weightx = 0;
		gbc.weighty = 0;
		gbc.anchor = GridBagConstraints.LINE_START;
		return gbc;
	}

	private GridBagConstraints checkSpellingButtonConstraints() {
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.weightx = 0;
		gbc.weighty = 0;
		gbc.anchor = GridBagConstraints.LINE_START;
		return gbc;
	}

	private GridBagConstraints inputPaneConstraints() {
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.gridwidth = 2;
		gbc.weightx = 0.8;
		gbc.weighty = 1;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.insets = new Insets(0, 5, 0, 5);
		return gbc;
	}

	private GridBagConstraints viewDictionaryButtonConstraints() {
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 1;
		gbc.gridy = 0;
		gbc.anchor = GridBagConstraints.LINE_END;
		return gbc;
	}

	private GridBagConstraints inputLabelConstraints() {
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.anchor = GridBagConstraints.LINE_START;
		return gbc;
	}

	private GridBagConstraints loadButtongConstraints() {
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 1;
		gbc.gridy = 2;
		gbc.anchor = GridBagConstraints.LINE_END;
		return gbc;
	}

	private GridBagConstraints inputTextFilePathFieldConstraints() {
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 1;
		gbc.gridy = 1;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weightx = 1;
		return gbc;
	}

	private GridBagConstraints selectTextButtonConstraints() {
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weightx = 0;
		return gbc;
	}

	private GridBagConstraints dictionaryFilePathFieldConstraints() {
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 1;
		gbc.gridy = 0;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weightx = 1;
		return gbc;
	}

	private GridBagConstraints selectDictionaryButtonConstraints() {
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.fill = GridBagConstraints.NONE;
		gbc.weightx = 0;
		return gbc;
	}

	public void selectDictionaryFile() {
		if (dictionaryChooser == null) {
			dictionaryChooser = new JFileChooser();
			dictionaryChooser.setFileFilter(TextFileFilter);
			dictionaryChooser.setAcceptAllFileFilterUsed(false);
		}
		int returnVal = dictionaryChooser.showOpenDialog(frame);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			dictionaryFile = dictionaryChooser.getSelectedFile();
			dictionaryFilePathField.setText(dictionaryFile.getName());
		}
	}

	public void selectTextFile() {
		if (textChooser == null) {
			textChooser = new JFileChooser();
			textChooser.setFileFilter(TextFileFilter);
			textChooser.setAcceptAllFileFilterUsed(false);
		}
		int returnVal = textChooser.showOpenDialog(frame);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			inputTextFile = textChooser.getSelectedFile();
			inputTextFilePathField.setText(inputTextFile.getName());
		}
	}

	public void loadFiles() {
		if (!dictionaryFilePathField.getText().equals("") && !inputTextFilePathField.getText().equals("")) {
			spellChecker.parseDictionary(readFile(dictionaryFile));
			spellChecker.setText(readFile(inputTextFile));

			inputTextArea.setText(spellChecker.text);

			filesLoaded = true;
		}
	}

	private String readFile(File file) {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			StringBuilder input = new StringBuilder();
			String line;
			line = reader.readLine();
			while (line != null) {
				input.append(line);
				line = reader.readLine();
				if (line != null)
					input.append(System.lineSeparator());
			}
			reader.close();
			return input.toString();
		} catch (IOException e) {
			System.err.println("Error: I/O Exception.");
			e.printStackTrace();
			System.exit(-1);
			return null;
		}
	}

	public void viewDictionary() {
		if (filesLoaded) {
			DictionaryFrame dictionaryFrame = new DictionaryFrame(spellChecker.dictionary);
			dictionaryFrame.pack();
			dictionaryFrame.setVisible(true);
		}
	}

	public void checkSpelling() {
		textUpdatedLabel.setText("");
		if (filesLoaded) {
			CorrectionsFrame correctionsFrame = new CorrectionsFrame(spellChecker, this);
			//If there are no misspellings then no need to show the CorrectionsFrame
			if(!spellChecker.executorFinished() || !spellChecker.misspellings.isEmpty()){
			correctionsFrame.pack();
			correctionsFrame.setVisible(true);
			}
		}
	}

	public void spellingsChecked() {
		spellChecker.updateText();
		textUpdatedLabel.setText("Spelling Checked");
		inputTextArea.setText(spellChecker.text);
	}

	public void save() {
		if (spellChecker.text == null)
			return;
		if (textSaver == null) {
			textSaver = new JFileChooser();
			textSaver.setFileFilter(TextFileFilter);
			textSaver.setAcceptAllFileFilterUsed(false);
		}
		int returnVal = textSaver.showSaveDialog(frame);
		if (returnVal == JFileChooser.APPROVE_OPTION && textSaver.getSelectedFile() != null) {
			String fileName = textSaver.getSelectedFile().getAbsolutePath();

			if (!fileName.endsWith(".txt")) {
				fileName = fileName.concat(".txt");
			}

			File saveFile = new File(fileName);

			try {
				FileWriter writer = new FileWriter(saveFile);
				writer.write(spellChecker.text);
				writer.close();
			} catch (IOException e) {
				System.err.println("Error: I/O Exception.");
				e.printStackTrace();
				System.exit(-1);
			}
		}
	}

	public void close() {
		frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
	}

	private class MainFrameListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			switch (e.getActionCommand()) {
			case SELECT_DICTIONARY_FILE:
				MainFrame.this.selectDictionaryFile();
				break;
			case SELECT_TEXT_FILE:
				MainFrame.this.selectTextFile();
				break;
			case LOAD_FILES:
				MainFrame.this.loadFiles();
				break;
			case VIEW_DICTIONARY:
				MainFrame.this.viewDictionary();
				break;
			case CHECK_SPELLING:
				MainFrame.this.checkSpelling();
				break;
			case SAVE:
				MainFrame.this.save();
				break;
			case CLOSE:
				MainFrame.this.close();
				break;
			default:
				System.err.println("Main Frame ActionEvent with unrecognized ActionCommand");
				System.err.println(e.getActionCommand());
				System.exit(-1);
			}
		}

	}
}
