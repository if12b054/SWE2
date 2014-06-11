package dataaccesslayer;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import utils.Parameter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import businessobjects.Adress;
import businessobjects.Article;
import businessobjects.Contact;
import businessobjects.Invoice;
import businessobjects.InvoiceData;
import businessobjects.InvoiceLine;
import businessobjects.InvoiceLineData;

public class InvoiceDAL {
	
	public void insertRechnung(InvoiceData r, Connection conn) throws SQLException {
		
		ResultSet rsAdress = null;
		ResultSet rsInvoice = null;
		ResultSet rsInvoiceLine = null;
		
		String sqlDelAdress = "INSERT INTO Adresse(Straﬂe,PLZ,Ort,Land) VALUES (?,?,?,?)";
		PreparedStatement cmdDelAdress = conn.prepareStatement(sqlDelAdress, Statement.RETURN_GENERATED_KEYS);
		cmdDelAdress.setString(1, r.getDelAdress().getStreet());
		cmdDelAdress.setInt(2, r.getDelAdress().getPostcode());
		cmdDelAdress.setString(3, r.getDelAdress().getCity());
		cmdDelAdress.setString(4, r.getDelAdress().getCountry());
		cmdDelAdress.execute();
		
		rsAdress = cmdDelAdress.getGeneratedKeys();
		rsAdress.next();
        int delAdressFK = rsAdress.getInt(1);
        
		String sqlInvAdress = "INSERT INTO Adresse(Straﬂe,PLZ,Ort,Land) VALUES (?,?,?,?)";
		PreparedStatement cmdInvAdress = conn.prepareStatement(sqlInvAdress, Statement.RETURN_GENERATED_KEYS);
		cmdInvAdress.setString(1, r.getInvAdress().getStreet());
		cmdInvAdress.setInt(2, r.getInvAdress().getPostcode());
		cmdInvAdress.setString(3, r.getInvAdress().getCity());
		cmdInvAdress.setString(4, r.getInvAdress().getCountry());
		cmdInvAdress.execute();
		
		rsAdress = cmdInvAdress.getGeneratedKeys();
		rsAdress.next();
        int invAdressFK = rsAdress.getInt(1);
        
		
		String sqlInvoice = "INSERT INTO Rechnung(Kunde_ID,Datum,F‰lligkeit,Kommentar,Nachricht,MWSt, Netto, Brutto,"
				+ "Rechnungsadresse,Lieferadresse) VALUES (?,?,?,?,?,?,?,?,?,?)";
		
		/*Calendar cal = Calendar.getInstance();
		cal.getTime();
		SimpleDateFormat sdf = new SimpleDateFormat("ss-mm-HH");
		String currentTime = sdf.format(cal.getTime());*/
		
		PreparedStatement cmdInvoice = conn.prepareStatement(sqlInvoice, Statement.RETURN_GENERATED_KEYS);
		cmdInvoice.setInt(1, r.getContact().getId()); 
		cmdInvoice.setString(2, r.getTodayDate());
		cmdInvoice.setString(3, r.getDueDate());
		cmdInvoice.setString(4, r.getComment());
		cmdInvoice.setString(5, r.getMessage());
		cmdInvoice.setDouble(6, r.getMWSt());
		cmdInvoice.setDouble(7, r.getNetto());
		cmdInvoice.setDouble(8, r.getBrutto());
		cmdInvoice.setInt(9, invAdressFK);
		cmdInvoice.setInt(10, delAdressFK);
		cmdInvoice.execute();
		
		rsInvoice = cmdInvoice.getGeneratedKeys();
		rsInvoice.next();
        int InvoiceFK = rsInvoice.getInt(1);
		
        List<InvoiceLineData> invoiceLines = r.getInvoiceLines();
        
        for(int i=0;i<invoiceLines.size();i++){
        	String sqlInvoiceLine = "INSERT INTO Rechnungszeilen(Rechnung_ID,Artikel_ID,Menge) VALUES(?,?,?)";
    		PreparedStatement cmdInvoiceLine = conn.prepareStatement(sqlInvoiceLine, Statement.RETURN_GENERATED_KEYS);
    		cmdInvoiceLine.setInt(1, InvoiceFK);
    		cmdInvoiceLine.setInt(2, invoiceLines.get(i).getArticle().getId());
    		cmdInvoiceLine.setInt(3, invoiceLines.get(i).getMenge());
    		cmdInvoiceLine.execute();
        }
	}
	
