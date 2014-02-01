package gameframework.services.ghost.providers;

import gameframework.transversal.logging.GameLogger;
import gameframework.transversal.utils.IOUtils;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * This abstract class is ready to read the list word from a list format.
 * 
 * @author Jose Carvajal
 */
public abstract class FileDictionaryProviderBase implements IDictionaryProvider
{
    private static final String ALPHABETIC_PATTERN = "[a-zA-Z]";

    private final List<String> wordList;

    /**
     * Initializes the dictionary list. Currently, the list of words will load in memory, but if there are other many
     * dictionary, review this load mechanism in order to avoid out of memory exceptions. The words with less than the
     * minimal letters will be discarded. Also the ones that starts with the previous word.
     * 
     * @param dictionaryFile
     */
    public FileDictionaryProviderBase(String dictionaryFile, int minimalLetters)
    {
        this.wordList = new ArrayList<String>();

        DataInputStream in = null;
        try {
            InputStream is = this.getClass().getResourceAsStream(dictionaryFile);
            in = new DataInputStream(is);

            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String previousWord = null;
            String word = br.readLine();

            // Read File Line By Line
            while (word != null) {
                if (word.length() > minimalLetters && (previousWord == null || !word.startsWith(previousWord))) {

                    this.wordList.add(word);
                    previousWord = word;
                }

                word = br.readLine();
            }

            GameLogger.info(String.format("%s words in dictionary %s", this.wordList.size(), dictionaryFile));

        } catch (Exception ex) {
            GameLogger.error("Error reading dictionary", ex);
        } finally {
            IOUtils.closeInputStream(in);
        }

    }

    /**
     * Checks whether there are existing words using the specified string as root of those words.
     * 
     * @param string
     * @return
     */
    public List<String> listPossibleWords(String string)
    {
        List<String> words = new ArrayList<String>();

        if (string != null && string.length() > 0) {

            for (String word : this.wordList) {
                if (word.equals(string)) {
                    // Game finished
                    words.clear();
                    break;
                } else if (word.startsWith(string)) {
                    // Continue playing
                    words.add(word);
                }
            }
        }

        GameLogger.debug(String.format("%s words for string %s", words.size(), string));

        return words;
    }

    /**
     * Checks if the specified string is a character and it is contained in the current dictionary.
     * 
     * @param letter
     * @return
     */
    public boolean checkLetter(String letter)
    {
        boolean result = false;

        if (letter != null && letter.length() == 1) {
            result = letter.matches(ALPHABETIC_PATTERN);
        }

        return result;
    }

}
