package businessobjects;

import java.util.Vector;

public class ContactData {
	
	private String typ ="";
	private int id = -1;
	private String titel;
	private String vorname;
	private String nachname;
	private String geburtsdatum;
	private String firmaId = null;
	
	private String uid;
	private String firma;
	
	private String[] adresse;
	private String adresseProperty;
	
	public String getTyp() {
		return typ;
	}
	public void setTyp(String typ) {
		this.typ = typ;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getTitel() {
		return titel;
	}
	public void setTitel(String titel) {
		this.titel = titel;
	}
	public String getVorname() {
		return vorname;
	}
	public void setVorname(String vorname) {
		this.vorname = vorname;
	}
	public String getNachname() {
		return nachname;
	}
	public void setNachname(String nachname) {
		this.nachname = nachname;
	}
	public String getGeburtstag() {
		return geburtsdatum;
	}
	public void setGeburtstag(String geburtsdatum) {
		this.geburtsdatum = geburtsdatum;
	}
	public String getFirmaId() {
		return firmaId;
	}
	public void setFirmaId(String firmaId) {
		this.firmaId = firmaId;
	}
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	public String getFirma() {
		return firma;
	}
	public void setFirma(String firma) {
		this.firma = firma;
	}
	public String[] getAdresse() {
		return adresse;
	}
	public void setAdresse(String[] adresse) {
		this.adresse = adresse;
	}
	public String getAdresseProperty() {
		return adresseProperty;
	}
	public void setAdresseProperty(String adresseProperty) {
		this.adresseProperty = adresseProperty;
	}


}
