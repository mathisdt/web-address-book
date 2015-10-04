package org.zephyrsoft.wab;

import org.apache.log4j.*;

/**
 * central logging facility for the application
 * 
 * @author Mathis Dirksen-Thedens
 */
public final class Log {
	
	private static final Logger LOG = Logger.getLogger(Log.class);

	private Log() {
		// static use only
	}
	
	private static String getCallingClassName() {
		return new Throwable().fillInStackTrace().getStackTrace()[1].getClassName();
	}

	public static void debug(Object message) {
		LOG.log(getCallingClassName(), Level.DEBUG, message, null);
	}

	public static void debug(Object message, Throwable t) {
		LOG.log(getCallingClassName(), Level.DEBUG, message, t);
	}

	public static void info(Object message) {
		LOG.log(getCallingClassName(), Level.INFO, message, null);
	}
	
	public static void info(Object message, Throwable t) {
		LOG.log(getCallingClassName(), Level.INFO, message, t);
	}
	
	public static void warn(Object message) {
		LOG.log(getCallingClassName(), Level.WARN, message, null);
	}
	
	public static void warn(Object message, Throwable t) {
		LOG.log(getCallingClassName(), Level.WARN, message, t);
	}
	
	public static void error(Object message) {
		LOG.log(getCallingClassName(), Level.ERROR, message, null);
	}

	public static void error(Object message, Throwable t) {
		LOG.log(getCallingClassName(), Level.ERROR, message, t);
	}

	public static void fatal(Object message) {
		LOG.log(getCallingClassName(), Level.FATAL, message, null);
	}

	public static void fatal(Object message, Throwable t) {
		LOG.log(getCallingClassName(), Level.FATAL, message, t);
	}

	public static boolean isDebugEnabled() {
		return LOG.isDebugEnabled();
	}
	
	public static boolean isInfoEnabled() {
		return LOG.isInfoEnabled();
	}
	
	
	
}
