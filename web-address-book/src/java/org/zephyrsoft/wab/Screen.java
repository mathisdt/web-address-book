package org.zephyrsoft.wab;

import java.util.*;
import nextapp.echo.app.*;
import nextapp.echo.app.Border.Side;
import nextapp.echo.app.event.*;
import nextapp.echo.filetransfer.app.*;
import org.apache.commons.collections.list.*;
import org.zephyrsoft.wab.model.*;
import org.zephyrsoft.wab.util.*;

/**
 * container for the whole application's UI
 * 
 * @author Mathis Dirksen-Thedens
 */
public class Screen extends ContentPane {
	
	private static final long serialVersionUID = 3483818110434112144L;
	
	private AddressBookApp app = null;
	
	private Button downloadPdf = null;
	private Button addFamily = null;
	private Column data = null;
	
	public Screen(AddressBookApp app) {
		super();
        this.app = app;
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
			public void actionPerformed(ActionEvent e) {
				downloadPdf();
			}
		});
        addFamily = EchoUtil.createButton("add family", null, Constants.BUTTON_ADD_FAMILY);
        addFamily.addActionListener(new ActionListener() {
			private static final long serialVersionUID = 9132114868976616923L;
			public void actionPerformed(ActionEvent e) {
				try {
					// add family
					Family family = null;
					DataUtil.beginTransaction();
					family = new Family();
					DataUtil.save(family);
					DataUtil.commitTransaction();
					// add view for new family
					data.add(new FamilyPanel(family));
				} catch (Exception ex) {
					ex.printStackTrace();
				} finally {
					DataUtil.endTransaction();
				}
			}
		});
        Row buttonRow = new Row();
        buttonRow.add(downloadPdf);
        topColumn.add(buttonRow);
        
        data = new Column();
        Side[] border = new Side[] {new Side(new Extent(0), Color.BLACK, Border.STYLE_NONE), new Side(new Extent(0), Color.BLACK, Border.STYLE_NONE), new Side(new Extent(10), Color.YELLOW, Border.STYLE_SOLID), new Side(new Extent(0), Color.BLACK, Border.STYLE_NONE)};
		data.setBorder(new Border(border));
        topColumn.add(data);
        
        List<Family> families = DataUtil.createQuery(Family.class).findList();
        Collections.sort(families);
        
        for (Family f : families) {
        	FamilyPanel panel = new FamilyPanel(f);
        	data.add(panel);
        }
        
        Row bottomRow = new Row();
        bottomRow.setCellSpacing(new Extent(10));
        bottomRow.add(addFamily);
        topColumn.add(bottomRow);
	}
	
	private void downloadPdf() {
		// TODO move to controller
		DownloadProvider provider = new PdfProvider();
		app.enqueueCommand(new DownloadCommand(provider));
		    
	}
	
}
