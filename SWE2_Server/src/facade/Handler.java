package facade;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Vector;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import applikation.Parameter;
import businesslayer.Businesslayer;
import businessobjects.KontaktModel;
import businessobjects.RechnungModel;
import businessobjects.RechnungZeileModel;

import com.sun.glass.ui.Platform;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

public class Handler implements Runnable{
	
	private Socket client;
	private String _requestHeader = "";
	private String _requestLine = "";
	private String _xml;
	
	public Handler(Socket client) {
	 	this.client = client;
	}
	
	@Override
	public void run() {
		ArrayList<RechnungZeileModel> searchAll = new ArrayList<RechnungZeileModel>();
		try {
			
			readMessage(client);
			System.out.println("rl: " + _requestLine);
			
			String action = parseHeader(_requestLine);
			
			Businesslayer b = new Businesslayer();
			
			System.out.println("Action: " + action);
			
			switch(action) 
			{
			case "insert/Kontakt":
				if(_xml != null) {
					System.out.println("Inserting Contact!");
					KontaktModel k = deserializeKontakt(_xml);			
					try {
						b.insertKontakt(k);
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
				break;
			case "/search/Rechnung":
				//ToDo: Wonach suchen, Roman? Parameter parsen, dude! Und dann mitgeben der searchRechnung methode...
				searchAll = b.searchRechnung();
				break;
			case "search/Kontakt":
				ObservableList<KontaktModel> kontakte = FXCollections.observableArrayList();
				Vector<Parameter> parms = deserializeVector(_xml);
				
				kontakte = b.searchContact(parms);
				
				for(int i=0; i<kontakte.size(); i++){
					System.out.println("gefunden: " + kontakte.get(i).getVorname());
					System.out.println("gefunden: " + kontakte.get(i).getNachname());
				}
				
				String xml = serializeKontaktSearch(kontakte);
				sendMessage(xml,client);
				client.close();
				
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void readMessage(Socket socket) throws IOException {
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		//Lesen des gesamten HttpHeaders
		
		StringBuffer buff = new StringBuffer();
		String line;
		
		_requestLine = bufferedReader.readLine();
		_requestHeader += _requestHeader + _requestLine;
		_requestHeader += bufferedReader.readLine();
		_requestHeader += bufferedReader.readLine();

		
		while(!(line = bufferedReader.readLine()).equals("EOF")) {
			if(line.equals("EOF"))break;
			buff.append(line);
		}
		
		
		
		//bufferedReader.close();	
			
		String content = buff.toString();
		_xml = content;
	}
	
	public void sendMessage(String xml, Socket socket) {
		try {
			PrintWriter writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
			
			writer.write("HTTP/1.1 200 OK\r\n");
			writer.write("\n");
			writer.write("host: " + socket.getInetAddress());
			writer.write("\n");
			//printWriter.println("Content-Length: ");
			writer.write("Content-Type: text/xml");
			writer.write("\n");
			writer.write(xml);
			writer.write("\n");
			writer.write("EOF");
			writer.write("\n");
			writer.flush();
			//writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public String serializeKontaktSearch(ObservableList<KontaktModel> k) {
		XStream xstream = new XStream(new DomDriver());
		xstream.processAnnotations(KontaktModel.class);
		//xstream.alias("kontakt", Kontakt.class);
		String xml = xstream.toXML(k);
		return xml;
	}
	
	public KontaktModel deserializeKontakt(String xml) {
		XStream xstream = new XStream();
		xstream.processAnnotations(KontaktModel.class);
		KontaktModel k = (KontaktModel)xstream.fromXML(xml);
		return k;
	}
	
	public Vector<Parameter> deserializeVector(String xml) {
		XStream xstream = new XStream();
		xstream.processAnnotations(Vector.class);
		Vector<Parameter> v = (Vector<Parameter>)xstream.fromXML(xml);
		return v;
	}
	
	public String parseHeader(String head) {
		String[] splitArray;
		splitArray = head.split(" ");
		return splitArray[3];
	}
}

