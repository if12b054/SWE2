package businessobjects;

import java.sql.Date;
import java.util.Vector;

import javafx.beans.property.FloatProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Contact extends AbstractObject{
	
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
	private int id = -1; //db id
	private StringProperty firma = new SimpleStringProperty();
	
	/* für Adressen: [0] = Strasse, [1] = PLZ, [2] = Ort, [3] = Land */
	private Vector<String> adresse = new Vector<String>(); 
	
	public Contact(String firma, String vorname, String nachname, String titel, String geburtsdatum) {
		this.typ.set("Person");
		this.firma.set(firma);
		this.vorname.set(vorname);
		this.nachname.set(nachname);
	}
	
	public Contact(String uid, String firma) {
		this.typ.set("Firma");
		this.firma.set(firma);
		this.setUid(uid);
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

	public Vector<String> getAdresse() {
		return adresse;
	}

	public void setAdresse(String strasse, String PLZ, String ort, String land) {
		this.adresse.add(strasse);
		this.adresse.add(PLZ);
		this.adresse.add(ort);
		this.adresse.add(land);
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
}
