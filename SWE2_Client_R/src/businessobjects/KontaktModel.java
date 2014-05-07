package businessobjects;

import java.sql.Date;
import java.util.Vector;

import javafx.beans.property.FloatProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class KontaktModel extends AModel{
	
	/* Kontakt-type: "Person" or "Firma" */
	private StringProperty typ = new SimpleStringProperty();
	
	/* Person */
	private String titel;
	private StringProperty vorname = new SimpleStringProperty();
	private StringProperty nachname = new SimpleStringProperty();
	private String geburtsdatum;
	
	/* Firma */
	private String uid;
	
	/* Beide */
	private StringProperty firma = new SimpleStringProperty();
	
	/* für Adressen: [0] = Strasse, [1] = PLZ, [2] = Ort, [3] = Land */
	private Vector<String> lieferadresse = new Vector<String>(); 
	private Vector<String> rechnungsadresse = new Vector<String>();
	
	public KontaktModel(String firma, String vorname, String nachname, String titel, String geburtsdatum) {
		this.typ.set("Person");
		this.firma.set(firma);
		this.vorname.set(vorname);
		this.nachname.set(nachname);
	}
	
	public KontaktModel(String uid, String firma) {
		this.typ.set("Firma");
		this.uid = uid;
	}
	
	public final StringProperty typProperty() {
		return typ;
	}
	
	public final StringProperty firmaProperty() {
		return firma;
	}
	
	public final StringProperty vornameProperty() {
		return vorname;
	}

	public final StringProperty nachnameProperty() {
		return nachname;
	}
	
	
	public String getTitel() {
		return titel;
	}

	public void setTitel(String titel) {
		this.titel = titel;
	}

	public String getVorname() {
		return vorname.get();
	}

	public void setVorname(String vorname) {
		this.vorname.set(vorname);
	}

	public String getNachname() {
		return nachname.get();
	}

	public void setNachname(String nachname) {
		this.nachname.set(nachname);
	}

	public String getFirma() {
		return firma.get();
	}

	public void setFirma(String firma) {
		this.firma.set(firma);
	}

	public String getGeburtsdatum() {
		return geburtsdatum;
	}

	public void setGeburtsdatum(String geburtsdatum) {
		this.geburtsdatum = geburtsdatum;
	}

	public Vector<String> getLieferadresse() {
		return lieferadresse;
	}

	public void setLieferadresse(String strasse, String PLZ, String ort, String land) {
		this.lieferadresse.add(strasse);
		this.lieferadresse.add(PLZ);
		this.lieferadresse.add(ort);
		this.lieferadresse.add(land);
	}

	public Vector<String> getRechnungsadresse() {
		return rechnungsadresse;
	}

	public void setRechnungsadresse(String strasse, String PLZ, String ort, String land) {
		this.rechnungsadresse.add(strasse);
		this.rechnungsadresse.add(PLZ);
		this.rechnungsadresse.add(ort);
		this.rechnungsadresse.add(land);
	}
}
