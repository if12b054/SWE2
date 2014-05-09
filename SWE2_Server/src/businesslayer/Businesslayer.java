package businesslayer;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Vector;

import javafx.collections.ObservableList;
import applikation.Parameter;
import businessobjects.Contact;
import businessobjects.Invoice;
import businessobjects.InvoiceLine;
import dataaccesslayer.Dataaccesslayer;

public class Businesslayer {
	Dataaccesslayer d = new Dataaccesslayer();
	
	public void insertKontakt(Contact k) throws SQLException {
		d.insertKontakt(k);
	}
	
	public ObservableList<Contact> searchContact(Vector<Parameter> parms) {
		return d.searchContact(parms);
	}

	public ArrayList<InvoiceLine> searchRechnung() {
		ArrayList<InvoiceLine> searchAll = new ArrayList<InvoiceLine>();
		
		searchAll = d.searchRechnung();
		
		return searchAll;
	}

}
