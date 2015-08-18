package gameframework.services;

import static org.mockito.Mockito.*;
import gameframework.services.ghost.providers.IDictionaryProvider;
import gameframework.services.ghost.strategies.IGhostStrategy;
import gameframework.transversal.models.SessionBean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * @version $Id$
 */
@RunWith(MockitoJUnitRunner.class)
public class GhostServiceTests {
	@Mock
	private IDictionaryProvider dictionaryProvider;

	@Mock
	private IGhostStrategy ghostStrategy;
	
	private GhostService service;
	private SessionBean session;
	private int level;
	private String lang;
	private String userLetter;
	private String computerLetter;
	private String currentString;
	private String actualString;
	private boolean actualUserLetterChecked;
	
	@Before
	public void setup() {		
		service = new GhostService(Arrays.asList(dictionaryProvider), 
				Arrays.asList(ghostStrategy));
	}

	@Test
	public void addLetter_returnsFalseWhenNoMoreChoicesWereFound() {
		// Arranges
		givenAValidLang("en-ES");
		givenAValidLevel(1);
		givenValidUserLetter("h");
		givenCurrentString("aa");
		givenSessionBean();
		
		givenNoMorePossibleWords();

		// Acts
		whenAddTheUserLetter();

		// Asserts
		thenSessionIsFinished();
	}

	@Test
	public void addLetter_returnsTrueWhenChoicesWereFound() {
		// Arranges
		givenAValidLang("en-ES");
		givenAValidLevel(1);
		givenValidUserLetter("h");
		givenCurrentString("aa");
		givenSessionBean();
		
		givenMorePossibleWords(Arrays.asList("aahja"));
		givenStrategyReturns("j");

		// Acts
		whenAddTheUserLetter();

		// Asserts
		thenComputerWordIs("j");
	}

	@Test
	public void addLetter_returnsFinishedWinnerComputer() {
		// Arranges
		givenAValidLang("en-ES");
		givenAValidLevel(1);
		givenValidUserLetter("a");
		givenCurrentString("a");
		givenSessionBean();
		
		givenNoMorePossibleWords();

		// Acts
		whenAddTheUserLetter();

		// Asserts
		thenComputerWins();
	}

	@Test
	public void addLetter_returnsFinishedWinnerUser() {
		// Arranges
		givenAValidLang("en-ES");
		givenAValidLevel(1);
		givenValidUserLetter("n");
		givenCurrentString("aahi");
		givenSessionBean();
		
		givenMorePossibleWords(Arrays.asList("aahing"));
		givenStrategyLetterResponseFor("aahig", "g");

		// Acts
		whenAddTheUserLetter();

		// Asserts
		thenUserWins();
	}

	@Test
	public void getString_returnsEmptyWhenStringDoesNotExist() {
		// Arranges
		givenSessionBean();

		// Acts
		whenGetString();

		// Asserts
		thenActualStringEmpty();
	}

	@Test
	public void getString_returnsExpectedString() {
		// Arranges
		givenCurrentString("aa");
		givenSessionBean();

		// Acts
		whenGetString();

		// Asserts
		thenActualStringEquals("aa");
	}

	@Test
	public void checkLetter_returnsTrue() {
		// Arranges
		givenAValidLang("en-ES");
		givenValidUserLetter("n");
		givenSessionBean();

		// Acts
		whenCheckUserLetter();

		// Asserts
		thenUserLetterCheckedTrue();
	}
	
	/**
	 * GIVENS
	 */
	private void givenAValidLevel(int level) {
		this.level = level;
		
		Mockito.when(ghostStrategy.isStrategyForLevel(level)).thenReturn(true);
	}
	
	private void givenAValidLang(String lang) {
		this.lang = lang;
		Mockito.when(dictionaryProvider.isDictionaryForLang(lang)).thenReturn(
				true);
	}
	
	private void givenValidUserLetter(String userLetter) {
		this.userLetter = userLetter;
		
		Mockito.when(dictionaryProvider.checkLetter(this.userLetter)).thenReturn(true);
	}
	
	private void givenCurrentString(String currentString) {
		this.currentString = currentString;
	}
	
	private void givenSessionBean() {
		String game = "ghost";
		Map<String, String> properties = new HashMap<String, String>();
		if (currentString != null) {
			properties.put("ghost_string", currentString);
		}
		
		session = new SessionBean();
		session.setGameName(game);
		session.setLevel(level);
		session.setLang(lang);
		session.setProperties(properties);
	}
	
	@SuppressWarnings("unchecked")
	private void givenStrategyLetterResponseFor(String string, String letter) {
		Mockito.when(ghostStrategy.addLetter(any(List.class), eq(string))).thenReturn(letter);
	}
	
	private void givenMorePossibleWords(List<String> nextWords) {
		Mockito.when(dictionaryProvider.listPossibleWords(currentString + userLetter)).thenReturn(nextWords);
	}
	
	private void givenNoMorePossibleWords() {
		Mockito.when(dictionaryProvider.listPossibleWords(currentString + userLetter)).thenReturn(new ArrayList<String>());
	}
	
	@SuppressWarnings("unchecked")
	private void givenStrategyReturns(String letter) {
		Mockito.when(ghostStrategy.addLetter(any(List.class), any(String.class))).thenReturn(letter);
	}
	
	/**
	 * WHENS
	 */
	private void whenAddTheUserLetter() {
		computerLetter = service.addLetter(userLetter, session);
	}
	
	private void whenGetString() {
		actualString = service.getString(session);
	}
	
	private void whenCheckUserLetter() {
		actualUserLetterChecked = service.checkLetter(userLetter, session);
	}
	
	/**
	 * THENS
	 */
	private void thenSessionIsFinished() {
		Assert.assertNull(computerLetter);
		Assert.assertTrue(session.isFinished());
	}
	
	private void thenComputerWordIs(String computerLetter) {
		Assert.assertEquals(computerLetter, this.computerLetter);
	}
	
	private void thenComputerWins() {
		thenSessionIsFinished();
		Assert.assertEquals("computer", session.getWinner());
	}
	
	private void thenUserWins() {
		thenSessionIsFinished();
		Assert.assertEquals("user", session.getWinner());
	}
	
	private void thenActualStringEmpty() {
		Assert.assertEquals("", actualString);
	}
	
	private void thenActualStringEquals(String expected) {
		Assert.assertEquals(expected, actualString);
	}
	
	private void thenUserLetterCheckedTrue() {
		Assert.assertTrue(actualUserLetterChecked);
	}
}
