package gameframework.transversal.logging.tests;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import gameframework.transversal.logging.GameLogger;

import org.apache.log4j.Appender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.spi.LoggingEvent;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

/**
 * @version $Id$
 */
public class GameLoggerTests
{
    private Appender appenderMock;

    @Before
    public void setupAppender()
    {
        appenderMock = mock(Appender.class);
        Logger.getRootLogger().addAppender(appenderMock);
    }

    @After
    public void removeAppender()
    {
        Logger.getRootLogger().removeAppender(appenderMock);
    }

    @Test
    public void error_checkWriteInLogger()
    {
        // Arrange
        String message = "TEST";
        Exception exp = new NullPointerException();

        // Act
        GameLogger.error(message, exp);

        // Assert
        ArgumentCaptor<LoggingEvent> arguments = ArgumentCaptor.forClass(LoggingEvent.class);
        verify(appenderMock).doAppend(arguments.capture());
        org.junit.Assert.assertEquals(arguments.getValue().getLevel(), Level.ERROR);
    }

    @Test
    public void warn_checkWriteInLogger()
    {
        // Arrange
        String message = "TEST1";

        // Act
        GameLogger.warn(message);

        // Assert
        ArgumentCaptor<LoggingEvent> arguments = ArgumentCaptor.forClass(LoggingEvent.class);
        verify(appenderMock).doAppend(arguments.capture());
        org.junit.Assert.assertEquals(arguments.getValue().getLevel(), Level.WARN);
        org.junit.Assert.assertEquals(arguments.getValue().getMessage(), message);
    }

    @Test
    public void debug_checkWriteInLogger()
    {
        // Arrange
        String message = "TEST1";

        // Act
        GameLogger.debug(message);

        // Assert
        ArgumentCaptor<LoggingEvent> arguments = ArgumentCaptor.forClass(LoggingEvent.class);
        verify(appenderMock).doAppend(arguments.capture());
        org.junit.Assert.assertEquals(arguments.getValue().getLevel(), Level.DEBUG);
        org.junit.Assert.assertEquals(arguments.getValue().getMessage(), message);
    }

    @Test
    public void info_checkWriteInLogger()
    {
        // Arrange
        String message = "TEST1";

        // Act
        GameLogger.info(message);

        // Assert
        ArgumentCaptor<LoggingEvent> arguments = ArgumentCaptor.forClass(LoggingEvent.class);
        verify(appenderMock).doAppend(arguments.capture());
        org.junit.Assert.assertEquals(arguments.getValue().getLevel(), Level.INFO);
        org.junit.Assert.assertEquals(arguments.getValue().getMessage(), message);
    }

}
