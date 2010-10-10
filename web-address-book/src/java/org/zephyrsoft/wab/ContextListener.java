package org.zephyrsoft.wab;

import java.io.*;
import java.sql.*;
import java.util.*;
import javax.servlet.*;
import com.avaje.ebean.*;
import org.apache.log4j.*;
import org.h2.tools.*;

/**
 * 
 * 
 * @author Mathis Dirksen-Thedens
 */
public class ContextListener implements ServletContextListener {
	
	private static final Object LOCK = new Object();
	
	private static ContextListener instance = null;
	
	public ContextListener() {
		synchronized (LOCK) {
			if (instance != null) {
				IllegalStateException exception = new IllegalStateException(Constants.ANOTHER_INSTANCE_EXISTS);
				Log.error(Constants.ANOTHER_INSTANCE_EXISTS, exception);
				throw exception;
			}
			instance = this;
		}
		// get properties for log4j and environment
		Properties log4jProps = new Properties();
		Properties environmentProps = new Properties();
		InputStream log4jStream = null;
		InputStream environmentStream = null;
		try {
			log4jStream = getClass().getResourceAsStream(Constants.LOG4J_PROPERTIES);
			if (log4jStream != null) {
				log4jProps.load(log4jStream);
			} else {
				throw new IllegalStateException(Constants.PROPERTIES_NOT_FOUND);
			}
			environmentStream = getClass().getResourceAsStream(Constants.ENVIRONMENT_PROPERTIES);
			if (environmentStream != null) {
				environmentProps.load(environmentStream);
			} else {
				throw new IllegalStateException(Constants.PROPERTIES_NOT_FOUND);
			}
		} catch (IOException e) {
			throw new IllegalStateException(Constants.PROPERTIES_NOT_FOUND, e);
		} finally {
			try {
				if (log4jStream != null) {
					log4jStream.close();
				}
			} catch (IOException e) {
				// ignore at this point
			}
			try {
				if (environmentStream != null) {
					environmentStream.close();
				}
			} catch (IOException e) {
				// ignore at this point
			}
		}
		// determine if dedicated database should be used
		String prop1 = environmentProps.getProperty(Constants.USE_DEDICATED_DATABASE);
		if (prop1 != null && prop1.trim().equalsIgnoreCase(Boolean.TRUE.toString())) {
			useDedicatedDatabase = true;
		}
		// initialize logging system
		PropertyConfigurator.configure(log4jProps);
		
		// get database name
		String databaseName = null;
		String prop2 = environmentProps.getProperty(Constants.DATABASE_NAME);
		if (prop2 != null) {
			databaseName = prop2;
		}
		// create ebean server
		ebeanServer = EbeanServerFactory.create(databaseName);
	}
	
	public static ContextListener getInstance() {
		synchronized (LOCK) {
			if (instance == null) {
				instance = new ContextListener();
			}
		}
		return instance;
	}
	
	// one specifically created EbeanServer - auto-creation does not work for multiple clients
	private EbeanServer ebeanServer = null;
	
	public EbeanServer getEbeanServer() {
		if (ebeanServer == null) {
			throw new IllegalStateException(Constants.CL_NOT_INITIALIZED_CORRECTLY);
		}
		return ebeanServer;
	}
	
	private boolean useDedicatedDatabase = false;
	private Server server = null;
	
	public void contextInitialized(ServletContextEvent sce) {
		// start H2 database is configured
		if (useDedicatedDatabase) {
			try {
				server = Server.createTcpServer(); // use argument "-tcpAllowOthers" if all interfaces should be bound
													// (and not only "localhost")
				server.start();
				Log.info(Constants.STARTED_H2_SERVER);
			} catch (SQLException e) {
				Log.info(Constants.COULD_NOT_START_H2_SERVER);
				e.printStackTrace();
			}
		} else {
			Log.info(Constants.DID_NOT_START_H2_SERVER);
		}
	}
	
	public void contextDestroyed(ServletContextEvent sce) {
		if (useDedicatedDatabase) {
			if (server != null) {
				server.stop();
				Log.info(Constants.STOPPED_H2_SERVER);
			} else {
				Log.info(Constants.COULD_NOT_STOP_H2_SERVER);
			}
		} else {
			Log.info(Constants.DID_NOT_STOP_H2_SERVER);
		}
	}
}
