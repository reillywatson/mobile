package com.vasken.util;

import java.util.HashMap;
import java.util.LinkedList;

// Only touches items that are retrieved through get(); iterating over the elements has no effect.
// Not synchronised at all...
public class MRUCache<T, U> extends HashMap<T, U> {
	private static final long serialVersionUID = 1L;
	private LinkedList<T> useList = new LinkedList<T>();
	private int capacity;
	
	public MRUCache(int capacity) {
		this.capacity = capacity;
	}
	
	@SuppressWarnings("unchecked")
	public U get(Object key) {
		U val = super.get(key);
		if (val != null) {
			useList.remove(key);
			useList.addFirst((T)key);
		}
		return val;
	}

	public U put(T key, U val) {
		super.put(key, val);
		useList.addFirst(key);
		if (useList.size() > capacity) {
			T removeKey = useList.removeLast();
			super.remove(removeKey);
		}
		return val;
	}

	@SuppressWarnings("unchecked")
	public U remove(Object key) {
		useList.remove((T)key);
		return super.remove(key);
	}
}
