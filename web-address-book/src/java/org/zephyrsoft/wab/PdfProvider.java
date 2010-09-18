package org.zephyrsoft.wab;

import java.io.*;
import java.text.*;
import java.util.*;
import org.zephyrsoft.wab.model.*;
import org.zephyrsoft.wab.report.*;
import org.zephyrsoft.wab.util.*;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.export.*;
import net.sf.jasperreports.engine.util.*;
import nextapp.echo.filetransfer.app.*;

public class PdfProvider implements DownloadProvider {
	
	private String fileName = "";
	private String printableDate = "";
	
	private ByteArrayOutputStream outStream = null;
	
	public PdfProvider() {
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd-HH-mm");
		SimpleDateFormat sdf2 = new SimpleDateFormat("dd.MM.yyyy");
		Date date = new Date();
		fileName = "Gemeindeverzeichnis-" + sdf1.format(date) + ".pdf";
		printableDate = sdf2.format(date);
		
		// load all families and build the pdf
		try {
			JasperReport jasperReport = (JasperReport)JRLoader.loadObject(getClass().getResourceAsStream("/org/zephyrsoft/wab/report/print.jasper"));
			
			// add peremeters "logo" and "date"
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("logo", getClass().getResourceAsStream("/org/zephyrsoft/wab/report/dove.jpg"));
			parameters.put("date", "Stand: " + printableDate);
			
			JasperPrint jasperPrint = 
				JasperFillManager.fillReport(jasperReport, parameters, new WABDataSource(DataUtil.find(Family.class).findList()));
			
			JRPdfExporter exporter = new JRPdfExporter();
			
			exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
			outStream = new ByteArrayOutputStream();
			exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, outStream);
			
			exporter.exportReport();
			
			
		} catch (JRException e) {
			e.printStackTrace();
		}
	}
	
	public String getContentType() {
		return "application/pdf";
	}
	
	public String getFileName() {
		return fileName;
	}
	
	public long getSize() {
		if (outStream==null) {
			return 0;
		} else {
			return outStream.size();
		}
	}
	
	public void writeFile(OutputStream out) throws IOException {
		out.write(outStream.toByteArray());
	}
	
}
