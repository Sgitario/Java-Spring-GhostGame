package gameframework.services.ghost.strategies;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

/**
 * @version $Id$
 */
public class OptimalGhostStrategyTests {

	private OptimalGhostStrategy strategy;
	
	private int level;
	private String string;
	private boolean actualIsStrategyForLevel;
	private List<String> choices;
	private String actualLetter;
	
	@Before
	public void setup() {
		strategy = new OptimalGhostStrategy();
	}

	@Test
	public void isStrategyForLevel_returnsTrue() {
		// Arrange
		givenLevel(1);

		// Act
		whenIsStrategyForLevel();

		// Assert
		thenIsStrategyForLevel(true);
	}

	@Test
	public void isStrategyForLevel_returnsFalse() {
		// Arrange
		givenLevel(2);

		// Act
		whenIsStrategyForLevel();

		// Assert
		thenIsStrategyForLevel(false);
	}

	@Test
	public void addLetter_returnsWinningMovement() {
		// Arrange
		givenString("aah");
		givenChoices("aahed", "aahing");

		// Act
		whenAddLetter();

		// Assert
		thenExpectedLetter("e");
	}

	@Test
	public void addLetter_returnsWinningMovementOtherApproach() {
		// Arrange
		givenString("aah");
		givenChoices("aahed", "aahedg", "aahing", "aahba", "aahbb");

		// Act
		whenAddLetter();

		// Assert
		thenExpectedLetter("b");
	}

	@Test
	public void addLetter_returnsLongestMovement() {
		// Arrange
		givenString("aah");
		givenChoices("aahededd", "aahing");

		// Act
		whenAddLetter();

		// Assert
		thenExpectedLetter("e");
	}
	
	/**
	 * GIVENs
	 */
	
	private void givenLevel(int level) {
		this.level = level;
	}
	
	private void givenString(String string) {
		this.string = string;
	}
	
	private void givenChoices(String... choices) {
		this.choices = Arrays.asList(choices);
	}
	
	/**
	 * WHENs
	 */
	
	private void whenIsStrategyForLevel() {
		actualIsStrategyForLevel = strategy.isStrategyForLevel(level);
	}
	
	private void whenAddLetter() {
		actualLetter = strategy.addLetter(choices, string);
	}
	
	/**
	 * THENs
	 */
	
	private void thenIsStrategyForLevel(boolean expected) {
		org.junit.Assert.assertEquals(expected, actualIsStrategyForLevel);
	}
	
	private void thenExpectedLetter(String letter) {
		org.junit.Assert.assertEquals(letter, actualLetter);
	}

}
