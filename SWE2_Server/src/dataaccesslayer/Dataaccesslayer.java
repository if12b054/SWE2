package dataaccesslayer;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Vector;

import utils.Parameter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import businessobjects.Article;
import businessobjects.Contact;
import businessobjects.Invoice;
import businessobjects.InvoiceData;
import businessobjects.InvoiceLine;

public class Dataaccesslayer {
	
	final String PCName = "Schlepptop\\SQLEXPRESS"; //Roman
//	final String PCName = "ULTRABOOK\\SQLEXPRESS"; //Victor
	
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
	
	public void insertKontakt(Contact k) throws SQLException {
		Connection conn = connectDB("ErpDB");
		ContactDAL c = new ContactDAL();
		
		c.insertKontakt(k, conn);
	}
	
	public ObservableList<Contact> searchContact(Vector<Parameter> parms) {
		ObservableList<Contact> kontakte = FXCollections.observableArrayList();
		Connection conn = connectDB("ErpDB");
		ContactDAL c = new ContactDAL();
		
		kontakte = c.searchContact(parms, conn);
		
		return kontakte;
	}
	
	public void updateKontakt(Contact k) {
		Connection conn = connectDB("ErpDB");
		ContactDAL c = new ContactDAL();
		c.updateKontakt(k, conn);
	}
	
	public void insertRechnung(InvoiceData r) throws SQLException {
		Connection conn = connectDB("ErpDB");
		InvoiceDAL i = new InvoiceDAL();
		
		i.insertRechnung(r, conn);
	}
	
	public ArrayList<Invoice> searchRechnung(Vector<Parameter> parms) {
		ArrayList<Invoice> invoices = new ArrayList<Invoice>();
		Connection conn = connectDB("ErpDB");
		InvoiceDAL i = new InvoiceDAL();
		
		invoices = i.searchRechnung(parms, conn);
		
		return invoices;
	}

	public ObservableList<Article> getArticles() throws SQLException {
		ObservableList<Article> articles = FXCollections.observableArrayList();
		Connection conn = connectDB("ErpDB");
		GuiDAL g = new GuiDAL();
		
		articles = g.getArticles(conn);
		
		return articles;
	}

	public ObservableList<Contact> findFirm(Vector<Parameter> parms) {
		ObservableList<Contact> kontakte = FXCollections.observableArrayList();
		Connection conn = connectDB("ErpDB");
		GuiDAL g = new GuiDAL();
		
		kontakte = g.findFirm(parms, conn);
		
		return kontakte;
	}

	public ObservableList<Contact> findPerson(Vector<Parameter> parms) {
		ObservableList<Contact> kontakte = FXCollections.observableArrayList();
		Connection conn = connectDB("ErpDB");
		GuiDAL g = new GuiDAL();
		
		kontakte = g.findPerson(parms, conn);
		
		return kontakte;
	}
}
