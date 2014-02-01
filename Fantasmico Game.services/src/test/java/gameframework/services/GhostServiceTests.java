package gameframework.services;

import gameframework.services.ghost.providers.IDictionaryProvider;
import gameframework.services.ghost.strategies.IGhostStrategy;
import gameframework.transversal.models.SessionBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * @version $Id$
 */
@RunWith(MockitoJUnitRunner.class)
public class GhostServiceTests
{
    @Mock
    private IDictionaryProvider dictionaryProvider;

    @Mock
    private IGhostStrategy ghostStrategy;

    @Test
    public void addLetter_returnsFalseWhenNoMoreChoicesWereFound()
    {
        // Arranges
        String newLetter = "h";
        String string = "aa";
        String lang = "en-ES";
        Integer level = 1;
        String game = "ghost";
        Map<String, String> properties = new HashMap<String, String>();
        properties.put("ghost_string", string);
        SessionBean session = new SessionBean();
        session.setGameName(game);
        session.setLevel(level);
        session.setLang(lang);
        session.setProperties(properties);

        Mockito.when(dictionaryProvider.checkLetter(newLetter)).thenReturn(true);
        Mockito.when(dictionaryProvider.isDictionaryForLang(lang)).thenReturn(true);
        Mockito.when(dictionaryProvider.listPossibleWords(string + newLetter)).thenReturn(new ArrayList<String>());
        Mockito.when(ghostStrategy.isStrategyForLevel(level)).thenReturn(true);

        List<IDictionaryProvider> dictionaries = new ArrayList<IDictionaryProvider>();
        dictionaries.add(dictionaryProvider);

        List<IGhostStrategy> strategies = new ArrayList<IGhostStrategy>();
        strategies.add(ghostStrategy);

        GhostService service = new GhostService(dictionaries, strategies);

        // Acts
        String actual = service.addLetter(newLetter, session);

        // Asserts
        Assert.assertNull(actual);
        Assert.assertTrue(session.isFinished());
    }

    @Test
    public void addLetter_returnsTrueWhenChoicesWereFound()
    {
        // Arranges
        String newLetter = "h";
        String expectedLetter = "j";
        String string = "aa";
        String lang = "en-ES";
        Integer level = 1;
        String game = "ghost";
        Map<String, String> properties = new HashMap<String, String>();
        properties.put("ghost_string", string);
        SessionBean session = new SessionBean();
        session.setGameName(game);
        session.setLevel(level);
        session.setLang(lang);
        session.setProperties(properties);

        List<String> choices = new ArrayList<String>();
        choices.add("aahja");

        Mockito.when(dictionaryProvider.checkLetter(newLetter)).thenReturn(true);
        Mockito.when(dictionaryProvider.isDictionaryForLang(lang)).thenReturn(true);
        Mockito.when(dictionaryProvider.listPossibleWords(string + newLetter)).thenReturn(choices);
        Mockito.when(dictionaryProvider.listPossibleWords(string + newLetter + expectedLetter)).thenReturn(choices);
        Mockito.when(ghostStrategy.isStrategyForLevel(level)).thenReturn(true);
        Mockito.when(ghostStrategy.addLetter(choices, string + newLetter)).thenReturn(expectedLetter);

        List<IDictionaryProvider> dictionaries = new ArrayList<IDictionaryProvider>();
        dictionaries.add(dictionaryProvider);

        List<IGhostStrategy> strategies = new ArrayList<IGhostStrategy>();
        strategies.add(ghostStrategy);

        GhostService service = new GhostService(dictionaries, strategies);

        // Acts
        String actual = service.addLetter(newLetter, session);

        // Asserts
        Assert.assertNotNull(actual);
        Assert.assertFalse(session.isFinished());
        Assert.assertEquals(expectedLetter, actual);
    }

    @Test
    public void addLetter_returnsFinishedWinnerComputer()
    {
        // Arranges
        String winnerExpected = "computer";
        String newLetter = "a";
        String string = "a";
        String lang = "en-ES";
        Integer level = 1;
        String game = "ghost";
        Map<String, String> properties = new HashMap<String, String>();
        properties.put("ghost_string", string);
        SessionBean session = new SessionBean();
        session.setGameName(game);
        session.setLevel(level);
        session.setLang(lang);
        session.setProperties(properties);

        List<String> choices = new ArrayList<String>();

        Mockito.when(dictionaryProvider.checkLetter(newLetter)).thenReturn(true);
        Mockito.when(dictionaryProvider.isDictionaryForLang(lang)).thenReturn(true);
        Mockito.when(dictionaryProvider.listPossibleWords(string + newLetter)).thenReturn(choices);

        List<IDictionaryProvider> dictionaries = new ArrayList<IDictionaryProvider>();
        dictionaries.add(dictionaryProvider);

        List<IGhostStrategy> strategies = new ArrayList<IGhostStrategy>();
        strategies.add(ghostStrategy);

        GhostService service = new GhostService(dictionaries, strategies);

        // Acts
        String actual = service.addLetter(newLetter, session);

        // Asserts
        Assert.assertNull(actual);
        Assert.assertTrue(session.isFinished());
        Assert.assertEquals(session.getWinner(), winnerExpected);
    }

