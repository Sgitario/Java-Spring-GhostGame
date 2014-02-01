package gameframework.transversal.utils;

import gameframework.transversal.logging.GameLogger;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Utilities for file management.
 * 
 * @author Jose Carvajal
 */
public class IOUtils
{
    /**
     * Close a input stream.
     * 
     * @param stream
     */
    public static void closeInputStream(InputStream stream)
    {
        try {
            stream.close();
        } catch (IOException e) {
            GameLogger.error("Error closing input stream", e);
        }
    }

    /**
     * Close a output stream.
     * 
     * @param stream
     */
    public static void closeOutputStream(OutputStream stream)
    {
        try {
            stream.close();
        } catch (IOException e) {
            GameLogger.error("Error closing output stream", e);
        }
    }
}
