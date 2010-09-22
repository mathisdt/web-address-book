package org.zephyrsoft.wab.util;

public class CompareUtil {
	
	@SuppressWarnings("null")
	public static <T extends Comparable<T>> int compareWithNullsLast(T o1, T o2) {
		if (o1==null && o2==null) {
			return 0;
		} else if (o1==null && o2!=null) {
			return 1;
		} else if (o1!=null && o2==null) {
			return -1;
		} else {
			return o1.compareTo(o2);
		}
	}
	
}
