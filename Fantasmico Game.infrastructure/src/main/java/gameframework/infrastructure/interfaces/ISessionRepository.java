package gameframework.infrastructure.interfaces;

import gameframework.transversal.models.SessionBean;

/**
 * This repository will retrieve and store information about sessions in the system.
 * 
 * @author José Carvajal
 */
public interface ISessionRepository
{
    /**
     * This method stores the session information in a persistence media.
     * 
     * @param token
     * @param properties
     */
    public boolean storeSession(SessionBean session);

    /**
     * This method deletes the session information.
     * 
     * @param token
     * @param properties
     */
    public boolean deleteSession(String game, String token);

    /**
     * This method retrieves the session information from the persistence media.
     * 
     * @param token
     * @return
     */
    public SessionBean retrieveSession(String game, String token);

    /**
     * Reset all active sessions.
     */
    public void resetSessions(String game);

}
