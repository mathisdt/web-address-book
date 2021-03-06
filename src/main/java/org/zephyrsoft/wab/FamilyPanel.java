package org.zephyrsoft.wab;

import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import nextapp.echo.app.Border;
import nextapp.echo.app.Border.Side;
import nextapp.echo.app.Button;
import nextapp.echo.app.Color;
import nextapp.echo.app.Column;
import nextapp.echo.app.Component;
import nextapp.echo.app.Extent;
import nextapp.echo.app.Insets;
import nextapp.echo.app.Panel;
import nextapp.echo.app.ResourceImageReference;
import nextapp.echo.app.Row;
import nextapp.echo.app.TextField;
import nextapp.echo.app.event.ActionEvent;
import nextapp.echo.app.event.ActionListener;

import org.zephyrsoft.wab.model.Family;
import org.zephyrsoft.wab.model.Person;
import org.zephyrsoft.wab.util.DataUtil;
import org.zephyrsoft.wab.util.EchoElementStore;
import org.zephyrsoft.wab.util.EchoUtil;

import echopoint.KeystrokeTextField;

/**
 * container for the UI elements of a family (including any member's UI elements as sub-panels)
 */
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

	private final EchoElementStore elements;

	public FamilyPanel(Family family, EchoElementStore elements) {
		super();

		this.family = family;
		this.elements = elements;

		if (this.family == null) {
			this.family = new Family();
		}
		initView();
	}

	private final void initView() {
		// create instances
		titleRow = new Row();
		detailColumn = new Column();
		personsColumn = new Column();
		deleteFamily = EchoUtil.createButton(null, "delete family", (ResourceImageReference) elements
			.get(Constants.BUTTON_DELETE_FAMILY));
		addPerson = EchoUtil.createButton("add person", null, (ResourceImageReference) elements
			.get(Constants.BUTTON_ADD_PERSON));
		lastName = new KeystrokeTextField(Constants.KEYSTROKE_SEND_INTERVAL);
		street = new KeystrokeTextField(Constants.KEYSTROKE_SEND_INTERVAL);
		postalCode = new KeystrokeTextField(Constants.KEYSTROKE_SEND_INTERVAL);
		city = new KeystrokeTextField(Constants.KEYSTROKE_SEND_INTERVAL);
		contact1 = new KeystrokeTextField(Constants.KEYSTROKE_SEND_INTERVAL);
		contact2 = new KeystrokeTextField(Constants.KEYSTROKE_SEND_INTERVAL);
		contact3 = new KeystrokeTextField(Constants.KEYSTROKE_SEND_INTERVAL);
		remarks = new KeystrokeTextField(Constants.KEYSTROKE_SEND_INTERVAL);

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
		Side[] border = new Side[] { new Side(new Extent(10), Color.YELLOW, Border.STYLE_SOLID),
			new Side(new Extent(0), Color.BLACK, Border.STYLE_NONE),
			new Side(new Extent(0), Color.BLACK, Border.STYLE_NONE),
			new Side(new Extent(0), Color.BLACK, Border.STYLE_NONE) };
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
		titleRow.add(EchoUtil.createSmallLabel(lastName, "last name"));
		titleRow.add(EchoUtil.createSmallLabel(street, "street"));
		titleRow.add(EchoUtil.createSmallLabel(postalCode, "postal code"));
		titleRow.add(EchoUtil.createSmallLabel(city, "city"));
		titleRow.add(EchoUtil.createSmallLabel(contact1, "phone/mobile/email 1"));
		titleRow.add(EchoUtil.createSmallLabel(contact2, "phone/mobile/email 2"));
		titleRow.add(EchoUtil.createSmallLabel(contact3, "phone/mobile/email 3"));
		titleRow.add(EchoUtil.createSmallLabel(remarks, "remarks"));
		titleRow.add(deleteFamily);
		topColumn.add(titleRow);

		// fill persons column
		if (family.getMembers() != null) {
			for (Person p : family.getMembers()) {
				personsColumn.add(new PersonPanel(FamilyPanel.this, elements, p));
			}
		}

		// build details area
		detailColumn.setInsets(new Insets(40, 0, 0, 0));
		detailColumn.add(personsColumn);
		Row addPersonRow = new Row();
		addPersonRow.add(addPerson);
		detailColumn.add(addPersonRow);
		topColumn.add(detailColumn);

		// bind view to model
		DataUtil.bindTextfield(lastName, family, Constants.ATTRIBUTE_LAST_NAME);
		DataUtil.bindTextfield(street, family, Constants.ATTRIBUTE_STREET);
		DataUtil.bindTextfield(postalCode, family, Constants.ATTRIBUTE_POSTAL_CODE);
		DataUtil.bindTextfield(city, family, Constants.ATTRIBUTE_CITY);
		DataUtil.bindTextfield(contact1, family, Constants.ATTRIBUTE_CONTACT1);
		DataUtil.bindTextfield(contact2, family, Constants.ATTRIBUTE_CONTACT2);
		DataUtil.bindTextfield(contact3, family, Constants.ATTRIBUTE_CONTACT3);
		DataUtil.bindTextfield(remarks, family, Constants.ATTRIBUTE_REMARKS);

		// Actions
		addPerson.addActionListener(new ActionListener() {
			private static final long serialVersionUID = -4798229206323253003L;

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					// add new person to this family
					Person person = null;
					DataUtil.beginTransaction();
					person = new Person();
					getFamily().addMember(person);
					DataUtil.save(person);
					DataUtil.save(getFamily());
					DataUtil.commitTransaction();
					// add view for new person
					personsColumn.add(new PersonPanel(FamilyPanel.this, elements, person));
				} catch (Exception ex) {
					ex.printStackTrace();
				} finally {
					DataUtil.endTransaction();
				}
			}
		});
		deleteFamily.addActionListener(new ActionListener() {
			private static final long serialVersionUID = -4798229206323253001L;

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					// delete family from database after reloading it by ID (to prevent an OptimisticLockException)
					DataUtil.beginTransaction();
					Family toDelete = DataUtil.find(Family.class, getFamily().getId());
					if (toDelete != null) {
						DataUtil.delete(toDelete);
					}
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

	public void reorderPersonPanels() {
		Component[] compononts = personsColumn.getComponents();
		Map<Person, PersonPanel> person2personPanel = new TreeMap<>();
		for (int i = 0; i < compononts.length; i++) {
			PersonPanel pp = (PersonPanel) compononts[i];
			person2personPanel.put(pp.getPerson(), pp);
		}
		for (Entry<Person, PersonPanel> p2pp : person2personPanel.entrySet()) {
			Person p = p2pp.getKey();
			PersonPanel pp = p2pp.getValue();
			if (p.getOrdering() != indexOf(pp)) {
				// rearrange the panel
				personsColumn.add(pp, p.getOrdering());
			}
			pp.checkButtonActivation();
		}
	}

	public final Family getFamily() {
		return family;
	}

	public void reloadFamilyMembers() {
		try {
			DataUtil.refreshMany(getFamily(), Constants.ATTRIBUTE_MEMBERS);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void removePersonPanel(PersonPanel personPanel) {
		personsColumn.remove(personPanel);
	}
}
