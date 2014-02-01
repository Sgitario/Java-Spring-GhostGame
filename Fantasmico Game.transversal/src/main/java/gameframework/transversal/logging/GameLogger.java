package gameframework.transversal.logging;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.apache.log4j.Logger;

/**
 * GameLogger is the infrastructure class to log any traces along to the system.
 */
public class GameLogger
{
    private final static String LOGGER_ROOT = "com.jcarvajal";

    /**
     * Internal Log4j logger.
     */
    private static Logger internalLogger = Logger.getLogger(LOGGER_ROOT);

    /**
     * @param message
     * @param ex
     */
    public static void error(String message, Exception ex)
    {
        String text = String.format("%s: %s", message, ex.getMessage());

        StringWriter sw = new StringWriter();
        ex.printStackTrace(new PrintWriter(sw));
        String stacktrace = sw.toString();
        internalLogger.error(text + stacktrace);
    }

    /**
     * @param message
     * @param ex
     */
    public static void warn(String message)
    {
        internalLogger.warn(message);
    }

    /**
     * @param message
     * @param ex
     */
    public static void debug(String message)
    {
        internalLogger.debug(message);
    }

    /**
     * @param message
     * @param ex
     */
    public static void info(String message)
    {
        internalLogger.info(message);
    }

}
