package businessobjects;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.FloatProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class InvoiceLine extends AbstractObject {
	private int idNumber = -1;
	private IntegerProperty menge = new SimpleIntegerProperty();
	private DoubleProperty stueckPreis = new SimpleDoubleProperty();
	private DoubleProperty brutto = new SimpleDoubleProperty();
	private StringProperty articleName = new SimpleStringProperty();
	private Article article;
	
	private double netto;
	
	public InvoiceLine(Article article, int newMenge, double newStueckPreis, double newMWSt) {
		this.setArticle(article);
		this.articleName.set(article.getName());
		this.menge.set(newMenge);
		this.stueckPreis.set(newStueckPreis);
		netto = newMenge*newStueckPreis;
		brutto.set(newMenge*newStueckPreis*(1+newMWSt));
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
