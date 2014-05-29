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

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import applikation.Parameter;
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
	 * Happens on Enter in reference field for firm in ContactController
	 * @param 	id	an id in the contacts table
	 * @return 		if is a firm or a person, true if firm
	 */
	public ObservableList<Contact> findFirm(String firm) {
		Contact contact = new Contact(null, "sddsaas", "asaddssad", "sss", "10.01.1999");
		contact.setAdresse("Hauptallee", "12345", "Wien", "Österreich");
		ObservableList<Contact> contacts = FXCollections.observableArrayList();
		contacts.add(contact);
		
		Contact contact2 = new Contact(null, "sddsaas", "asaddssad", "sss", "10.01.1999");
		contacts.add(contact2);
		
		return contacts;
	}
	
	public ObservableList<Contact> findContact(String searchString) {
		Contact contact = new Contact(null, "sddsaas", "asaddssad", "sss", "10.01.1999");
		contact.setAdresse("Hauptallee", "12345", "Wien", "Österreich");
		ObservableList<Contact> contacts = FXCollections.observableArrayList();
		contacts.add(contact);
		
		Contact contact2 = new Contact(null, "sddsaas", "asaddssad", "sss", "10.01.1999");
		contacts.add(contact2);
		
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

		String xml = serializeArtikelSearch(articles);
		sendMessage(action, xml);
		
		try {
			String xml2;
			xml2 = readMessage();
			articles = deserializeArtikelSearch(xml2);
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
	public int upsertContact(Contact k) {
		String action;
		if(k.getId() != -1) {
			//EDIT contact
			action = "update/Kontakt";
			System.out.println("updating contact!");
		} else {
			//INSERT contact
			action = "insert/Kontakt";
			System.out.println("inserting contact!");
		}
		String xml = serializeKontakt(k);
		sendMessage(action, xml);
		
		return 1; //TODO return the ID
	}
	
	/**
	 * same as insertKontakt, just with Rechnung, except no update possible
	 * @param r		Rechnung-Object to be inserted in database
	 */
	public int insertInvoice(Invoice r) {
		System.out.println("Sending Rechnung-data to server.");
		String action = "insert/Rechnung";
		String xml = serializeRechnung(r);
		sendMessage(action, xml);
		
		System.out.println("inserting Recite with date: " + r.datumProperty().get());
		
		return 0;
	}
	
	
	/**
	 * sends searchParms to server, which will look for results in database via SQL-queries
	 * @param searchParms	[0] = Vorname, [1] = Nachname, [2] = Firma
	 * @return				an ArrayList of Kontakt-Objects to be displayed in TableView
	 */
	public ObservableList<Contact> searchKontakt(Vector<Parameter> searchParms) {
		String action = "search/Kontakt";
		ObservableList<Contact> kontakte = FXCollections.observableArrayList();
		
		System.out.println("Searching for Kontakt: ");
		System.out.println("Vorname: " + searchParms.get(0).getStringParameter());
		System.out.println("Nachname: " + searchParms.get(1).getStringParameter());
		System.out.println("Firma: " + searchParms.get(2).getStringParameter());
		
		
		/* Server-SQL abfr hier */
	
		try {
			String xml = serializeKontaktSearch(searchParms);
			sendMessage(action, xml);
			
			String xml2 = readMessage();
			kontakte = deserializeKontaktSearch(xml2);
			
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		
		String typ="Person", firma="Smartass GmbH", vorname="Bart", nachname="Simpson";
		Contact testKontakt = new Contact(null, vorname, nachname, "Herr", "10-10-2012");
		testKontakt.setAdresse("Hauptgasse", "12345", "Wieeen", "Austria!");
		kontakte.add(testKontakt);
		
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
		
		System.out.println("Searching for Rechnung: ");
		System.out.println("VonDatum: " + searchParms.get(0).getDateParameter());
		System.out.println("BisDatum: " + searchParms.get(1).getDateParameter());
		System.out.println("VonPreis: " + searchParms.get(2).getStringParameter());
		System.out.println("BisPreis: " + searchParms.get(3).getStringParameter());
		System.out.println("Kontakt: " + searchParms.get(4).getStringParameter());
//		try {
//			Socket socket = new Socket("127.0.0.1",11111);
//			String xml = serializeRechnungSearch(searchParms);
//
//			sendMessage(action, xml, socket);
//			
//			String xml2 = readMessage(socket);
//			rechnungen = deserializeRechnungSearch(xml2);
//			
//		} catch (UnknownHostException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
		
		Contact contact = new Contact(null, "sddsaas", "asaddssad", "sss", "10.01.1999");
		contact.setAdresse("Hauptallee", "12345", "Wien", "Österreich");
		
		/* TEST */
		System.out.println("WORKING..");
		ObservableList<InvoiceLine> testRechnungszeilen = FXCollections.observableArrayList();
		testRechnungszeilen.add(new InvoiceLine(new Article(6, "Artikel", 12.3), 13, 0.20));
		String nachricht="Deree", kommentar="Könnt wichtig sein...";
		rechnungen.add(new Invoice(testRechnungszeilen, new Date(), new Date(), contact, nachricht, kommentar, 
				new Adress("streeeet", 11221, "cityyy", "countryyyyyyy"), 
				new Adress("strettt", 12345, "cityyy", "countryyyyyyy")));
		/* TEST-ENDE */
		
		return rechnungen;
	}
	
	public String serializeKontakt(Contact k) {
		XStream xstream = new XStream(new DomDriver());
		xstream.processAnnotations(Contact.class);
		//xstream.alias("kontakt", Kontakt.class);
		String xml = xstream.toXML(k);
		return xml;
	}
	
	public String serializeKontaktSearch(Vector<Parameter> searchParms) {
		XStream xstream = new XStream(new DomDriver());
		xstream.processAnnotations(Contact.class);
		//xstream.alias("kontakt", Kontakt.class);
		String xml = xstream.toXML(searchParms);
		return xml;
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
		
		//bufferedReader.close();	
			
		String content = buff.toString();
		xml = content;
		return xml;
	}
	
	public ObservableList<Contact> deserializeKontaktSearch(String xml) {
		XStream xstream = new XStream();
		xstream.processAnnotations(Contact.class);
		ObservableList<Contact> k = (ObservableList<Contact>) xstream.fromXML(xml);
		return k;
	}
	
	public ObservableList<Invoice> deserializeRechnungSearch(String xml) {
		XStream xstream = new XStream();
		xstream.processAnnotations(Contact.class);
		ObservableList<Invoice> r = (ObservableList<Invoice>) xstream.fromXML(xml);
		return r;
	}
	
	public String serializeRechnungSearch(Vector<Parameter> searchParms) {
		XStream xstream = new XStream(new DomDriver());
		xstream.processAnnotations(Invoice.class);
		//xstream.alias("kontakt", Kontakt.class);
		String xml = xstream.toXML(searchParms);
		return xml;
	}
	
	public String serializeRechnung(Invoice r) {
		XStream xstream = new XStream(new DomDriver());
		xstream.processAnnotations(Invoice.class);
		//xstream.alias("kontakt", Kontakt.class);
		String xml = xstream.toXML(r);
		return xml;
	}
	
	public ObservableList<Article> deserializeArtikelSearch(String xml) {
		XStream xstream = new XStream();
		xstream.processAnnotations(Contact.class);
		ObservableList<Article> a = (ObservableList<Article>) xstream.fromXML(xml);
		return a;
	}
	
	public String serializeArtikelSearch(ObservableList<Article> articles) {
		XStream xstream = new XStream(new DomDriver());
		xstream.processAnnotations(Article.class);
		//xstream.alias("kontakt", Kontakt.class);
		String xml = xstream.toXML(articles);
		return xml;
	}
}
