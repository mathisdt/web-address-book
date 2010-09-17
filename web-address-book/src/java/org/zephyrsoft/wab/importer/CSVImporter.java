package org.zephyrsoft.wab.importer;

import java.io.*;
import java.util.*;
import org.supercsv.io.*;
import org.supercsv.prefs.*;
import org.zephyrsoft.wab.*;
import org.zephyrsoft.wab.model.*;
import org.zephyrsoft.wab.util.*;

public class CSVImporter {
	
	/**
	 * Import old data. Expects two parameters: 1. the family csv file and 2. the person csv file. 
	 * 
	 * ATTENTION: The csv files have to be in UTF-8 encoding!
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		ICsvListReader inFile = null;
		try {
			// start ebean server and underlying structures
			ContextListener cl = new ContextListener();
			cl.contextInitialized(null);
			// delete all families (including their persons)
			try {
				DataUtil.beginTransaction();
				for (Family f : DataUtil.createQuery(Family.class).findList()) {
					DataUtil.delete(f);
				}
				DataUtil.commitTransaction();
			} finally {
				DataUtil.endTransaction();
			}
			// now populate the families from the CSV file
			inFile = new CsvListReader(new FileReader(args[0]), CsvPreference.EXCEL_NORTH_EUROPE_PREFERENCE);
			Map<Integer, Family> oldIdToNewFamily = new HashMap<Integer, Family>();
			List<String> line;
			try {
				DataUtil.beginTransaction();
				while ((line = inFile.read()) != null) {
					Family f = new Family();
					f.setLastName(line.get(1));
					f.setStreet(line.get(2));
					f.setPostalCode(line.get(3));
					f.setCity(line.get(4));
					f.setContact1(line.get(5));
					f.setContact2(line.get(6));
					DataUtil.save(f);
					oldIdToNewFamily.put(Integer.valueOf(line.get(0)), f);
				}
				DataUtil.commitTransaction();
			} finally {
				DataUtil.endTransaction();
			}
			// now populate the persons from the CSV file
			inFile = new CsvListReader(new FileReader(args[1]), CsvPreference.EXCEL_NORTH_EUROPE_PREFERENCE);
			try {
				DataUtil.beginTransaction();
				while ((line = inFile.read()) != null) {
					Person p = new Person();
					p.setFirstName(line.get(1));
					p.setBirthday(line.get(2));
					p.setContact1(line.get(3));
					p.setContact2(line.get(4));
					Family f = oldIdToNewFamily.get(Integer.valueOf(line.get(0)));
					f.addMember(p);
					DataUtil.save(p);
					DataUtil.save(f);
				}
				DataUtil.commitTransaction();
			} finally {
				DataUtil.endTransaction();
			}
			// stop ebean server and underlying structures
			cl.contextInitialized(null);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (inFile!=null) {
					inFile.close();
				}
			} catch (IOException e) {
				// do nothing
			}
		}
		
	}
	
}
