package gameframework.infrastructure;

import gameframework.infrastructure.interfaces.ISessionRepository;
import gameframework.transversal.logging.GameLogger;
import gameframework.transversal.models.SessionBean;
import gameframework.transversal.utils.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * The information to be persisted a simple file storage.
 * 
 * @author José Carvajal
 */
public class FileSessionRepository implements ISessionRepository
{
    private static final String FILE_SESSION_FILE_EXTENSION = ".tmp";

    private final int hours;

    /**
     * Initializes a new instance of the FileSessionRepository class.
     * 
     * @param hours from now whereas a session is considered as expired.
     */
    public FileSessionRepository(int hours)
    {
        this.hours = hours;
    }

    /**
     * @return Hours when a session is active.
     */
    public int getHours()
    {
        return hours;
    }

    /**
     * {@inheritDoc}
     * 
     * @see gameframework.infrastructure.interfaces.ISessionRepository#storeSession(java.lang.String, java.util.Map)
     */
    public boolean storeSession(SessionBean session)
    {
        List<SessionBean> sessions = this.getSessions(session.getGameName());

        if (sessions == null) {
            sessions = new ArrayList<SessionBean>();
        }

        sessions.add(session);

        return this.saveSessions(session.getGameName(), sessions);
    }

    /**
     * {@inheritDoc}
     * 
     * @see gameframework.infrastructure.interfaces.ISessionRepository#retrieveSession(java.lang.String)
     */
    public SessionBean retrieveSession(String game, String token)
    {
        SessionBean session = null;
        List<SessionBean> sessions = this.getSessions(game);

        if (sessions != null) {
            int index = 0;
            boolean found = false;

            while (index < sessions.size() && !found) {
                SessionBean current = sessions.get(index);

                if (current.getToken() != null && current.getToken().equals(token)) {
                    session = current;

                    found = true;
                }

                index++;
            }
        }

        return session;
    }

    /**
     * {@inheritDoc}
     * 
     * @see gameframework.infrastructure.interfaces.ISessionRepository#deleteSession(java.lang.String)
     */
    public boolean deleteSession(String game, String token)
    {
        int indexToRemove = -1;
        List<SessionBean> sessions = this.getSessions(game);

        if (sessions != null) {
            int index = 0;
            boolean found = false;

            while (index < sessions.size() && !found) {
                SessionBean current = sessions.get(index);

                if (current.getToken() != null && current.getToken().equals(token)) {
                    indexToRemove = index;

                    found = true;
                }

                index++;
            }

            if (found) {
                sessions.remove(indexToRemove);

                this.saveSessions(game, sessions);

                GameLogger.debug(String.format("Session deleted: %s.", token));
            }
        }

        return indexToRemove >= 0;
    }

    /**
     * {@inheritDoc}
     * 
     * @see gameframework.infrastructure.interfaces.ISessionRepository#resetSessions
     */
    public synchronized void resetSessions(String game)
    {
        File file = new File(this.getFileNameForGame(game));
        if (file.exists()) {
            file.delete();
        }

        GameLogger.debug(String.format("All sessions were reset."));
    }

    /**
     * @return Retrieve all sessions stored in file.
     */
    private synchronized List<SessionBean> getSessions(String game)
    {
        List<SessionBean> sessions = null;
        File file = new File(this.getFileNameForGame(game));
        if (file.exists()) {

            ObjectInputStream in = null;
            FileInputStream fileIn = null;
            try {
                fileIn = new FileInputStream(file);
                in = new ObjectInputStream(fileIn);
                sessions = (List<SessionBean>) in.readObject();
            } catch (Exception ex) {
                GameLogger.error("Error retrieving sessions", ex);
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
    private void updateExpiredSessions(String game, List<SessionBean> sessions)
    {
        if (sessions != null) {
            // Date comparison
            Calendar calendar = new GregorianCalendar();
            calendar.add(Calendar.HOUR, (-1) * this.hours);
            Date now = calendar.getTime();

            List<SessionBean> expired = new ArrayList<SessionBean>();

            for (SessionBean session : sessions) {
                if (session.getStartedAt() == null || session.getStartedAt().before(now)) {
                    expired.add(session);
                }
            }

            // Delete
            for (SessionBean session : expired) {
                sessions.remove(session);

                GameLogger.debug(String.format("Session expired: %s.", session.toString()));
            }

            // Update
            this.saveSessions(game, sessions);
        }
    }

    /**
     * @param sessions
     */
    private synchronized boolean saveSessions(String game, List<SessionBean> sessions)
    {
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
            GameLogger.error("Error storing sessions", ex);
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
    private String getFileNameForGame(String game)
    {
        return game + FILE_SESSION_FILE_EXTENSION;
    }

}
