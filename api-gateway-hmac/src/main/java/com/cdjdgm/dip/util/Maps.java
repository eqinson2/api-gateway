package com.cdjdgm.dip.util;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author guanjianghuai
 *
 */
public class Maps {
	private Map<Object, Object> map = null;
	
	private Maps(){
		map = new HashMap<>();
	}
	
	public static Maps init(){
		Maps map = new Maps();
		return map;
	}
	
	public static Maps init(Object key, Object value){
		Maps maps = new Maps();
		maps.put(key, value);
		return maps;
	}
	
	public Maps put(Object key, Object value){
		map.put(key, value);
		return this;
	}
	
	@SuppressWarnings("unchecked")
	public <K, V> Map<K, V> build(){
		return (Map<K, V>)map;
	}
}
