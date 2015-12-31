package org.zephyrsoft.wab;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.Properties;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.h2.tools.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.avaje.ebean.EbeanServer;
import com.avaje.ebean.EbeanServerFactory;

/**
 * central class for initializing the environment (init logging, load properties, init persistence)
 * 
 * @author Mathis Dirksen-Thedens
 */
public class ContextListener implements ServletContextListener {
	
	private static final Logger LOG = LoggerFactory.getLogger(ContextListener.class);
	private static final Object LOCK = new Object();
	
	private static ContextListener instance = null;
	
	public ContextListener() {
		synchronized (LOCK) {
			if (instance != null) {
				IllegalStateException exception = new IllegalStateException(Constants.ANOTHER_INSTANCE_EXISTS);
				LOG.error(Constants.ANOTHER_INSTANCE_EXISTS, exception);
				throw exception;
			}
			instance = this;
		}
		// get properties for environment
		Properties environmentProps = new Properties();
		InputStream environmentStream = null;
		try {
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
	
	@Override
	public void contextInitialized(ServletContextEvent sce) {
		// start H2 database is configured
		if (useDedicatedDatabase) {
			try {
				server = Server.createTcpServer(); // use argument "-tcpAllowOthers" if all interfaces should be bound
													// (and not only "localhost")
				server.start();
				LOG.info(Constants.STARTED_H2_SERVER);
			} catch (SQLException e) {
				LOG.info(Constants.COULD_NOT_START_H2_SERVER);
				e.printStackTrace();
			}
		} else {
			LOG.info(Constants.DID_NOT_START_H2_SERVER);
		}
	}
	
	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		if (useDedicatedDatabase) {
			if (server != null) {
				server.stop();
				LOG.info(Constants.STOPPED_H2_SERVER);
			} else {
				LOG.info(Constants.COULD_NOT_STOP_H2_SERVER);
			}
		} else {
			LOG.info(Constants.DID_NOT_STOP_H2_SERVER);
		}
	}
}
