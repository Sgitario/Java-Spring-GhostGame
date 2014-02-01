package gameframework.infrastructure;

import gameframework.transversal.models.SessionBean;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.BasicConfigurator;
import org.junit.Before;
import org.junit.Test;

/**
 * @version $Id$
 */
public class FileSessionRepositoryTests
{
    private static final int HOURS_ACTIVE = 1;

    @Before
    public void setupLogging()
    {
        BasicConfigurator.configure();
    }

    /**
     * Integration test of the file session repository. Methods: StoreSession and retrieveSession.
     */
    @Test
    public void integrationTests_checkIfStored()
    {
        // Arrange
        Date started = new Date();
        String token = "INTEGRATION-TEST1";
        String gameName = "GHOST";
        Integer level = 1;
        String lang = "en-EN";
        Map<String, String> properties = new HashMap<String, String>();
        properties.put("key", "value");

        SessionBean session = new SessionBean();
        session.setGameName(gameName);
        session.setLevel(level);
        session.setLang(lang);
        session.setToken(token);
        session.setStartedAt(started);
        session.setProperties(properties);

        FileSessionRepository repository = new FileSessionRepository(HOURS_ACTIVE);

        // Acts
        repository.resetSessions(gameName);
        repository.storeSession(session);
        SessionBean actual = repository.retrieveSession(gameName, token);
        repository.resetSessions(gameName);

        // Asserts
        org.junit.Assert.assertNotNull(actual);

        // Asserts values
        org.junit.Assert.assertEquals(actual.getGameName(), gameName);
        org.junit.Assert.assertEquals(actual.getLevel(), level);
        org.junit.Assert.assertEquals(actual.getLang(), lang);
        org.junit.Assert.assertEquals(actual.getToken(), token);
        org.junit.Assert.assertEquals(actual.getStartedAt(), started);
        org.junit.Assert.assertEquals(actual.getProperties().get("key"), "value");
    }

    /**
     * Integration test of the file session repository when a session is deleted. Methods: StoreSession, DeleteSession
     * and retrieveSession.
     */
    @Test
    public void integrationTests_checkIfDeleted()
    {
        // Arrange
        String gameName = "GHOST";
        String token = "INTEGRATION-TEST2";
        SessionBean session = createSessionMock(token, new Date());

        FileSessionRepository repository = new FileSessionRepository(HOURS_ACTIVE);

        // Acts
        repository.resetSessions(gameName);
        repository.storeSession(session);
        repository.deleteSession(gameName, token);
        SessionBean actual = repository.retrieveSession(gameName, token);
        repository.resetSessions(gameName);

        // Asserts
        org.junit.Assert.assertNull(actual);
    }

    /**
     * Integration test of the file session repository when a session is expired, get session method must not retrieve
     * it.
     */
    @Test
    public void integrationTests_checkIfExpired()
    {
        // Arrange
        String gameName = "GHOST";

        // Session expired
        Calendar date = new GregorianCalendar();
        date.add(Calendar.DAY_OF_MONTH, -1);
        String tokenExpired = "INTEGRATION-TEST1";
        SessionBean sessionExpired = createSessionMock(tokenExpired, date.getTime());

        // Session valid
        String tokenValid = "INTEGRATION-TEST2";
        SessionBean sessionValid = createSessionMock(tokenValid, new Date());

        FileSessionRepository repository = new FileSessionRepository(HOURS_ACTIVE);

        // Acts
        repository.resetSessions(gameName);
        repository.storeSession(sessionExpired);
        repository.storeSession(sessionValid);
        SessionBean actualExpired = repository.retrieveSession(gameName, tokenExpired);
        SessionBean actualValid = repository.retrieveSession(gameName, tokenValid);
        repository.resetSessions(gameName);

        // Asserts
        org.junit.Assert.assertNull(actualExpired);
        org.junit.Assert.assertNotNull(actualValid);
    }

    /**
     * Creation of mocking session.
     * 
     * @param token
     * @param started
     * @return
     */
    private static SessionBean createSessionMock(String token, Date started)
    {
        String gameName = "GHOST";
        Integer level = 1;
        String lang = "en-EN";
        Map<String, String> properties = new HashMap<String, String>();
        properties.put("key", "value");

        SessionBean session = new SessionBean();
        session.setGameName(gameName);
        session.setLevel(level);
        session.setLang(lang);
        session.setToken(token);
        session.setStartedAt(started);
        session.setProperties(properties);

        return session;
    }
}
