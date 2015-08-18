package gameframework.services.ghost.providers;

import java.util.List;

/**
 * This interfaces provides information to check letters, words and conditions
 * to finish a ghost game.
 * 
 * @author Jose Carvajal
 */
public interface IDictionaryProvider {
	/**
	 * This method checks whether the concrete dictionary provider is linked
	 * with the specified language code.
	 * 
	 * @param lang
	 * @return
	 */
	public boolean isDictionaryForLang(String lang);

	/**
	 * Checks whether there are existing words using the specified string as
	 * root of those words.
	 * 
	 * @param string
	 * @return
	 */
	public List<String> listPossibleWords(String string);

	/**
	 * Checks if the specified string is a character and it is contained in the
	 * current dictionary.
	 * 
	 * @param letter
	 * @return
	 */
	public boolean checkLetter(String letter);
}
