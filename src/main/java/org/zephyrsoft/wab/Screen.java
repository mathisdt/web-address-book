package org.zephyrsoft.wab;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.springframework.util.StringUtils;
import org.zephyrsoft.wab.model.Family;
import org.zephyrsoft.wab.model.HasContacts;
import org.zephyrsoft.wab.model.Person;
import org.zephyrsoft.wab.util.DataUtil;
import org.zephyrsoft.wab.util.EchoElementStore;
import org.zephyrsoft.wab.util.EchoUtil;

import echopoint.HtmlLabel;
import nextapp.echo.app.Border;
import nextapp.echo.app.Border.Side;
import nextapp.echo.app.Button;
import nextapp.echo.app.Color;
import nextapp.echo.app.Column;
import nextapp.echo.app.ContentPane;
import nextapp.echo.app.Extent;
import nextapp.echo.app.Font;
import nextapp.echo.app.Insets;
import nextapp.echo.app.Label;
import nextapp.echo.app.Row;
import nextapp.echo.app.event.ActionEvent;
import nextapp.echo.app.event.ActionListener;
import nextapp.echo.filetransfer.app.DownloadCommand;
import nextapp.echo.filetransfer.app.DownloadProvider;

/**
 * container for the whole application's UI
 *
 * @author Mathis Dirksen-Thedens
 */
public class Screen extends ContentPane {

	private static final long serialVersionUID = 3483818110434112144L;

	private static final Pattern emailPattern = Pattern.compile("^\\S+@\\S+\\.\\S+");

	private EchoElementStore elements = null;

	private AddressBookApp app = null;

	private Button downloadPdf = null;
	private HtmlLabel mailLink = null;
	private Button addFamily = null;
	private Column data = null;

	public Screen(AddressBookApp app) {
		super();
		this.app = app;
		initReusableElements();

		List<Family> families = DataUtil.createQuery(Family.class).findList();
		Collections.sort(families);

		Column topColumn = new Column();
		topColumn.setInsets(new Insets(30));
		topColumn.setCellSpacing(new Extent(10));
		add(topColumn);
		Label title = new Label("Web Address Book");
		title.setFont(new Font(Font.ARIAL, Font.BOLD + Font.UNDERLINE, new Extent(20)));
		topColumn.add(title);
		downloadPdf = EchoUtil.createButton("Download as PDF", null, Constants.BUTTON_REPORT);
		downloadPdf.addActionListener(new ActionListener() {
			private static final long serialVersionUID = 9132114868976616925L;

			@Override
			public void actionPerformed(ActionEvent e) {
				downloadPdf();
			}
		});
		String mailAddresses = families.stream()
			.flatMap(f -> {
				List<String> addresses = new LinkedList<>();
				extractEmailAddresses(f, addresses);
				return addresses.stream();
			})
			.map(String::toLowerCase)
			.distinct()
			.collect(Collectors.joining(","));
		mailLink = new HtmlLabel("<a href=\"mailto:?bcc=" + mailAddresses + "\">Send Email</a>");
		addFamily = EchoUtil.createButton("add family", null, Constants.BUTTON_ADD_FAMILY);
		addFamily.addActionListener(new ActionListener() {
			private static final long serialVersionUID = 9132114868976616923L;

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					// add family
					Family family = null;
					DataUtil.beginTransaction();
					family = new Family();
					DataUtil.save(family);
					DataUtil.commitTransaction();
					// add view for new family
					data.add(new FamilyPanel(family, elements));
				} catch (Exception ex) {
					ex.printStackTrace();
				} finally {
					DataUtil.endTransaction();
				}
			}
		});
		Row buttonRow = new Row();
		buttonRow.setCellSpacing(new Extent(15));
		buttonRow.add(downloadPdf);
		buttonRow.add(mailLink);
		topColumn.add(buttonRow);

		data = new Column();
		Side[] border = new Side[] { new Side(new Extent(0), Color.BLACK, Border.STYLE_NONE),
			new Side(new Extent(0), Color.BLACK, Border.STYLE_NONE),
			new Side(new Extent(10), Color.YELLOW, Border.STYLE_SOLID),
			new Side(new Extent(0), Color.BLACK, Border.STYLE_NONE) };
		data.setBorder(new Border(border));
		topColumn.add(data);

		for (Family f : families) {
			FamilyPanel panel = new FamilyPanel(f, elements);
			data.add(panel);
		}

		Row bottomRow = new Row();
		bottomRow.setCellSpacing(new Extent(10));
		bottomRow.add(addFamily);
		topColumn.add(bottomRow);
	}

	private static List<String> extractEmailAddresses(HasContacts hasContacts, List<String> emailAddresses) {
		addIfEmail(hasContacts.getContact1(), emailAddresses);
		addIfEmail(hasContacts.getContact2(), emailAddresses);
		addIfEmail(hasContacts.getContact3(), emailAddresses);
		if (hasContacts instanceof Family) {
			for (Person person : ((Family)hasContacts).getMembers()) {
				extractEmailAddresses(person, emailAddresses);
			}
		}
		return emailAddresses;
	}
	private static void addIfEmail(String contact, List<String> emailAddresses) {
		if (!StringUtils.isEmpty(contact) && emailPattern.matcher(contact.trim()).matches()) {
			emailAddresses.add(contact.trim());
		}
	}

	private void initReusableElements() {
		elements = new EchoElementStore();
		elements.put(Constants.BUTTON_UP_GREEN, EchoUtil.createImage(Constants.BUTTON_UP_GREEN));
		elements.put(Constants.BUTTON_UP_GREY, EchoUtil.createImage(Constants.BUTTON_UP_GREY));
		elements.put(Constants.BUTTON_DOWN_GREEN, EchoUtil.createImage(Constants.BUTTON_DOWN_GREEN));
		elements.put(Constants.BUTTON_DOWN_GREY, EchoUtil.createImage(Constants.BUTTON_DOWN_GREY));
		elements.put(Constants.BUTTON_DELETE_FAMILY, EchoUtil.createImage(Constants.BUTTON_DELETE_FAMILY));
		elements.put(Constants.BUTTON_ADD_PERSON, EchoUtil.createImage(Constants.BUTTON_ADD_PERSON));
		elements.put(Constants.BUTTON_DELETE_PERSON, EchoUtil.createImage(Constants.BUTTON_DELETE_PERSON));
	}

	private void downloadPdf() {
		DownloadProvider provider = new PdfProvider();
		app.enqueueCommand(new DownloadCommand(provider));

	}

}
