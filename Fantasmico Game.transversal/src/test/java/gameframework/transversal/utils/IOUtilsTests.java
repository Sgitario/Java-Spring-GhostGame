package gameframework.transversal.utils;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.log4j.Appender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.spi.LoggingEvent;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * @version $Id$
 */
@RunWith(MockitoJUnitRunner.class)
public class IOUtilsTests
{

    @Mock
    private InputStream inputStream;

    @Mock
    private OutputStream outputStream;

    private Appender appenderMock;

    @Before
    public void setup()
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
    public void closeInputStream_closeCalled() throws IOException
    {
        // Arrange

        // Act
        IOUtils.closeInputStream(inputStream);

        // Assert
        Mockito.verify(inputStream).close();
    }

    @Test
    public void closeInputStream_throwsException() throws IOException
    {
        // Arrange
        Mockito.doThrow(new IOException()).when(inputStream).close();

        // Act
        IOUtils.closeInputStream(inputStream);

        ArgumentCaptor<LoggingEvent> arguments = ArgumentCaptor.forClass(LoggingEvent.class);
        verify(appenderMock).doAppend(arguments.capture());

        // Assert
        Mockito.verify(inputStream).close();
        org.junit.Assert.assertEquals(arguments.getValue().getLevel(), Level.ERROR);
    }

    @Test
    public void closeOutputStream_closeCalled() throws IOException
    {
        // Arrange

        // Act
        IOUtils.closeOutputStream(outputStream);

        // Assert
        Mockito.verify(outputStream).close();
    }

    @Test
    public void closeOutputStream_throwsException() throws IOException
    {
        // Arrange
        Mockito.doThrow(new IOException()).when(outputStream).close();

        // Act
        IOUtils.closeOutputStream(outputStream);

        ArgumentCaptor<LoggingEvent> arguments = ArgumentCaptor.forClass(LoggingEvent.class);
        verify(appenderMock).doAppend(arguments.capture());

        // Assert
        Mockito.verify(outputStream).close();
        org.junit.Assert.assertEquals(arguments.getValue().getLevel(), Level.ERROR);
    }

}
