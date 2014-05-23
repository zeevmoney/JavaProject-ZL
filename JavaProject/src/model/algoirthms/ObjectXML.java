package model.algoirthms;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.StaxDriver;


/* 
 * This class can write/read any object to XML.
 * There is no need for serialization.
 */


public class ObjectXML {
	//TODO: remove this.
//	private XStream xstream;
//	
//	public ObjectXML() {
//		xstream = new XStream(new StaxDriver());
//	}
	
	/*
	 * Export Object to XML
	 * Input: Object & file name
	 * saves the file inside "resources" folder inside the project dir.
	 */
	public static void objectToXML(Object o,String fileName) throws IOException {
				
		//make a GameState XML
		XStream xstream = new XStream(new StaxDriver());
		PrintWriter output = null;
		String xml = xstream.toXML(o);
		output = new PrintWriter(new FileWriter(fileName));
		output.println(xml);
		output.close();
	}
	
	/*
	 * Import Object from XML
	 * Input: file name inside the "resources" folder.
	 */	
	public static Object ObjectFromXML (String fileName) throws IOException {
		XStream xstream = new XStream(new StaxDriver());
		BufferedReader input = new BufferedReader(new FileReader(fileName));
		Object o = xstream.fromXML(input);
		input.close();
		return o;
	}	
	
}
