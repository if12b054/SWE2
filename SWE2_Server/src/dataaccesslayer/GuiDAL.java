package dataaccesslayer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import utils.Parameter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import businessobjects.Article;
import businessobjects.Contact;

public class GuiDAL {
	
	public ObservableList<Article> getArticles(Connection conn) throws SQLException {
		ObservableList<Article> articles = FXCollections.observableArrayList();
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
	
	public ObservableList<Contact> findFirm(Vector<Parameter> parms, Connection conn) {
		//nur Firma
		ObservableList<Contact> kontakte = FXCollections.observableArrayList();
		
		ResultSet rs = null;
		ResultSet rs1 = null;
		PreparedStatement cmd;
		Contact k;
		try {
			String sql = "SELECT * FROM Kontakt WHERE Firmenname LIKE ?";
			//String sql = "SELECT * FROM Kontakt WHERE Firmenname = ?";
			cmd = conn.prepareStatement(sql);
			cmd.setString(1, "%" + parms.get(0).getStringParameter() + "%");
			rs = cmd.executeQuery();
			
			while(rs.next()) {
				
				int AdressID = rs.getInt("Adresse");
				
				//Kontakt ist eine Firma
				if(rs.getString("Vorname") == null){
					k = new Contact(rs.getString("UID"),rs.getString("Firmenname"));
					kontakte.add(k);
					
					sql = "SELECT * FROM Adresse WHERE ID = ?";
					cmd = conn.prepareStatement(sql);
					cmd.setInt(1, AdressID);
					rs1 = cmd.executeQuery();
					while(rs1.next()) {
						k.setAdresse(rs1.getString("Straﬂe"), rs1.getString("PLZ"), rs1.getString("Ort"), rs1.getString("Land"));
					}
					k.setId(rs.getInt("ID"));
				}
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return kontakte;
	}

	
	public ObservableList<Contact> findPerson(Vector<Parameter> parms, Connection conn) {
		ObservableList<Contact> kontakte = FXCollections.observableArrayList();
		
		ResultSet rsFnameSearch = null;
		ResultSet rsLnameSearch = null;
		ResultSet rsAdress = null;
		
		PreparedStatement cmdFnameSearch;
		PreparedStatement cmdLnameSearch;
		PreparedStatement cmdAdress;
		Contact k;
		
		try {
			String sqlFnameSearch = "SELECT * FROM Kontakt WHERE Vorname LIKE ?";
			cmdFnameSearch = conn.prepareStatement(sqlFnameSearch);
			cmdFnameSearch.setString(1, "%" + parms.get(0).getStringParameter() + "%");
			rsFnameSearch = cmdFnameSearch.executeQuery();
			
			while(rsFnameSearch.next()) {
				
				int AdressID = rsFnameSearch.getInt("Adresse");
				
				//Kontakt ein Mensch
				if(!(rsFnameSearch.getString("Vorname") == null)){
					k = new Contact(null, rsFnameSearch.getString("Vorname"), rsFnameSearch.getString("Nachname")
							,rsFnameSearch.getString("Titel"),rsFnameSearch.getString("Geburtsdatum"));
					kontakte.add(k);
					
					String sqlAdress = "SELECT * FROM Adresse WHERE ID = ?";
					cmdAdress = conn.prepareStatement(sqlAdress);
					cmdAdress.setInt(1, AdressID);
					rsAdress = cmdAdress.executeQuery();
					while(rsAdress.next()) {
						k.setAdresse(rsAdress.getString("Straﬂe"), rsAdress.getString("PLZ"), rsAdress.getString("Ort"), rsAdress.getString("Land"));
					}
					k.setId(rsFnameSearch.getInt("ID"));
				}
			}
			
			String sqlLnameSearch = "SELECT * FROM Kontakt WHERE Nachname LIKE ?";
			cmdLnameSearch = conn.prepareStatement(sqlLnameSearch);
			cmdLnameSearch.setString(1, "%" + parms.get(0).getStringParameter() + "%");
			rsLnameSearch = cmdLnameSearch.executeQuery();
			
			while(rsLnameSearch.next()) {
				
				int AdressID = rsLnameSearch.getInt("Adresse");
				
				//Kontakt ein Mensch
				if(!(rsLnameSearch.getString("Vorname") == null)){
					k = new Contact(null, rsLnameSearch.getString("Vorname"), rsLnameSearch.getString("Nachname")
							,rsLnameSearch.getString("Titel"),rsLnameSearch.getString("Geburtsdatum"));
					kontakte.add(k);
					
					String sqlAdress = "SELECT * FROM Adresse WHERE ID = ?";
					cmdAdress = conn.prepareStatement(sqlAdress);
					cmdAdress.setInt(1, AdressID);
					rsAdress = cmdAdress.executeQuery();
					while(rsAdress.next()) {
						k.setAdresse(rsAdress.getString("Straﬂe"), rsAdress.getString("PLZ"), rsAdress.getString("Ort"), rsAdress.getString("Land"));
					}
					k.setId(rsLnameSearch.getInt("ID"));
				}
			}
				
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return kontakte;
}
}
