/*	SpellChecker
 *	Written by Matthew Schmeiser, April 2017
 *	For Jean-Francois St-Amand, Lockheed Martin
 *	As part of job application process
 */

package Model;

import static org.junit.Assert.*;

import java.util.List;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import org.junit.Test;

import Model.SpellChecker;

public class SpellCheckerTest {

	@Test
	public void testDictionaryParse() {
		SpellChecker spellChecker = new SpellChecker();
		spellChecker.parseDictionary(readFile(new File("test/sampleDictionary.txt")));

		HashSet<String> checkDictionary = dictionaryParseCheck();
		assertTrue(spellChecker.dictionary.equals(checkDictionary));
	}

	@Test
	public void testReadInputFile() {
		SpellChecker spellChecker = new SpellChecker();
		spellChecker.setText(readFile(new File("test/sampleInput.txt")));

		String checkInput = "Teh quick brown fox jumped over the lazy dog." + System.lineSeparator()
				+ "The quck brown fox jumped over the lazy dgo?!" + System.lineSeparator()
				+ "Apostrophes shouldn't not be included." + System.lineSeparator()
				+ "Periods aren't always at end of line. Sometimes they're in the middle." + System.lineSeparator()
				+ "Alot trips people up a lot of the time.";

		assertTrue(spellChecker.text.compareTo(checkInput) == 0);
	}

	@Test
	public void testInputToWordList() {
		SpellChecker spellChecker = new SpellChecker();
		spellChecker.setText(readFile(new File("test/sampleInput.txt")));

		spellChecker.getWordList();
		List<String> wordsCheck = inputToWordListCheck();

		assertTrue(spellChecker.textAsTokens.equals(wordsCheck));
	}

	@Test
	public void testBadInputAgainstDictionary() {
		try {
			SpellChecker spellChecker = new SpellChecker();
			spellChecker.setText(readFile(new File("test/sampleInput.txt")));
			spellChecker.parseDictionary(readFile(new File("test/sampleDictionary.txt")));

			spellChecker.findMisspellings();

			HashMap<Integer, String> badWordsCheck = badInputTestCheck();

			spellChecker.waitOnExecutor();

			assertTrue(spellChecker.misspellings.equals(badWordsCheck));
		} catch (InterruptedException e) {
			System.err.println("Error: Timed Out Waiting for Spell Check.");
			e.printStackTrace();
		}
	}

	@Test
	public void testGoodInputAgainstDictionary() {
		try {
			SpellChecker spellChecker = new SpellChecker();
			spellChecker.setText(readFile(new File("test/sampleInput2.txt")));
			spellChecker.parseDictionary(readFile(new File("test/sampleDictionary.txt")));

			spellChecker.findMisspellings();
			spellChecker.waitOnExecutor();

			assertTrue(spellChecker.misspellings.isEmpty());
		} catch (InterruptedException e) {
			System.err.println("Error: Timed Out Waiting for Spell Check.");
			e.printStackTrace();
		}
	}

	@Test
	public void testReplaceSpellingOnUncapital() {
		SpellChecker spellChecker = new SpellChecker();
		spellChecker.textAsTokens = new ArrayList<String>();
		spellChecker.textAsTokens.add("teh");

		spellChecker.replaceSpelling("the", 0);
		assertTrue(spellChecker.textAsTokens.get(0).equals("the"));
	}

	@Test
	public void testReplaceSpellingOnCapital() {
		SpellChecker spellChecker = new SpellChecker();
		spellChecker.textAsTokens = new ArrayList<String>();
		spellChecker.textAsTokens.add("Teh");

		spellChecker.replaceSpelling("the", 0);
		assertTrue(spellChecker.textAsTokens.get(0).equals("The"));
	}

	@Test
	public void testReplaceSpellingOnAllCapital() {
		SpellChecker spellChecker = new SpellChecker();
		spellChecker.textAsTokens = new ArrayList<String>();
		spellChecker.textAsTokens.add("AHHHHHH");

		spellChecker.replaceSpelling("ahhh", 0);
		assertTrue(spellChecker.textAsTokens.get(0).equals("ahhh"));
	}

	@Test
	public void testUpdateText() {
		SpellChecker spellChecker = new SpellChecker();
		spellChecker.text = "";

		ArrayList<String> textCheck = updateTextCheck();
		spellChecker.textAsTokens = textCheck;

		spellChecker.updateText();

		assertTrue(spellChecker.text.equals("Here's a new sentence."));
	}

