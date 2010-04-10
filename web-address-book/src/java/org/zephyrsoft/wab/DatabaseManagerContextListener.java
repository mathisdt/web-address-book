package org.zephyrsoft.wab;

import java.sql.*;
import javax.servlet.*;
import org.h2.tools.*;

public class DatabaseManagerContextListener implements ServletContextListener {
	
	private static ServletContextListener _instance = null;
	
	private Server server = null;
	
	public DatabaseManagerContextListener() {
		if (_instance == null) {
			_instance = this;
		} else {
			throw new IllegalStateException("another instance of this class already exists");
		}
	}
	
	public void contextInitialized(ServletContextEvent sce) {
		try {
			server = Server.createTcpServer(); // use argument "-tcpAllowOthers" if all interfaces should be bound (and not only "localhost")
			server.start();
			System.out.println("started h2 server");
		} catch (SQLException e) {
			System.out.println("couldn't start h2 server - SQLException occurred");
			e.printStackTrace();
		}
	}

	public void contextDestroyed(ServletContextEvent sce) {
		if (server!=null) {
			server.stop();
			System.out.println("stopped h2 server");
		} else {
			System.out.println("couldn't stop h2 server - reference was null");
		}
	}
}
