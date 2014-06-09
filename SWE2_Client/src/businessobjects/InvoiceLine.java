package businessobjects;

import utils.Observer;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.FloatProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class InvoiceLine extends AbstractObject implements Observer {
	private int idNumber = -1; //just for GUI-updating purposes, not for DB!
	private IntegerProperty menge = new SimpleIntegerProperty();
	private DoubleProperty stueckPreis = new SimpleDoubleProperty();
	private DoubleProperty brutto = new SimpleDoubleProperty();
	private DoubleProperty netto = new SimpleDoubleProperty();
	private StringProperty articleName = new SimpleStringProperty();
	private Article article;
	private double MWSt;
	
	public InvoiceLine(Article article, int newMenge, double newMWSt) {
		this.MWSt = newMWSt;
		this.setArticle(article);
		this.articleName.set(article.getName());
		this.menge.set(newMenge);
		this.stueckPreis.set(article.getPrice());
		netto.set(Math.round(article.getPrice()*menge.get()*100.0)/100.0);
		brutto.set(Math.round((article.getPrice()*menge.get()*(newMWSt+1))*100.0)/100.0);
	}
	
	public final StringProperty articleNameProperty() {
		return articleName;
	}
	
	public final DoubleProperty nettoProperty() {
		return netto;
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
		this.MWSt = newMWSt;
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
	
	public void setMenge(int menge) {
		this.menge.set(menge);
		netto.set(Math.round(article.getPrice()*menge*100.0)/100.0);
		brutto.set(Math.round((article.getPrice()*menge*(MWSt+1))*100.0)/100.0);
	}

	public double getNetto() {
		return netto.get();
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

	@Override
	public void update(double newMWSt) {
		brutto.set(Math.round((article.getPrice()*menge.get()*(newMWSt+1))*100.0)/100.0);
	}
	
	public InvoiceLineData generateInvoiceLineData(){
		InvoiceLineData iL = new InvoiceLineData();
		
		iL.setIdNumber(this.idNumber);
		iL.setMenge(this.menge.get());
		iL.setStueckPreis(this.stueckPreis.get());
		iL.setBrutto(this.brutto.get());
		iL.setNetto(this.netto.get());
		iL.setArticle(this.article);
		iL.setArticleName(this.articleName.get());
		iL.setMWSt(this.MWSt);
		
		return iL;
	}
	
}
