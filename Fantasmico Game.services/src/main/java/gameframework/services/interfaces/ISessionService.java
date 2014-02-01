package gameframework.services.interfaces;

import gameframework.transversal.models.SessionBean;

import java.util.Map;

/**
 * Session service objectives: - Register sessions of user - Generate tokens - Recover sessions by tokens
 * 
 * @author José Carvajal
 */
public interface ISessionService
{
    /**
     * Register the user session
     * 
     * @param properties
     * @return The token session.
     */
    public String registerSession(String game, String lang, Integer level, Map<String, String> properties);

    /**
     * Unregister the user session.
     * 
     * @param token
     */
    public boolean unregisterSession(String game, String token);

    /**
     * Check whether the token session exists and is active.
     * 
     * @param token
     */
    public SessionBean getSession(String game, String token);

    /**
     * Update the current session in the system.
     * 
     * @param session
     */
    public void updateSession(SessionBean session);
}
