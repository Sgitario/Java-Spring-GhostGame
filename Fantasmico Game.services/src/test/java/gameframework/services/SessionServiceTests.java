package gameframework.services;

import gameframework.infrastructure.interfaces.ISessionRepository;
import gameframework.transversal.models.SessionBean;

import java.util.HashMap;
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
public class SessionServiceTests {
	@Mock
	private ISessionRepository sessionRepository;
	
	private SessionService service;
	private String game = "ghost";
	private int level = 1;
	private String lang = "es-ES";
	private String string = "aah";
	private String actualToken;
	private boolean actualSessionUnregistered;
	private SessionBean actualSession;

	@Before
	public void setup() {
		service = new SessionService(sessionRepository);
	}

	@Test
	public void registerSession_getTokenNotNull() {
		// Arrange
		givenStoreSessionResult(true);

		// Act
		whenRegisterSession();

		// Assert
		thenTokenNotNull();
	}

	@Test
	public void registerSession_getTokenNull() {
		// Arrange
		givenStoreSessionResult(false);

		// Act
		whenRegisterSession();

		// Assert
		thenTokenNull();
	}

	@Test
	public void getSession_getPropertiesNotNull() {
		// Arrange
		givenStoreSessionResult(true);
		givenRegisteredSession();

		// Act
		whenGetSession();

		// Assert
		thenSessionExpected();
	}

	@Test
	public void unregisterSession_returnsTrue() {
		// Arrange
		givenStoreSessionResult(true);
		givenRegisteredSession();
		givenDeleteSessionResult(true);

		// Act
		whenUnregisterSession();

		// Assert
		thenSessionIsUnregistered();
	}
	
	/**
	 * GIVENs
	 */
	
	private void givenStoreSessionResult(boolean expected) {
		Mockito.when(sessionRepository.storeSession(Mockito.<SessionBean> any()))
				.thenReturn(expected);
	}
	
	private void givenDeleteSessionResult(boolean expected) {
		Mockito.when(sessionRepository.deleteSession(game, actualToken)).thenReturn(
				expected);
	}
	
	private void givenRegisteredSession() {
		Map<String, String> properties = new HashMap<String, String>();
		properties.put("string", string);

		SessionBean session = new SessionBean();
		session.setProperties(properties);
		
		whenRegisterSession();
		Mockito.when(sessionRepository.retrieveSession(game, actualToken)).thenReturn(session);
	}
	
	/**
	 * WHENS
	 */
	
	private void whenGetSession() {
		actualSession = service.getSession(game, actualToken);
	}
	
	private void whenRegisterSession() {
		Map<String, String> properties = new HashMap<String, String>();
		properties.put("string", string);
		actualToken = service.registerSession(game, lang, level, properties);
	}
	
	private void whenUnregisterSession() {
		actualSessionUnregistered = service.unregisterSession(game, actualToken);
	}
	
	/**
	 * THENS
	 */
	
	private void thenTokenNotNull() {
		Assert.assertNotNull(actualToken);
	}
	
	private void thenTokenNull() {
		Assert.assertNull(actualToken);
	}
	
	private void thenSessionExpected() {
		Assert.assertNotNull(actualSession);
		Assert.assertEquals(actualSession.getProperties().get("string"), string);
	}
	
	private void thenSessionIsUnregistered() {
		Assert.assertTrue(actualSessionUnregistered);
	}
}
