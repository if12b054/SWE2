package javafxModels;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;
import java.util.ResourceBundle;

import eu.schudt.javafx.controls.calendar.DatePicker;
import businessobjects.AbstractObject;
import businessobjects.Adress;
import businessobjects.Contact;
import businessobjects.Invoice;
import businessobjects.InvoiceLine;
import businessobjects.ResultList;
import applikation.Utils;
import javafx.beans.property.ObjectProperty;
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
import ObserverPattern.Observer;
import ObserverPattern.Subject;

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
	
	private DatePicker dp;
	
	private ArrayList<Observer> MWStList = new ArrayList<Observer>(); //List which gets affected if MWSt ComboBox changes values
	private int invoiceLineCount = 0;
	
	public InvoiceModel(InvoiceController controller) {
		this.controller = controller;
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
		} else if(!Utils.isDouble(MWSt.get())) {
			controller.showErrorDialog("MWSt needs to be a double value.");
			return;
		} else if(!validInvoiceAdress()) {
			return;
		} else if(!validDeliveryAdress()) {
			return;
		} else if(dp.getSelectedDate() == null) {
			return;
		} else if( dp.invalidProperty().get()) {
			return;
		} else {
			insertInvoice();
		}
	}
	
	public void exit() {
		closeChildren();
		Stage stage = controller.getStage();
		stage.close();
	}
	
	public void loadModel(AbstractObject model) {
		
	}
	
	public void findContact() {
		ObservableList<Contact> results = controller.getParent().getProxy().findFirm(contact.get());
		
		if(results == null || results.isEmpty()) {
			//no Contact found
			contactReference = null;
			controller.setContactImg(controller.getParent().getNoCheckMark());
		} else if(results.size() == 1) {
			//one Contact found
			contactReference = results.get(0);
		} else {
			//multiple Contacts found
			//open new dialog
			controller.showNewDialog(controller.SEARCH_CONTACT_PATH, controller, new ResultList(results));
		}
	}
	
	public void openInvoiceLine() {
		setInvoiceLineCount(getInvLineCnt() + 1);
		InvoiceLineController invLineWindow = (InvoiceLineController) controller.showNewDialog(controller.CREATE_LINE_PATH, controller, null); //need to change
		invLineWindow.setOnClose();
	}
	
	public void insertInvoice() {
		Date curDate = Calendar.getInstance().getTime();
		Adress invAdress = new Adress(invStreet.get(), Integer.parseInt(invPostCode.get()), invCity.get(), invCountry.get());
		Adress delAdress = new Adress(delStreet.get(), Integer.parseInt(delPostCode.get()), delCity.get(), delCountry.get());
		curInvoice = new Invoice(invoiceLines, curDate, dp.getSelectedDate(), contactReference, message.get(), comment.get(), invAdress, delAdress);
		int invoiceID = controller.getParent().getProxy().insertInvoice(curInvoice);
	}
	
	public boolean validInvoiceAdress() {
		if(Utils.isNullOrEmpty(invStreet.get())) {
			return false;
		} else if(Utils.isNullOrEmpty(invCity.get())) {
			return false;
		} else if(Utils.isNullOrEmpty(invCountry.get())) {
			return false;
		} else if(Utils.isNullOrEmpty(invPostCode.get())) {
			return false;
		} else if(!Utils.isInteger(invPostCode.get())) {
			controller.showErrorDialog("The invoice adress postcode needs to be numeric(0-9).");
			return false;
		}
		return true;
	}
	
	public boolean validDeliveryAdress() {
		if(Utils.isNullOrEmpty(delStreet.get())) {
			return false;
		} else if(Utils.isNullOrEmpty(delCity.get())) {
			return false;
		} else if(Utils.isNullOrEmpty(delCountry.get())) {
			return false;
		} else if(Utils.isNullOrEmpty(delPostCode.get())) {
			return false;
		} else if(!Utils.isInteger(delPostCode.get())) {
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
	            if(mouseEvent.getClickCount() == 2){
	                
	            	InvoiceLine rZeile = controller.getSelectedInvoiceLine();
	                if(rZeile != null) {
	                	//TODO check if invoice line is already open
	                	InvoiceLineController invLineWindow = (InvoiceLineController) controller.showNewDialog(controller.EDIT_LINE_PATH, controller, rZeile);
	                	
	                	invLineWindow.setOnClose();
	                }
	            }
	        }
	    }
	};
	
	public ChangeListener<String> updateObservers = new ChangeListener<String>() {
		@Override
		public void changed(ObservableValue<? extends String> observable,
				String oldValue, String newValue) {
			notifyObserver();
		}
	};
	
	public EventHandler<WindowEvent> controllerClosing = new EventHandler<WindowEvent>() {
	    @Override
	    public void handle(WindowEvent windowEvent) {
	    	closeChildren();
	    }
	};
	
	public void closeChildren() {
		Iterator <Observer> iterator = MWStList.iterator();
		int i = 0;
		while(iterator.hasNext()) {
			Observer controller = iterator.next();
			if(controller instanceof InvoiceLineController) {
				System.out.println("Is an InvoiceLineController");
				((InvoiceLineController) controller).close();
			}
		}
	}
	
	public ArrayList<Observer> getMWStList() {
		return MWStList;
	}
	
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
		MWStList.add(o);
		
	}
	@Override
	public void unregister(Observer o) {
		MWStList.remove(o);
	}
	@Override
	public void notifyObserver() {
		for(Observer i : MWStList) {
			i.update();
		}
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
