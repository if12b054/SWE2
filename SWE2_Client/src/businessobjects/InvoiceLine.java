package businessobjects;

import ObserverPattern.Observer;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.FloatProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class InvoiceLine extends AbstractObject {
	private int idNumber = -1; //just for GUI-updating purposes, not for DB!
	private IntegerProperty menge = new SimpleIntegerProperty();
	private DoubleProperty stueckPreis = new SimpleDoubleProperty();
	private DoubleProperty brutto = new SimpleDoubleProperty();
	private StringProperty articleName = new SimpleStringProperty();
	private Article article;
	
	private double netto;
	
	public InvoiceLine(Article article, int newMenge, double newMWSt) {
		this.setArticle(article);
		this.articleName.set(article.getName());
		this.menge.set(newMenge);
		this.stueckPreis.set(article.getPrice());
		netto = Math.round(article.getPrice()*menge.get()*100.0)/100.0;
		brutto.set(Math.round((article.getPrice()*menge.get()*(newMWSt+1))*100.0)/100.0);
	}
	
	public final StringProperty articleNameProperty() {
		return articleName;
	}

	public final DoubleProperty stueckPreisProperty() {
		return stueckPreis;
	}

	public final DoubleProperty bruttoProperty() {
		return brutto;
	}
	
	public final IntegerProperty mengeProperty() {
		return menge;
	}
	
	/* getters and setters */
	
	public void updateMWSt(double newMWSt) {
		brutto.set(Math.round((article.getPrice()*menge.get()*(newMWSt+1))*100.0)/100.0);
	}
	
	public final String getArtikel() {
		return articleName.get();
	}

	public final double getStueckPreis() {
		return stueckPreis.get();
	}

	public final double getBrutto() {
		return brutto.get();
	}
	
	public final int getMenge() {
		return menge.get();
	}

	public double getNetto() {
		return netto;
	}

	public int getIdNumber() {
		return idNumber;
	}

	public void setIdNumber(int idNumber) {
		this.idNumber = idNumber;
	}

	public Article getArticle() {
		return article;
	}

	public void setArticle(Article article) {
		this.article = article;
	}
}