    @Test
    public void addLetter_returnsFinishedWinnerUser()
    {
        // Arranges
        String winnerExpected = "user";
        String expectedLetter = "g";
        String newLetter = "n";
        String string = "aahi";
        String lang = "en-ES";
        Integer level = 1;
        String game = "ghost";
        Map<String, String> properties = new HashMap<String, String>();
        properties.put("ghost_string", string);
        SessionBean session = new SessionBean();
        session.setGameName(game);
        session.setLevel(level);
        session.setLang(lang);
        session.setProperties(properties);

        List<String> choices = new ArrayList<String>();
        choices.add("aahing");

        Mockito.when(dictionaryProvider.checkLetter(newLetter)).thenReturn(true);
        Mockito.when(dictionaryProvider.isDictionaryForLang(lang)).thenReturn(true);
        Mockito.when(dictionaryProvider.listPossibleWords(string + newLetter)).thenReturn(choices);
        Mockito.when(ghostStrategy.isStrategyForLevel(level)).thenReturn(true);
        Mockito.when(ghostStrategy.addLetter(choices, string + newLetter)).thenReturn(expectedLetter);

        List<IDictionaryProvider> dictionaries = new ArrayList<IDictionaryProvider>();
        dictionaries.add(dictionaryProvider);

        List<IGhostStrategy> strategies = new ArrayList<IGhostStrategy>();
        strategies.add(ghostStrategy);

        GhostService service = new GhostService(dictionaries, strategies);

        // Acts
        String actual = service.addLetter(newLetter, session);

        // Asserts
        Assert.assertNotNull(actual);
        Assert.assertTrue(session.isFinished());
        Assert.assertEquals(expectedLetter, actual);
        Assert.assertEquals(session.getWinner(), winnerExpected);
    }

    @Test
    public void getString_returnsEmptyWhenStringDoesNotExist()
    {
        // Arranges
        Map<String, String> properties = new HashMap<String, String>();
        SessionBean session = new SessionBean();
        session.setProperties(properties);

        List<IDictionaryProvider> dictionaries = new ArrayList<IDictionaryProvider>();
        dictionaries.add(dictionaryProvider);

        List<IGhostStrategy> strategies = new ArrayList<IGhostStrategy>();
        strategies.add(ghostStrategy);

        GhostService service = new GhostService(dictionaries, strategies);

        // Acts
        String actual = service.getString(session);

        // Asserts
        Assert.assertNotNull(actual);
        Assert.assertEquals("", actual);
    }

    @Test
    public void getString_returnsExpectedString()
    {
        // Arranges
        String string = "aa";
        Map<String, String> properties = new HashMap<String, String>();
        properties.put("ghost_string", string);
        SessionBean session = new SessionBean();
        session.setProperties(properties);

        List<IDictionaryProvider> dictionaries = new ArrayList<IDictionaryProvider>();
        dictionaries.add(dictionaryProvider);

        List<IGhostStrategy> strategies = new ArrayList<IGhostStrategy>();
        strategies.add(ghostStrategy);

        GhostService service = new GhostService(dictionaries, strategies);

        // Acts
        String actual = service.getString(session);

        // Asserts
        Assert.assertNotNull(actual);
        Assert.assertEquals(string, actual);
    }

    @Test
    public void checkLetter_returnsTrue()
    {
        // Arranges
        String newLetter = "n";
        String lang = "en-ES";
        SessionBean session = new SessionBean();
        session.setLang(lang);

        Mockito.when(dictionaryProvider.isDictionaryForLang(lang)).thenReturn(true);
        Mockito.when(dictionaryProvider.checkLetter(newLetter)).thenReturn(true);
        List<IDictionaryProvider> dictionaries = new ArrayList<IDictionaryProvider>();
        dictionaries.add(dictionaryProvider);

        List<IGhostStrategy> strategies = new ArrayList<IGhostStrategy>();
        strategies.add(ghostStrategy);

        GhostService service = new GhostService(dictionaries, strategies);

        // Acts
        boolean actual = service.checkLetter(newLetter, session);

        // Asserts
        Assert.assertTrue(actual);
    }
}
