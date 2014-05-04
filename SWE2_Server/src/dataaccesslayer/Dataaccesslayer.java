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
	
	final String PCName = "Schlepptop\\SQLEXPRESS";
	
	public void insertKontakt(KontaktModel k) {
		Connection conn = connectDB("ErpDB");
		
		String sql = "INSERT INTO Kunde VALUES (?,?,?,?,?,?,?,?,?,?)";
		
		try {
			PreparedStatement cmd = conn.prepareStatement(sql);			

			cmd.setInt(1, 3);
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
		
		System.out.println("Server Vorname: " + parms.get(0).getStringParameter());
		System.out.println("Server Nachname: " + parms.get(1).getStringParameter());
		System.out.println("Server Firma: " + parms.get(2).getStringParameter());
		
		
		ResultSet rs = null;
		try{
			if(!parms.get(0).getStringParameter().equals(null)){
				String sql = "SELECT * FROM Kunde WHERE Vorname = ?";
				PreparedStatement cmd = conn.prepareStatement(sql);
				cmd.setString(1, parms.get(0).getStringParameter());
				rs = cmd.executeQuery();
			}
			
			boolean empty = true;
			
			while(rs.next()) {
				empty = false;
				System.out.println("nname " + rs.getString("Nachname"));
				System.out.println("vname " + rs.getString("Vorname"));	
			}
			
			if(empty) {
				System.out.println("ResultSet ist leer");
			}
			
			while(rs.next()) {
				//Kontakt ist eine Firma
				if(rs.getString("UID") != null){
					KontaktModel k = new KontaktModel(rs.getString("UID"),rs.getString("Firmenname"));
					kontakte.add(k);
				}
				
				//Kontakt ein Mensch
				if(rs.getString("UID") != null){
					KontaktModel k = new KontaktModel(rs.getString("Firmenname"), rs.getString("Vorname"), rs.getString("Nachname")
							,rs.getString("Titel"),rs.getString("Geburtsdatum"));
					System.out.println("nname " + k.getNachname());
					System.out.println("vname " + k.getVorname());
				}
			}
			/*
			//Check ob Vorname != 0
			if(!parms.get(0).getStringParameter().equals(null)) {
				System.out.println("01");
				//Check ob Nachname != 0
				if(!parms.get(1).getStringParameter().equals(null)){
					System.out.println("02");
					//Check ob Firma != 0
					if(!parms.get(2).getStringParameter().equals(null)){
						//Alle drei
						String sql = "SELECT * FROM Kunde WHERE VORNAME = ? AND NACHNAME = ? AND Firmenname = ?";
						PreparedStatement cmd;
							cmd = conn.prepareStatement(sql);
							System.out.println("1");
						cmd.setString(1, parms.get(0).toString());
						cmd.setString(2, parms.get(1).toString());
						cmd.setString(3, parms.get(2).toString());
						rs = cmd.executeQuery();
					}
					else{//Vorname und Nachname
						String sql = "SELECT * FROM Kunde WHERE VORNAME = ? AND NACHNAME = ?";
						System.out.println("2");
						PreparedStatement cmd = conn.prepareStatement(sql);
						cmd.setString(1, parms.get(0).toString());
						cmd.setString(2, parms.get(1).toString());
						rs = cmd.executeQuery();
					}
				}
				else {
					//Vorname und Firma
					if(!parms.get(2).getStringParameter().equals(null)){
						System.out.println("3");
						String sql = "SELECT * FROM Kunde WHERE VORNAME = ? AND Firmenname = ?";
						PreparedStatement cmd = conn.prepareStatement(sql);
						cmd.setString(1, parms.get(0).toString());
						cmd.setString(2, parms.get(2).toString());
						rs = cmd.executeQuery();
					}
					else{//nur Vorname
						System.out.println("4");
						String sql = "SELECT * FROM Kunde WHERE VORNAME = ?";
						PreparedStatement cmd = conn.prepareStatement(sql);
						cmd.setString(1, parms.get(0).toString());
						rs = cmd.executeQuery();
					}
				}
			}
			//Check ob Nachname != 0
			if(parms.get(1).toString() != null) {
				//Check ob Firma != 0
				if(parms.get(2).toString() != null) {
					//Nachname und Firma
					String sql = "SELECT * FROM Kunde WHERE NACHNAME = ? AND Firmenname = ?";
					PreparedStatement cmd = conn.prepareStatement(sql);
					cmd.setString(1, parms.get(1).toString());
					cmd.setString(2, parms.get(2).toString());
					rs = cmd.executeQuery();
				}
				else {	//nur Nachname
					String sql = "SELECT * FROM Kunde WHERE NACHNAME = ?";
					PreparedStatement cmd = conn.prepareStatement(sql);
					cmd.setString(1, parms.get(1).toString());
					rs = cmd.executeQuery();
				}
			}
			//Check ob Firma != 0
			if(parms.get(2).toString() != null) {
				//nur Firma
				String sql = "SELECT * FROM Kunde WHERE Firmenname = ?";
				PreparedStatement cmd = conn.prepareStatement(sql);
				cmd.setString(1, parms.get(2).toString());
				rs = cmd.executeQuery();
			}
			
			while(rs.next()) {
				//Kontakt ist eine Firma
				if(rs.getString("UID") != null){
					KontaktModel k = new KontaktModel(rs.getString("UID"),rs.getString("Firmenname"));
					kontakte.add(k);
				}
				
				//Kontakt ein Mensch
				if(rs.getString("UID") != null){
					KontaktModel k = new KontaktModel(rs.getString("Firmenname"), rs.getString("Vorname"), rs.getString("Nachname")
							,rs.getString("Titel"),rs.getString("Geburtsdatum"));
				}
			}
			*/
		}catch (SQLException e) {
			e.printStackTrace();
		}
		
		return kontakte;
	}
	
	public Connection connectDB(String dbName) {
		try {
			String url = "jdbc:sqlserver://" + PCName + ";databaseName=" + dbName + ";integratedSecurity=true";
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			Connection conn = DriverManager.getConnection(url);
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
