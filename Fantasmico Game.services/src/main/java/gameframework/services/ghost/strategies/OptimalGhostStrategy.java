package gameframework.services.ghost.strategies;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import org.apache.log4j.Logger;

/**
 * The optimal ghost strategy works by following the next strategy: If there are
 * several winning moves, the computer will choose one randomly. If there are no
 * winning moves, the computer will choose one among the longest word randomly
 * to extend the game as long as possible.
 * 
 * @author Jose Carvajal
 */
public class OptimalGhostStrategy implements IGhostStrategy {
	private static final Logger LOG = Logger
			.getLogger(OptimalGhostStrategy.class);

	private final static Integer LEVEL = 1;

	private Random rand = new Random(System.currentTimeMillis());

	/**
	 * {@inheritDoc}
	 * 
	 * @see gameframework.services.ghost.strategies.IGhostStrategy#isStrategyForLevel(java.lang.Integer)
	 */
	public boolean isStrategyForLevel(Integer level) {
		return LEVEL.equals(level);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see gameframework.services.ghost.strategies.IGhostStrategy#addLetter(java.lang.String)
	 */
	public String addLetter(final List<String> choices,
			final String currentString) {
		// Retrieve all possible letters.
		Map<String, List<String>> letters = this.retrieveLetterChoices(choices,
				currentString);

		// Look for the letters that satisfy the conditions for winning the game
		List<String> lettersMoves = this.satisfyOptimalSolution(letters,
				currentString);

		if (lettersMoves.size() == 0) {
			// Computer thinks it may lose, choose the longest choices.
			lettersMoves = this.retrieveLongestChoices(letters, currentString);
		}

		// Choose randomly among movements
		Integer index = rand.nextInt(lettersMoves.size());

		return lettersMoves.get(index);
	}

	/**
	 * Returns the next distinct letter among choices.
	 * 
	 * @param choices
	 * @return
	 */
	private Map<String, List<String>> retrieveLetterChoices(
			List<String> choices, String currentString) {
		Map<String, List<String>> letters = new HashMap<String, List<String>>();

		for (String choice : choices) {
			if (isChoice(choice, currentString)) {
				String letter = choice.substring(currentString.length(),
						currentString.length() + 1);

				List<String> words = null;

				if (letters.containsKey(letter)) {
					words = letters.get(letter);
				} else {
					words = new ArrayList<String>();
					letters.put(letter, words);
				}

				words.add(choice);
			}
		}

		LOG.debug(String.format("Letters %s for current string %s.", letters,
				currentString));

		return letters;
	}

	/**
	 * True whether all possibilities after choice is made, have odd length.
	 * 
	 * @param letter
	 * @param currentString
	 * @return
	 */
	private List<String> satisfyOptimalSolution(
			Map<String, List<String>> letters, String currentString) {
		List<String> optimalLetters = new ArrayList<String>();

		for (Entry<String, List<String>> node : letters.entrySet()) {
			// Check whether there are choices with pair length
			boolean allChoicesOddLength = true;
			int index = 0;

			while (index < node.getValue().size() && allChoicesOddLength) {
				String alternative = node.getValue().get(index);

				// Future choice if choice is chosen.
				if (alternative.length() % 2 == 0) {
					allChoicesOddLength = false;
				}

				index++;
			}

			// If all future choices have pair length and there is no words, it
			// is a optimal solution.
			if (allChoicesOddLength) {
				optimalLetters.add(node.getKey());
			}
		}

		LOG.debug(String.format("Winning moves %s for current string %s.",
				optimalLetters, currentString));

		return optimalLetters;
	}

	/**
	 * Retrieve the longest choices.
	 * 
	 * @param choices
	 * @return
	 */
	private List<String> retrieveLongestChoices(
			final Map<String, List<String>> letters, final String currentString) {
		int maxLengthChoice = 0;
		List<String> longestLetters = new ArrayList<String>();

		for (Entry<String, List<String>> node : letters.entrySet()) {

			String currentLetter = node.getKey();

			for (String choice : node.getValue()) {
				// If the choice has the same length that the longest one, then
				// add it. If not, reset.
				if (choice.length() >= maxLengthChoice) {
					if (choice.length() > maxLengthChoice) {
						longestLetters.clear();
					}

					if (!longestLetters.contains(currentLetter)) {
						longestLetters.add(currentLetter);
					}

					maxLengthChoice = choice.length();
				}
			}
		}

		LOG.debug(String.format("Longest moves %s for current string %s.",
				longestLetters, currentString));

		return longestLetters;
	}

	/**
	 * The list should have filtered the choices except when the minimal length
	 * it has not achieved yes. Therefore, it needs to be checked here as well.
	 * 
	 * @param choice
	 * @return
	 */
	private boolean isChoice(String choice, String currentString) {
		return currentString.length() < choice.length();
	}
}
