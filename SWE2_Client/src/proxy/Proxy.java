package proxy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Vector;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

import applikation.Parameter;
import applikation.Utils;
import businessobjects.Article;
import businessobjects.Contact;
import businessobjects.Invoice;
import businessobjects.InvoiceLine;

public class Proxy {
	
	/**
	 * Happens on Enter in reference field for firm in ContactController
	 * @param 	id	an id in the contacts table
	 * @return 		if is a firm or a person, true if firm
	 */
	public ArrayList<Contact> findFirm(String firm) {
		if(Utils.isInteger(firm)) {
			//search just for id
		} else {
			//search for firm name
		}
		Contact contact = new Contact("blaa", "sddsaas", "asaddssad", "sss", "10.01.1999");
		ArrayList<Contact> contacts = new ArrayList<Contact>();
		contacts.add(contact);
		
		return contacts;
	}
	
	public ArrayList<Contact> findContact(String firstName, String lastName, String firm) {
		Contact contact = new Contact("blaa", "sddsaas", "asaddssad", "sss", "10.01.1999");
		ArrayList<Contact> contacts = new ArrayList<Contact>();
		contacts.add(contact);
		
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
		
		try {
			Socket socket = new Socket("127.0.0.1",11111);
			String xml = serializeArtikelSearch(articles);

			sendMessage(action, xml, socket);
			
			String xml2 = readMessage(socket);
			articles = deserializeArtikelSearch(xml2);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return articles;
	}
	
	public Socket createSocket() {
		Socket socket = null;
		try {
			socket = new Socket("127.0.0.1",11111);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return socket;
	}
	
	/**
	 * sends Kontakt data to server, which inserts it into database
	 * CHECK on server-side if contact already exists, if
	 * it does, do UPDATE!
	 * @param k		Kontakt-Object which data is inserted into database
	 */
	public void insertKontakt(Contact k) {
		
		System.out.println("Sending Kontakt-data to server.");
		String action = "insert/Kontakt";
		Socket socket = createSocket();
		String xml = serializeKontakt(k);
		sendMessage(action, xml, socket);

	}
	
	/**
	 * same as insertKontakt, just with Rechnung
	 * @param r		Rechnung-Object to be inserted in database
	 */
	public void insertRechnung(Invoice r) {
		System.out.println("Sending Rechnung-data to server.");
		String action = "insert/Rechnung";
		Socket socket = createSocket();
		String xml = serializeRechnung(r);
		sendMessage(action, xml, socket);
		
		System.out.println("inserting Recite with date: " + r.datumProperty().get());
		
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
			Socket socket = new Socket("127.0.0.1",11111);
			String xml = serializeKontaktSearch(searchParms);

			sendMessage(action, xml, socket);
			
			String xml2 = readMessage(socket);
			kontakte = deserializeKontaktSearch(xml2);
			
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		/* TEST 
		System.out.println("WORKING..");
		String typ="Person", firma="Smartass GmbH", vorname="Bart", nachname="Simpson";
		KontaktModel testKontakt = new KontaktModel(firma, vorname, nachname, null, null);
		kontakte.add(testKontakt);
		 TEST-ENDE */
		
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
		
		/* Server-SQL abfr hier */
		
		try {
			Socket socket = new Socket("127.0.0.1",11111);
			String xml = serializeRechnungSearch(searchParms);

			sendMessage(action, xml, socket);
			
			String xml2 = readMessage(socket);
			rechnungen = deserializeRechnungSearch(xml2);
			
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		/* TEST */
		System.out.println("WORKING..");
		ArrayList<InvoiceLine> testRechnungszeilen = new ArrayList<InvoiceLine>();
//		testRechnungszeilen.add(new InvoiceLine("Artikel", 1, 1, 1));
		String datum="10.10.2000", faelligkeit="11.11.2000", kunde="Gott", nachricht="Deree", kommentar="Könnt wichtig sein...";
//		rechnungen.add(new Invoice(testRechnungszeilen, datum, faelligkeit, kunde, nachricht, kommentar));
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
	
	public void sendMessage(String action, String xml, Socket socket) {
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
	
	public String readMessage(Socket socket) throws IOException {
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		//Lesen des gesamten HttpHeaders
		
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
