package org.zephyrsoft.wab.model;

import java.sql.*;
import java.util.*;
import javax.persistence.*;
import com.avaje.ebean.*;
import org.zephyrsoft.wab.*;
import org.zephyrsoft.wab.util.*;

@Entity
@Table(name = Constants.ENTITY_FAMILY)
public class Family implements Comparable<Family> {
	@Id
	private Integer id;
	@Version
	private Timestamp lastUpdate;
	
	private String lastName;
	private String street;
	private String postalCode;
	private String city;
	private String contact1;
	private String contact2;
	private String contact3;
	private String remarks;
	
	@OneToMany(targetEntity = Person.class, mappedBy = Constants.ENTITY_FAMILY, cascade = CascadeType.ALL)
	private List<Person> members = null;
	
	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	public Timestamp getLastUpdate() {
		return lastUpdate;
	}
	
	public void setLastUpdate(Timestamp lastUpdate) {
		this.lastUpdate = lastUpdate;
	}
	
	public String getLastName() {
		return lastName;
	}
	
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	public String getStreet() {
		return street;
	}
	
	public void setStreet(String street) {
		this.street = street;
	}
	
	public String getPostalCode() {
		return postalCode;
	}
	
	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}
	
	public String getCity() {
		return city;
	}
	
	public void setCity(String city) {
		this.city = city;
	}
	
	public String getContact1() {
		return contact1;
	}
	
	public void setContact1(String contact1) {
		this.contact1 = contact1;
	}
	
	public String getContact2() {
		return contact2;
	}
	
	public void setContact2(String contact2) {
		this.contact2 = contact2;
	}
	
	public String getContact3() {
		return contact3;
	}
	
	public void setContact3(String contact3) {
		this.contact3 = contact3;
	}
	
	public String getRemarks() {
		return remarks;
	}
	
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	
	private void initMembers() {
		members = new ArrayList<Person>();
	}
	
	private void correctMemberOrderIfNecessary() {
		for (int i = 0; i < members.size(); i++) {
			Person p = members.get(i);
			if (p.getOrdering() == null || p.getOrdering().intValue() != i) {
				p.setOrdering(i);
			}
		}
	}
	
	public List<Person> getMembers() {
		return members;
	}
	
	public void setMembers(List<Person> members) {
		this.members = members;
		correctMemberOrderIfNecessary();
	}
	
	public boolean addMember(Person e) {
		if (members == null) {
			initMembers();
		}
		boolean returnValue = members.add(e);
		correctMemberOrderIfNecessary();
		return returnValue;
	}
	
	public void clearMembers() {
		if (members == null) {
			return;
		} else {
			for (Person p : this.members) {
				removeMember(p);
			}
		}
	}
	
	public boolean containsMember(Person o) {
		if (members == null) {
			return false;
		}
		return members.contains(o);
	}
	
	public Person newMember() {
		if (members == null) {
			initMembers();
		}
		Person p = new Person();
		addMember(p);
		return p;
	}
	
	public boolean removeMember(Person o) {
		if (members == null) {
			return false;
		}
		boolean returnValue = members.remove(o);
		correctMemberOrderIfNecessary();
		return returnValue;
	}
	
	public boolean isEmpty() {
		if (members == null) {
			return true;
		}
		return members.isEmpty();
	}
	
	public int size() {
		if (members == null) {
			return 0;
		}
		return members.size();
	}
	
	public boolean mayMoveUp(Person p) {
		// person is in list, but not at first position
		return containsMember(p) && members != null && members.indexOf(p) > 0;
	}
	
	public boolean mayMoveDown(Person p) {
		// person is in list, but not at last position
		return containsMember(p) && members != null && members.lastIndexOf(p) < members.size() - 1;
	}
	
	public void moveUp(Person p) {
		if (mayMoveUp(p)) {
			int sourceIndex = members.indexOf(p);
			int targetIndex = sourceIndex - 1;
			Person toSwitch = members.get(targetIndex);
			members.set(targetIndex, p);
			members.set(sourceIndex, toSwitch);
			correctMemberOrderIfNecessary();
		}
	}
	
	public void moveDown(Person p) {
		if (mayMoveDown(p)) {
			int sourceIndex = members.lastIndexOf(p);
			int targetIndex = sourceIndex + 1;
			Person toSwitch = members.get(targetIndex);
			members.set(targetIndex, p);
			members.set(sourceIndex, toSwitch);
			correctMemberOrderIfNecessary();
		}
	}
	
	/**
	 * Compare this family to another. This is done by comparing the last names
	 * and then the street and then the contact fields (null values are always last).
	 */
	public int compareTo(Family o) {
		// other object is null => other object is larger than this
		if (o == null) {
			return -1;
		}
		// now compare by lastName, street, contact1, contact2, contact3
		int returnValue = 0;
		if (returnValue==0) {
			returnValue=CompareUtil.compareWithNullsLast(getLastName(), o.getLastName());
		}
		if (returnValue==0) {
			returnValue=CompareUtil.compareWithNullsLast(getStreet(), o.getStreet());
		}
		if (returnValue==0) {
			returnValue=CompareUtil.compareWithNullsLast(getContact1(), o.getContact1());
		}
		if (returnValue==0) {
			returnValue=CompareUtil.compareWithNullsLast(getContact2(), o.getContact2());
		}
		if (returnValue==0) {
			returnValue=CompareUtil.compareWithNullsLast(getContact3(), o.getContact3());
		}
		return returnValue;
	}
}
