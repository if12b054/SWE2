package javafxModels;

import businessobjects.Article;
import businessobjects.InvoiceLine;
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
	
	private InvoiceLine invoiceLine;
	
	public ChangeListener<Article> articleValueChanged = new ChangeListener<Article>()
	{
	    @Override
	    public void changed(ObservableValue<? extends Article> arg0, Article oldArticle, Article newArticle)
	    {
	    	updateValues();
	    }
	};
	
	public ChangeListener<Integer> quantityValueChanged = new ChangeListener<Integer>()
	{
	    @Override
	    public void changed(ObservableValue<? extends Integer> arg0, Integer oldQuantity, Integer newQuantity)
	    {
	    	updateValues();
	    }
	};
	
	public ChangeListener<String> taxValueChanged = new ChangeListener<String>()
	{
	    @Override
	    public void changed(ObservableValue<? extends String> arg0, String oldSalesTax, String newSalesTax)
	    {
	    	updateValues();
	    }
	};
	
	public void updateValues() {
		invoiceLine = new InvoiceLine(article.get(), quantity.get().intValue(), Double.parseDouble(salesTax.get()));
		double newNetto = Math.round(article.get().getPrice()*quantity.get()*100.0)/100.0;
    	double newBrutto = Math.round((article.get().getPrice()*quantity.get()*(Double.parseDouble(salesTax.get())+1))*100.0)/100.0;
    	unitPrice.set(Double.toString(invoiceLine.getStueckPreis()));
    	netto.set(Double.toString(invoiceLine.getNetto()));
    	brutto.set(Double.toString(invoiceLine.getBrutto()));
    	System.out.println("Values changing");
	}
	
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
	
	public InvoiceLine getInvoiceLine() {
		return invoiceLine;
	}
}
