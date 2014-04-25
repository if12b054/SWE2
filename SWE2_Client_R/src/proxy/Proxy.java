package proxy;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Vector;

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
	 * @param k		Kontakt-Object which data is inserted into database
	 */
	public void insertKontakt(KontaktModel k) {
		System.out.println("Sending Kontakt-data to server.");
		String action = "insert/Kontakt";
		
		/*//Send to server 
		Socket socket;
		socket= createConnection();
		String xml = serializeObject(k);
		sendMessage(action, xml, socket);*/
	}
	
	
	/**
	 * sends searchParms to server, which will look for results in database via SQL-queries
	 * @param searchParms	[0] = Vorname, [1] = Nachname, [2] = Firma
	 * @return				an ArrayList of Kontakt-Objects to be displayed in TableView
	 */
	public ArrayList<KontaktModel> searchKontakt(Vector<Parameter> searchParms) {
		String action = "search/Kontakt";
		ArrayList<KontaktModel> kontakte = new ArrayList<KontaktModel>();
		
		/* Server-SQL abfr hier */
		
		return kontakte;
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
	 * @param searchParms	[0] = VonDatum, [1] = BisDatum, [2] = VonPreis, [3] = BisPreis, [4] = KontaktBezeichnung
	 * 						get the value with the right getter e.g.: VonDatum - aDate.getDateParameter();
	 * @return				an ArrayList of Rechnung-Objects to be displayed in TableView
	 */
	public ArrayList<RechnungModel> searchRechnung(Vector<String> searchParms) {
		String action = "search/Rechnung";
		ArrayList<RechnungModel> rechnungen = new ArrayList<RechnungModel>();
		
		/* Server-SQL abfr hier */
		
		return rechnungen;
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
	
	public String serializeObject(KontaktModel k) {
		XStream xstream = new XStream(new DomDriver());
		xstream.processAnnotations(KontaktModel.class);
		//xstream.alias("kontakt", Kontakt.class);
		String xml = xstream.toXML(k);
		return xml;
	}
	
	public ArrayList<RechnungZeileModel> sendMessage(String action, String xml, Socket socket) {
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
	
	
}
