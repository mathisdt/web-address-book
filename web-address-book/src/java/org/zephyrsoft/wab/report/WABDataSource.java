package org.zephyrsoft.wab.report;

import java.util.*;
import org.zephyrsoft.wab.*;
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
		String[] dataPath = jrField.getName().split(Constants.REGEX_FULLSTOP);
		if (dataPath.length==0) {
			System.out.println(Constants.PROBLEM_WITH_FIELD_NAME + jrField.getName());
			return null;
		} else {
			if (dataPath[0].equals(Constants.ENTITY_FAMILY)) {
				// get family attribute
				try {
					return Family.class.getMethod(getGetterName(dataPath[1])).invoke(family);
				} catch (Exception e) {
					e.printStackTrace();
					return null;
				}
			} else {
				// get person attribute
				try {
					// TODO "person_first_name" has to contain also the last name (if filled and different from family's last name)!
					return Person.class.getMethod(getGetterName(dataPath[1])).invoke(person);
				} catch (Exception e) {
					e.printStackTrace();
					return null;
				}
			}
		}
	}
	
	private static String getGetterName(String fieldName) {
		if (fieldName==null || fieldName.length()==0) {
			return null;
		} else {
			return Constants.GET + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
		}
	}
	
}
