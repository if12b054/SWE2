package businesslayer;

import java.util.ArrayList;

import proxy.Client;

public class Businesslayer {
	
	public void insertKontakt(Kunde k, String action) {
		Client c = new Client();
		
		if(action.equals("insert/Kontakt")) {
			c.insertKontakt(k, action);
		}
	}
	
	public ArrayList<RechnungszeileModel> searchRechnung(String action) {
		Client c = new Client();
		ArrayList<RechnungszeileModel> searchAll = new ArrayList<RechnungszeileModel>();
		
		if(action.equals("search/Rechnung")) {
			searchAll = c.searchRechnung(action);
		}
		return searchAll;
	}
}
