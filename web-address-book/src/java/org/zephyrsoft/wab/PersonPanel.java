package org.zephyrsoft.wab;

import echopoint.*;
import org.zephyrsoft.wab.model.*;
import org.zephyrsoft.wab.util.*;
import nextapp.echo.app.*;

public class PersonPanel extends Panel {

	private static final long serialVersionUID = -5291254570310018206L;
	
	private Person person = null;
	
	private Row titleRow = null;
	
	private Button deletePerson = null;
	
	private TextField firstName = null;
	private TextField birthday = null;
	private TextField contact1 = null;
	private TextField contact2 = null;
	private TextField contact3 = null;
	private TextField remarks = null;
	
	public PersonPanel(Person person) {
		super();
		
		this.person = person;
		
		if (this.person==null) {
			this.person = new Person();
		}
		initView();
	}
	
	private void initView() {
		// create instances
		titleRow = new Row();
		deletePerson = new Button("delete this person");
		firstName = new KeystrokeTextField(500);
		birthday = new KeystrokeTextField(500);
		contact1 = new KeystrokeTextField(500);
		contact2 = new KeystrokeTextField(500);
		contact3 = new KeystrokeTextField(500);
		remarks = new KeystrokeTextField(500);
		
		// set start values
		firstName.setText(person.getFirstName());
		birthday.setText(person.getBirthday());
		contact1.setText(person.getContact1());
		contact2.setText(person.getContact2());
		contact3.setText(person.getContact3());
		remarks.setText(person.getRemarks());
		
		// top level container
		Column topColumn = new Column();
        topColumn.setCellSpacing(new Extent(10));
        add(topColumn);
        
        // title row
        firstName.setToolTipText("first name");
		birthday.setToolTipText("birthday");
		contact1.setToolTipText("phone/mobile/email 1");
		contact2.setToolTipText("phone/mobile/email 2");
		contact3.setToolTipText("phone/mobile/email 3");
		remarks.setToolTipText("remarks");
        titleRow.add(firstName);
        titleRow.add(birthday);
        titleRow.add(contact1);
        titleRow.add(contact2);
        titleRow.add(contact3);
        titleRow.add(remarks);
        deletePerson.setBackground(Color.LIGHTGRAY);
        deletePerson.setInsets(new Insets(4));
        titleRow.add(deletePerson);
        topColumn.add(titleRow);
        
        // bind view to model
        DataUtil.bindTextfield(firstName, person, "firstName");
        DataUtil.bindTextfield(birthday, person, "birthday");
        DataUtil.bindTextfield(contact1, person, "contact1");
        DataUtil.bindTextfield(contact2, person, "contact2");
        DataUtil.bindTextfield(contact3, person, "contact3");
        DataUtil.bindTextfield(remarks, person, "remarks");
	}

	public Person getPerson() {
		return person;
	}
}
