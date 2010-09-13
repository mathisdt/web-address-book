package org.zephyrsoft.wab;

import java.io.*;
import java.text.*;
import java.util.*;
import nextapp.echo.filetransfer.app.*;

public class PdfProvider implements DownloadProvider {
	
	String fileName = "";
	String printableDate = "";
	
	public PdfProvider() {
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd-HH-mm");
		SimpleDateFormat sdf2 = new SimpleDateFormat("dd.MM.yyyy");
		Date date = new Date();
		fileName = "Gemeindeverzeichnis-" + sdf1.format(date) + ".pdf";
		printableDate = sdf2.format(date);
		// TODO load all families and build the pdf
		
		
		
		
		// REPORT_PARAMETER_MAP: "logo", "date"
		
		// hint: "person_first_name" has to contain also the last name (if filled and different from family's last name)!
		
		
		
	}
	
	public String getContentType() {
		// TODO
		return null;
	}
	
	public String getFileName() {
		return fileName;
	}
	
	public long getSize() {
		// TODO
		return 0;
	}
	
	public void writeFile(OutputStream out) throws IOException {
		// TODO
		
	}
	
}
