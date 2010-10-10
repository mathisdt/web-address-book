package org.zephyrsoft.wab.model;

import java.io.*;
import java.sql.*;
import javax.persistence.*;
import org.zephyrsoft.wab.*;
import org.zephyrsoft.wab.util.*;

/**
 * 
 * 
 * @author Mathis Dirksen-Thedens
 */
@Entity
@Table(name = Constants.ENTITY_PERSON)
public class Person extends ComparableBean<Person> implements Serializable {
	private static final long serialVersionUID = -3541739218662947140L;
	
	@Id
	private Integer id;
	@Version
	private Timestamp lastUpdate;
	
	private String firstName;
	private String lastName;
	private String birthday;
	private String contact1;
	private String contact2;
	private String contact3;
	private String remarks;
	private Integer ordering;
	
	@ManyToOne
	private Family family;
	
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
	
	public String getFirstName() {
		return firstName;
	}
	
	public void setFirstName(String firstName) {
		this.firstName = firstName;
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
	
	public Family getFamily() {
		return family;
	}
	
	public void setFamily(Family family) {
		this.family = family;
	}
	
	public String getBirthday() {
		return birthday;
	}
	
	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}
	
	public String getLastName() {
		return lastName;
	}
	
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	public Integer getOrdering() {
		return ordering;
	}
	
	public void setOrdering(Integer ordering) {
		this.ordering = ordering;
	}
	
	@Override
	public int compareTo(Person o) {
		if (o == null) {
			return -1;
		}
		int ret = CompareUtil.compareWithNullsLast(this.getOrdering(), o.getOrdering());
		if (ret == 0) {
			CompareUtil.compareWithNullsLast(this.getFirstName(), o.getFirstName());
		}
		return ret;
	}
	
	@Override
	public boolean equals(Object o) {
		if (o instanceof Person) {
			return 0 == compareTo((Person) o);
		} else {
			return super.equals(o);
		}
	}

	@Override
	public int hashCode() {
		// nothing special required, use superclass implementation
		return super.hashCode();
	}
	
}
