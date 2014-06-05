package businesslayer;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Vector;

import utils.Parameter;
import javafx.collections.ObservableList;
import businessobjects.Article;
import businessobjects.Contact;
import businessobjects.Invoice;
import businessobjects.InvoiceLine;
import dataaccesslayer.Dataaccesslayer;

public class Businesslayer {
	Dataaccesslayer d = new Dataaccesslayer();
	
	public void insertKontakt(Contact k) throws SQLException {
		d.insertKontakt(k);
	}
	
	public void updateKontakt(Contact k) {
		d.updateKontakt(k);
	}
	
	public void insertRechnung(Invoice r) throws SQLException {
		d.insertRechnung(r);
	}
	
	public ObservableList<Contact> searchContact(Vector<Parameter> parms) {
		return d.searchContact(parms);
	}

	public ArrayList<InvoiceLine> searchRechnung(Vector<Parameter> parms) {
		return d.searchRechnung(parms);
	}

	public ObservableList<Article> getArticles() throws SQLException {
		return d.getArticles();
	}



}
