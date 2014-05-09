package businessobjects;

import java.util.ArrayList;

import javafx.beans.property.FloatProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Invoice extends AbstractObject{
	private ArrayList<InvoiceLine> rechnungszeilen = new ArrayList<InvoiceLine>();
	private FloatProperty betrag = new SimpleFloatProperty();
	private StringProperty datum = new SimpleStringProperty();
	private StringProperty faelligkeit = new SimpleStringProperty();
	private StringProperty kunde = new SimpleStringProperty();
	private StringProperty nachricht = new SimpleStringProperty();
	private StringProperty kommentar = new SimpleStringProperty();
	
	public Invoice(ArrayList<InvoiceLine> rechnungszeilen, String datum, String faelligkeit, String kunde, String nachricht, String kommentar) {
		this.rechnungszeilen = rechnungszeilen;	
		this.datum.set(datum);
		this.faelligkeit.set(faelligkeit);
		this.kunde.set(kunde);
		this.nachricht.set(nachricht);
		this.nachricht.set(kommentar);
		
		for (InvoiceLine r : rechnungszeilen) {
			betrag.set(betrag.get()+r.getBrutto());
		}
	}
	
	public final FloatProperty betragProperty() {
		return betrag;
	}
	
	public final StringProperty datumProperty() {
		return datum;
	}
	
	public final StringProperty faelligkeitProperty() {
		return faelligkeit;
	}

	public final StringProperty kundeProperty() {
		return kunde;
	}
	
	public final StringProperty nachrichtProperty() {
		return nachricht;
	}
	
	public final StringProperty kommentarProperty() {
		return kommentar;
	}
	
	public final void setDatum(String datum) {
		this.datum.set(datum);
	}
	
	public final void setFaelligkeit(String faelligkeit) {
		this.datum.set(faelligkeit);
	}
	
	public final void setKunde(String kunde) {
		this.kunde.set(kunde);
	}
	
	public final void setNachricht(String nachricht) {
		this.nachricht.set(nachricht);
	}
	
	public final void setKommentar(String kommentar) {
		this.kommentar.set(kommentar);
	}
}
