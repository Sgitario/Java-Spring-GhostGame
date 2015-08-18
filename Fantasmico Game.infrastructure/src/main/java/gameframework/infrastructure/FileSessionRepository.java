package gameframework.infrastructure;

import gameframework.infrastructure.interfaces.ISessionRepository;
import gameframework.transversal.models.SessionBean;
import gameframework.transversal.utils.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.LinkedHashSet;
import java.util.Set;

import org.apache.log4j.Logger;

/**
 * The information to be persisted a simple file storage.
 * 
 * @author José Carvajal
 */
public class FileSessionRepository implements ISessionRepository {
	private static final String FILE_SESSION_FILE_EXTENSION = ".tmp";
	private static final Logger LOG = Logger
			.getLogger(FileSessionRepository.class);

	private final int hours;

	/**
	 * Initializes a new instance of the FileSessionRepository class.
	 * 
	 * @param hours
	 *            from now whereas a session is considered as expired.
	 */
	public FileSessionRepository(int hours) {
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
		Set<SessionBean> sessions = this.getSessions(session.getGameName());

		if (sessions == null) {
			sessions = new LinkedHashSet<SessionBean>();
		}

		sessions.add(session);

		return this.saveSessions(session.getGameName(), sessions);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see gameframework.infrastructure.interfaces.ISessionRepository#retrieveSession(java.lang.String)
	 */
	public SessionBean retrieveSession(String game, String token) {
		SessionBean session = null;
		Set<SessionBean> sessions = this.getSessions(game);

		if (sessions != null) {
			for (SessionBean current : sessions) {
				if (current.getToken() != null
						&& current.getToken().equals(token)) {
					session = current;

					break;
				}
			}
		}

		return session;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see gameframework.infrastructure.interfaces.ISessionRepository#deleteSession(java.lang.String)
	 */
	public boolean deleteSession(String game, String token) {
		SessionBean sessionToRemove = new SessionBean();
		sessionToRemove.setGameName(game);
		sessionToRemove.setToken(token);
		
		Set<SessionBean> sessions = this.getSessions(game);
		
		boolean deleted = sessions.remove(sessionToRemove);
		this.saveSessions(game, sessions);

		return deleted;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see gameframework.infrastructure.interfaces.ISessionRepository#resetSessions
	 */
	public synchronized void resetSessions(String game) {
		File file = new File(this.getFileNameForGame(game));
		if (file.exists()) {
			file.delete();
		}

		LOG.debug(String.format("All sessions were reset."));
	}

	/**
	 * @return Retrieve all sessions stored in file.
	 */
	@SuppressWarnings("unchecked")
	private synchronized Set<SessionBean> getSessions(String game) {
		Set<SessionBean> sessions = null;
		File file = new File(this.getFileNameForGame(game));
		if (file.exists()) {

			ObjectInputStream in = null;
			FileInputStream fileIn = null;
			try {
				fileIn = new FileInputStream(file);
				in = new ObjectInputStream(fileIn);
				sessions = (Set<SessionBean>) in.readObject();
			} catch (Exception ex) {
				LOG.error("Error retrieving sessions", ex);
			} finally {
				IOUtils.closeInputStream(in);
				IOUtils.closeInputStream(fileIn);
			}

			this.updateExpiredSessions(game, sessions);
		}

		return sessions;
	}

	/**
	 * Method to check and update expired sessions.
	 * 
	 * @param sessions
	 */
	private void updateExpiredSessions(String game, Set<SessionBean> sessions) {
		if (sessions != null) {
			// Date comparison
			Calendar calendar = new GregorianCalendar();
			calendar.add(Calendar.HOUR, (-1) * this.hours);
			Date now = calendar.getTime();

			Set<SessionBean> expired = new LinkedHashSet<SessionBean>();

			for (SessionBean session : sessions) {
				if (session.getStartedAt() == null
						|| session.getStartedAt().before(now)) {
					expired.add(session);
				}
			}

			// Delete
			for (SessionBean session : expired) {
				sessions.remove(session);

				LOG.debug(String.format("Session expired: %s.",
						session.toString()));
			}

			// Update
			this.saveSessions(game, sessions);
		}
	}

	/**
	 * @param sessions
	 */
	private synchronized boolean saveSessions(String game,
			Set<SessionBean> sessions) {
		boolean stored = false;
		File file = new File(this.getFileNameForGame(game));
		ObjectOutputStream out = null;
		FileOutputStream fileOut = null;
		try {
			if (file.exists()) {
				file.delete();
			}

			fileOut = new FileOutputStream(file);
			out = new ObjectOutputStream(fileOut);
			out.writeObject(sessions);

			stored = true;
		} catch (IOException ex) {
			LOG.error("Error storing sessions", ex);
		} finally {
			IOUtils.closeOutputStream(out);
			IOUtils.closeOutputStream(fileOut);
		}

		return stored;
	}

	/**
	 * Calculates the session file.
	 * 
	 * @param game
	 * @return
	 */
	private String getFileNameForGame(String game) {
		return game + FILE_SESSION_FILE_EXTENSION;
	}

}
