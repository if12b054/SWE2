package proxy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Vector;

import utils.Parameter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import businessobjects.Adress;
import businessobjects.Article;
import businessobjects.Contact;
import businessobjects.Invoice;
import businessobjects.InvoiceLine;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;



public class Proxy {
	public static final int SOCKET_NUMBER = 11111;
	public static Socket socket = null;
	private SerializerClient serializer;
	private DeserializerClient deserializer;
	
	public static boolean serverConnection() {
		try {
			socket = new Socket("127.0.0.1", SOCKET_NUMBER);
		} catch (UnknownHostException e) {
			return false;
		} catch (IOException e) {
			return false;
		}
		return true;
	}
	
	/**
	 * Happens on Enter in reference field for firm in ContactController, or if firm is toggled
	 * in the mainSearch or invoiceSearch(on button "Finden" click(
	 * @param 	firm	the firm string
	 * @return 			the found firms
	 */
	public ObservableList<Contact> findFirm(String firm) {
		
		ObservableList<Contact> contacts = FXCollections.observableArrayList();
		Vector<Parameter> searchParms = new Vector<Parameter>();
		searchParms.add(new Parameter(firm));
		serializer = new SerializerClient();
		deserializer = new DeserializerClient();
		
		String action = "find/Firm";
		String xml = serializer.serializeKontaktSearch(searchParms);
		sendMessage(action, xml);
		
		try {
			String xml2;
			xml2 = readMessage();
			contacts = deserializer.deserializeKontaktSearch(xml2);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return contacts;
	}
	
	/**
	 * Happens on Enter in reference field in mainSearch or InvoiceSearch if Person is
	 * toggled
	 * @param person	the person string
	 * @return
	 */
	public ObservableList<Contact> findPerson(String person) {
		ObservableList<Contact> contacts = FXCollections.observableArrayList();
		Vector<Parameter> searchParms = new Vector<Parameter>();
		searchParms.add(new Parameter(person));
		serializer = new SerializerClient();
		deserializer = new DeserializerClient();
		
		String action = "find/Person";
		String xml = serializer.serializeKontaktSearch(searchParms);
		sendMessage(action, xml);
		
		try {
			String xml2;
			xml2 = readMessage();
			contacts = deserializer.deserializeKontaktSearch(xml2);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return contacts;
	}
	
	
	/**
	 * gets the articles from database and returns them as an ArrayList
	 * (for dropdown while adding Rechnungszeilen
	 * @return
	 */
	public ObservableList<Article> getArticles() {
		ObservableList<Article> articles = FXCollections.observableArrayList();
		String action = "get/Artikel";
		serializer = new SerializerClient();
		deserializer = new DeserializerClient();

		String xml = serializer.serializeArtikelSearch(articles);
		sendMessage(action, xml);
		
		try {
			String xml2;
			xml2 = readMessage();
			articles = deserializer.deserializeArtikelSearch(xml2);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return articles;
	}
	
	/**
	 * sends Kontakt data to server, which inserts it into database
	 * CHECK on server-side if contact already exists, if
	 * it does, do UPDATE!
	 * @param k		Kontakt-Object which data is inserted into database
	 * @return		int - the id, so entry can be edited
	 */
	public void upsertContact(Contact k) {
		String action;
		serializer = new SerializerClient();
		if(k.getId() != -1) {
			//EDIT contact
			action = "update/Kontakt";
			System.out.println("updating contact!");
		} else {
			//INSERT contact
			action = "insert/Kontakt";
			System.out.println("inserting contact!");
		}
		String xml = serializer.serializeKontakt(k);
		sendMessage(action, xml);
	}
	
	/**
	 * same as insertKontakt, just with Rechnung, except no update possible
	 * @param r		Rechnung-Object to be inserted in database
	 */
	public void insertInvoice(Invoice r) {
		serializer = new SerializerClient();
		String action = "insert/Rechnung";
		String xml = serializer.serializeRechnung(r);
		sendMessage(action, xml);
	}
	
	
	/**
	 * sends searchParms to server, which will look for results in database via SQL-queries
	 * @param searchParms	[0] = Vorname, [1] = Nachname, [2] = Firma
	 * @return				an ArrayList of Kontakt-Objects to be displayed in TableView
	 */
	public ObservableList<Contact> searchKontakt(Vector<Parameter> searchParms) {
		String action = "search/Kontakt";
		ObservableList<Contact> kontakte = FXCollections.observableArrayList();
		serializer = new SerializerClient();
		deserializer = new DeserializerClient();
	
		try {
			String xml = serializer.serializeKontaktSearch(searchParms);
			sendMessage(action, xml);
			
			String xml2 = readMessage();
			kontakte = deserializer.deserializeKontaktSearch(xml2);
			socket.close();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return kontakte;
	}
	
	/**
	 * sends searchParms to server, which will look for results in database via SQL-queries
	 * @param searchParms	[0] = VonDatum, [1] = BisDatum, [2] = VonPreis, [3] = BisPreis, [4] = KontaktBezeichnung
	 * 						get the value with the right getter e.g.: VonDatum - aDate.getDateParameter();
	 * @return				an ArrayList of Rechnung-Objects to be displayed in TableView
	 */
	public ObservableList<Invoice> searchRechnung(Vector<Parameter> searchParms) {
		String action = "search/Rechnung";
		ObservableList<Invoice> rechnungen = FXCollections.observableArrayList();
		
		serializer = new SerializerClient();
		deserializer = new DeserializerClient();
		
		try {
			String xml = serializer.serializeRechnungSearch(searchParms);
			sendMessage(action, xml);
			
			String xml2 = readMessage();
			rechnungen = deserializer.deserializeRechnungSearch(xml2);
			socket.close();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		/*
		System.out.println("Searching for Rechnung: ");
		System.out.println("VonDatum: " + searchParms.get(0).getDateParameter());
		System.out.println("BisDatum: " + searchParms.get(1).getDateParameter());
		System.out.println("VonPreis: " + searchParms.get(2).getStringParameter());
		System.out.println("BisPreis: " + searchParms.get(3).getStringParameter());
		System.out.println("Kontakt: " + searchParms.get(4).getStringParameter());*/
		

		
		return rechnungen;
	}
	
	public void sendMessage(String action, String xml) {
		try {
			PrintWriter writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
			
			System.out.println("Socket Client: " + socket);
			
			writer.write("GET / HTTP/1.1 " + action);
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
	
	public String readMessage() throws IOException {
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		StringBuffer buff = new StringBuffer();
		String line;
		String requestHeader=null;
		String xml;
		
		String _requestLine = bufferedReader.readLine();
		requestHeader += requestHeader + _requestLine;
		requestHeader += bufferedReader.readLine();
		requestHeader += bufferedReader.readLine();
		
		while(!(line = bufferedReader.readLine()).equals("EOF")) {
			if(line.equals("EOF"))break;
			buff.append(line);
		}	
			
		String content = buff.toString();
		xml = content;
		return xml;
	}	
}
