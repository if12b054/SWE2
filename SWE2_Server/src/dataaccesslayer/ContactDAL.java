package dataaccesslayer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import utils.Parameter;
import businessobjects.Contact;

public class ContactDAL {
	
	public void insertKontakt(Contact k, Connection conn) throws SQLException {
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
	
	public ObservableList<Contact> searchContact(Vector<Parameter> parms, Connection conn) {
		ObservableList<Contact> kontakte = FXCollections.observableArrayList();
		
		ResultSet rs = null;
		
			try{
			//Check ob Vorname != 0
			if(!(parms.get(0).getStringParameter()==null) && !(parms.get(0).getStringParameter()=="")) {
				//Check ob Nachname != 0
				if(!(parms.get(1).getStringParameter()==null) && !(parms.get(1).getStringParameter()=="")){
					//Check ob Firma != 0
					if(!(parms.get(2).getStringParameter()==null) && !(parms.get(2).getStringParameter()=="")){
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
					if(!(parms.get(2).getStringParameter()==null) && !(parms.get(2).getStringParameter()=="")){
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
				if(!(parms.get(1).getStringParameter()==null) && !(parms.get(1).getStringParameter()=="")) {
					//Check ob Firma != 0
					if(!(parms.get(2).getStringParameter()==null) && !(parms.get(2).getStringParameter()=="")) {
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
					if(!(parms.get(2).getStringParameter()==null) && !(parms.get(2).getStringParameter()=="")) {
						//nur Firma
						String sql = "SELECT * FROM Kontakt WHERE Firmenname = ?";
						PreparedStatement cmd = conn.prepareStatement(sql);
						cmd.setString(1, parms.get(2).getStringParameter());
						rs = cmd.executeQuery();
					}
				}
			}
					
			Contact k = null;
			ResultSet rs1;
			
			while(rs.next()) {		
				int AdressID = rs.getInt("Adresse");
				
				//Kontakt ist eine Firma
				if(!(rs.getString("UID") == null)){
					k = new Contact(rs.getString("UID"),rs.getString("Firmenname"));
					kontakte.add(k);
					
					String sql = "SELECT * FROM Adresse WHERE ID = ?";
					PreparedStatement cmd = conn.prepareStatement(sql);
					cmd.setInt(1, AdressID);
					rs1 = cmd.executeQuery();
					while(rs1.next()) {
						k.setAdresse(rs1.getString("Straﬂe"), rs1.getString("PLZ"), rs1.getString("Ort"), rs1.getString("Land"));
					}
					k.setId(rs.getInt("ID"));
				}//Kontakt ein Mensch
				
				if(!(rs.getString("Vorname") == null)){
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
					k.setId(rs.getInt("ID"));
				}
			}
		}catch (SQLException e) {
			e.printStackTrace();
		}
		return kontakte;
	}
	
	public void updateKontakt(Contact k, Connection conn) {
		ResultSet rs = null;
		PreparedStatement cmd;
		try{
		String sql = "UPDATE Kontakt "
				+ "SET UID = ?, Firmenname = ?, Titel = ?, Vorname = ?,Nachname = ?,Geburtsdatum = ?,Typ = ? WHERE ID = ?;";
		
		cmd = conn.prepareStatement(sql);
		cmd.setString(1,k.getUid());
		cmd.setString(2,k.getFirma());
		cmd.setString(3,k.getTitel());
		cmd.setString(4,k.getVorname());
		cmd.setString(5,k.getNachname());
		cmd.setString(6, k.getGeburtsdatum());
		cmd.setString(7,k.getType());
		cmd.setInt(8, k.getId());
		cmd.execute();
		
		String sql1 = "SELECT Adresse FROM Kontakt WHERE ID = ?";
		
		cmd = conn.prepareStatement(sql1);
		cmd.setInt(1, k.getId());
		rs = cmd.executeQuery();
		int AdressID = 0;
		
		if(rs.next())AdressID = rs.getInt("Adresse");
		
		String sql2 = "UPDATE Adresse "
				+ "SET Straﬂe = ?,"
				+ "PLZ = ?,"
				+ "Ort = ?,"
				+ "Land = ? "
				+ "WHERE ID = ?;";
		
		cmd = conn.prepareStatement(sql2);
		cmd.setString(1,k.getAdresse().get(0));
		cmd.setString(2,k.getAdresse().get(1));
		cmd.setString(3,k.getAdresse().get(2));
		cmd.setString(4,k.getAdresse().get(3));
		cmd.setInt(5, AdressID);
		cmd.execute();
		
		} catch(SQLException e) {
			e.printStackTrace();
		}
	}
}
