package javafxModels;

import java.util.Calendar;
import java.util.Date;

import businessobjects.Adress;
import businessobjects.Invoice;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafxControllers.InvoiceController;

public class InvoiceModel {
	private ObjectProperty<Double> MWSt = new SimpleObjectProperty<Double>();
	private StringProperty contact = new SimpleStringProperty();
	private StringProperty message = new SimpleStringProperty();
	private StringProperty comment = new SimpleStringProperty();
	private StringProperty invStreet = new SimpleStringProperty();
	private StringProperty invPostCode = new SimpleStringProperty();
	private StringProperty invCity = new SimpleStringProperty();
	private StringProperty invCountry = new SimpleStringProperty();
	private StringProperty delStreet = new SimpleStringProperty();
	private StringProperty delPostCode = new SimpleStringProperty();
	private StringProperty delCity = new SimpleStringProperty();
	private StringProperty delCountry = new SimpleStringProperty();
	
	private InvoiceController controller;
	
	public boolean errorsFound() {
		//do error checking here!
		return false;
	}
	
	public void createInvoice(Date date) {
		Date curDate = Calendar.getInstance().getTime();
		Adress invAdress = new Adress(invStreet.get(), Integer.parseInt(invPostCode.get()), invCity.get(), invCountry.get());
		Adress delAdress = new Adress(delStreet.get(), Integer.parseInt(delPostCode.get()), delCity.get(), delCountry.get());
		Invoice invoice = new Invoice(controller.getRechnungszeilen(), curDate, date, contact.get(), message.get(), comment.get(), invAdress, delAdress);
		controller.getParent().getProxy().insertRechnung(invoice);
	}
	
/* returning PROPERTIES */
	
	public final ObjectProperty<Double> MWStProperty() {
		return MWSt;
	}

	public final StringProperty contactProperty() {
		return contact;
	}

	public final StringProperty messageProperty() {
		return message;
	}
	
	public final StringProperty commentProperty() {
		return comment;
	}
	
	public final StringProperty invStreetProperty() {
		return invStreet;
	}

	public final StringProperty invPLZProperty() {
		return invPostCode;
	}
	
	public final StringProperty invCityProperty() {
		return invCity;
	}
	
	public final StringProperty invCountryProperty() {
		return invCountry;
	}
	
	public final StringProperty delStreetProperty() {
		return delStreet;
	}

	public final StringProperty delPLZProperty() {
		return delPostCode;
	}
	
	public final StringProperty delCityProperty() {
		return delCity;
	}
	
	public final StringProperty delCountryProperty() {
		return delCountry;
	}
	
	/* setters and getters */

	public void setController(InvoiceController controller) {
		this.controller = controller;
	}
}
