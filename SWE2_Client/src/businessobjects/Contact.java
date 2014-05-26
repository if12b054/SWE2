package businessobjects;

import java.sql.Date;
import java.util.Vector;

import javafx.beans.property.FloatProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Contact extends AbstractObject{
	
	/* Kontakt-type: "Person" or "Firma" */
	private StringProperty typ = new SimpleStringProperty();
	private int id = -1; //db id
	
	/* Person */
	private StringProperty titel = new SimpleStringProperty();
	private StringProperty vorname = new SimpleStringProperty();
	private StringProperty nachname = new SimpleStringProperty();
	private StringProperty geburtsdatum = new SimpleStringProperty();
	private Contact firmaRef = null;
	
	/* Firma */
	private StringProperty uid = new SimpleStringProperty();
	
	/* Beide */
	private StringProperty firma = new SimpleStringProperty();
	
	/* für Adressen: [0] = Strasse, [1] = PLZ, [2] = Ort, [3] = Land */
	private Vector<String> adresse = new Vector<String>(); 
	private StringProperty adresseProperty = new SimpleStringProperty();
	
	public Contact(String firma, String vorname, String nachname, String titel, String geburtsdatum) {
		this.typ.set("Person");
		this.firma.set(firma);
		this.vorname.set(vorname);
		this.nachname.set(nachname);
		this.geburtsdatum.set(geburtsdatum);
	}
	
	public Contact(String uid, String firma) {
		this.typ.set("Firma");
		this.firma.set(firma);
		this.setUid(uid);
	}
	
	/* properties */
	
	public final StringProperty typProperty() {
		return typ;
	}
	
	public final StringProperty firmaProperty() {
		return firma;
	}
	
	public final StringProperty uidProperty() {
		return firma;
	}
	
	public final StringProperty titelProperty() {
		return vorname;
	}
	
	public final StringProperty vornameProperty() {
		return vorname;
	}

	public final StringProperty nachnameProperty() {
		return nachname;
	}

	public final StringProperty geburtsdatumProperty() {
		return geburtsdatum;
	}
	
	public final StringProperty adresseProperty() {
		return adresseProperty;
	}
	
	/* getter and setter */
	
	public String getTitel() {
		return titel.get();
	}

	public void setTitel(String titel) {
		this.titel.set(titel);
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
		return geburtsdatum.get();
	}

	public void setGeburtsdatum(String geburtsdatum) {
		this.geburtsdatum.set(geburtsdatum);
	}

	public Vector<String> getAdresse() {
		return adresse;
	}

	public void setAdresse(String strasse, String PLZ, String ort, String land) {
		this.adresse.add(strasse);
		this.adresse.add(PLZ);
		this.adresse.add(ort);
		this.adresse.add(land);
		
		adresseProperty.set(strasse + ", " + PLZ + " " + ort + ", " + land);
	}

	public String getUid() {
		return uid.get();
	}

	public void setUid(String uid) {
		this.uid.set(uid);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Contact getFirmaRef() {
		return firmaRef;
	}

	public void setFirmaRef(Contact firmaRef) {
		this.firmaRef = firmaRef;
	}
}
