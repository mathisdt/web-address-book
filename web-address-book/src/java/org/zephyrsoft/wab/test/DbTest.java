package org.zephyrsoft.wab.test;

import com.avaje.ebean.*;
import com.avaje.ebean.validation.*;

import org.junit.*;
import static org.junit.Assert.*;
import org.zephyrsoft.wab.*;

public class DbTest {
	
	@Test
	public void createInsertSelectDrop() {
		
		ContextListener cl = new ContextListener();
		cl.contextInitialized(null);
		
		try {
			Ebean.createSqlUpdate("create table test (one char)").execute();
			Ebean.createSqlUpdate("insert into test (one) values ('1')").execute();
			SqlRow row = Ebean.createSqlQuery("select one from test").findUnique();
			Integer i = row.getInteger("one");
			assertTrue(1==i);
			Ebean.createSqlUpdate("drop table test").execute();
		} finally {
			cl.contextDestroyed(null);
		}
	}
	
}
