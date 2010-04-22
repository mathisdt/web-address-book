package org.zephyrsoft.wab.test;

import com.avaje.ebean.*;
import org.zephyrsoft.wab.*;

public class DbConnectionTest {
	
	public static void main(String[] args) {
		
		ContextListener cl = new ContextListener();
		cl.contextInitialized(null);
		
		try {
			Ebean.createSqlUpdate("create table test (one char)").execute();
			Ebean.createSqlUpdate("insert into test (one) values ('1')").execute();
			SqlRow row = Ebean.createSqlQuery("select one from test").findUnique();
			
			Integer i = row.getInteger("count");
			
			System.out.println("Got " + i + ".");
		} finally {
			cl.contextDestroyed(null);
		}
	}
	
}
