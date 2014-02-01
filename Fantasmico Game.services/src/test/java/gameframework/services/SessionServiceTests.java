package gameframework.services;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import gameframework.infrastructure.interfaces.ISessionRepository;
import gameframework.transversal.models.SessionBean;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Appender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.spi.LoggingEvent;
import org.junit.After;
import org.junit.Assert;
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
public class SessionServiceTests
{
    @Mock
    private ISessionRepository sessionRepository;

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
    public void registerSession_getTokenNotNull()
    {
        // Arrange
        boolean expected = true;
        String game = "ghost";
        Integer level = 1;
        String lang = "es-ES";
        Map<String, String> properties = new HashMap<String, String>();
        properties.put("string", "aah");
        Mockito.when(sessionRepository.storeSession(Mockito.<SessionBean> any())).thenReturn(expected);

        SessionService service = new SessionService(sessionRepository);

        // Act
        String token = service.registerSession(game, lang, level, properties);

        // Assert
        Assert.assertNotNull(token);
    }

    @Test
    public void registerSession_getTokenNull()
    {
        // Arrange
        boolean expected = false;
        String game = "ghost";
        Integer level = 1;
        String lang = "es-ES";
        Map<String, String> properties = new HashMap<String, String>();
        properties.put("string", "aah");
        Mockito.when(sessionRepository.storeSession(Mockito.<SessionBean> any())).thenReturn(expected);

        SessionService service = new SessionService(sessionRepository);

        // Act
        String token = service.registerSession(game, lang, level, properties);

        ArgumentCaptor<LoggingEvent> arguments = ArgumentCaptor.forClass(LoggingEvent.class);
        verify(appenderMock).doAppend(arguments.capture());

        // Assert
        Assert.assertNull(token);
        org.junit.Assert.assertEquals(arguments.getValue().getLevel(), Level.WARN);
    }

    @Test
    public void getSession_getPropertiesNotNull()
    {
        // Arrange
        boolean expected = true;
        String game = "ghost";
        Integer level = 1;
        String lang = "es-ES";
        Map<String, String> properties = new HashMap<String, String>();
        properties.put("string", "aah");

        SessionBean session = new SessionBean();
        session.setProperties(properties);

        Mockito.when(sessionRepository.storeSession(Mockito.<SessionBean> any())).thenReturn(expected);

        SessionService service = new SessionService(sessionRepository);
        String token = service.registerSession(game, lang, level, properties);

        Mockito.when(sessionRepository.retrieveSession(game, token)).thenReturn(session);

        // Act
        SessionBean actual = service.getSession(game, token);

        // Assert
        Assert.assertNotNull(token);
        Assert.assertNotNull(actual);
        Assert.assertEquals("aah", actual.getProperties().get("string"));
    }

    @Test
    public void getSession_getPropertiesNull()
    {
        // Arrange
        boolean expected = true;
        String game = "ghost";
        Integer level = 1;
        String lang = "es-ES";
        Map<String, String> properties = new HashMap<String, String>();
        properties.put("string", "aah");

        SessionBean session = new SessionBean();
        session.setProperties(properties);

        Mockito.when(sessionRepository.storeSession(Mockito.<SessionBean> any())).thenReturn(expected);

        SessionService service = new SessionService(sessionRepository);
        String token = service.registerSession(game, lang, level, properties);

        Mockito.when(sessionRepository.retrieveSession(game, token)).thenReturn(session);

        // Act
        SessionBean actual = service.getSession(game, token + "NotExisting");

        // Assert
        Assert.assertNotNull(token);
        Assert.assertNull(actual);
    }

    @Test
    public void unregisterSession_returnsTrue()
    {
        // Arrange
        boolean expected = true;
        String game = "ghost";
        Integer level = 1;
        String lang = "es-ES";
        Map<String, String> properties = new HashMap<String, String>();
        properties.put("string", "aah");

        SessionBean session = new SessionBean();
        session.setProperties(properties);

        Mockito.when(sessionRepository.storeSession(Mockito.<SessionBean> any())).thenReturn(expected);

        SessionService service = new SessionService(sessionRepository);
        String token = service.registerSession(game, lang, level, properties);

        Mockito.when(sessionRepository.retrieveSession(game, token)).thenReturn(session);
        Mockito.when(sessionRepository.deleteSession(game, token)).thenReturn(true);

        // Act
        boolean actual = service.unregisterSession(game, token);

        // Assert
        Assert.assertNotNull(token);
        Assert.assertTrue(actual);
    }
}
