package gameframework.services.ghost.strategies;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.BasicConfigurator;
import org.junit.Before;
import org.junit.Test;

/**
 * @version $Id$
 */
public class OptimalGhostStrategyTests
{

    @Before
    public void setupLogging()
    {
        BasicConfigurator.configure();
    }

    @Test
    public void isStrategyForLevel_returnsTrue()
    {
        // Arrange
        Integer level = 1;
        OptimalGhostStrategy strategy = new OptimalGhostStrategy();

        // Act
        boolean actual = strategy.isStrategyForLevel(level);

        // Assert
        org.junit.Assert.assertTrue(actual);
    }

    @Test
    public void isStrategyForLevel_returnsFalse()
    {
        // Arrange
        Integer level = 2;
        OptimalGhostStrategy strategy = new OptimalGhostStrategy();

        // Act
        boolean actual = strategy.isStrategyForLevel(level);

        // Assert
        org.junit.Assert.assertFalse(actual);
    }

    @Test
    public void isStrategyForLevel_returnsFalseWhenNull()
    {
        // Arrange
        Integer level = null;
        OptimalGhostStrategy strategy = new OptimalGhostStrategy();

        // Act
        boolean actual = strategy.isStrategyForLevel(level);

        // Assert
        org.junit.Assert.assertFalse(actual);
    }

    @Test
    public void addLetter_returnsWinningMovement()
    {
        // Arrange
        String expected = "e";
        String currentString = "aah";
        List<String> choices = new ArrayList<String>();
        choices.add("aahed");
        choices.add("aahing");
        OptimalGhostStrategy strategy = new OptimalGhostStrategy();

        // Act
        String actual = strategy.addLetter(choices, currentString);

        // Assert
        org.junit.Assert.assertEquals(expected, actual);
    }

    @Test
    public void addLetter_returnsWinningMovementOtherApproach()
    {
        // Arrange
        String expected = "b";
        String currentString = "aah";
        List<String> choices = new ArrayList<String>();
        choices.add("aahed");
        choices.add("aahedg");
        choices.add("aahing");
        choices.add("aahba");
        choices.add("aahbb");
        OptimalGhostStrategy strategy = new OptimalGhostStrategy();

        // Act
        String actual = strategy.addLetter(choices, currentString);

        // Assert
        org.junit.Assert.assertEquals(expected, actual);
    }

    @Test
    public void addLetter_returnsLongestMovement()
    {
        // Arrange
        String expected = "e";
        String currentString = "aah";
        List<String> choices = new ArrayList<String>();
        choices.add("aahededd");
        choices.add("aahing");
        OptimalGhostStrategy strategy = new OptimalGhostStrategy();

        // Act
        String actual = strategy.addLetter(choices, currentString);

        // Assert
        org.junit.Assert.assertEquals(expected, actual);
    }

}
