package gameframework.services;

import gameframework.infrastructure.interfaces.ISessionRepository;
import gameframework.services.interfaces.ISessionService;
import gameframework.transversal.models.SessionBean;

import java.util.Date;
import java.util.Map;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

/**
 * Session service objectives: - Register sessions of user - Generate tokens -
 * Recover sessions by tokens
 * 
 * @author José Carvajal
 */
@Service
public class SessionService implements ISessionService {

	private static final Logger LOG = Logger.getLogger(SessionService.class);

	private ISessionRepository sessionRepository;

	/**
	 * Initializes a new instance of the SessionService class.
	 * 
	 * @param repository
	 */
	public SessionService(ISessionRepository sessionRepository) {
		this.sessionRepository = sessionRepository;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see gameframework.services.interfaces.ISessionService#registerSession(java.util.Map)
	 */
	public String registerSession(String game, String lang, Integer level,
			Map<String, String> properties) {
		// Generate token
		String token = UUID.randomUUID().toString();

		// Create instance
		SessionBean session = new SessionBean();
		session.setGameName(game);
		session.setLang(lang);
		session.setLevel(level);
		session.setProperties(properties);
		session.setToken(token);
		session.setStartedAt(new Date());

		// Store
		if (this.sessionRepository.storeSession(session)) {
			LOG.debug(String.format("Session was properly stored: %s.",
					session.toString()));
		} else {
			// Session not stored
			LOG.warn(String.format("Session was not stored properly: %s.",
					session.toString()));

			token = null;
		}

		return token;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see gameframework.services.interfaces.ISessionService#unregisterSession(java.lang.String)
	 */
	public boolean unregisterSession(String game, String token) {
		return this.sessionRepository.deleteSession(game, token);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see gameframework.services.interfaces.ISessionService#hasSession(java.lang.String)
	 */
	public SessionBean getSession(String game, String token) {
		return sessionRepository.retrieveSession(game, token);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see gameframework.services.interfaces.ISessionService#updateSession(gameframework.transversal.models.SessionBean)
	 */
	public void updateSession(SessionBean session) {
		sessionRepository.deleteSession(session.getGameName(),
				session.getToken());

		sessionRepository.storeSession(session);

		LOG.debug(String.format("Session was properly updated: %s.",
				session.toString()));
	}

}
