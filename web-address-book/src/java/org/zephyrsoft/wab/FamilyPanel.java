package org.zephyrsoft.wab;

import echopoint.*;
import org.zephyrsoft.wab.model.*;
import org.zephyrsoft.wab.util.*;
import nextapp.echo.app.*;
import nextapp.echo.app.event.*;

public class FamilyPanel extends Panel {

	private static final long serialVersionUID = -7732074007710939064L;
	
	private Family family = null;
	
	private Row titleRow = null; // title row with family data and "delete family" button
	private Column detailColumn = null; // contains the personsColumn (see below) and the "add member" button
	private Column personsColumn = null; // contains all person panels that exist
	
	private Button deleteFamily = null;
	private Button addMember = null;
	
	private TextField lastName = null;
	private TextField street = null;
	private TextField postalCode = null;
	private TextField city = null;
	private TextField contact1 = null;
	private TextField contact2 = null;
	private TextField contact3 = null;
	private TextField remarks = null;
	
	public FamilyPanel(Family family) {
		super();
		
		this.family = family;
		
		if (this.family==null) {
			this.family = new Family();
		}
		initView();
	}
	
	private void initView() {
		// create instances
		titleRow = new Row();
		detailColumn = new Column();
		personsColumn = new Column();
		deleteFamily = new Button("delete this family");
		addMember = new Button("add member");
		lastName = new KeystrokeTextField(500);
		street = new KeystrokeTextField(500);
		postalCode = new KeystrokeTextField(500);
		city = new KeystrokeTextField(500);
		contact1 = new KeystrokeTextField(500);
		contact2 = new KeystrokeTextField(500);
		contact3 = new KeystrokeTextField(500);
		remarks = new KeystrokeTextField(500);
		
		// set start values
		lastName.setText(family.getLastName());
		street.setText(family.getStreet());
		postalCode.setText(family.getPostalCode());
		city.setText(family.getCity());
		contact1.setText(family.getContact1());
		contact2.setText(family.getContact2());
		contact3.setText(family.getContact3());
		remarks.setText(family.getRemarks());
		
		// top level container
		Column topColumn = new Column();
        topColumn.setCellSpacing(new Extent(10));
        add(topColumn);
        
        // title row
        lastName.setToolTipText("last name");
		street.setToolTipText("street");
		postalCode.setToolTipText("postal code");
		city.setToolTipText("city");
		contact1.setToolTipText("phone/mobile/email 1");
		contact2.setToolTipText("phone/mobile/email 2");
		contact3.setToolTipText("phone/mobile/email 3");
		remarks.setToolTipText("remarks");
        titleRow.add(lastName);
        titleRow.add(street);
        titleRow.add(postalCode);
        titleRow.add(city);
        titleRow.add(contact1);
        titleRow.add(contact2);
        titleRow.add(contact3);
        titleRow.add(remarks);
        deleteFamily.setBackground(Color.LIGHTGRAY);
        deleteFamily.setInsets(new Insets(4));
        titleRow.add(deleteFamily);
        topColumn.add(titleRow);
        
        // fill persons column
        for (Person p : family.getMembers()) {
        	personsColumn.add(new PersonPanel(p));
        }
        
        // build details area
        detailColumn.setInsets(new Insets(40, 0, 0, 0));
        detailColumn.add(personsColumn);
        addMember.setBackground(Color.LIGHTGRAY);
        addMember.setInsets(new Insets(4));
        Row addMemberRow = new Row();
        addMemberRow.add(addMember);
        detailColumn.add(addMemberRow);
        topColumn.add(detailColumn);
        
        // bind view to model
        DataUtil.bindTextfield(lastName, family, "lastName");
        DataUtil.bindTextfield(street, family, "street");
        DataUtil.bindTextfield(postalCode, family, "postalCode");
        DataUtil.bindTextfield(city, family, "city");
        DataUtil.bindTextfield(contact1, family, "contact1");
        DataUtil.bindTextfield(contact2, family, "contact2");
        DataUtil.bindTextfield(contact3, family, "contact3");
        DataUtil.bindTextfield(remarks, family, "remarks");
	}

	public Family getFamily() {
		return family;
	}
}
