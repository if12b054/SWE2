package businessobjects;

import javafx.beans.property.FloatProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class RechnungZeileModel extends AModel {
	private int idNumber = -1;
	private IntegerProperty menge = new SimpleIntegerProperty();
	private FloatProperty stueckPreis = new SimpleFloatProperty();
	private FloatProperty brutto = new SimpleFloatProperty();
	private StringProperty artikel = new SimpleStringProperty();
	
	private float netto;
	
	public RechnungZeileModel(String newArtikel, int newMenge, float newStueckPreis, float newMWSt) {
		this.artikel.set(newArtikel);
		this.menge.set(newMenge);
		this.stueckPreis.set(newStueckPreis);
		setNetto(newMenge*newStueckPreis);
	}
	
	public final StringProperty artikelProperty() {
		return artikel;
	}

	public final FloatProperty stueckPreisProperty() {
		return stueckPreis;
	}

	public final FloatProperty bruttoProperty() {
		return brutto;
	}
	
	public final IntegerProperty mengeProperty() {
		return menge;
	}
	
	public final String getArtikel() {
		return artikel.get();
	}

	public final float getStueckPreis() {
		return stueckPreis.get();
	}

	public final float getBrutto() {
		return brutto.get();
	}
	
	public final int getMenge() {
		return menge.get();
	}

	public float getNetto() {
		return netto;
	}

	public void setNetto(float netto) {
		this.netto = netto;
	}

	public int getIdNumber() {
		return idNumber;
	}

	public void setIdNumber(int idNumber) {
		this.idNumber = idNumber;
	}
}
