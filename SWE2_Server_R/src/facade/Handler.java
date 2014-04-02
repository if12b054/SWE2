package facade;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;

import businesslayer.Businesslayer;
import businesslayer.Kunde;
import businesslayer.RechnungszeileModel;

import com.sun.glass.ui.Platform;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

public class Handler implements Runnable{
	
	private Socket _client;
	private String _requestHeader = "";
	private String _requestLine = "";
	private String _xml;
	
	public Handler(Socket client) {
	 	this._client = client;
	}
	
	@Override
	public void run() {
		Kunde k = null;
		ArrayList<RechnungszeileModel> searchAll = new ArrayList<RechnungszeileModel>();
		try {
			setNachricht(_client);
			if(_xml != null) {
				k = deserializeObject(_xml);
			}
			String action = parseHeader(_requestLine);
			
			Businesslayer b = new Businesslayer();
			
			if(action.equals("insert/Kontakt")) {
				b.insertKontakt(k);
			}
			if(action.equals("search/Rechnung")) {
				searchAll = b.searchRechnung();
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
	
	public Kunde deserializeObject(String xml) {
		XStream xstream = new XStream();
		xstream.processAnnotations(Kunde.class);
		Kunde k = (Kunde)xstream.fromXML(xml);
		return k;
	}
	
	public String parseHeader(String head) {
		String[] splitArray;
		splitArray = head.split(" ");
		return splitArray[3];
	}
}

