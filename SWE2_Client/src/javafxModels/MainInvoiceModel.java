package javafxModels;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Vector;

import utils.Parameter;
import eu.schudt.javafx.controls.calendar.DatePicker;
import businessobjects.Contact;
import businessobjects.Invoice;
import businessobjects.ResultList;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafxControllers.MainController;

public class MainInvoiceModel {
	
	private MainController controller;
	private DatePicker dpFrom, dpTill;
	private Contact contactReference = null;
	
	/* Invoice Page: */
	private StringProperty priceFrom = new SimpleStringProperty();
	private StringProperty priceTill = new SimpleStringProperty();
	private StringProperty contactString = new SimpleStringProperty();
	private StringProperty rResultCount = new SimpleStringProperty();
	
	private boolean settingReference = false;
	
	public MainInvoiceModel(MainController controller) {
		this.controller = controller;
		this.contactString.addListener(delContactReference);
		createDatePickers();
	}
	
	/**
	 * looks for a contact, similar to the value in contactString, depending on how many
	 * results it finds, different actions are executed:
	 * no result: cross mark is set
	 * one result: gets saved as reference and check mark is set
	 * multiple results: new dialog is opened where one can be selected
	 */
	public void findContact() {
		/* send to proxy */
		ObservableList<Contact> results = controller.getProxy().findContact(contactString.get());
		
		if(results == null || results.isEmpty()) {
			//no Contact found
			contactReference = null;
			controller.setImgContactValid(controller.getNoCheckMark());
		} else if(results.size() == 1) {
			//one Contact found
			setContactReference(results.get(0));
		} else {
			//multiple Contacts found
			controller.showNewDialog(controller.SEARCH_CONTACT_PATH, controller, new ResultList(results));
		}
	}
	
	/**
	 * looks for invoice in database, using the specifications entered in the textfields
	 * proxy returns the found results, and those are set in the invoice table
	 */
	public void searchInvoices() {
		ObservableList<Invoice> rechnungen = FXCollections.observableArrayList();
		
		/* get search parms from TextFields */
		Vector<Parameter> searchParms = new Vector<Parameter>();
		searchParms.addElement(new Parameter(dpFrom.getSelectedDate()));
		searchParms.addElement(new Parameter(dpTill.getSelectedDate()));
		searchParms.addElement(new Parameter(priceFrom.getValue()));
		searchParms.addElement(new Parameter(priceTill.getValue()));
		searchParms.addElement(new Parameter(contactString.getValue()));
		
		/* get Rechnung objects from server according to searchParms */
		rechnungen = controller.getProxy().searchRechnung(searchParms);
		rResultCount.setValue(Integer.toString(rechnungen.size()));
		
		/* display rechnungen in table */
		controller.setTableRechnungSuche(rechnungen);
	}
	
	public void setContactReference(Contact contact) {
		this.contactReference = contact;
		if(!contactReference.getType().equals("Person")) {
			contactString.set(contact.getFirma());
		} else {
			contactString.set(contact.getVorname() + ", " + contact.getNachname());
		}
		controller.setImgContactValid(controller.getCheckMark());
	}
	
	private void createDatePickers() {
		/* Initialize and create the DatePickers */
		dpFrom = new DatePicker(Locale.GERMAN);
		dpFrom.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
		dpFrom.getCalendarView().todayButtonTextProperty().set("Today");
		dpFrom.getCalendarView().setShowWeeks(false);
		dpFrom.getStylesheets().add("fxml/datepicker.css");
		dpTill = new DatePicker(Locale.GERMAN);
		dpTill.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
		dpTill.getCalendarView().todayButtonTextProperty().set("Today");
		dpTill.getCalendarView().setShowWeeks(false);
		dpTill.getStylesheets().add("fxml/datepicker.css");
		
		controller.initDatePickers(dpFrom, dpTill);
	}
	
	public StringProperty priceFromProperty() {
		return priceFrom;
	}
	public StringProperty priceTillProperty() {
		return priceTill;
	}
	public StringProperty contactProperty() {
		return contactString;
	}
	
	/* listeners */
	private ChangeListener<String> delContactReference = new ChangeListener<String>() {
		@Override
		public void changed(ObservableValue<? extends String> observable,
				String oldValue, String newValue) {
			if(!settingReference) {
				contactReference = null;
				controller.setImgContactValid(controller.getNoCheckMark());
			}
		}
	};
}
