package social.tests.pageobjects;

import java.io.UnsupportedEncodingException;

public class PO_Properties {
	
	//
	// locale is the index in idioms array.
	//
    public String getString(String prop, int locale) {
		
		String result = null;
		try {
			//Transformamos la cadena leída en formato ISO-8859-1 a UTF8
			result = new String(prop.getBytes("ISO-8859-1"), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		return result;
	}

	
}
