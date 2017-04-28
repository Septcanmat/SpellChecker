/*	SpellChecker
 *	Written by Matthew Schmeiser, April 2017
 *	For Jean-Francois St-Amand, Lockheed Martin
 *	As part of job application process
 */

package Model;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class SpellChecker {

	public HashSet<String> dictionary;
	public String text;
	public List<String> textAsTokens;
	private ExecutorService executor;
	public ConcurrentHashMap<Integer, String> misspellings;

	public void parseDictionary(String wordList) {
		dictionary = new HashSet<String>();

		dictionary.addAll(Arrays.asList(wordList.toLowerCase().split("\r\n|\r|\n")));
	}

	public void setText(String input) {
		text = input;
	}

	public void getWordList() {
		// Split on ANY whitespace OR punctuation EXCEPT apostrophes, also add
		// delimiters as tokens
		textAsTokens = Arrays.asList(text.split("((?<=\\s+)|(?=\\s+)|(?<=[\\p{Punct}&&[^']])|(?=[\\p{Punct}&&[^']]))"));
	}

	// Original serial version of findMisspellings()
	// Kept for testing purposes
	public HashMap<Integer, String> findMisspellingsSerial() {
		getWordList();
		HashMap<Integer, String> badWords = new HashMap<Integer, String>();

		for (String word : textAsTokens) {
			// if word is actually a word and not one of the delimiter tokens
			if (!word.matches("\\s+") && !word.matches("[\\p{Punct}&&[^']]")) {
				String wordSpelling = word.toLowerCase();
				if (!dictionary.contains(wordSpelling))
					badWords.put(textAsTokens.indexOf(word), wordSpelling);
			}
		}

		return badWords;
	}

	private class WordCheckThread implements Runnable {

		String word;
		int index;
		ConcurrentHashMap<Integer, String> wordList;

		public WordCheckThread(String wordIn, int indexIn, ConcurrentHashMap<Integer, String> map) {
			word = wordIn;
			index = indexIn;
			wordList = map;
		}

		@Override
		public void run() {
			String wordSpelling = word.toLowerCase();
			if (!dictionary.contains(wordSpelling))
				wordList.put(index, wordSpelling);
		}

	}

	public void findMisspellings() {
		getWordList();
		misspellings = new ConcurrentHashMap<Integer, String>();

		executor = Executors.newFixedThreadPool(textAsTokens.size());
		for (int i = 0; i < textAsTokens.size(); i++) {
			String word = textAsTokens.get(i);
			//We only need to check the tokens that are actually words,
			//rather than the ones that are delimiters
			if (!word.matches("\\s+") && !word.matches("[\\p{Punct}&&[^']]")) {
				Runnable thread = new WordCheckThread(word, i, misspellings);
				executor.execute(thread);
			}
		}

		executor.shutdown();
		// Don't necessarily wait for execution to complete immediately
		// Entries will be entered into the map ASAP, there's no need to wait
		// for the whole
		// list to be built before we can start processing them
	}

	public void waitOnExecutor() throws InterruptedException {
		executor.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS);
	}

	public boolean executorFinished() {
		return executor.isTerminated();
	}

	public void replaceSpelling(String newSpelling, int index) {
		if (textAsTokens.size() < index)
			return;

		String oldSpelling = textAsTokens.get(index);

		newSpelling = newSpelling.toLowerCase();
		
		// If the replaced word starts with a Capital letter followed by
		// zero or more lowercase letters, capitalize the first letter
		// of the replacement spelling.
		// This is intended to preserve capitalization of sentences and
		// single-letter words like 'I'
		if (oldSpelling.matches("\\A\\p{Upper}\\p{Lower}*")) {
			newSpelling = newSpelling.substring(0, 1).toUpperCase() + newSpelling.substring(1);
		}

		textAsTokens.set(index, newSpelling);
	}

	public void updateText() {
		StringBuilder builder = new StringBuilder();

		for (String token : textAsTokens) {
			builder.append(token);
		}

		text = builder.toString();
	}
}