	private HashSet<String> dictionaryParseCheck() {
		HashSet<String> checkDictionary = new HashSet<String>();
		checkDictionary.add("the");
		checkDictionary.add("quick");
		checkDictionary.add("brown");
		checkDictionary.add("fox");
		checkDictionary.add("jumped");
		checkDictionary.add("over");
		checkDictionary.add("lazy");
		checkDictionary.add("dog");
		checkDictionary.add("apostrophes");
		checkDictionary.add("shouldn't");
		checkDictionary.add("not");
		checkDictionary.add("be");
		checkDictionary.add("included");
		checkDictionary.add("periods");
		checkDictionary.add("aren't");
		checkDictionary.add("always");
		checkDictionary.add("at");
		checkDictionary.add("end");
		checkDictionary.add("of");
		checkDictionary.add("line");
		checkDictionary.add("sometimes");
		checkDictionary.add("they're");
		checkDictionary.add("in");
		checkDictionary.add("middle");
		checkDictionary.add("trips");
		checkDictionary.add("people");
		checkDictionary.add("up");
		checkDictionary.add("a lot");
		checkDictionary.add("a");
		checkDictionary.add("lot");
		checkDictionary.add("time");
		return checkDictionary;
	}

	private List<String> inputToWordListCheck() {
		List<String> wordsCheck = new ArrayList<String>();
		wordsCheck.add("Teh");
		wordsCheck.add(" ");
		wordsCheck.add("quick");
		wordsCheck.add(" ");
		wordsCheck.add("brown");
		wordsCheck.add(" ");
		wordsCheck.add("fox");
		wordsCheck.add(" ");
		wordsCheck.add("jumped");
		wordsCheck.add(" ");
		wordsCheck.add("over");
		wordsCheck.add(" ");
		wordsCheck.add("the");
		wordsCheck.add(" ");
		wordsCheck.add("lazy");
		wordsCheck.add(" ");
		wordsCheck.add("dog");
		wordsCheck.add(".");
		wordsCheck.add(System.lineSeparator());
		wordsCheck.add("The");
		wordsCheck.add(" ");
		wordsCheck.add("quck");
		wordsCheck.add(" ");
		wordsCheck.add("brown");
		wordsCheck.add(" ");
		wordsCheck.add("fox");
		wordsCheck.add(" ");
		wordsCheck.add("jumped");
		wordsCheck.add(" ");
		wordsCheck.add("over");
		wordsCheck.add(" ");
		wordsCheck.add("the");
		wordsCheck.add(" ");
		wordsCheck.add("lazy");
		wordsCheck.add(" ");
		wordsCheck.add("dgo");
		wordsCheck.add("?");
		wordsCheck.add("!");
		wordsCheck.add(System.lineSeparator());
		wordsCheck.add("Apostrophes");
		wordsCheck.add(" ");
		wordsCheck.add("shouldn't");
		wordsCheck.add(" ");
		wordsCheck.add("not");
		wordsCheck.add(" ");
		wordsCheck.add("be");
		wordsCheck.add(" ");
		wordsCheck.add("included");
		wordsCheck.add(".");
		wordsCheck.add(System.lineSeparator());
		wordsCheck.add("Periods");
		wordsCheck.add(" ");
		wordsCheck.add("aren't");
		wordsCheck.add(" ");
		wordsCheck.add("always");
		wordsCheck.add(" ");
		wordsCheck.add("at");
		wordsCheck.add(" ");
		wordsCheck.add("end");
		wordsCheck.add(" ");
		wordsCheck.add("of");
		wordsCheck.add(" ");
		wordsCheck.add("line");
		wordsCheck.add(".");
		wordsCheck.add(" ");
		wordsCheck.add("Sometimes");
		wordsCheck.add(" ");
		wordsCheck.add("they're");
		wordsCheck.add(" ");
		wordsCheck.add("in");
		wordsCheck.add(" ");
		wordsCheck.add("the");
		wordsCheck.add(" ");
		wordsCheck.add("middle");
		wordsCheck.add(".");
		wordsCheck.add(System.lineSeparator());
		wordsCheck.add("Alot");
		wordsCheck.add(" ");
		wordsCheck.add("trips");
		wordsCheck.add(" ");
		wordsCheck.add("people");
		wordsCheck.add(" ");
		wordsCheck.add("up");
		wordsCheck.add(" ");
		wordsCheck.add("a");
		wordsCheck.add(" ");
		wordsCheck.add("lot");
		wordsCheck.add(" ");
		wordsCheck.add("of");
		wordsCheck.add(" ");
		wordsCheck.add("the");
		wordsCheck.add(" ");
		wordsCheck.add("time");
		wordsCheck.add(".");
		return wordsCheck;
	}

	private HashMap<Integer, String> badInputTestCheck() {
		HashMap<Integer, String> badWordsCheck = new HashMap<Integer, String>();
		badWordsCheck.put(0, "teh");
		badWordsCheck.put(21, "quck");
		badWordsCheck.put(35, "dgo");
		badWordsCheck.put(76, "alot");
		return badWordsCheck;
	}

	private ArrayList<String> updateTextCheck() {
		ArrayList<String> textCheck = new ArrayList<String>();

		textCheck.add("Here's");
		textCheck.add(" ");
		textCheck.add("a");
		textCheck.add(" ");
		textCheck.add("new");
		textCheck.add(" ");
		textCheck.add("sentence");
		textCheck.add(".");
		return textCheck;
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
			return null;
		}
	}
}
