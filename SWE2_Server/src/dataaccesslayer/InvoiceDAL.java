package dataaccesslayer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import utils.Parameter;
import javafx.collections.ObservableList;
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
		cmdInvoice.setString(2, null);
		cmdInvoice.setString(3, null);
		cmdInvoice.setString(4, r.getComment());
		cmdInvoice.setString(5, r.getMessage());
		cmdInvoice.setDouble(6, r.getMWSt());
		cmdInvoice.setInt(7, 0);
		cmdInvoice.setInt(8, 0);
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
	
	public ArrayList<InvoiceLine> searchRechnung(Vector<Parameter> parms, Connection conn) {
		ArrayList<InvoiceLine> searchAll = new ArrayList<InvoiceLine>();
		ResultSet rd;
		
		try {
			String sql = "SELECT * FROM Kontakt";
			PreparedStatement cmd = conn.prepareStatement(sql);
			rd = cmd.executeQuery();
			
			while(rd.next()) {
				//Rechnungszeile rm = new Rechnungszeile();
				
				String zeile = rd.getString("");
			}	
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return searchAll;
	}
	
}
