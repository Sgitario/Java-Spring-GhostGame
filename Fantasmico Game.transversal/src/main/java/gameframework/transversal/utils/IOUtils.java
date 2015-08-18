package gameframework.transversal.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.log4j.Logger;

/**
 * Utilities for file management.
 * 
 * @author Jose Carvajal
 */
public class IOUtils {

	private static final Logger LOG = Logger.getLogger(IOUtils.class);

	/**
	 * Close a input stream.
	 * 
	 * @param stream
	 */
	public static void closeInputStream(InputStream stream) {
		try {
			stream.close();
		} catch (IOException e) {
			LOG.error("Error closing input stream", e);
		}
	}

	/**
	 * Close a output stream.
	 * 
	 * @param stream
	 */
	public static void closeOutputStream(OutputStream stream) {
		try {
			stream.close();
		} catch (IOException e) {
			LOG.error("Error closing output stream", e);
		}
	}
}
