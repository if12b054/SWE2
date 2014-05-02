package businesslayer;

import java.util.ArrayList;
import java.util.Vector;

import javafx.collections.ObservableList;
import applikation.Parameter;
import businessobjects.KontaktModel;
import businessobjects.RechnungModel;
import businessobjects.RechnungZeileModel;
import dataaccesslayer.Dataaccesslayer;

public class Businesslayer {
	Dataaccesslayer d = new Dataaccesslayer();
	
	public void insertKontakt(KontaktModel k) {
		d.insertKontakt(k);
	}
	
	public ObservableList<RechnungModel> searchContact(Vector<Parameter> parms) {
		return d.searchContact(parms);
	}

	public ArrayList<RechnungZeileModel> searchRechnung() {
		ArrayList<RechnungZeileModel> searchAll = new ArrayList<RechnungZeileModel>();
		
		searchAll = d.searchRechnung();
		
		return searchAll;
	}

}
