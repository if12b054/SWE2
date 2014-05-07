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
import businessobjects.KontaktModel;
import businessobjects.RechnungModel;
import businessobjects.RechnungZeileModel;

public class Dataaccesslayer {
	
	final String PCName = "ULTRABOOK\\SQLEXPRESS";
	
	public void insertKontakt(KontaktModel k) {
		Connection conn = connectDB("ErpDB");
		
		String sql = "INSERT INTO Kunde VALUES (?,?,?,?,?,?,?,?,?,?)";
		
		try {
			PreparedStatement cmd = conn.prepareStatement(sql);			

			cmd.setInt(1, 4);
			cmd.setInt(2, 1);//cmd.setInt(1, Integer.parseInt(k.getUid()));
			cmd.setString(3,k.getFirma());
			cmd.setString(4,k.getTitel());
			cmd.setString(5,k.getVorname());
			cmd.setString(6,k.getNachname());
			cmd.setString(7,k.getGeburtsdatum()); //cmd.setDate(6,k.getGeburtsdatum());
			cmd.setString(8, null); //Adresse
			cmd.setString(9,k.getRechnungsadresse().elementAt(0));
			cmd.setString(10,k.getLieferadresse().elementAt(0));
			
			cmd.execute();
			
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	public ObservableList<KontaktModel> searchContact(Vector<Parameter> parms) {
		ObservableList<KontaktModel> kontakte = FXCollections.observableArrayList();
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
					KontaktModel k = new KontaktModel(rs.getString("UID"),rs.getString("Firmenname"));
					kontakte.add(k);
				}
				
				//Kontakt ein Mensch
				if(!rs.getString("Vorname").equals(null)){
					KontaktModel k = new KontaktModel(rs.getString("Firmenname"), rs.getString("Vorname"), rs.getString("Nachname")
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

	public ArrayList<RechnungZeileModel> searchRechnung() {
		ArrayList<RechnungZeileModel> searchAll = new ArrayList<RechnungZeileModel>();
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
}
