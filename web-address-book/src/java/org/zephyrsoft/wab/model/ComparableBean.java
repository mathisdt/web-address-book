package org.zephyrsoft.wab.model;

/**
 * superclass for any data beans unsed in this applications
 * 
 * @author Mathis Dirksen-Thedens
 */
public abstract class ComparableBean<T> implements Comparable<T> {

	public abstract int compareTo(T o);
	
}
