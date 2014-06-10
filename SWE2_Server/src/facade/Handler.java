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

import proxy.DeserializerClient;
import proxy.SerializerClient;
import utils.Parameter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import businesslayer.Businesslayer;
import businessobjects.Article;
import businessobjects.Contact;
import businessobjects.Invoice;
import businessobjects.InvoiceData;
import businessobjects.InvoiceLine;

import com.sun.glass.ui.Platform;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

public class Handler implements Runnable{
	
	private Socket client;
	private String _requestHeader = "";
	private String _requestLine = "";
	private String _xml;
	private Vector<Parameter> parms;
	private String xml;
	
	private SerializerServer serializer;
	private DeserializerServer deserializer;
	
	public Handler(Socket client) {
	 	this.client = client;
	}
	
	@Override
	public void run() {
		try {
			serializer = new SerializerServer();
			deserializer = new DeserializerServer();
			
			readMessage(client);
			System.out.println("rl: " + _requestLine);
			
			String action = parseHeader(_requestLine);
			
			Businesslayer b = new Businesslayer();
			
			System.out.println("Action: " + action);
			
			ObservableList<Contact> kontakte = FXCollections.observableArrayList();
			ArrayList<Invoice> rechnungen = new ArrayList<Invoice>();
			String xml;
			
			switch(action) 
			{
			case "insert/Kontakt":
				if(_xml != null) {
					System.out.println("Inserting Contact!");
					Contact k = deserializer.deserializeKontakt(_xml);			
					try {
						b.insertKontakt(k);
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
				break;
			case "update/Kontakt":
				if(_xml != null) {
					System.out.println("Updating Contact!");
					Contact k = deserializer.deserializeKontakt(_xml);			
					b.updateKontakt(k);
				}
				break;
			case "search/Rechnung":
				parms = deserializer.deserializeVector(_xml);
				
				rechnungen = b.searchRechnung(parms);
				xml = serializer.serializeRechnung(rechnungen);
				
				sendMessage(xml,client);
				client.close();
				break;
			case "search/Kontakt":
				parms = deserializer.deserializeVector(_xml);
				
				kontakte = b.searchContact(parms);				
				xml = serializer.serializeKontaktSearch(kontakte);
				sendMessage(xml,client);
				client.close();
				break;
			case "insert/Rechnung":
				if(_xml != null) {
					InvoiceData r = deserializer.deserializeRechnung(_xml);	
					try {
						b.insertRechnung(r);
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
				break;
			case "get/Artikel":
				ObservableList<Article> articles = FXCollections.observableArrayList();
				articles = b.getArticles();				
				xml = serializer.serializeGetArticle(articles);
				sendMessage(xml,client);
				client.close();
				break;
			case "find/Firm":
				parms = deserializer.deserializeVector(_xml);
				kontakte = b.findFirm(parms);				
				xml = serializer.serializeKontaktSearch(kontakte);
				sendMessage(xml,client);
				client.close();
				break;
			case "find/Person":
				parms = deserializer.deserializeVector(_xml);
				kontakte = b.findPerson(parms);				
				xml = serializer.serializeKontaktSearch(kontakte);
				sendMessage(xml,client);
				client.close();
				break;
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public String parseHeader(String head) {
		String[] splitArray;
		splitArray = head.split(" ");
		return splitArray[3];
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
	

	

}

