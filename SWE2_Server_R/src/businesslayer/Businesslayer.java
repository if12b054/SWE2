package businesslayer;

import java.util.ArrayList;

import businessobjects.KontaktModel;
import businessobjects.RechnungZeileModel;
import dataaccesslayer.Dataaccesslayer;

public class Businesslayer {
	Dataaccesslayer d = new Dataaccesslayer();
	
	public void insertKontakt(KontaktModel k) {
		d.insertKontakt(k);
	}

	public ArrayList<RechnungZeileModel> searchRechnung() {
		ArrayList<RechnungZeileModel> searchAll = new ArrayList<RechnungZeileModel>();
		
		searchAll = d.searchRechnung();
		
		return searchAll;
	}

}
