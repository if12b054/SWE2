package dataaccesslayer;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Vector;

import utils.Parameter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import businessobjects.Article;
import businessobjects.Contact;
import businessobjects.Invoice;
import businessobjects.InvoiceLine;

public class Dataaccesslayer {
	
	final String PCName = "Schlepptop\\SQLEXPRESS"; //Roman
	//final String PCName = "ULTRABOOK\\SQLEXPRESS"; //Victor
	
	public void insertKontakt(Contact k) throws SQLException {
		Connection conn = connectDB("ErpDB");
		ResultSet rs1 = null;
		
		String sql1 = "INSERT INTO Adresse(Straﬂe,PLZ,Ort,Land) VALUES (?,?,?,?)";
		PreparedStatement cmd1 = conn.prepareStatement(sql1, Statement.RETURN_GENERATED_KEYS);
		cmd1.setString(1, k.getAdresse().get(0));
		cmd1.setInt(2, Integer.parseInt(k.getAdresse().get(1)));
		cmd1.setString(3, k.getAdresse().get(2));
		cmd1.setString(4, k.getAdresse().get(3));
		cmd1.execute();
		
		rs1 = cmd1.getGeneratedKeys();
		rs1.next();
		int adresseFK = rs1.getInt(1);
        
		PreparedStatement cmd;
		if(k.typProperty().getValue().equals("Person")) {
			String sql = "INSERT INTO Kontakt(Firmenname,Titel,Vorname,Nachname,Geburtsdatum,Adresse,Typ) VALUES (?,?,?,?,?,?,?)";
			cmd = conn.prepareStatement(sql);
			cmd.setString(1,k.getFirma());
			cmd.setString(2,k.getTitel());
			cmd.setString(3,k.getVorname());
			cmd.setString(4,k.getNachname());
			cmd.setString(5,k.getGeburtsdatum());
			cmd.setInt(6, adresseFK);
			cmd.setString(7,k.typProperty().getValue());
		}
		else {
			String sql = "INSERT INTO Kontakt(UID,Firmenname,Adresse,Typ) VALUES (?,?,?,?)";
			cmd = conn.prepareStatement(sql);
			cmd.setString(1,k.getUid());
			cmd.setString(2,k.getFirma());
			cmd.setInt(3, adresseFK); 
			cmd.setString(4,k.typProperty().getValue());
		}
		
		cmd.execute();
	}
	
	public void insertRechnung(Invoice r) throws SQLException {
		Connection conn = connectDB("ErpDB");
		
		ResultSet rs = null;
		
		String sql1 = "INSERT INTO Adresse(Straﬂe,PLZ,Ort,Land) VALUES (?,?,?,?)";
		PreparedStatement cmd = conn.prepareStatement(sql1, Statement.RETURN_GENERATED_KEYS);
		cmd.setString(1, r.getDelAdress().getStreet());
		cmd.setInt(2, r.getDelAdress().getPostcode());
		cmd.setString(3, r.getDelAdress().getCity());
		cmd.setString(4, r.getDelAdress().getCountry());
		cmd.execute();
		
		rs = cmd.getGeneratedKeys();
        int delAdressFK = rs.getInt(1);
        
		ResultSet rs1 = null;
		
		String sql2 = "INSERT INTO Adresse(Straﬂe,PLZ,Ort,Land) VALUES (?,?,?,?)";
		PreparedStatement cmd1 = conn.prepareStatement(sql1, Statement.RETURN_GENERATED_KEYS);
		cmd1.setString(1, r.getInvAdress().getStreet());
		cmd1.setInt(2, r.getInvAdress().getPostcode());
		cmd1.setString(3, r.getInvAdress().getCity());
		cmd1.setString(4, r.getInvAdress().getCountry());
		cmd1.execute();
		
		rs1 = cmd1.getGeneratedKeys();
        int invAdressFK = rs1.getInt(1);
		
        String sql3 = "INSERT INTO Rechnung(Kunde_ID, Datum, F‰lligkeit, Kommentar, Nachricht, MWSt, Netto, Brutto,"
        		+ "Rechnungsadresse, Lieferadresse) VALUES ()";
        
        
		
	}
	
