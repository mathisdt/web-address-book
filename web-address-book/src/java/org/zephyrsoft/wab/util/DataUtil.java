package org.zephyrsoft.wab.util;

import java.beans.*;
import java.io.*;
import java.lang.reflect.*;
import java.util.*;
import javax.persistence.*;
import com.avaje.ebean.*;
import com.avaje.ebean.Query;
import org.zephyrsoft.wab.*;
import nextapp.echo.app.*;
import nextapp.echo.app.event.*;

public class DataUtil {
	
	private static EbeanServer getEbeanServerInstance() {
		// fetch the one and only instance from the context listener
		return ContextListener.getInstance().getEbeanServer();
	}
	
	public static Transaction beginTransaction() {
		Transaction t = getEbeanServerInstance().beginTransaction();
		t.setPersistCascade(true);
		return t;
	}

	public static void commitTransaction() {
		getEbeanServerInstance().commitTransaction();
	}

	public static <T> Query<T> createQuery(Class<T> beanType) {
		return getEbeanServerInstance().createQuery(beanType);
	}

	public static void delete(Object bean) throws OptimisticLockException {
		getEbeanServerInstance().delete(bean);
	}

	public static void endTransaction() {
		getEbeanServerInstance().endTransaction();
	}

	public static <T> Query<T> find(Class<T> beanType) {
		return getEbeanServerInstance().find(beanType);
	}

	public static Object nextId(Class<?> beanType) {
		return getEbeanServerInstance().nextId(beanType);
	}

	public static void rollbackTransaction() {
		getEbeanServerInstance().rollbackTransaction();
	}

	public static void save(Object bean) throws OptimisticLockException {
		getEbeanServerInstance().save(bean);
	}

	public static void update(Object bean) {
		getEbeanServerInstance().update(bean);
	}

	/**
	 * bind an Echo3 textfield to a String property of a data model
	 * @param textfield view
	 * @param instance model instance
	 * @param property name of property to bind
	 */
	public static void bindTextfield(TextField textfield, Object instance, String property) {
		// create an event listener managing both directions of change
		new TextfieldBinding(textfield, instance, property);
	}
	
	private static class TextfieldBinding implements PropertyChangeListener, ActionListener {
		private static final long serialVersionUID = 2299534198052432679L;
		private TextField textfield = null;
		private Object instance = null;
		private String property = null;
		private Method getter = null;
		private Method setter = null;
		private PropertyChangeSupport beanPropertyChangeSupport = null;
		private boolean changeInProgress = false;
		
		public TextfieldBinding(TextField textfield, Object instance, String property) {
			this.textfield = textfield;
			this.instance = instance;
			this.property = property;
			try {
				getter = instance.getClass().getMethod(getGetterName(property));
				setter = instance.getClass().getMethod(getSetterName(property), String.class);
			} catch (Exception e) {
				throw new IllegalArgumentException(Constants.PROPERTY_COULD_NOT_BE_BOUND, e);
			}
			// add myself as listener
			textfield.addActionListener(this);
			beanPropertyChangeSupport = new PropertyChangeSupport(instance);
			beanPropertyChangeSupport.addPropertyChangeListener(property, this);
		}

		public void propertyChange(PropertyChangeEvent evt) {
			if (!changeInProgress) {
				synchronized (this) {
					changeInProgress = true;
					// set textfield
					textfield.setText((String)evt.getNewValue());
					changeInProgress = false;
				}
			}
		}

		public void actionPerformed(ActionEvent ev) {
			if (!changeInProgress) {
				synchronized (this) {
					changeInProgress = true;
					// set bean property
					try {
						setter.invoke(instance, textfield.getText());
					} catch (Exception e) {
						throw new IllegalArgumentException(Constants.COULD_NOT_SET_BEAN_PROPERTY, e);
					}
					// save bean
					try {
						DataUtil.save(instance);
					} catch (OptimisticLockException ole) {
						// in case of error reload bean from database and use those values
						try {
							getEbeanServerInstance().refresh(instance);
							String text = (String)getter.invoke(instance);
							textfield.setText(text);
						} catch (Exception e) {
							throw new IllegalArgumentException(Constants.COULD_NOT_GET_BEAN_PROPERTY, e);
						}
					}
					changeInProgress = false;
				}
			}
		}
	}
	
	private static String getGetterName(String property) {
		if (property==null || property.length()==0) {
			throw new IllegalArgumentException(Constants.PROPERTY_NAME_LENGTH_PROBLEM);
		}
		return Constants.GET + property.substring(0, 1).toUpperCase() + property.substring(1);
	}
	
	private static String getSetterName(String property) {
		if (property==null || property.length()==0) {
			throw new IllegalArgumentException(Constants.PROPERTY_NAME_LENGTH_PROBLEM);
		}
		return Constants.SET + property.substring(0, 1).toUpperCase() + property.substring(1);
	}
}
