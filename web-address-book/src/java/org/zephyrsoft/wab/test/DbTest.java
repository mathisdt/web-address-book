package org.zephyrsoft.wab.test;

import static org.junit.Assert.*;
import com.avaje.ebean.*;
import org.junit.*;
import org.zephyrsoft.wab.*;

/**
 * 
 * 
 * @author Mathis Dirksen-Thedens
 */
public class DbTest {
	
	private static ContextListener cl;

	@BeforeClass
	public static void setup() {
		cl = new ContextListener();
		cl.contextInitialized(null);
	}
	
	@AfterClass
	public static void teardown() {
		cl.contextDestroyed(null);
	}
	
	@Test
	public void createInsertSelectDrop() {
		Ebean.createSqlUpdate("create table test (one char)").execute();
		Ebean.createSqlUpdate("insert into test (one) values ('1')").execute();
		SqlRow row = Ebean.createSqlQuery("select one from test").findUnique();
		Integer i = row.getInteger("one");
		assertTrue(1==i);
		Ebean.createSqlUpdate("drop table test").execute();
	}
	
}
