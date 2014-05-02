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
	
	final String PCName = "Ultrabook\\SQLEXPRESS";
	
	public void insertKontakt(KontaktModel k) {
		Connection conn = connectDB("ErpDB");
		
		String sql = "INSERT INTO Kontakt VALUES (?,?,?,?,?,?,?,?,?)";
		
		try {
			PreparedStatement cmd = conn.prepareStatement(sql);
			cmd.setInt(1, 5);
			cmd.setString(2,null);
			cmd.setString(3,k.getTitel());
			cmd.setString(4,k.getVorname());
			cmd.setString(5,k.getNachname());
//			cmd.setDate(6,k.getGeburtsdatum());
			cmd.setString(7," 0 "); //Adresse
			cmd.setString(8,null);
			cmd.setString(9,null);
			cmd.execute();
			
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	public ObservableList<RechnungModel> searchContact(Vector<Parameter> parms) {
		ObservableList<RechnungModel> rechnungen = FXCollections.observableArrayList();
		Connection conn = connectDB("ErpDB");
		
		String sql = "INSERT INTO Kontakt VALUES (?,?,?,?,?,?,?,?,?)";
		
		System.out.println("Searching for Contact");
		
//		try {
//			PreparedStatement cmd = conn.prepareStatement(sql);
//			cmd.setInt(1, 5);
//			cmd.setString(2,null);
//			cmd.setString(3,k.getTitel());
//			cmd.setString(4,k.getVorname());
//			cmd.setString(5,k.getNachname());
////			cmd.setDate(6,k.getGeburtsdatum());
//			cmd.setString(7," 0 "); //Adresse
//			cmd.setString(8,null);
//			cmd.setString(9,null);
//			cmd.execute();
//			
//			
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
		return rechnungen;
	}
	
	public Connection connectDB(String dbName) {
		try {
			String url = "jdbc:sqlserver://" + PCName + ";databaseName=" + dbName;
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
