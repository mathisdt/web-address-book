package org.zephyrsoft.wab;

import echopoint.*;
import org.zephyrsoft.wab.model.*;
import org.zephyrsoft.wab.util.*;
import nextapp.echo.app.*;
import nextapp.echo.app.Border.Side;
import nextapp.echo.app.event.*;

public class FamilyPanel extends Panel {

	private static final long serialVersionUID = -7732074007710939064L;
	
	private Family family = null;
	
	private Row titleRow = null; // title row with family data and "delete family" button
	private Column detailColumn = null; // contains the personsColumn (see below) and the "add member" button
	private Column personsColumn = null; // contains all person panels that exist
	
	private Button deleteFamily = null;
	private Button addPerson = null;
	
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
		deleteFamily = new Button("delete family");
		addPerson = new Button("add person");
		lastName = new KeystrokeTextField(EchoUtil.KEYSTROKE_SEND_INTERVAL);
		street = new KeystrokeTextField(EchoUtil.KEYSTROKE_SEND_INTERVAL);
		postalCode = new KeystrokeTextField(EchoUtil.KEYSTROKE_SEND_INTERVAL);
		city = new KeystrokeTextField(EchoUtil.KEYSTROKE_SEND_INTERVAL);
		contact1 = new KeystrokeTextField(EchoUtil.KEYSTROKE_SEND_INTERVAL);
		contact2 = new KeystrokeTextField(EchoUtil.KEYSTROKE_SEND_INTERVAL);
		contact3 = new KeystrokeTextField(EchoUtil.KEYSTROKE_SEND_INTERVAL);
		remarks = new KeystrokeTextField(EchoUtil.KEYSTROKE_SEND_INTERVAL);
		
		// set start values
		lastName.setText(family.getLastName());
		street.setText(family.getStreet());
		postalCode.setText(family.getPostalCode());
		city.setText(family.getCity());
		contact1.setText(family.getContact1());
		contact2.setText(family.getContact2());
		contact3.setText(family.getContact3());
		remarks.setText(family.getRemarks());
		
		// borders
		Side[] border = new Side[] {new Side(new Extent(10), Color.YELLOW, Border.STYLE_SOLID), new Side(new Extent(0), Color.BLACK, Border.STYLE_NONE), new Side(new Extent(0), Color.BLACK, Border.STYLE_NONE), new Side(new Extent(0), Color.BLACK, Border.STYLE_NONE)};
		setBorder(new Border(border));
		setInsets(new Insets(new Extent(0), new Extent(0), new Extent(0), new Extent(10)));
		
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
        EchoUtil.layoutAsButton(deleteFamily);
        titleRow.add(deleteFamily);
        topColumn.add(titleRow);
        
        // fill persons column
        if (family.getMembers()!=null) {
	        for (Person p : family.getMembers()) {
	        	personsColumn.add(new PersonPanel(p));
	        }
        }
        
        // build details area
        detailColumn.setInsets(new Insets(40, 0, 0, 0));
        detailColumn.add(personsColumn);
        EchoUtil.layoutAsButton(addPerson);
        Row addPersonRow = new Row();
        addPersonRow.add(addPerson);
        detailColumn.add(addPersonRow);
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
        
        // Actions
        addPerson.addActionListener(new ActionListener() {
			private static final long serialVersionUID = -4798229206323253003L;
			public void actionPerformed(ActionEvent e) {
				try {
					// add new person to this family
					Person person = null;
					DataUtil.beginTransaction();
					person = new Person();
					getFamily().addMember(person);
					DataUtil.save(getFamily());
					DataUtil.commitTransaction();
					// add view for new person
					personsColumn.add(new PersonPanel(person));
				} catch (Exception ex) {
					ex.printStackTrace();
				} finally {
					DataUtil.endTransaction();
				}
			}
		});
        deleteFamily.addActionListener(new ActionListener() {
			private static final long serialVersionUID = -4798229206323253001L;
			public void actionPerformed(ActionEvent e) {
				try {
					// delete family from database
					DataUtil.beginTransaction();
					DataUtil.delete(getFamily());
					DataUtil.commitTransaction();
					// hide view for this family
					getParent().remove(FamilyPanel.this);
				} catch (Exception ex) {
					ex.printStackTrace();
				} finally {
					DataUtil.endTransaction();
				}
			}
		});
	}

	public Family getFamily() {
		return family;
	}
}
