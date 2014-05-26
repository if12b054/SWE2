package javafxControllerFunctions;

import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.ResourceBundle;

import businessobjects.AbstractObject;
import businessobjects.Adress;
import businessobjects.Invoice;
import businessobjects.InvoiceLine;
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
	
	private Invoice invoice;
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
	private ObjectProperty<Date> dueDate = new SimpleObjectProperty<Date>();
	
	private ArrayList<Observer> MWStList = new ArrayList<Observer>(); //List which gets affected if MWSt ComboBox changes values
	private int invoiceLineCount = 0;
	
	public InvoiceModel(InvoiceController controller) {
		this.controller = controller;
	}
	
	public void save() {
		if(!errorsFound()) {
			createInvoice();
		}
	}
	
	public void exit() {
		closeChildren();
		Stage stage = controller.getStage();
		stage.close();
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
	
	public void loadModel(AbstractObject model) {
		
	}
	
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
	
	public void findContact() {
		ContactSearchController contactSearchWindow = (ContactSearchController) controller.showNewDialog(controller.SEARCH_CONTACT_PATH, controller, null);
	}
	
	public void openInvoiceLine() {
		setInvoiceLineCount(getInvoiceLineCount() + 1);
		InvoiceLineController invLineWindow = (InvoiceLineController) controller.showNewDialog(controller.CREATE_LINE_PATH, controller, null); //need to change
		invLineWindow.setOnClose();
	}
	
	public void createInvoice() {
		Date curDate = Calendar.getInstance().getTime();
		Adress invAdress = new Adress(invStreet.get(), Integer.parseInt(invPostCode.get()), invCity.get(), invCountry.get());
		Adress delAdress = new Adress(delStreet.get(), Integer.parseInt(delPostCode.get()), delCity.get(), delCountry.get());
		invoice = new Invoice(invoiceLines, curDate, dueDate.getValue(), contact.get(), message.get(), comment.get(), invAdress, delAdress);
		controller.getParent().getProxy().insertRechnung(invoice);
	}
	
	public boolean errorsFound() {
		if(dueDate.getValue() == null) {
			controller.showErrorDialog("Due Date cannot be empty. Format: 2000-01-01");
			return true;
		} 
		/* checking if postcode correct */
		else if(!Utils.isInteger(invPostCode.get()) || !Utils.isInteger(delPostCode.get())) {
			controller.showErrorDialog("The postcode needs to be numeric(0-9).");
			return true;
			
		}
		return false;
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
	
	public final ObjectProperty<Date> tillDateProperty() {
		return dueDate;
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
	
	public ArrayList<Observer> getMWStList() {
		return MWStList;
	}
	
	public Double getMWSt() {
		return Double.parseDouble(MWSt.get());
	}
	
	public ObservableList<InvoiceLine> getInvoiceLines() {
		return invoiceLines;
	}

	public int getInvoiceLineCount() {
		return invoiceLineCount;
	}

	public void setInvoiceLineCount(int invoiceLineCount) {
		this.invoiceLineCount = invoiceLineCount;
	}
}
