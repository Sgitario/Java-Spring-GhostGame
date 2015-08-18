package gameframework.services.interfaces;

import gameframework.transversal.models.SessionBean;

/**
 * This interfaces provides following features: - checkString - addLetter
 * 
 * @author Jose Carvajal
 */
public interface IGhostService {
	/**
	 * Check the game and add a letter to the current string in the session. If
	 * the game cannot be continued, the flag of finished in session will be
	 * true.
	 * 
	 * @param session
	 * @return
	 */
	public String addLetter(String letter, SessionBean session);

	/**
	 * Get the current string in the specified session.
	 * 
	 * @param session
	 * @return
	 */
	public String getString(SessionBean session);

	/**
	 * Checks if the specified string is a character and it is contained in the
	 * current dictionary.
	 * 
	 * @param letter
	 * @return
	 */
	public boolean checkLetter(String letter, SessionBean session);
}
