package gameframework.services.ghost.providers;

import java.util.List;
import java.util.Locale;

import org.apache.log4j.BasicConfigurator;
import org.junit.Before;
import org.junit.Test;

/**
 * @version $Id$
 */
public class EnglishDictionaryProviderTests
{
    @Before
    public void setupLogging()
    {
        BasicConfigurator.configure();
    }

    @Test
    public void isDictionaryForLang_returnsTrue()
    {
        // Arrange
        String langCode = Locale.ENGLISH.getISO3Language();
        EnglishFileDictionaryProvider dict = new EnglishFileDictionaryProvider(4);

        // Act
        boolean result = dict.isDictionaryForLang(langCode);

        // Asserts
        org.junit.Assert.assertTrue(result);
    }

    @Test
    public void isDictionaryForLang_returnsFalse()
    {
        // Arrange
        String langCode = Locale.CHINA.getISO3Language();
        EnglishFileDictionaryProvider dict = new EnglishFileDictionaryProvider(4);

        // Act
        boolean result = dict.isDictionaryForLang(langCode);

        // Asserts
        org.junit.Assert.assertFalse(result);
    }

    @Test
    public void isDictionaryForLang_returnsFalseWhenNull()
    {
        // Arrange
        String langCode = null;
        EnglishFileDictionaryProvider dict = new EnglishFileDictionaryProvider(4);

        // Act
        boolean result = dict.isDictionaryForLang(langCode);

        // Asserts
        org.junit.Assert.assertFalse(result);
    }

    @Test
    public void checkLetter_returnsTrue()
    {
        // Arrange
        String letter = "a";
        EnglishFileDictionaryProvider dict = new EnglishFileDictionaryProvider(4);

        // Act
        boolean result = dict.checkLetter(letter);

        // Asserts
        org.junit.Assert.assertTrue(result);
    }

    @Test
    public void checkLetter_returnsFalse()
    {
        // Arrange
        String letter = "|";
        EnglishFileDictionaryProvider dict = new EnglishFileDictionaryProvider(4);

        // Act
        boolean result = dict.checkLetter(letter);

        // Asserts
        org.junit.Assert.assertFalse(result);
    }

    @Test
    public void listPossibleWords_returnsNotEmptyBecauseItIsLimit()
    {
        // Arrange
        Integer expected = 2;
        String letter = "aah";
        EnglishFileDictionaryProvider dict = new EnglishFileDictionaryProvider(4);

        // Act
        List<String> words = dict.listPossibleWords(letter);

        // Asserts
        org.junit.Assert.assertEquals(expected, words.size(), 0.00001);
    }

    @Test
    public void listPossibleWords_returnsEmpty()
    {
        // Arrange
        Integer expected = 0;
        String letter = "ancylostomiases";
        EnglishFileDictionaryProvider dict = new EnglishFileDictionaryProvider(4);

        // Act
        List<String> words = dict.listPossibleWords(letter);

        // Asserts
        org.junit.Assert.assertEquals(expected, words.size(), 0.00001);
    }

    @Test
    public void listPossibleWords_returnsTrue()
    {
        // Arrange
        Integer expected = 1;
        String letter = "aali";
        EnglishFileDictionaryProvider dict = new EnglishFileDictionaryProvider(4);

        // Act
        List<String> words = dict.listPossibleWords(letter);

        // Asserts
        org.junit.Assert.assertEquals(expected, words.size(), 0.00001);
    }

    @Test
    public void listPossibleWords_returnsFalse()
    {
        // Arrange
        String letter = "aahing";
        EnglishFileDictionaryProvider dict = new EnglishFileDictionaryProvider(4);

        // Act
        List<String> words = dict.listPossibleWords(letter);

        // Asserts
        org.junit.Assert.assertEquals(0, words.size(), 0.00001);
    }

    @Test
    public void listPossibleWords_returnsTrueWhenIsNull()
    {
        // Arrange
        String letter = null;
        EnglishFileDictionaryProvider dict = new EnglishFileDictionaryProvider(4);

        // Act
        List<String> words = dict.listPossibleWords(letter);

        // Asserts
        org.junit.Assert.assertEquals(0, words.size(), 0.00001);
    }

    @Test
    public void listPossibleWords_returnsTrueWhenIsEmpty()
    {
        // Arrange
        String letter = "";
        EnglishFileDictionaryProvider dict = new EnglishFileDictionaryProvider(4);

        // Act
        List<String> words = dict.listPossibleWords(letter);

        // Asserts
        org.junit.Assert.assertEquals(0, words.size(), 0.00001);
    }
}
