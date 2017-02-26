package org.zephyrsoft.wab;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.zephyrsoft.wab.model.Family;
import org.zephyrsoft.wab.model.Person;
import org.zephyrsoft.wab.report.ReportLoader;
import org.zephyrsoft.wab.report.SimpleDataSource;
import org.zephyrsoft.wab.util.DataUtil;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.util.JRLoader;
import nextapp.echo.filetransfer.app.DownloadProvider;

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
			parameters.put(Constants.PERSON_SUBREPORT, ReportLoader.loadLayout(Constants.PERSON));
			
			// the sorting of the families is done inside buildDataSource()
			JRDataSource dataSource = buildDataSource();
			
			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);
			
			JRPdfExporter exporter = new JRPdfExporter();
			
			exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
			outStream = new ByteArrayOutputStream();
			exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, outStream);
			
			exporter.exportReport();
		} catch (JRException e) {
			// JasperReports
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
