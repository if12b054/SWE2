package businesslayer;

import javafx.beans.property.FloatProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class RechnungszeileModel {
	private IntegerProperty menge = new SimpleIntegerProperty();
	private FloatProperty stueckPreis = new SimpleFloatProperty();
	private FloatProperty netto = new SimpleFloatProperty();
	private FloatProperty MWSt = new SimpleFloatProperty();
	private FloatProperty brutto = new SimpleFloatProperty();
	private StringProperty artikel = new SimpleStringProperty();
	
	public RechnungszeileModel(String artikel, int newMenge, float newStuekPreis, float newMWSt) {
		this.artikel.set(artikel);
		this.menge.set(newMenge);
		this.stueckPreis.set(newStuekPreis);
		this.netto.set(menge.get()*stueckPreis.get());
		this.MWSt.set(newMWSt);
		this.brutto.set(this.netto.get()-(this.netto.get()*this.MWSt.get()));
	}
	
	public final StringProperty artikelProperty() {
		return artikel;
	}

	public final FloatProperty stueckPreisProperty() {
		return stueckPreis;
	}
	
	public final FloatProperty nettoProperty() {
		return netto;
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
	
	public final float getNetto() {
		return netto.get();
	}

	public final float getBrutto() {
		return brutto.get();
	}
	
	public final float getMenge() {
		return menge.get();
	}
}
