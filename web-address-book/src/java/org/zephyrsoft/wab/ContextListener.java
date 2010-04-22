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
			throw new IllegalStateException("another instance of this class already exists");
		}
		
		System.out.println("============== initializing WAB context listener ==============");
		
		// get environment properties
		Properties props = new Properties();
		try {
			InputStream is = getClass().getResourceAsStream("/environment.properties");
			if (is!=null) {
				props.load(is);
			} else {
				throw new IllegalStateException("could not find environment.properties");
			}
		} catch (IOException e) {
			throw new IllegalStateException("could not find environment.properties", e);
		}
		String prop1 = props.getProperty("useDedicatedDatabase");
		if (prop1!=null && prop1.trim().equalsIgnoreCase("true")) {
			useDedicatedDatabase = true;
		}
		
		// get database name
		String databaseName = null;
		String prop2 = props.getProperty("databaseName");
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
				System.out.println("started h2 server");
			} catch (SQLException e) {
				System.out.println("couldn't start h2 server - SQLException occurred");
				e.printStackTrace();
			}
		} else {
			System.out.println("didn't start h2 server - it's not configured in environment.properties");
		}
	}

	public void contextDestroyed(ServletContextEvent sce) {
		if (useDedicatedDatabase) {
			if (server!=null) {
				server.stop();
				System.out.println("stopped h2 server");
			} else {
				System.out.println("couldn't stop h2 server - reference was null");
			}
		} else {
			System.out.println("didn't stop h2 server - it's not configured in environment.properties");
		}
	}
}
