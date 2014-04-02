package businesslayer;

import java.util.ArrayList;

import dataaccesslayer.Dataaccesslayer;

public class Businesslayer {
	Dataaccesslayer d = new Dataaccesslayer();
	
	public void insertKontakt(Kunde k) {
		d.insertKontakt(k);
	}

	public ArrayList<RechnungszeileModel> searchRechnung() {
		ArrayList<RechnungszeileModel> searchAll = new ArrayList<RechnungszeileModel>();
		
		searchAll = d.searchRechnung();
		
		return searchAll;
	}

}
