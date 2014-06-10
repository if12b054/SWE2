package javafxModels;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;
import java.util.ResourceBundle;

import proxy.Proxy;
import utils.Observer;
import utils.Subject;
import utils.ErrorCheckUtils;
import eu.schudt.javafx.controls.calendar.DatePicker;
import businessobjects.AbstractObject;
import businessobjects.Adress;
import businessobjects.Contact;
import businessobjects.Invoice;
import businessobjects.InvoiceLine;
import businessobjects.ResultList;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafxControllers.AbstractController;
import javafxControllers.ContactSearchController;
import javafxControllers.InvoiceController;
import javafxControllers.InvoiceLineController;
import javafxControllers.MainController;

public class InvoiceModel implements Subject{
	private Invoice curInvoice = null;
	public Contact contactReference = null;
	
	private InvoiceController controller;
	private ObservableList<InvoiceLine> invoiceLines = FXCollections.observableArrayList();
	
	private StringProperty MWSt = new SimpleStringProperty();
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
	private BooleanProperty searchFirm = new SimpleBooleanProperty();
	
	private DatePicker dp;
	
	private int invoiceLineCount = 0;
	private boolean settingReference = false;
	
	public InvoiceModel(InvoiceController controller) {
		this.controller = controller;
		contact.addListener(delContactReference);
	}
	
	public DatePicker createDatePicker() {
		dp = new DatePicker(Locale.GERMAN);
		dp.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
		dp.getCalendarView().todayButtonTextProperty().set("Today");
		dp.getCalendarView().setShowWeeks(false);
		dp.getStylesheets().add("fxml/datepicker.css");
		return dp;
	}
	
	public void save() {
		if(contactReference == null) {
			controller.showErrorDialog("You need to find a valid contact in database. Try searching for it.");
			return;
		} else if(!ErrorCheckUtils.isDouble(MWSt.get())) {
			controller.showErrorDialog("MWSt needs to be a double value.");
			return;
		} else if(!validInvoiceAdress()) {
			return;
		} else if(!validDeliveryAdress()) {
			return;
		} else if(dp.getSelectedDate() == null) {
			controller.showErrorDialog("The dueDate needs to be set!");
			return;
		} else if( dp.invalidProperty().get()) {
			controller.showErrorDialog("Wrong date format! E.g. 2010-01-15");
			return;
		} else if( invoiceLines == null || invoiceLines.isEmpty()) {
			controller.showErrorDialog("No invoicelines inserted.");
			return;
		} 
		/* if everything is alright, */
		else {
			Date curDate = Calendar.getInstance().getTime();
			Adress invAdress = new Adress(invStreet.get(), Integer.parseInt(invPostCode.get()), invCity.get(), invCountry.get());
			Adress delAdress = new Adress(delStreet.get(), Integer.parseInt(delPostCode.get()), delCity.get(), delCountry.get());
			curInvoice = new Invoice(invoiceLines, Double.parseDouble(MWSt.get()), curDate, dp.getSelectedDate(), contactReference, message.get(), comment.get(), invAdress, delAdress);
			if(Proxy.serverConnection()) {
				controller.getParent().getProxy().insertInvoice(curInvoice);
				//curInvoice.setId(invoiceID);
				controller.setEditMode();
			} else {
				controller.showErrorDialog("Connection Error. Server might not be reachable.");
			}
		}
	}
	
	public void exit() {
		Stage stage = controller.getStage();
		stage.close();
	}
	
	public void print() {
		//TODO
	}
	
	public void loadModel(AbstractObject model) {
		Invoice invoice = (Invoice) model;
		/* turn on editMode => disable most of the fields*/
		controller.setEditMode();
		/* load invoice to gui */
		curInvoice = invoice;
		MWSt.set(Double.toString(invoice.getMWSt()));
		contact.set(invoice.getContactString());
		comment.set(invoice.getComment());
		message.set(invoice.getMessage());
		invStreet.set(invoice.getInvAdress().getStreet());
		invPostCode.set(Integer.toString(invoice.getInvAdress().getPostcode()));
		invCity.set(invoice.getInvAdress().getCity());
		invCountry.set(invoice.getInvAdress().getCountry());
		delStreet.set(invoice.getDelAdress().getStreet());
		delPostCode.set(Integer.toString(invoice.getDelAdress().getPostcode()));
		delCity.set(invoice.getDelAdress().getCity());
		delCountry.set(invoice.getDelAdress().getCountry());
		controller.addInvoiceLineItems(invoice.getInvoiceLines());
		controller.setImgContactValid(controller.getParent().getCheckMark());
	}
	
	public void findContact() {
		ObservableList<Contact> results;
		if(Proxy.serverConnection()) {
			/* send to proxy */
			if(searchFirm.get()) {
				results = controller.getParent().getProxy().findFirm(contact.get());
			} else {
				results = controller.getParent().getProxy().findPerson(contact.get());
			}
		} else {
			controller.showErrorDialog("Connection Error. Server might not be reachable.");
			return;
		}
		
		if(results == null || results.isEmpty()) {
			//no Contact found
			contactReference = null;
			controller.setImgContactValid(controller.getParent().getNoCheckMark());
		} else if(results.size() == 1) {
			//one Contact found
			setContactReference(results.get(0));
			controller.setImgContactValid(controller.getParent().getCheckMark());
		} else {
			//multiple Contacts found
			//open new dialog
			controller.showNewDialog(controller.SEARCH_CONTACT_PATH, controller, new ResultList(results));
		}
	}
	
