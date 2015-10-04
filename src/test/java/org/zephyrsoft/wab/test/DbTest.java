package org.zephyrsoft.wab.test;

import static org.junit.Assert.assertTrue;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.zephyrsoft.wab.ContextListener;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.SqlRow;

/**
 * tests the database connection directly using SQL
 * 
 * @author Mathis Dirksen-Thedens
 */
// TODO fix this test
@Ignore("test needs to be fixed")
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
		assertTrue(1 == i);
		Ebean.createSqlUpdate("drop table test").execute();
	}
	
}
