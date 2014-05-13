package dataaccesslayer;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Vector;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import applikation.Parameter;
import businessobjects.Article;
import businessobjects.Contact;
import businessobjects.Invoice;
import businessobjects.InvoiceLine;

public class Dataaccesslayer {
	
	final String PCName = "Schlepptop\\SQLEXPRESS";
	
	public void insertKontakt(Contact k) throws SQLException {
		Connection conn = connectDB("ErpDB");
		/*
		 * ResultSet rs1;
		ResultSet rs2;
		DatabaseMetaData meta = conn.getMetaData();
		
		String sql1 = "INSERT INTO Adresse(Straﬂe,PLZ,Ort,Land) VALUES (?,?,?,?)";
		cmd = conn.prepareStatement(sql1);
		cmd.setString(1, k.getAdresse().get(0));
		cmd.setInt(2, Integer.parseInt(k.getAdresse().get(1)));
		cmd.setString(3, k.getAdresse().get(2));
		cmd.setString(4, k.getAdresse().get(3));
		
		String sql2 = "SELECT COUNT(*) FROM Adresse";
		cmd = conn.prepareStatement(sql2);
		rs1 = cmd.executeQuery();
		
		int rowCount = rs1.getInt(1);
		
		rs2 = meta.getPrimaryKeys(null, null,"Adresse");
		
		int lastEntryID = rs2.getInt(rowCount);
		 * 
		 * 
		 * */
		
		PreparedStatement cmd;
		if(k.typProperty().getValue().equals("Person")) {
			String sql = "INSERT INTO Kontakt(Firmenname,Titel,Vorname,Nachname,Geburtsdatum,Adresse,Typ) VALUES (?,?,?,?,?,?,?)";
			cmd = conn.prepareStatement(sql);
			cmd.setString(1,k.getFirma());
			cmd.setString(2,k.getTitel());
			cmd.setString(3,k.getVorname());
			cmd.setString(4,k.getNachname());
			cmd.setString(5,k.getGeburtsdatum());
			cmd.setInt(6,1); //TODO ID of an adress
			cmd.setString(7,k.typProperty().getValue());
		}
		else {
			String sql = "INSERT INTO Kontakt(UID,Firmenname,Adresse,Typ) VALUES (?,?,?,?)";
			cmd = conn.prepareStatement(sql);
			cmd.setString(1,k.getUid());
			cmd.setString(2,k.getFirma());
			cmd.setInt(3,1); //TODO ID of an adress
			cmd.setString(4,k.typProperty().getValue());
		}
		
		cmd.execute();
		
	}
	
	public void insertRechnung(Invoice r) {
		Connection conn = connectDB("ErpDB");
		
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

			while(rs.next()) {				
				//Kontakt ist eine Firma
				if(rs.getString("Vorname").equals(null)){
					Contact k = new Contact(rs.getString("UID"),rs.getString("Firmenname"));
					kontakte.add(k);
				}
				
				//Kontakt ein Mensch
				if(!rs.getString("Vorname").equals(null)){
					Contact k = new Contact(rs.getString("Firmenname"), rs.getString("Vorname"), rs.getString("Nachname")
							,rs.getString("Titel"),rs.getString("Geburtsdatum"));
					kontakte.add(k);
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
