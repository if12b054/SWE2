package javafxModels;

import businessobjects.Article;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

public class InvoiceLineModel {
	private ObjectProperty<Article> article = new SimpleObjectProperty<Article>();
	private ObjectProperty<Integer> quantity = new SimpleObjectProperty<Integer>();
	private StringProperty netto = new SimpleStringProperty();
	private StringProperty salesTax = new SimpleStringProperty();
	private StringProperty brutto = new SimpleStringProperty();
	private StringProperty unitPrice = new SimpleStringProperty();
	
	public ChangeListener<Article> articleValueChanged = new ChangeListener<Article>()
	{
	    @Override
	    public void changed(ObservableValue<? extends Article> arg0, Article oldArticle, Article newArticle)
	    {
	    	double newNetto = Math.round(newArticle.getPrice()*quantity.get()*100.0)/100.0;
	    	double newBrutto = Math.round((newArticle.getPrice()*quantity.get()*(Double.parseDouble(salesTax.get())+1))*100.0)/100.0;
	    	unitPrice.set(Double.toString(newArticle.getPrice()));
	    	netto.set(Double.toString(newNetto));
	    	brutto.set(Double.toString(newBrutto));
	    	System.out.println("Values changing");
	    }
	};
	
	public ChangeListener<Integer> quantityValueChanged = new ChangeListener<Integer>()
	{
	    @Override
	    public void changed(ObservableValue<? extends Integer> arg0, Integer oldQuantity, Integer newQuantity)
	    {
	    	double newNetto = Math.round(article.get().getPrice()*newQuantity*100.0)/100.0;
	    	double newBrutto = Math.round((article.get().getPrice()*newQuantity*(Double.parseDouble(salesTax.get())+1))*100.0)/100.0;
	    	unitPrice.set(Double.toString(article.get().getPrice()));
	    	netto.set(Double.toString(newNetto));
	    	brutto.set(Double.toString(newBrutto));
	    	System.out.println("Values changing");
	    }
	};
	
	public ChangeListener<String> salesTaxValueChanged = new ChangeListener<String>()
	{
	    @Override
	    public void changed(ObservableValue<? extends String> arg0, String oldSalesTax, String newSalesTax)
	    {
	    	double newNetto = Math.round(article.get().getPrice()*quantity.get()*100.0)/100.0;
	    	double newBrutto = Math.round((article.get().getPrice()*quantity.get()*(Double.parseDouble(newSalesTax)+1))*100.0)/100.0;
	    	unitPrice.set(Double.toString(article.get().getPrice()));
	    	netto.set(Double.toString(newNetto));
	    	brutto.set(Double.toString(newBrutto));
	    	System.out.println("Values changing");
	    }
	};
	
	public ObjectProperty<Article> articleProperty() {
		return article;
	}
	
	public ObjectProperty<Integer> quantityProperty() {
		return quantity;
	}
	
	public StringProperty nettoProperty() {
		return netto;
	}

	public StringProperty salesTaxProperty() {
		return salesTax;
	}
	
	public StringProperty bruttoProperty() {
		return brutto;
	}
	
	public StringProperty unitPriceProperty() {
		return unitPrice;
	}
}
