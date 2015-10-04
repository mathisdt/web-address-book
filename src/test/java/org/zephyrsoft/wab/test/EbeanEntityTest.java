package org.zephyrsoft.wab.test;

import static org.junit.Assert.assertEquals;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.zephyrsoft.wab.ContextListener;
import org.zephyrsoft.wab.model.Family;
import org.zephyrsoft.wab.model.Person;

import com.avaje.ebean.Ebean;

/**
 * tests the persistence framework
 * 
 * @author Mathis Dirksen-Thedens
 */
// TODO fix this test
@Ignore("test needs to be fixed")
public class EbeanEntityTest {
	
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
	public void insertUpdateQueryDelete() {
		try {
			Ebean.beginTransaction();
			
			// insert
			Family f = new Family();
			f.setLastName("Test-123");
			Ebean.save(f);
			
			// update
			f.setPostalCode("30159");
			f.setCity("Hannover");
			Person p = new Person();
			p.setFirstName("Bla");
			f.addMember(p);
			Ebean.save(f);
			
			// query
			Family f2 = Ebean.find(Family.class, f.getId());
			assertEquals(f.getLastName(), f2.getLastName());
			
			// delete
			Ebean.delete(f);
			
		} finally {
			Ebean.rollbackTransaction();
		}
	}
	
}
