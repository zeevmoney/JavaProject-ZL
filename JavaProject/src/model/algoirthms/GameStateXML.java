package model.algoirthms;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;



//XStream is a simple library to serialize objects to XML and back again.
//http://xstream.codehaus.org/index.html
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.StaxDriver;


//This class turns GameState object to XML & XML to GameState object.
public class GameStateXML {
	private XStream xstream;
	
	//Constructor
	public GameStateXML ()
	{
		xstream = new XStream(new StaxDriver());
		xstream.alias("GameState", GameState.class);
	}
	
	/*
	 * Export GameState to XML
	 * Input: GameState object & file name
	 * saves the file inside "resources" folder inside the project dir.
	 */

	public void gameStateToXML(GameState gameState,String fileName) throws IOException {
		System.out.println("DEBUG: Save XML");
		PrintWriter output = null;
		String xml = xstream.toXML(gameState);
		output = new PrintWriter(new FileWriter("resources\\" + fileName));
		output.println(xml);
		output.close();		
	}
	
	/*
	 * Import GameState from XML
	 * Input: file name inside the "resources" folder.
	 * saves the file inside "res" folder inside the project dir.
	 */
	
	public GameState gameStateFromXML (String fileName) throws IOException {
		System.out.println("DEBUG: Load XML");
		BufferedReader input = new BufferedReader(new FileReader("resources\\" + fileName));
		GameState m = (GameState) xstream.fromXML(input);
		input.close();
		return m;
	}
	
	
}
