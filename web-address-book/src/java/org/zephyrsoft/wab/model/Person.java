package org.zephyrsoft.wab.model;

import java.sql.*;
import javax.persistence.*;

@Entity
@Table(name="person")
public class Person {
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
}
