package gameframework.infrastructure;

import static org.junit.Assert.*;
import gameframework.transversal.models.SessionBean;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.log4j.BasicConfigurator;
import org.junit.Before;
import org.junit.Test;

public class FileSessionRepositoryTests {
	private static final int HOURS_ACTIVE = 1;
	
	private FileSessionRepository repository;
	private String gameName;
	private boolean wasDeleted;
	private SessionBean expectedSession;
	private SessionBean actualSession;

	@Before
	public void setup() {
		BasicConfigurator.configure();
		
		repository = new FileSessionRepository(HOURS_ACTIVE);
	}

	/**
	 * Integration test of the file session repository. Methods: StoreSession
	 * and retrieveSession.
	 */
	@Test
	public void integrationTests_checkIfStored() {
		givenAGameName();
		givenASessionBeanWithToken();

		// Acts
		whenResetSessionsForGame();
		whenStoreSession();
		whenRetrieveSessionByToken();
		whenResetSessionsForGame();

		// Asserts
		thenActualSessionIsExpected();
	}

	/**
	 * Integration test of the file session repository when a session is
	 * deleted. Methods: StoreSession, DeleteSession and retrieveSession.
	 */
	@Test
	public void integrationTests_checkIfDeleted() {
		// Arrange
		givenAGameName();
		givenASessionBeanWithToken();

		// Acts
		whenResetSessionsForGame();
		whenStoreSession();
		whenDeleteSession();
		thenSessionWasDeleted();
		whenRetrieveSessionByToken();
		whenResetSessionsForGame();

		// Asserts
		thenActualSessionIsNull();
	}

	/**
	 * Integration test of the file session repository when a session is
	 * expired, get session method must not retrieve it.
	 */
	@Test
	public void integrationTests_checkIfExpired() {
		// Arrange
		givenAGameName();
		givenAnExpiredSessionBeanWithToken();

		// Acts
		whenResetSessionsForGame();
		whenStoreSession();
		whenRetrieveSessionByToken();
		whenResetSessionsForGame();

		// Asserts
		thenActualSessionIsNull();
	}
	
	/**
	 * GIVENS
	 */
	private void givenAGameName() {
		gameName = UUID.randomUUID().toString();
	}
	
	private void givenAnExpiredSessionBeanWithToken() {
		givenASessionBeanWithToken();
		
		Calendar date = new GregorianCalendar();
		date.add(Calendar.DAY_OF_MONTH, -1);
		expectedSession.setStartedAt(date.getTime());
	}
	
	private void givenASessionBeanWithToken() {
		// Arrange
		Date started = new Date();
		String token = "INTEGRATION-TEST1";
		Integer level = 1;
		String lang = "en-EN";
		Map<String, String> properties = new HashMap<String, String>();
		properties.put("key", "value");

		expectedSession = new SessionBean();
		expectedSession.setGameName(gameName);
		expectedSession.setLevel(level);
		expectedSession.setLang(lang);
		expectedSession.setToken(token);
		expectedSession.setStartedAt(started);
		expectedSession.setProperties(properties);
	}
	
	/**
	 * WHEN
	 */
	private void whenResetSessionsForGame() {
		repository.resetSessions(gameName);
	}
	
	private void whenStoreSession() {
		repository.storeSession(expectedSession);
	}
	
	private void whenRetrieveSessionByToken() {
		actualSession = repository.retrieveSession(gameName, expectedSession.getToken());
	}
	
	private void whenDeleteSession() {
		wasDeleted = repository.deleteSession(gameName, expectedSession.getToken());
	}
	
	/**
	 * THEN
	 */
	
	private void thenActualSessionIsExpected() {
		assertNotNull(actualSession);
		assertEquals(actualSession.getGameName(), gameName);
		assertEquals(actualSession.getLevel(), expectedSession.getLevel());
		assertEquals(actualSession.getLang(), expectedSession.getLang());
		assertEquals(actualSession.getToken(), expectedSession.getToken());
		assertEquals(actualSession.getStartedAt(), expectedSession.getStartedAt());
		assertEquals(actualSession.getProperties().get("key"),
				expectedSession.getProperties().get("key"));
	}
	
	private void thenActualSessionIsNull() {
		assertNull(actualSession);
	}
	
	private void thenSessionWasDeleted() {
		assertTrue(wasDeleted);
	}
}
