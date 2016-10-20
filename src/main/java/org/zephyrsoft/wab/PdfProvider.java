package org.zephyrsoft.wab;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.util.JRLoader;
import nextapp.echo.filetransfer.app.DownloadProvider;

import org.zephyrsoft.wab.model.Family;
import org.zephyrsoft.wab.model.Person;
import org.zephyrsoft.wab.report.ReportLoader;
import org.zephyrsoft.wab.report.SimpleDataSource;
import org.zephyrsoft.wab.util.DataUtil;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.PageSize;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfImportedPage;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfWriter;

/**
 * provider for a downloadable PDF file containing all data from the address book
 * 
 * @author Mathis Dirksen-Thedens
 */
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
			JasperReport jasperReport = (JasperReport) JRLoader.loadObject(getClass().getResourceAsStream(
				Constants.REPORT_TEMPLATE));
			
			// add parameters "logo" and "date"
			Map<String, Object> parameters = new HashMap<>();
			parameters.put(Constants.LOGO, getClass().getResourceAsStream(Constants.LOGO_IMAGE));
			parameters.put(Constants.DATE, "Stand: " + printableDate);
			parameters.put(Constants.HEADER_SUBREPORT, ReportLoader.loadLayout(Constants.HEADER));
			parameters.put(Constants.PERSON_SUBREPORT, ReportLoader.loadLayout(Constants.PERSON));
			
			// the sorting of the families is done inside buildDataSource()
			JRDataSource dataSource = buildDataSource();
			
			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);
			
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
				if (Math.abs(i) % 2 == 1) {
					document.newPage();
				}
				offsetY = (Math.abs(i) % 2 == 1 ? PageSize.A4.getHeight() : PageSize.A4.getHeight() / 2);
				page = writer.getImportedPage(reader, i);
				// cb.addTemplate(page, 1, 0, 0, 1, 0, offsetY); // portrait mode
				cb.addTemplate(page, 0, -1, 1, 0, 0, offsetY); // landscape mode
			}
			document.close();
			writer.flush();
			writer.close();
		} catch (JRException e) {
			// JasperReports
			e.printStackTrace();
		} catch (DocumentException e) {
			// iText
			e.printStackTrace();
		} catch (IOException e) {
			// basic I/O
			e.printStackTrace();
		}
	}
	
	private JRDataSource buildDataSource() {
		SimpleDataSource ret = new SimpleDataSource();
		
		List<Family> families = DataUtil.find(Family.class).findList();
		Collections.sort(families);
		for (Family f : families) {
			// add one line in the DS for every family
			ret.beginNewRow();
			ret.put(Constants.ATTRIBUTE_LAST_NAME, f.getLastName());
			ret.put(Constants.ATTRIBUTE_STREET, f.getStreet());
			ret.put(Constants.ATTRIBUTE_POSTAL_CODE, f.getPostalCode());
			ret.put(Constants.ATTRIBUTE_CITY, f.getCity());
			ret.put(Constants.ATTRIBUTE_CONTACT1, f.getContact1());
			ret.put(Constants.ATTRIBUTE_CONTACT2, f.getContact2());
			ret.put(Constants.ATTRIBUTE_CONTACT3, f.getContact3());
			SimpleDataSource members = new SimpleDataSource(Constants.ATTRIBUTE_MEMBERS);
			List<Person> persons = f.getMembers();
			Collections.sort(persons);
			for (Person p : persons) {
				// add one line in the DS for every member of the family
				members.beginNewRow();
				// the "first name" field contains also the person's last name if filled and different from family last
				// name
				String firstName = p.getFirstName();
				if (p.getLastName() != null && !p.getLastName().trim().isEmpty()
					&& !p.getLastName().equalsIgnoreCase(f.getLastName())) {
					firstName += Constants.BLANK + p.getLastName();
				}
				members.put(Constants.ATTRIBUTE_FIRST_NAME, firstName);
				members.put(Constants.ATTRIBUTE_BIRTHDAY, p.getBirthday());
				members.put(Constants.ATTRIBUTE_CONTACT1, p.getContact1());
				members.put(Constants.ATTRIBUTE_CONTACT2, p.getContact2());
				members.put(Constants.ATTRIBUTE_CONTACT3, p.getContact3());
			}
			ret.put(members);
		}
		
		return ret;
	}
	
	/**
	 * Output the report, use the target file name "/tmp/adressen.pdf".
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			// start ebean server and underlying structures
			ContextListener cl = ContextListener.getInstance();
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
	
	@Override
	public String getContentType() {
		return Constants.APPLICATION_PDF;
	}
	
	@Override
	public String getFileName() {
		return fileName;
	}
	
	@Override
	public long getSize() {
		if (outStream == null) {
			return 0;
		} else {
			return outStream.size();
		}
	}
	
	@Override
	public void writeFile(OutputStream out) throws IOException {
		out.write(outStream.toByteArray());
	}
	
}