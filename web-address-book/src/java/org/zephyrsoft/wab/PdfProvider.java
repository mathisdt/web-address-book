package org.zephyrsoft.wab;

import java.io.*;
import java.text.*;
import java.util.*;
import java.util.List;
import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import org.h2.util.*;
import org.supercsv.io.*;
import org.supercsv.prefs.*;
import org.zephyrsoft.wab.model.*;
import org.zephyrsoft.wab.report.*;
import org.zephyrsoft.wab.util.*;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.export.*;
import net.sf.jasperreports.engine.util.*;
import nextapp.echo.filetransfer.app.*;

public class PdfProvider implements DownloadProvider {
	
	private String fileName = Constants.EMPTY_STRING;
	private String printableDate = Constants.EMPTY_STRING;
	private ByteArrayOutputStream outStream = null;
	
	public PdfProvider() {
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd-HH-mm");
		SimpleDateFormat sdf2 = new SimpleDateFormat("dd.MM.yyyy");
		Date date = new Date();
		fileName = "Gemeindeverzeichnis-" + sdf1.format(date) + ".pdf";
		printableDate = sdf2.format(date);
		
		// load all families and build the pdf
		try {
			JasperReport jasperReport = (JasperReport)JRLoader.loadObject(getClass().getResourceAsStream(Constants.REPORT_TEMPLATE));
			
			// add peremeters "logo" and "date"
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put(Constants.LOGO, getClass().getResourceAsStream(Constants.LOGO_IMAGE));
			parameters.put(Constants.DATE, "Stand: " + printableDate);
			
			JasperPrint jasperPrint = 
				JasperFillManager.fillReport(jasperReport, parameters, new WABDataSource(DataUtil.find(Family.class).findList()));
			
			JRPdfExporter exporter = new JRPdfExporter();
			
			exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
			ByteArrayOutputStream outStreamRaw = new ByteArrayOutputStream();
			exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, outStreamRaw);
			
			exporter.exportReport();
			
			// now merge the A5 pages to A4 pages
			outStream = new ByteArrayOutputStream();
			PdfReader reader = new PdfReader(outStreamRaw.toByteArray());
	        Document document = new Document(PageSize.A4, 0, 0, 0, 0);
	        PdfWriter writer = PdfWriter.getInstance(document, outStream);
	        document.open();
	        PdfContentByte cb = writer.getDirectContent();
	        PdfImportedPage page;
	        float offsetY;
	        int total = reader.getNumberOfPages();
	        for (int i = 1; i <= total; i++) {
	            if (i % 2 == 1) {
	                document.newPage();
	            }
	            offsetY = (i % 2 == 1 ? PageSize.A4.getHeight() : PageSize.A4.getHeight() / 2);
	            page = writer.getImportedPage(reader, i);
//	            cb.addTemplate(page, 1, 0, 0, 1, 0, offsetY); // portrait mode
	            cb.addTemplate(page, 0, -1, 1, 0, 0, offsetY); // landscape mode
	        }
	        document.close();
			writer.flush();
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Output the report, use the target file name "/tmp/adressen.pdf".
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			// start ebean server and underlying structures
			ContextListener cl = new ContextListener();
			cl.contextInitialized(null);
			// output report
			PdfProvider provider = new PdfProvider();
			FileOutputStream out = new FileOutputStream(new File("/tmp/adressen.pdf"));
			provider.writeFile(out);
			out.close();
			// stop ebean server and underlying structures
			cl.contextDestroyed(null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public String getContentType() {
		return Constants.APPLICATION_PDF;
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
