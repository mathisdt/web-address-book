package org.zephyrsoft.wab.model;

/**
 * 
 * 
 * @author Mathis Dirksen-Thedens
 */
public abstract class ComparableBean<T> implements Comparable<T> {

	public abstract int compareTo(T o);
	
}
