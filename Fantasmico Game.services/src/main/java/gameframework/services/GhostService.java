package gameframework.services;

import gameframework.services.ghost.providers.IDictionaryProvider;
import gameframework.services.ghost.strategies.IGhostStrategy;
import gameframework.services.interfaces.IGhostService;
import gameframework.transversal.logging.GameLogger;
import gameframework.transversal.models.SessionBean;

import java.util.List;

import org.perf4j.aop.Profiled;
import org.springframework.stereotype.Service;

/**
 * @author Jose Carvajal
 */
@Service
public class GhostService implements IGhostService
{
    private static final String STRING_KEY = "ghost_string";

    private static final String WINNER_COMPUTER = "computer";

    private static final String WINNER_USER = "user";

    private List<IDictionaryProvider> dictionaries;

    private List<IGhostStrategy> strategies;

    /**
     * Initialize a new instance of the GhostService class. This class is initialized in spring configuration.
     * 
     * @param dictionaries
     * @param strategies
     * @param minimalLetters Minimal letters to accomplish a final of game.
     */
    public GhostService(List<IDictionaryProvider> dictionaries, List<IGhostStrategy> strategies)
    {
        this.dictionaries = dictionaries;
        this.strategies = strategies;
    }

    /**
     * {@inheritDoc}
     * 
     * @see gameframework.services.interfaces.IGhostService#checkLetter(java.lang.String)
     */
    @Profiled
    public boolean checkLetter(String letter, SessionBean session)
    {
        boolean isValid = false;
        if (session != null) {
            // Find language
            IDictionaryProvider dict = this.findDictionary(session.getLang());

            // Check whether letter is a character and exists in the dictionary.
            isValid = dict != null && dict.checkLetter(letter);
        }

        GameLogger.debug(String.format("GhostService.checkLetter with letter %s and result %s.", letter, isValid));

        return isValid;
    }

    /**
     * {@inheritDoc}
     * 
     * @see gameframework.services.interfaces.IGhostService#addLetter(gameframework.infrastructure.entities.SessionBean)
     */
    @Profiled
    public String addLetter(String addedLetter, SessionBean session)
    {
        String letter = null;

        if (session != null) {

            // Find language
            IDictionaryProvider dict = this.findDictionary(session.getLang());

            if (dict != null && dict.checkLetter(addedLetter)) {
                // Compose current string
                String previousString = "";
                if (session.getProperties().containsKey(STRING_KEY)) {
                    previousString = session.getProperties().get(STRING_KEY);
                }
                String currentString = previousString + addedLetter;

                // Check whether current string is a word or there are no more choices. If not, game is over and
                // computer wins.
                List<String> choices = dict.listPossibleWords(currentString);
                if (choices != null && choices.size() > 0) {
                    // Let us play, find strategy
                    IGhostStrategy strategy = this.findStrategy(session.getLevel());
                    if (strategy != null) {
                        letter = strategy.addLetter(choices, currentString);

                        // Update game
                        String newString = currentString + letter;
                        session.getProperties().remove(STRING_KEY);
                        session.getProperties().put(STRING_KEY, newString);

                        // Check again whether the new string is a word or there are no more choices. If so, game is
                        // over and user wins.
                        List<String> newChoices = dict.listPossibleWords(newString);
                        if (newChoices == null || newChoices.size() == 0) {
                            session.setFinished(true);
                            session.setWinner(WINNER_USER);
                        }
                    } else {
                        throw new IllegalArgumentException("Level invalid! Strategy not found!");
                    }
                } else {
                    // Game finished.
                    session.setFinished(true);
                    session.setWinner(WINNER_COMPUTER);
                }
            } else {
                throw new IllegalArgumentException("Dictionary not found or letter invalid");
            }
        }

        GameLogger.debug(String
            .format("GhostService.addLetter with user letter %s and answer %s.", addedLetter, letter));

        return letter;
    }

    /**
     * {@inheritDoc}
     * 
     * @see gameframework.services.interfaces.IGhostService#getString(gameframework.transversal.models.SessionBean)
     */
    @Profiled
    public String getString(SessionBean session)
    {
        String string = "";
        if (session != null && session.getProperties().containsKey(STRING_KEY)) {
            string = session.getProperties().get(STRING_KEY);

            GameLogger.debug(String.format("GhostService.getString for session %s is %s.", session.getToken(), string));
        } else {
            GameLogger.warn("GhostService.getString: Session is null!");
        }

        return string;
    }

    /**
     * Find the proper dictionary for the specified language.
     * 
     * @param lang
     * @return
     */
    private IDictionaryProvider findDictionary(String lang)
    {
        IDictionaryProvider provider = null;
        int index = 0;
        boolean found = false;

        while (index < this.dictionaries.size() && !found) {
            IDictionaryProvider dict = this.dictionaries.get(index);
            if (dict.isDictionaryForLang(lang)) {
                provider = dict;
                found = true;
            }

            index++;
        }

        if (!found) {
            GameLogger.warn(String.format("Dictionary with lang %s not found!", lang));
        }

        return provider;
    }

    /**
     * Find the proper strategy for the specified level.
     * 
     * @param lang
     * @return
     */
    private IGhostStrategy findStrategy(Integer level)
    {
        IGhostStrategy strategy = null;
        int index = 0;
        boolean found = false;

        while (index < this.strategies.size() && !found) {
            IGhostStrategy dict = this.strategies.get(index);
            if (dict.isStrategyForLevel(level)) {
                strategy = dict;
                found = true;
            }

            index++;
        }

        if (!found) {
            GameLogger.warn(String.format("Strategy with level %s not found!", level));
        }

        return strategy;
    }
}
