package org.zephyrsoft.wab;

import echopoint.*;
import org.zephyrsoft.wab.model.*;
import org.zephyrsoft.wab.util.*;
import nextapp.echo.app.*;
import nextapp.echo.app.event.*;

/**
 * container for the UI elements of a person (as member of a family)
 * 
 * @author Mathis Dirksen-Thedens
 */
public class PersonPanel extends Panel {

	private static final long serialVersionUID = -5291254570310018206L;
	
	private Person person = null;
	
	private Row titleRow = null;
	
	private Button deletePerson = null;
	private Button moveUp = null;
	private Button moveDown = null;
	
	private TextField firstName = null;
	private TextField lastName = null;
	private TextField birthday = null;
	private TextField contact1 = null;
	private TextField contact2 = null;
	private TextField contact3 = null;
	private TextField remarks = null;

	private final FamilyPanel parent;

	private final EchoElementStore elements;
	
	public PersonPanel(FamilyPanel parent, EchoElementStore elements, Person person) {
		super();
		this.parent = parent;
		this.elements = elements;
		this.person = person;
		
		if (this.person==null) {
			this.person = new Person();
		}
		initView();
	}
	
	private void initView() {
		// create instances
		titleRow = new Row();
		deletePerson = EchoUtil.createButton(null, "delete person", (ResourceImageReference)elements.get(Constants.BUTTON_DELETE_PERSON));
		moveUp = new Button();
		moveUp.setToolTipText("move up");
		moveDown = new Button();
		moveDown.setToolTipText("move down");
		firstName = new KeystrokeTextField(Constants.KEYSTROKE_SEND_INTERVAL);
		lastName = new KeystrokeTextField(Constants.KEYSTROKE_SEND_INTERVAL);
		birthday = new KeystrokeTextField(Constants.KEYSTROKE_SEND_INTERVAL);
		contact1 = new KeystrokeTextField(Constants.KEYSTROKE_SEND_INTERVAL);
		contact2 = new KeystrokeTextField(Constants.KEYSTROKE_SEND_INTERVAL);
		contact3 = new KeystrokeTextField(Constants.KEYSTROKE_SEND_INTERVAL);
		remarks = new KeystrokeTextField(Constants.KEYSTROKE_SEND_INTERVAL);
		
		// set start values
		firstName.setText(person.getFirstName());
		lastName.setText(person.getLastName());
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
        lastName.setToolTipText("last name" + " (if different from family name)");
		birthday.setToolTipText("birthday");
		contact1.setToolTipText("phone/mobile/email 1");
		contact2.setToolTipText("phone/mobile/email 2");
		contact3.setToolTipText("phone/mobile/email 3");
		remarks.setToolTipText("remarks");
		
		Column moveButtons = new Column();
		moveButtons.setCellSpacing(new Extent(0));
		moveButtons.add(moveUp);
        moveButtons.add(moveDown);
        titleRow.add(moveButtons);
		
        titleRow.add(EchoUtil.createSmallLabel(firstName, "first name"));
        titleRow.add(EchoUtil.createSmallLabel(lastName, "last name"));
        titleRow.add(EchoUtil.createSmallLabel(birthday, "birthday"));
        titleRow.add(EchoUtil.createSmallLabel(contact1, "phone/mobile/email 1"));
        titleRow.add(EchoUtil.createSmallLabel(contact2, "phone/mobile/email 2"));
        titleRow.add(EchoUtil.createSmallLabel(contact3, "phone/mobile/email 3"));
        titleRow.add(EchoUtil.createSmallLabel(remarks, "remarks"));
        titleRow.add(deletePerson);
        topColumn.add(titleRow);
        
        // bind view to model
        DataUtil.bindTextfield(firstName, person, Constants.ATTRIBUTE_FIRST_NAME);
        DataUtil.bindTextfield(lastName, person, Constants.ATTRIBUTE_LAST_NAME);
        DataUtil.bindTextfield(birthday, person, Constants.ATTRIBUTE_BIRTHDAY);
        DataUtil.bindTextfield(contact1, person, Constants.ATTRIBUTE_CONTACT1);
        DataUtil.bindTextfield(contact2, person, Constants.ATTRIBUTE_CONTACT2);
        DataUtil.bindTextfield(contact3, person, Constants.ATTRIBUTE_CONTACT3);
        DataUtil.bindTextfield(remarks, person, Constants.ATTRIBUTE_REMARKS);
        
		checkButtonActivation();
        
        // Actions
        deletePerson.addActionListener(new ActionListener() {
			private static final long serialVersionUID = -4798229206323253003L;
			public void actionPerformed(ActionEvent e) {
				try {
					// delete person from database after reloading it by ID (to prevent an OptimisticLockException)
					DataUtil.beginTransaction();
					Person toDelete = DataUtil.find(Person.class, getPerson().getId());
					if (toDelete != null) {
						toDelete.getFamily().removeMember(toDelete);
						DataUtil.delete(toDelete);
						parent.reloadFamilyMembers();
					}
					DataUtil.commitTransaction();
					// hide view
					parent.removePersonPanel(PersonPanel.this);
				} catch (Exception ex) {
					ex.printStackTrace();
				} finally {
					DataUtil.endTransaction();
				}
			}
		});
        moveUp.addActionListener(new ActionListener() {
			private static final long serialVersionUID = -4798229206323253005L;
			public void actionPerformed(ActionEvent e) {
				try {
					// delete person from database
					DataUtil.beginTransaction();
					HasContacts switchedWith = getPerson().getFamily().moveUp(getPerson());
					DataUtil.save(switchedWith);
					DataUtil.save(getPerson());
					DataUtil.commitTransaction();
					parent.reorderPersonPanels();
				} catch (Exception ex) {
					ex.printStackTrace();
				} finally {
					DataUtil.endTransaction();
				}
			}
		});
        moveDown.addActionListener(new ActionListener() {
			private static final long serialVersionUID = -4798229206323253007L;
			public void actionPerformed(ActionEvent e) {
				try {
					// delete person from database
					DataUtil.beginTransaction();
					HasContacts switchedWith = getPerson().getFamily().moveDown(getPerson());
					DataUtil.save(switchedWith);
					DataUtil.save(getPerson());
					DataUtil.commitTransaction();
					parent.reorderPersonPanels();
				} catch (Exception ex) {
					ex.printStackTrace();
				} finally {
					DataUtil.endTransaction();
				}
			}
		});
	}

	public final void checkButtonActivation() {
		boolean mayMoveUp = getPerson().getFamily().mayMoveUp(getPerson());
		boolean mayMoveDown = getPerson().getFamily().mayMoveDown(getPerson());
		// set state
		moveUp.setEnabled(mayMoveUp);
		moveDown.setEnabled(mayMoveDown);
		// set icons
		moveUp.setIcon(mayMoveUp ? EchoUtil.createImage(Constants.BUTTON_UP_GREEN) : EchoUtil.createImage(Constants.BUTTON_UP_GREY));
		moveDown.setIcon(mayMoveDown ? EchoUtil.createImage(Constants.BUTTON_DOWN_GREEN) : EchoUtil.createImage(Constants.BUTTON_DOWN_GREY));
	}
	
	public final Person getPerson() {
		return person;
	}
}
