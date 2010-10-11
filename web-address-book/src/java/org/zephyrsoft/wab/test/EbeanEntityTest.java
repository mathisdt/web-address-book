package org.zephyrsoft.wab.test;

import com.avaje.ebean.*;
import org.junit.*;
import static org.junit.Assert.*;
import org.zephyrsoft.wab.*;
import org.zephyrsoft.wab.model.*;

/**
 * tests the persistence framework
 * 
 * @author Mathis Dirksen-Thedens
 */
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
			
			//query
			Family f2 = Ebean.find(Family.class, f.getId());
			assertEquals(f.getLastName(), f2.getLastName());
			
			// delete
			Ebean.delete(f);
			
		} finally {
			Ebean.rollbackTransaction();
		}
	}
	
}
