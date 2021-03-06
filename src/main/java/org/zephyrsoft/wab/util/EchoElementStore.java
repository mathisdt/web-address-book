package org.zephyrsoft.wab.util;

import java.util.HashMap;
import java.util.Map;

/**
 * stores reusable Echo3 elements, e.g. images
 */
public class EchoElementStore {

	private Map<String, Object> elements = new HashMap<>();

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
