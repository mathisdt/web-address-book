package org.zephyrsoft.wab.test;

import com.avaje.ebean.*;
import org.zephyrsoft.wab.*;
import org.zephyrsoft.wab.model.*;

public class EbeanEntityTest {
	
	public static void main(String[] args) {
		
		DatabaseManagerContextListener cl = new DatabaseManagerContextListener();
		cl.contextInitialized(null);
		
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
			System.out.println("f:" + f.getLastName() + " / " + "f2:" + f2.getLastName());
			
			// delete
//			Ebean.delete(f);
			
			Ebean.commitTransaction();
			
		} finally {
			Ebean.endTransaction();
			cl.contextDestroyed(null);
		}
	}
	
}
