package org.zephyrsoft.wab;

import javax.servlet.*;
import org.hsqldb.Server;

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
		server = new Server();
        server.setDatabaseName(0, "test");
        server.setDatabasePath(0, "hsqldb/test");
        server.setLogWriter(null);
        server.setErrWriter(null);
        server.start();
        System.out.println("started hsqldb server");
	}

	public void contextDestroyed(ServletContextEvent sce) {
		if (server!=null) {
			server.stop();
			System.out.println("stopped hsqldb server");
		} else {
			System.out.println("couldn't stop hsqldb server - reference was null");
		}
	}
}
