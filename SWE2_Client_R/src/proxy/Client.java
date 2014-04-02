package proxy;

//Client.java

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import businesslayer.Kunde;
import businesslayer.RechnungszeileModel;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

public class Client {
	
	
	public void insertKontakt(Kunde k, String action) {
		Socket socket;
		socket= createConnection();
		String xml = serializeObject(k);
		sendMessage(action, xml, socket);	
	}
	
	public Socket createConnection() {
		Socket s = null;
		try {
			s = new Socket("127.0.0.1",11111);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return s;
	}
	
	public String serializeObject(Kunde k) {
		XStream xstream = new XStream(new DomDriver());
		xstream.processAnnotations(Kunde.class);
		//xstream.alias("kontakt", Kontakt.class);
		String xml = xstream.toXML(k);
		return xml;
	}
	
	public ArrayList<RechnungszeileModel> sendMessage(String action, String xml, Socket socket) {
		try {
			PrintWriter writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
			writer.write("GET / HTTP/1.1 " + action);
			writer.write("\n");
			writer.write("host: " + socket.getInetAddress());
			writer.write("\n");
			//printWriter.println("Content-Length: ");
			writer.write("Content-Type: text/xml");
			writer.write("\n");
			writer.write(xml);
			writer.flush();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;  
	}

	public ArrayList<RechnungszeileModel> searchRechnung(String action) {
		ArrayList<RechnungszeileModel> searchAll = new ArrayList<RechnungszeileModel>();
		
		Socket socket;
		socket= createConnection();
		
		searchAll = sendMessage(action, null, socket);	
		
		return searchAll;
	}
}