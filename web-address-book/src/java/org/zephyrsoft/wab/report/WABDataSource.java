package org.zephyrsoft.wab.report;

import java.util.*;
import org.zephyrsoft.wab.model.*;
import net.sf.jasperreports.engine.*;

/**
 * DataSource for Family and Person data. Does not need to be initiated.
 * 
 * @author Mathis Dirksen-Thedens
 */
public class WABDataSource implements JRDataSource {
	
	private Iterator<Family> familyIterator = null;
	private Family family = null;
	private Iterator<Person> personIterator = null;
	private Person person = null;
	
	public WABDataSource(Collection<Family> data) {
		familyIterator = data.iterator();
		try {
			// automatically position the DataSource at the first record (if any)
			next();
		} catch (JRException e) {
			// ignore exception
		}
	}
	
	public boolean next() throws JRException {
		if (personIterator!=null && personIterator.hasNext()) {
			// next person in current family
			person = personIterator.next();
			return true;
		} else {
			// try first person in next family
			while (familyIterator.hasNext()) {
				family = familyIterator.next();
				personIterator = family.getMembers().iterator();
				if (personIterator.hasNext()) {
					// person found
					person = personIterator.next();
					return true;
				} else {
					// empty family, keep searching for a non-empty one
				}
			}
		}
		// nothing found
		return false;
	}
	
	public Object getFieldValue(JRField jrField) throws JRException {
		String[] dataPath = jrField.getName().split(".");
		if (dataPath[0].equals("family")) {
			// get family attribute
			// TODO
		} else {
			// get person attribute
			// TODO
		}
		return null;
	}
	
}
