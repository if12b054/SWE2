package businesslayer;

public class Rechnungszeile {
	private int menge;
	private float stueckPreis, netto, MWSt, brutto;
	private String artikel;
	
	public Rechnungszeile(String artikel, int newMenge, float newStuekPreis, float newMWSt) {
		//TODO: Setter/Getter, ist einfacher beim DAL
		
		this.artikel = artikel;
		this.menge = newMenge;
		this.stueckPreis = newStuekPreis;
		this.netto = menge*stueckPreis;
		this.MWSt = newMWSt;
		this.brutto = this.netto-this.netto*this.MWSt;
	}
}
