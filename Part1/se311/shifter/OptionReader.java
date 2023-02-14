package se311.shifter;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.ResourceBundle;

// Code provided by the professor; unmodified
public class OptionReader {
	private static HashMap<String, String> userOptions = null;
	private static ObjectLoader objLoader = new ObjectLoader();
	
	private OptionReader() {
		// TODO Auto-generated constructor stub
	}
	
	public static void readOptions() {
		ResourceBundle rb = ResourceBundle.getBundle("se311//shifter//config");
		Enumeration<String> keys = rb.getKeys();
		userOptions = new HashMap<String, String>();
		while (keys.hasMoreElements()) {
			String key = keys.nextElement();
			String value = rb.getString(key);
			userOptions.put(key, value);
		}
	}

//	public static void printOptions() {
//		userOptions.entrySet().forEach(entry -> {
//			System.out.println(entry.getKey() + ": " + entry.getValue());
//		});
//	}
	
	public static Object getObjectFromKey(String keyStr) {
		Object kwicObj = null;
		if (userOptions.containsKey(keyStr)) {
			String objName;
			objName = userOptions.get(keyStr);
			kwicObj = objLoader.loadObject(objName);
		}
		return kwicObj;
	}
	
	public static Object getObjectFromStr(String objStr) {
		return objLoader.loadObject(objStr);
	}
	
	public static String getString(String keyStr) {
		String valueStr = "";
		if (userOptions.containsKey(keyStr)) {			
			valueStr = userOptions.get(keyStr);			
		}
		return valueStr;
	}
	
	
	

}
