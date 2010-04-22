package org.zephyrsoft.wab;

import java.io.*;
import java.util.*;
import com.avaje.ebean.*;
import com.avaje.ebeaninternal.server.lib.cron.*;
import org.zephyrsoft.wab.model.*;
import org.zephyrsoft.wab.util.*;
import nextapp.echo.app.*;
import nextapp.echo.app.Font.*;
import nextapp.echo.app.event.*;
import nextapp.echo.filetransfer.app.*;

public class Screen extends ContentPane {
	
	private static final long serialVersionUID = 3483818110434112144L;
	
	private AddressBookApp app = null;
	
	private Button downloadPdf = null;
	private Column data = null;
	
	public Screen(AddressBookApp app) {
		super();
        this.app = app;
        Column topColumn = new Column();
        topColumn.setInsets(new Insets(30));
        topColumn.setCellSpacing(new Extent(10));
        add(topColumn);
        Label title = new Label("Web Address Book");
        title.setFont(new Font(new Typeface("Arial"), Font.BOLD + Font.UNDERLINE, new Extent(20)));
        topColumn.add(title);
        downloadPdf = new Button("Download as PDF");
        downloadPdf.setBackground(Color.LIGHTGRAY);
        downloadPdf.setInsets(new Insets(4));
        downloadPdf.addActionListener(new ActionListener() {
			private static final long serialVersionUID = 9132114868976616925L;
			public void actionPerformed(ActionEvent e) {
				downloadPdf();
			}
		});
        Row buttonRow = new Row();
        buttonRow.add(downloadPdf);
        topColumn.add(buttonRow);
        
        data = new Column();
        topColumn.add(data);
        
        List<Family> families = DataUtil.createQuery(Family.class).findList();
        
        for (Family f : families) {
        	FamilyPanel panel = new FamilyPanel(f);
        	data.add(panel);
        }
	}
	
	private void downloadPdf() {
		// TODO move to controller
		DownloadProvider provider = app.getPdfProvider();
		app.enqueueCommand(new DownloadCommand(provider));
		    
	}
	
}
