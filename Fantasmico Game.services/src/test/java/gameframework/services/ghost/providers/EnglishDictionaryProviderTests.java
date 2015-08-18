package gameframework.services.ghost.providers;

import static org.junit.Assert.*;

import java.util.List;
import java.util.Locale;

import org.junit.Before;
import org.junit.Test;

/**
 * @version $Id$
 */
public class EnglishDictionaryProviderTests {
	
	private EnglishFileDictionaryProvider dict;
	
	private String langCode;
	private String letter;
	private boolean actualIsDictForLang;
	private boolean actualCheckLetter;
	private List<String> actualListWords;
	
	@Before
	public void setupLogging() {
		dict = new EnglishFileDictionaryProvider(4);
	}

	@Test
	public void isDictionaryForLang_returnsTrue() {
		// Arrange
		givenLangCode(Locale.ENGLISH.getISO3Language());

		// Act
		whenIsDictForLang();

		// Asserts
		thenIsDictForLang(true);
	}

	@Test
	public void isDictionaryForLang_returnsFalse() {
		// Arrange
		givenLangCode(Locale.CHINA.getISO3Language());

		// Act
		whenIsDictForLang();

		// Asserts
		thenIsDictForLang(false);
	}

	@Test
	public void isDictionaryForLang_returnsFalseWhenNull() {
		// Act
		whenIsDictForLang();

		// Asserts
		thenIsDictForLang(false);
	}

	@Test
	public void checkLetter_returnsTrue() {
		// Arrange
		givenLetter("a");
		
		// Act
		whenCheckLetter();

		// Asserts
		thenCheckLetter(true);
	}

	@Test
	public void checkLetter_returnsFalse() {
		// Arrange
		givenLetter("|");

		// Act
		whenCheckLetter();

		// Asserts
		thenCheckLetter(false);
	}

	@Test
	public void listPossibleWords_returnsNotEmptyBecauseItIsLimit() {
		// Arrange
		givenLetter("aah");

		// Act
		whenListPossibleWords();

		// Asserts
		thenListPossibleWords(2);
	}

	@Test
	public void listPossibleWords_returnsEmpty() {
		// Arrange
		givenLetter("ancylostomiases");

		// Act
		whenListPossibleWords();

		// Asserts
		thenListPossibleWords(0);
	}
	
	/**
	 * GIVENs
	 */
	
	private void givenLangCode(String langCode) {
		this.langCode = langCode;
	}
	
	private void givenLetter(String letter) {
		this.letter = letter;
	}
	
	/**
	 * WHENs
	 */
	
	private void whenIsDictForLang() {
		actualIsDictForLang = dict.isDictionaryForLang(langCode);
	}
	
	private void whenCheckLetter() {
		actualCheckLetter = dict.checkLetter(letter);
	}
	
	private void whenListPossibleWords() {
		actualListWords = dict.listPossibleWords(letter);
	}
	
	/**
	 * THENs
	 */
	
	private void thenIsDictForLang(boolean expected) {
		assertEquals(expected, actualIsDictForLang);
	}
	
	private void thenCheckLetter(boolean expected) {
		assertEquals(expected, actualCheckLetter);
	}
	
	private void thenListPossibleWords(int expected) {
		assertEquals(expected, actualListWords.size());
	}
}