	public ArrayList<Invoice> searchRechnung(Vector<Parameter> parms, Connection conn) {
		ArrayList<Invoice> invoices = new ArrayList<Invoice>();
		ResultSet rs = null;
		ResultSet rsContact = null;
		
		try {
			
			//Von Datum gesetzt + Bis Datum gesetzt
			if(!(parms.get(0).getDateParameter()==null) && !(parms.get(1).getDateParameter()==null)){
					String sqlDatum = "SELECT * FROM Rechnung WHERE Datum > ? AND Datum < ?";
					PreparedStatement cmdDatum = conn.prepareStatement(sqlDatum);
					java.sql.Date parm0sql = new java.sql.Date (parms.get(0).getDateParameter().getTime());
					java.sql.Date parm1sql = new java.sql.Date (parms.get(1).getDateParameter().getTime());
					
					cmdDatum.setDate(1, parm0sql);
					cmdDatum.setDate(2, parm1sql);
					rs = cmdDatum.executeQuery();
			}
			
			//Nur Von Datum gesetzt
			if(!(parms.get(0).getDateParameter()==null) && (parms.get(1).getDateParameter()==null)){
					String sqlDatum = "SELECT * FROM Rechnung WHERE Datum > ?";
					PreparedStatement cmdDatum = conn.prepareStatement(sqlDatum);
					java.sql.Date parm0sql = new java.sql.Date (parms.get(0).getDateParameter().getTime());
					cmdDatum.setDate(1,parm0sql);
					rs = cmdDatum.executeQuery();
			}
			
			//Nur Bis Datum  gesetzt
			if((parms.get(0).getDateParameter()==null) && !(parms.get(1).getDateParameter()==null)){
					String sqlDatum = "SELECT * FROM Rechnung WHERE Datum < ?";
					PreparedStatement cmdDatum = conn.prepareStatement(sqlDatum);
					java.sql.Date parm1sql = new java.sql.Date (parms.get(1).getDateParameter().getTime());
					cmdDatum.setDate(1,parm1sql);
					rs = cmdDatum.executeQuery();
			}
			
			//Von Preis gesetzt + Bis Preis gesetzt
			if(!(parms.get(2).getStringParameter() == null) && !(parms.get(3).getStringParameter() == null)){
					String sqlPrice = "SELECT * FROM Rechnung WHERE Brutto > ? AND Brutto < ?";
					PreparedStatement cmdPrice = conn.prepareStatement(sqlPrice);
					
					cmdPrice.setDouble(1, Double.parseDouble(parms.get(2).getStringParameter()));
					cmdPrice.setDouble(2, Double.parseDouble(parms.get(3).getStringParameter()));
					rs = cmdPrice.executeQuery();
			}
			
			//Nur Von Preis gesetzt
			if(!(parms.get(2).getStringParameter() == null) && (parms.get(3).getStringParameter() == null)){
					String sqlPrice = "SELECT * FROM Rechnung WHERE Brutto > ?";
					PreparedStatement cmdPrice = conn.prepareStatement(sqlPrice);
					cmdPrice.setDouble(1, Double.parseDouble(parms.get(2).getStringParameter()));
					rs = cmdPrice.executeQuery();
			}
			
			//Nur Bis Preis gesetzt
			if((parms.get(2).getStringParameter() == null) && !(parms.get(3).getStringParameter() == null)){
					String sqlPrice = "SELECT * FROM Rechnung WHERE Brutto < ?";
					PreparedStatement cmdPrice = conn.prepareStatement(sqlPrice);
					cmdPrice.setDouble(1, Double.parseDouble(parms.get(3).getStringParameter()));
					rs = cmdPrice.executeQuery();
			}
			
			if(!(parms.get(4).getDoubleParameter() == 0)){
				String sqlContact = "SELECT * FROM Rechnung WHERE Kunde_ID = ?";
				PreparedStatement cmdContact = conn.prepareStatement(sqlContact);
				cmdContact.setInt(1, (int) parms.get(4).getDoubleParameter());
				rs = cmdContact.executeQuery();
			}
			
			while(rs.next()) {
				ObservableList<InvoiceLine> ilArray = FXCollections.observableArrayList();
				
				ResultSet rsDelAdress = null;
				Adress delAdress = null;
				String sqlDelAdress = "SELECT * FROM Adresse WHERE ID = ?";
				PreparedStatement cmdDelAdress = conn.prepareStatement(sqlDelAdress, Statement.RETURN_GENERATED_KEYS);
				cmdDelAdress.setInt(1, rs.getInt("Lieferadresse"));
				rsDelAdress = cmdDelAdress.executeQuery();				
				
				if(rsDelAdress.next())delAdress = new Adress(rsDelAdress.getString("Straﬂe"), rsDelAdress.getInt("PLZ"),
						rsDelAdress.getString("Ort"), rsDelAdress.getString("Land"));
				
				ResultSet rsInvAdress = null;
				Adress invAdress = null;
				String sqlInvAdress = "SELECT * FROM Adresse WHERE ID = ?";
				PreparedStatement cmdInvAdress = conn.prepareStatement(sqlInvAdress, Statement.RETURN_GENERATED_KEYS);
				cmdInvAdress.setInt(1, rs.getInt("Rechnungsadresse"));
				rsInvAdress = cmdInvAdress.executeQuery();
				
				if(rsInvAdress.next())invAdress = new Adress(rsInvAdress.getString("Straﬂe"), rsInvAdress.getInt("PLZ"),
						rsInvAdress.getString("Ort"), rsInvAdress.getString("Land"));			
				
				ResultSet rsInvLines = null;
				String sqlInvLines = "SELECT * FROM Rechnungszeilen WHERE Rechnung_ID = ?";
				PreparedStatement cmdInvLines = conn.prepareStatement(sqlInvLines, Statement.RETURN_GENERATED_KEYS);
				cmdInvLines.setString(1, rs.getString("ID"));
				rsInvLines = cmdInvLines.executeQuery();
				
				while(rsInvLines.next()){
					
					ResultSet rsArticle = null;
					String sqlArticle = "SELECT * FROM Artikel WHERE ID = ?";
					PreparedStatement cmdArticle = conn.prepareStatement(sqlArticle, Statement.RETURN_GENERATED_KEYS);
					cmdArticle.setInt(1, rsInvLines.getInt("Artikel_ID"));
					rsArticle = cmdArticle.executeQuery();
					
					Article ar = null;
					
					if(rsArticle.next())ar = new Article(rsArticle.getInt("ID"), rsArticle.getString("Bezeichnung"), rsArticle.getDouble("PreisNetto"));
					
					InvoiceLine line = new InvoiceLine(ar, rsInvLines.getInt("Menge"), rs.getDouble("MWSt"));
					
					ilArray.add(line);
				}
				//Wenn nicht nach einem Namen gesucht wird
				if(rsContact == null) {
					String sqlPrice = "SELECT * FROM Rechnung WHERE Kunde_ID = ?";
					PreparedStatement cmdPrice = conn.prepareStatement(sqlPrice);
					cmdPrice.setInt(1, rs.getInt("Kunde_ID"));
					rsContact = cmdPrice.executeQuery();
				}
				
				Contact k = null;
				ResultSet rsAdressContact;
				ResultSet rsContactSearch;
				
				String sqlContactSearch = "SELECT * FROM Kontakt WHERE ID = ?";
				PreparedStatement cmdContactSearch = conn.prepareStatement(sqlContactSearch);
				cmdContactSearch.setInt(1, rs.getInt("Kunde_ID"));
				rsContactSearch = cmdContactSearch.executeQuery();
				
				while(rsContactSearch.next()) {		
					int AdressID = rsContactSearch.getInt("Adresse");
					
					//Kontakt ist eine Firma
					if(!(rsContactSearch.getString("UID") == null)){
						k = new Contact(rsContactSearch.getString("UID"),rsContactSearch.getString("Firmenname"));
						
						String sql = "SELECT * FROM Adresse WHERE ID = ?";
						PreparedStatement cmd = conn.prepareStatement(sql);
						cmd.setInt(1, AdressID);
						rsAdressContact = cmd.executeQuery();
						while(rsAdressContact.next()) {
							k.setAdresse(rsAdressContact.getString("Straﬂe"), rsAdressContact.getString("PLZ"), rsAdressContact.getString("Ort"), rsAdressContact.getString("Land"));
						}
						k.setId(rsContactSearch.getInt("ID"));
					}//Kontakt ein Mensch
					
					if(!(rsContactSearch.getString("Vorname") == null)){
						k = new Contact(null, rsContactSearch.getString("Vorname"), rsContactSearch.getString("Nachname")
								,rsContactSearch.getString("Titel"),rsContactSearch.getString("Geburtsdatum"));
						
						String sql = "SELECT * FROM Adresse WHERE ID = ?";
						PreparedStatement cmd = conn.prepareStatement(sql);
						cmd.setInt(1, AdressID);
						rsAdressContact = cmd.executeQuery();
						while(rsAdressContact.next()) {
							k.setAdresse(rsAdressContact.getString("Straﬂe"), rsAdressContact.getString("PLZ"), rsAdressContact.getString("Ort"), rsAdressContact.getString("Land"));
						}
						k.setId(rsContactSearch.getInt("ID"));
					}
				}
				
				Invoice in = new Invoice(ilArray, rs.getDouble("MWSt"), rs.getDate("Datum"), rs.getDate("F‰lligkeit"),
						k, rs.getString("Nachricht"), rs.getString("Kommentar"), invAdress, delAdress);
				
				invoices.add(in);
			}	
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return invoices;
	}
	
}
