package org.zephyrsoft.wab.model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Version;

import org.zephyrsoft.wab.Constants;
import org.zephyrsoft.wab.util.CompareUtil;

/**
 * family data bean
 */
@Entity
@Table(name = Constants.ENTITY_FAMILY)
public class Family extends ComparableBean<Family> implements HasContacts, Serializable {
	private static final long serialVersionUID = 4985298161866949004L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
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

	@OrderBy("ordering, firstName")
	@OneToMany(targetEntity = Person.class, mappedBy = Constants.ENTITY_FAMILY, cascade = CascadeType.ALL)
	private List<Person> members = null;

	public Integer getId() {
		return id;
	}

	public void setId(final Integer id) {
		this.id = id;
	}

	public Timestamp getLastUpdate() {
		return lastUpdate;
	}

	public void setLastUpdate(final Timestamp lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(final String lastName) {
		this.lastName = lastName;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(final String street) {
		this.street = street;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(final String postalCode) {
		this.postalCode = postalCode;
	}

	public String getCity() {
		return city;
	}

	public void setCity(final String city) {
		this.city = city;
	}

	@Override
	public String getContact1() {
		return contact1;
	}

	public void setContact1(final String contact1) {
		this.contact1 = contact1;
	}

	@Override
	public String getContact2() {
		return contact2;
	}

	public void setContact2(final String contact2) {
		this.contact2 = contact2;
	}

	@Override
	public String getContact3() {
		return contact3;
	}

	public void setContact3(final String contact3) {
		this.contact3 = contact3;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(final String remarks) {
		this.remarks = remarks;
	}

	private void initMembers() {
		members = new ArrayList<>();
	}

	private void setMemberOrdering() {
		for (int i = 0; i < members.size(); i++) {
			Person p = members.get(i);
			p.setOrdering(i);
		}
	}

	public List<Person> getMembers() {
		return members;
	}

	public void setMembers(final List<Person> members) {
		this.members = members;
		setMemberOrdering();
	}

	public boolean addMember(final Person e) {
		if (members == null) {
			initMembers();
		}
		boolean returnValue = members.add(e);
		setMemberOrdering();
		return returnValue;
	}

	public void clearMembers() {
		if (members == null) {
			return;
		} else {
			for (HasContacts p : this.members) {
				removeMember(p);
			}
		}
	}

	public boolean containsMember(final HasContacts o) {
		if (members == null) {
			return false;
		}
		return members.contains(o);
	}

	public HasContacts newMember() {
		if (members == null) {
			initMembers();
		}
		Person p = new Person();
		addMember(p);
		return p;
	}

	public boolean removeMember(final HasContacts o) {
		if (members == null) {
			return false;
		}
		boolean returnValue = members.remove(o);
		setMemberOrdering();
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

	public boolean mayMoveUp(final HasContacts p) {
		// person is in list, but not at first position
		return containsMember(p) && members != null && members.indexOf(p) > 0;
	}

	public boolean mayMoveDown(final HasContacts p) {
		// person is in list, but not at last position
		return containsMember(p) && members != null && members.lastIndexOf(p) < members.size() - 1;
	}

	public HasContacts moveUp(final Person p) {
		if (mayMoveUp(p)) {
			int sourceIndex = members.indexOf(p);
			int targetIndex = sourceIndex - 1;
			Person toSwitch = members.get(targetIndex);
			members.set(targetIndex, p);
			members.set(sourceIndex, toSwitch);
			setMemberOrdering();
			// return the person which changed places with the given person
			return toSwitch;
		} else {
			return null;
		}
	}

	public HasContacts moveDown(final Person p) {
		if (mayMoveDown(p)) {
			int sourceIndex = members.lastIndexOf(p);
			int targetIndex = sourceIndex + 1;
			Person toSwitch = members.get(targetIndex);
			members.set(targetIndex, p);
			members.set(sourceIndex, toSwitch);
			setMemberOrdering();
			// return the person which changed places with the given person
			return toSwitch;
		} else {
			return null;
		}
	}

	/**
	 * Compare this family to another. This is done by comparing the last names and
	 * then the street and then the contact fields (null values are always last).
	 */
	@Override
	public int compareTo(final Family o) {
		// other object is null => other object is larger than this
		if (o == null) {
			return -1;
		}
		// now compare by lastName, street, contact1, contact2, contact3
		int returnValue = 0;
		if (returnValue == 0) {
			returnValue = CompareUtil.compareWithNullsLast(getLastName(), o.getLastName());
		}
		if (returnValue == 0) {
			returnValue = CompareUtil.compareWithNullsLast(getStreet(), o.getStreet());
		}
		if (returnValue == 0) {
			returnValue = CompareUtil.compareWithNullsLast(getContact1(), o.getContact1());
		}
		if (returnValue == 0) {
			returnValue = CompareUtil.compareWithNullsLast(getContact2(), o.getContact2());
		}
		if (returnValue == 0) {
			returnValue = CompareUtil.compareWithNullsLast(getContact3(), o.getContact3());
		}
		return returnValue;
	}

	@Override
	public boolean equals(final Object o) {
		if (o instanceof Family f) {
			return 0 == compareTo(f);
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