	public void openInvoiceLine() {
		setInvoiceLineCount(getInvLineCnt() + 1);
		controller.showNewDialog(controller.CREATE_LINE_PATH, controller, null);
	}
	
	public boolean validInvoiceAdress() {
		if(ErrorCheckUtils.isNullOrEmpty(invStreet.get())) {
			controller.showErrorDialog("Street is missing in invoice adress.");
			return false;
		} else if(ErrorCheckUtils.isNullOrEmpty(invCity.get())) {
			controller.showErrorDialog("City is missing in invoice adress.");
			return false;
		} else if(ErrorCheckUtils.isNullOrEmpty(invCountry.get())) {
			controller.showErrorDialog("Country is missing in invoice adress.");
			return false;
		} else if(ErrorCheckUtils.isNullOrEmpty(invPostCode.get())) {
			controller.showErrorDialog("Postcode is missing in invoice adress.");
			return false;
		} else if(!ErrorCheckUtils.isInteger(invPostCode.get())) {
			controller.showErrorDialog("The invoice adress postcode needs to be numeric(0-9).");
			return false;
		}
		return true;
	}
	
	public boolean validDeliveryAdress() {
		if(ErrorCheckUtils.isNullOrEmpty(delStreet.get())) {
			controller.showErrorDialog("Street is missing in delivery adress.");
			return false;
		} else if(ErrorCheckUtils.isNullOrEmpty(delCity.get())) {
			controller.showErrorDialog("City is missing in delivery adress.");
			return false;
		} else if(ErrorCheckUtils.isNullOrEmpty(delCountry.get())) {
			controller.showErrorDialog("Country is missing in delivery adress.");
			return false;
		} else if(ErrorCheckUtils.isNullOrEmpty(delPostCode.get())) {
			controller.showErrorDialog("Postcode is missing in delivery adress.");
			return false;
		} else if(!ErrorCheckUtils.isInteger(delPostCode.get())) {
			controller.showErrorDialog("The delivery adress postcode needs to be numeric(0-9).");
			return false;
		}
		return true;
	}
	
	public void addInvoiceLineToTable(InvoiceLine rechnungszeile) {
		invoiceLines.add(rechnungszeile);
		controller.addInvoiceLineItems(invoiceLines);
	}
	
	/* listeners and event-handlers*/
	public EventHandler<MouseEvent> handleDoubleClick = new EventHandler<MouseEvent>() {
	    @Override
	    public void handle(MouseEvent mouseEvent) {
	        if(mouseEvent.getButton().equals(MouseButton.PRIMARY)){
	            if(mouseEvent.getClickCount() == 2 && curInvoice == null){
	            	InvoiceLine rZeile = controller.getSelectedInvoiceLine();
	                if(rZeile != null) {
	                	controller.showNewDialog(controller.EDIT_LINE_PATH, controller, rZeile);
	                }
	            }
	        }
	    }
	};
	
	public ChangeListener<String> updateObservers = new ChangeListener<String>() {
		@Override
		public void changed(ObservableValue<? extends String> observable,
				String oldValue, String newValue) {
			if(ErrorCheckUtils.isDouble(newValue)) {
				notifyObserver();
			} else {
				controller.showErrorDialog("MWSt needs to be a double value.");
				MWSt.set(oldValue);
			}
		}
	};
	
	private ChangeListener<String> delContactReference = new ChangeListener<String>() {
		@Override
		public void changed(ObservableValue<? extends String> observable,
				String oldValue, String newValue) {
			if(!settingReference) {
				contactReference = null;
				controller.setImgContactValid(controller.getParent().getNoCheckMark());
			}
		}
	};
	
	public Double getMWSt() {
		return Double.parseDouble(MWSt.get());
	}
	
	public ObservableList<InvoiceLine> getInvoiceLines() {
		return invoiceLines;
	}

	public int getInvLineCnt() {
		return invoiceLineCount;
	}

	public void setInvoiceLineCount(int invoiceLineCount) {
		this.invoiceLineCount = invoiceLineCount;
	}
	
	/* returning properties */
	public BooleanProperty searchFirmProperty() {
		return searchFirm;
	}
	
	public final StringProperty MWStProperty() {
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
	
	@Override
	public void register(Observer o) {
		invoiceLines.add((InvoiceLine) o);
		
	}
	@Override
	public void unregister(Observer o) {
		invoiceLines.remove(o);
	}
	@Override
	public void notifyObserver() {
		for(Observer i : invoiceLines) {
			i.update(Double.parseDouble(MWSt.get()));
		}
	}
	
	public void setContactReference(Contact contact) {
		settingReference = true;
		this.contactReference = contact;
		if(!contactReference.getType().equals("Person")) {
			this.contact.set(contact.getFirma());
		} else {
			this.contact.set(contact.getVorname() + ", " + contact.getNachname());
		}
		controller.setImgContactValid(controller.parent.getCheckMark());
		settingReference = false;
	}
	
	public void clear() {
		contact.set(null);
		message.set(null);
		comment.set(null);
		invStreet.set(null);
		invPostCode.set(null);
		invCity.set(null);
		invCountry.set(null);
		delStreet.set(null);
		delPostCode.set(null);
		delCity.set(null);
		delCountry.set(null);
		invoiceLines.clear();
	}
}
