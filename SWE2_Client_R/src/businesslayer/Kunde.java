package businesslayer;

import java.sql.Date;

public class Kunde {
	public boolean kundeType;
	
	/* Person */
	private String titel;
	private String vorname;
	private String nachname;
	private String firma;
	private Date geburtsdatum;
	
	/* Firma */
	private String firmenName;

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

	public String getFirma() {
		return firma;
	}

	public void setFirma(String firma) {
		this.firma = firma;
	}

	public Date getGeburtsdatum() {
		return geburtsdatum;
	}

	public void setGeburtsdatum(Date geburtsdatum) {
		this.geburtsdatum = geburtsdatum;
	}

	public String getFirmenName() {
		return firmenName;
	}

	public void setFirmenName(String firmenName) {
		this.firmenName = firmenName;
	}
	
}
