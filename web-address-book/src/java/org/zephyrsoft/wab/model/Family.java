package org.zephyrsoft.wab.model;

import java.sql.*;
import java.util.*;
import javax.persistence.*;

@Entity
@Table(name="family")
public class Family {
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
	
	@OneToMany(targetEntity=Person.class, mappedBy="family", cascade=CascadeType.ALL)
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
	
	public List<Person> getMembers() {
		if (members==null) {
			initMembers();
		}
		return members;
	}
	public void setMembers(List<Person> members) {
		this.members = members;
	}
	public boolean addMember(Person e) {
		if (members==null) {
			initMembers();
		}
		return members.add(e);
	}
	public void clearMembers() {
		if (members==null) {
			initMembers();
		}
		members.clear();
	}
	public boolean containsMember(Object o) {
		if (members==null) {
			initMembers();
		}
		return members.contains(o);
	}
	public boolean removeMember(Object o) {
		if (members==null) {
			initMembers();
		}
		return members.remove(o);
	}
	public boolean isEmpty() {
		if (members==null) {
			initMembers();
		}
		return members.isEmpty();
	}
	public int size() {
		if (members==null) {
			initMembers();
		}
		return members.size();
	}
}