	public ObservableList<Contact> searchContact(Vector<Parameter> parms) {
		ObservableList<Contact> kontakte = FXCollections.observableArrayList();
		Connection conn = connectDB("ErpDB");
		
		ResultSet rs = null;
		
			try{
			//Check ob Vorname != 0
			if(!(parms.get(0).getStringParameter()==null)) {
				//Check ob Nachname != 0
				if(!(parms.get(1).getStringParameter()==null)){
					//Check ob Firma != 0
					if(!(parms.get(2).getStringParameter()==null)){
						//Alle drei
						String sql = "SELECT * FROM Kontakt WHERE Vorname = ? AND Nachname = ? AND Firmenname = ?";
						PreparedStatement cmd;
							cmd = conn.prepareStatement(sql);
						cmd.setString(1, parms.get(0).getStringParameter());
						cmd.setString(2, parms.get(1).getStringParameter());
						cmd.setString(3, parms.get(2).getStringParameter());
						rs = cmd.executeQuery();
					}
					else{//Vorname und Nachname
						String sql = "SELECT * FROM Kontakt WHERE Vorname = ? AND NACHNAME = ?";
						PreparedStatement cmd = conn.prepareStatement(sql);
						cmd.setString(1, parms.get(0).getStringParameter());
						cmd.setString(2, parms.get(1).getStringParameter());
						rs = cmd.executeQuery();
					}
				}
				else {
					//Vorname und Firma
					if(!(parms.get(2).getStringParameter()==null)){
						String sql = "SELECT * FROM Kontakt WHERE Vorname = ? AND Firmenname = ?";
						PreparedStatement cmd = conn.prepareStatement(sql);
						cmd.setString(1, parms.get(0).getStringParameter());
						cmd.setString(2, parms.get(2).getStringParameter());
						rs = cmd.executeQuery();
					}
					else{//nur Vorname
						String sql = "SELECT * FROM Kontakt WHERE Vorname = ?";
						PreparedStatement cmd = conn.prepareStatement(sql);
						cmd.setString(1, parms.get(0).getStringParameter());
						rs = cmd.executeQuery();
					}
				}
			}
			else{
				//Check ob Nachname != 0	
				if(parms.get(1).getStringParameter()==null) {
					//Check ob Firma != 0
					if(parms.get(2).getStringParameter()==null) {
						//Nachname und Firma
						String sql = "SELECT * FROM Kontakt WHERE Nachname = ? AND Firmenname = ?";
						PreparedStatement cmd = conn.prepareStatement(sql);
						cmd.setString(1, parms.get(1).getStringParameter());
						cmd.setString(2, parms.get(2).getStringParameter());
						rs = cmd.executeQuery();
					}
					else {	//nur Nachname
						String sql = "SELECT * FROM Kontakt WHERE Nachname = ?";
						PreparedStatement cmd = conn.prepareStatement(sql);
						cmd.setString(1, parms.get(1).getStringParameter());
						rs = cmd.executeQuery();
					}
				}
				else{
					//Check ob Firma != 0
					if(parms.get(2).getStringParameter()==null) {
						//nur Firma
						String sql = "SELECT * FROM Kontakt WHERE Firmenname = ?";
						PreparedStatement cmd = conn.prepareStatement(sql);
						cmd.setString(1, parms.get(2).getStringParameter());
						rs = cmd.executeQuery();
					}
				}
			}
			
			//TODO bei contact, contact referenz ist ein Contact objekt! falls keine firma eingetragen wurde
			// ist es null, habe mal vor¸bergehen statisch null eingetragen!
					
			Contact k = null;
			ResultSet rs1;
			
			while(rs.next()) {
				
				int AdressID = rs.getInt("Adresse");
				
				//Kontakt ist eine Firma
				if(rs.getString("Vorname").equals(null)){
					k = new Contact(rs.getString("UID"),rs.getString("Firmenname"));
					kontakte.add(k);
					
					String sql = "SELECT * FROM Adresse WHERE ID = ?";
					PreparedStatement cmd = conn.prepareStatement(sql);
					cmd.setInt(1, AdressID);
					rs1 = cmd.executeQuery();
					while(rs1.next()) {
						k.setAdresse(rs1.getString("Straﬂe"), rs1.getString("PLZ"), rs1.getString("Ort"), rs1.getString("Land"));
					}
				}
				
				//Kontakt ein Mensch
				if(!rs.getString("Vorname").equals(null)){
//					Contact k = new Contact(rs.getString("Firmenname"), rs.getString("Vorname"), rs.getString("Nachname")
//							,rs.getString("Titel"),rs.getString("Geburtsdatum"));
					k = new Contact(null, rs.getString("Vorname"), rs.getString("Nachname")
							,rs.getString("Titel"),rs.getString("Geburtsdatum"));
					kontakte.add(k);
					
					String sql = "SELECT * FROM Adresse WHERE ID = ?";
					PreparedStatement cmd = conn.prepareStatement(sql);
					cmd.setInt(1, AdressID);
					rs1 = cmd.executeQuery();
					while(rs1.next()) {
						k.setAdresse(rs1.getString("Straﬂe"), rs1.getString("PLZ"), rs1.getString("Ort"), rs1.getString("Land"));
					}
					
				}
			}
		}catch (SQLException e) {
			e.printStackTrace();
		}
		return kontakte;
	}
	
	public Connection connectDB(String dbName) {
		try {
			String url = "jdbc:sqlserver://" + PCName + ";databaseName=" + dbName + ";integratedSecurity=true";
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver"); 
			Connection conn = DriverManager.getConnection(url, "Test", "test");
			return conn;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public ArrayList<InvoiceLine> searchRechnung(Vector<Parameter> parms) {
		ArrayList<InvoiceLine> searchAll = new ArrayList<InvoiceLine>();
		Connection conn = connectDB("ErpDB");
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

	public ObservableList<Article> getArticles() throws SQLException {
		ObservableList<Article> articles = FXCollections.observableArrayList();
		Connection conn = connectDB("ErpDB");
		ResultSet rs = null;
		
		String sql = "SELECT * FROM Artikel";
		PreparedStatement cmd;
		try {
			cmd = conn.prepareStatement(sql);
			rs = cmd.executeQuery();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		while(rs.next()) {
			Article a = new Article(rs.getInt("ID"), rs.getString("Bezeichnung"), rs.getDouble("PreisNetto"));
			articles.add(a);
		}

		return articles;
	}
}
