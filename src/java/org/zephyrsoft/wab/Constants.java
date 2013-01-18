package org.zephyrsoft.wab;

/**
 * constants defined for the whole application
 * 
 * @author Mathis Dirksen-Thedens
 */
public final class Constants {
	
	public static final int KEYSTROKE_SEND_INTERVAL = 50;
	
	public static final int ICON_PIXEL_SIZE = 16;
	
	public static final String REPORT_TEMPLATE = "/org/zephyrsoft/wab/report/print.jasper";
	public static final String LOGO_IMAGE = "/org/zephyrsoft/wab/report/dove.jpg";
	public static final String PERSON = "person";
	public static final String PERSON_SUBREPORT = "PERSON_SUBREPORT_DESIGN";
	
	public static final String BUTTON_REPORT = "/org/zephyrsoft/wab/ressources/approval.png";
	public static final String BUTTON_DOWN_GREEN = "/org/zephyrsoft/wab/ressources/down.png";
	public static final String BUTTON_DOWN_GREY = "/org/zephyrsoft/wab/ressources/down-grey.png";
	public static final String BUTTON_UP_GREEN = "/org/zephyrsoft/wab/ressources/up.png";
	public static final String BUTTON_UP_GREY = "/org/zephyrsoft/wab/ressources/up-grey.png";
	public static final String BUTTON_ADD_PERSON = "/org/zephyrsoft/wab/ressources/add-participant.png";
	public static final String BUTTON_DELETE_PERSON = "/org/zephyrsoft/wab/ressources/delete-participant.png";
	public static final String BUTTON_ADD_FAMILY = "/org/zephyrsoft/wab/ressources/user-group.png";
	public static final String BUTTON_DELETE_FAMILY = "/org/zephyrsoft/wab/ressources/delete-all-participants.png";
	
	public static final String EMPTY_STRING = "";
	public static final String BLANK = " ";
	public static final String DATE = "date";
	public static final String LOGO = "logo";
	public static final String APPLICATION_PDF = "application/pdf";
	public static final String DID_NOT_START_H2_SERVER = "didn't start h2 server - it's not configured in environment.properties";
	public static final String DID_NOT_STOP_H2_SERVER = "didn't stop h2 server - it's not configured in environment.properties";
	public static final String COULD_NOT_STOP_H2_SERVER = "couldn't stop h2 server - reference was null";
	public static final String COULD_NOT_START_H2_SERVER = "couldn't start h2 server - SQLException occurred";
	public static final String STOPPED_H2_SERVER = "stopped h2 server";
	public static final String STARTED_H2_SERVER = "started h2 server";
	public static final String DATABASE_NAME = "databaseName";
	public static final String USE_DEDICATED_DATABASE = "useDedicatedDatabase";
	public static final String PROPERTIES_NOT_FOUND = "could not find log4j or environment.properties";
	public static final String ENVIRONMENT_PROPERTIES = "/environment.properties";
	public static final String LOG4J_PROPERTIES = "/log4j.properties";
	public static final String ANOTHER_INSTANCE_EXISTS = "another instance of this class already exists";
	public static final String CL_NOT_INITIALIZED_CORRECTLY = "ContextListener was not initialized correctly";
	public static final String ATTRIBUTE_REMARKS = "remarks";
	public static final String ATTRIBUTE_CONTACT3 = "contact3";
	public static final String ATTRIBUTE_CONTACT2 = "contact2";
	public static final String ATTRIBUTE_CONTACT1 = "contact1";
	public static final String ATTRIBUTE_CITY = "city";
	public static final String ATTRIBUTE_POSTAL_CODE = "postalCode";
	public static final String ATTRIBUTE_STREET = "street";
	public static final String ATTRIBUTE_LAST_NAME = "lastName";
	public static final String ATTRIBUTE_FIRST_NAME = "firstName";
	public static final String ATTRIBUTE_BIRTHDAY = "birthday";
	public static final String ATTRIBUTE_MEMBERS = "members";
	public static final String ENTITY_FAMILY = "family";
	public static final String ENTITY_PERSON = "person";
	public static final String REGEX_UNDERSCORE = "_";
	public static final String PROBLEM_WITH_FIELD_NAME = "problem with field name ";
	public static final String GET = "get";
	public static final String SET = "set";
	public static final String COULD_NOT_GET_BEAN_PROPERTY = "could not get bean property";
	public static final String COULD_NOT_SET_BEAN_PROPERTY = "could not set bean property";
	public static final String PROPERTY_COULD_NOT_BE_BOUND = "property could not be bound";
	public static final String PROPERTY_NAME_LENGTH_PROBLEM = "property name has to have at least one character";
}
