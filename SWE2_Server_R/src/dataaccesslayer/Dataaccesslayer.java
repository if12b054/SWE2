package dataaccesslayer;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import businesslayer.Kunde;
import businesslayer.RechnungszeileModel;

public class Dataaccesslayer {
	public void insertKontakt(Kunde k) {
		Connection conn = connectDB("Schlepptop", "ErpDB");
		
		String sql = "INSERT INTO Kontakt VALUES (?,?,?,?,?,?,?,?,?)";
		
		try {
			PreparedStatement cmd = conn.prepareStatement(sql);
			cmd.setInt(1, 5);
			cmd.setString(2,null);
			cmd.setString(3,k.getTitel());
			cmd.setString(4,k.getVorname());
			cmd.setString(5,k.getNachname());
			cmd.setDate(6,k.getGeburtsdatum());
			cmd.setString(7," 0 "); //Adresse
			cmd.setString(8,null);
			cmd.setString(9,null);
			cmd.execute();
			
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	public Connection connectDB(String pcName, String dbName) {
		try {
			String url = "jdbc:sqlserver://" + pcName + "\\SQLEXPRESS;databaseName=" + dbName +";integratedSecurity=true";
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

	public ArrayList<RechnungszeileModel> searchRechnung() {
		ArrayList<RechnungszeileModel> searchAll = new ArrayList<RechnungszeileModel>();
		Connection conn = connectDB("Schlepptop", "ErpDB");
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
