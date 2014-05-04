package proxy;

import java.io.IOException;
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
import businessobjects.Artikel;
import businessobjects.KontaktModel;
import businessobjects.RechnungModel;
import businessobjects.RechnungZeileModel;

public class Proxy {
	
	/**
	 * Sends an Request to the server, to check if the id is a firm or a person
	 * e.g. Needed for quick checking, when user enters name in Firm-Field to reference a firm in a Contact
	 * NO ERROR CHECKING, id needs to be correct
	 * @param 	id	an id in the contacts table
	 * @return 		if is a firm or a person, true if firm
	 */
	public boolean isFirma(String firma) {
		return true;
	}
	
	
	/**
	 * gets the articles from database and returns them as an ArrayList
	 * (for dropdown while adding Rechnungszeilen
	 * @return
	 */
	public ArrayList<Artikel> getArticles() {
		ArrayList<Artikel> articles = new ArrayList<Artikel>();
		
		return articles;
	}
	
	/**
	 * sends Kontakt data to server, which inserts it into database
	 * CHECK on server-side if contact already exists, if
	 * it does, do UPDATE!
	 * @param k		Kontakt-Object which data is inserted into database
	 */
	public void insertKontakt(KontaktModel k) {
		
		try {
			System.out.println("Sending Kontakt-data to server.");
			String action = "insert/Kontakt";
			Socket socket = new Socket("127.0.0.1",11111);
			String xml = serializeKontakt(k);
			sendMessage(action, xml, socket);
			socket.close();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	/**
	 * same as insertKontakt, just with Rechnung
	 * @param r		Rechnung-Object to be inserted in database
	 */
	public void insertRechnung(RechnungModel r) {
		System.out.println("Sending Rechnung-data to server.");
		String action = "insert/Rechnung";
		
	}
	
	
	/**
	 * sends searchParms to server, which will look for results in database via SQL-queries
	 * @param searchParms	[0] = Vorname, [1] = Nachname, [2] = Firma
	 * @return				an ArrayList of Kontakt-Objects to be displayed in TableView
	 */
	public ObservableList<KontaktModel> searchKontakt(Vector<Parameter> searchParms) {
		String action = "search/Kontakt";
		ObservableList<KontaktModel> kontakte = FXCollections.observableArrayList();
		
		System.out.println("Searching for Kontakt: ");
		System.out.println("Vorname: " + searchParms.get(0).getStringParameter());
		System.out.println("Nachname: " + searchParms.get(1).getStringParameter());
		System.out.println("Firma: " + searchParms.get(2).getStringParameter());
		
		
		/* Server-SQL abfr hier */
	
		try {
			Socket socket = new Socket("127.0.0.1",11111);
			String xml = serializeKontaktSearch(searchParms);
			sendMessage(action, xml, socket);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		/* TEST */
		System.out.println("WORKING..");
		String typ="Person", firma="Smartass GmbH", vorname="Bart", nachname="Simpson";
		KontaktModel testKontakt = new KontaktModel(firma, vorname, nachname, null, null);
		kontakte.add(testKontakt);
		/* TEST-ENDE */
		
		return kontakte;
	}
	
	/**
	 * sends searchParms to server, which will look for results in database via SQL-queries
	 * @param searchParms	[0] = VonDatum, [1] = BisDatum, [2] = VonPreis, [3] = BisPreis, [4] = KontaktBezeichnung
	 * 						get the value with the right getter e.g.: VonDatum - aDate.getDateParameter();
	 * @return				an ArrayList of Rechnung-Objects to be displayed in TableView
	 */
	public ObservableList<RechnungModel> searchRechnung(Vector<Parameter> searchParms) {
		String action = "search/Rechnung";
		ObservableList<RechnungModel> rechnungen = FXCollections.observableArrayList();
		
		System.out.println("Searching for Rechnung: ");
		System.out.println("VonDatum: " + searchParms.get(0).getDateParameter());
		System.out.println("BisDatum: " + searchParms.get(1).getDateParameter());
		System.out.println("VonPreis: " + searchParms.get(2).getStringParameter());
		System.out.println("BisPreis: " + searchParms.get(3).getStringParameter());
		System.out.println("Kontakt: " + searchParms.get(4).getStringParameter());
		
		/* Server-SQL abfr hier */
		
		/* TEST */
		System.out.println("WORKING..");
		ArrayList<RechnungZeileModel> testRechnungszeilen = new ArrayList<RechnungZeileModel>();
		testRechnungszeilen.add(new RechnungZeileModel("Artikel", 1, 1, 1));
		String datum="10.10.2000", faelligkeit="11.11.2000", kunde="Gott", nachricht="Deree", kommentar="Könnt wichtig sein...";
		rechnungen.add(new RechnungModel(testRechnungszeilen, datum, faelligkeit, kunde, nachricht, kommentar));
		/* TEST-ENDE */
		
		return rechnungen;
	}
	
	public String serializeKontakt(KontaktModel k) {
		XStream xstream = new XStream(new DomDriver());
		xstream.processAnnotations(KontaktModel.class);
		//xstream.alias("kontakt", Kontakt.class);
		String xml = xstream.toXML(k);
		return xml;
	}
	
	public String serializeKontaktSearch(Vector<Parameter> searchParms) {
		XStream xstream = new XStream(new DomDriver());
		xstream.processAnnotations(KontaktModel.class);
		//xstream.alias("kontakt", Kontakt.class);
		String xml = xstream.toXML(searchParms);
		return xml;
	}
	
	public void sendMessage(String action, String xml, Socket socket) {
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
	}
}
