package facade;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
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
			setNachricht(client);
			
			String action = parseHeader(_requestLine);
			
			Businesslayer b = new Businesslayer();
			
			switch(action) 
			{
			case "/insert/Kontakt":
				if(_xml != null) {
					System.out.println("Inserting Contact!");
					KontaktModel k = deserializeObject(_xml);
					b.insertKontakt(k);
				}
				break;
			case "/search/Rechnung":
				//ToDo: Wonach suchen, Roman? Parameter parsen, dude! Und dann mitgeben der searchRechnung methode...
				searchAll = b.searchRechnung();
				break;
			case "/search/Kontakt":
				ObservableList<RechnungModel> rechnungen = FXCollections.observableArrayList();
				Vector<Parameter> parms = deserializeVector(_xml);
				rechnungen = b.searchContact(parms);
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void setNachricht(Socket socket) throws IOException {
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		//Lesen des gesamten HttpHeaders
		
		StringBuffer buff = new StringBuffer();
		String line;
		
		_requestLine = bufferedReader.readLine();
		_requestHeader += _requestHeader + _requestLine;
		_requestHeader += bufferedReader.readLine();
		_requestHeader += bufferedReader.readLine();
		
		while((line = bufferedReader.readLine()) != null) {
			buff.append(line);
		}
		bufferedReader.close();	
			
		String content = buff.toString();
		_xml = content;
	}
	
	public KontaktModel deserializeObject(String xml) {
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
		return splitArray[1];
	}
}

