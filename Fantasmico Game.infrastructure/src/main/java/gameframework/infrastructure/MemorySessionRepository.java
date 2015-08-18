package gameframework.infrastructure;

import gameframework.infrastructure.interfaces.ISessionRepository;
import gameframework.transversal.models.SessionBean;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;

/**
 * The information to be persisted a simple file storage.
 * 
 * @author José Carvajal
 */
public class MemorySessionRepository implements ISessionRepository {
	private static final Map<String, SessionBean> sessions = new ConcurrentHashMap<String, SessionBean>();
	private static final Logger LOG = Logger
			.getLogger(MemorySessionRepository.class);

	private final int hours;

	/**
	 * Initializes a new instance of the FileSessionRepository class.
	 * 
	 * @param hours
	 *            from now whereas a session is considered as expired.
	 */
	public MemorySessionRepository(int hours) {
		this.hours = hours;
	}

	/**
	 * @return Hours when a session is active.
	 */
	public int getHours() {
		return hours;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see gameframework.infrastructure.interfaces.ISessionRepository#storeSession(java.lang.String,
	 *      java.util.Map)
	 */
	public boolean storeSession(SessionBean session) {
		sessions.put(session.getToken(), session);

		this.updateExpiredSessions();

		return true;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see gameframework.infrastructure.interfaces.ISessionRepository#retrieveSession(java.lang.String)
	 */
	public SessionBean retrieveSession(String game, String token) {
		return sessions.get(token);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see gameframework.infrastructure.interfaces.ISessionRepository#deleteSession(java.lang.String)
	 */
	public boolean deleteSession(String game, String token) {
		sessions.remove(token);

		return true;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see gameframework.infrastructure.interfaces.ISessionRepository#resetSessions
	 */
	public synchronized void resetSessions(String game) {
		sessions.clear();

		LOG.debug(String.format("All sessions were reset."));
	}

	/**
	 * Method to check and update expired sessions.
	 * 
	 * @param sessions
	 */
	private void updateExpiredSessions() {
		if (sessions != null) {
			// Date comparison
			Calendar calendar = new GregorianCalendar();
			calendar.add(Calendar.HOUR, (-1) * this.hours);
			Date now = calendar.getTime();

			List<SessionBean> expired = new ArrayList<SessionBean>();

			for (SessionBean session : sessions.values()) {
				if (session.getStartedAt() == null
						|| session.getStartedAt().before(now)) {
					expired.add(session);
				}
			}

			// Delete
			for (SessionBean session : expired) {
				sessions.remove(session.getToken());

				LOG.debug(String.format("Session expired: %s.",
						session.toString()));
			}
		}
	}

}
