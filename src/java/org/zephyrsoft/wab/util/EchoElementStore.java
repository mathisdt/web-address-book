package org.zephyrsoft.wab.util;

import java.util.*;

/**
 * stores reusable Echo3 elements, e.g. images
 * 
 * @author Mathis Dirksen-Thedens
 */
public class EchoElementStore {
	
	private Map<String, Object> elements = new HashMap<String, Object>();

	public boolean containsKey(String key) {
		return elements.containsKey(key);
	}

	public Object get(String key) {
		return elements.get(key);
	}

	public void put(String key, Object value) {
		elements.put(key, value);
	}
	
}
