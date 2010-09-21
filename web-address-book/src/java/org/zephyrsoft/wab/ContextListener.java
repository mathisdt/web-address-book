package org.zephyrsoft.wab;

import java.io.*;
import java.rmi.*;
import java.sql.*;
import java.util.*;
import javax.servlet.*;
import com.avaje.ebean.*;
import org.h2.tools.*;

public class ContextListener implements ServletContextListener {
	
	private static ContextListener _instance = null;
	
	public static ContextListener getInstance() {
		return _instance;
	}

	// one specifically created EbeanServer - auto-creation does not work for multiple clients
	private EbeanServer ebeanServer = null;
	
	public EbeanServer getEbeanServer() {
		return ebeanServer;
	}

	private boolean useDedicatedDatabase = false;
	private Server server = null;
	
	public ContextListener() {
		// first of all: populate instance variable or throw exception
		if (_instance == null) {
			_instance = this;
		} else {
			throw new IllegalStateException(Constants.ANOTHER_INSTANCE_EXISTS);
		}
		
		// get environment properties
		Properties props = new Properties();
		try {
			InputStream is = getClass().getResourceAsStream(Constants.ENVIRONMENT_PROPERTIES);
			if (is!=null) {
				props.load(is);
			} else {
				throw new IllegalStateException(Constants.ENVIRONMENT_PROPERTIES_NOT_FOUND);
			}
		} catch (IOException e) {
			throw new IllegalStateException(Constants.ENVIRONMENT_PROPERTIES_NOT_FOUND, e);
		}
		String prop1 = props.getProperty(Constants.USE_DEDICATED_DATABASE);
		if (prop1!=null && prop1.trim().equalsIgnoreCase(Boolean.TRUE.toString())) {
			useDedicatedDatabase = true;
		}
		
		// get database name
		String databaseName = null;
		String prop2 = props.getProperty(Constants.DATABASE_NAME);
		if (prop2!=null) {
			databaseName = prop2;
		}
		// create ebean server
		ebeanServer = EbeanServerFactory.create(databaseName);
	}
	
	public void contextInitialized(ServletContextEvent sce) {
		if (useDedicatedDatabase) {
			try {
				server = Server.createTcpServer(); // use argument "-tcpAllowOthers" if all interfaces should be bound (and not only "localhost")
				server.start();
				System.out.println(Constants.STARTED_H2_SERVER);
			} catch (SQLException e) {
				System.out.println(Constants.COULD_NOT_START_H2_SERVER);
				e.printStackTrace();
			}
		} else {
			System.out.println(Constants.DID_NOT_START_H2_SERVER);
		}
	}

	public void contextDestroyed(ServletContextEvent sce) {
		if (useDedicatedDatabase) {
			if (server!=null) {
				server.stop();
				System.out.println(Constants.STOPPED_H2_SERVER);
			} else {
				System.out.println(Constants.COULD_NOT_STOP_H2_SERVER);
			}
		} else {
			System.out.println(Constants.DID_NOT_STOP_H2_SERVER);
		}
	}
}
