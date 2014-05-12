package javafxModels;

import java.util.Vector;

import eu.schudt.javafx.controls.calendar.DatePicker;
import businessobjects.Invoice;
import applikation.Parameter;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafxControllers.MainController;

public class MainInvoiceModel {
	
	private MainController controller;
	private DatePicker fDatePicker, tDatePicker;
	private boolean isValidFirm = false;
	
	/* "Kontakte" Page: */
	private StringProperty rPreisVon = new SimpleStringProperty();
	private StringProperty rPreisBis = new SimpleStringProperty();
	private StringProperty rKontakt = new SimpleStringProperty();
	private StringProperty rResultCount = new SimpleStringProperty();
	
	public MainInvoiceModel(MainController controller) {
		this.controller = controller;
	}
	
	/**
	 * happens on the fly, everytime a field on the Rechnung-page LOSES focus,
	 * this function is called 
	 * @param event
	 */
	private void searchInvoices() {
		ObservableList<Invoice> rechnungen = FXCollections.observableArrayList();
		
		/* get search parms from TextFields */
		Vector<Parameter> searchParms = new Vector<Parameter>();
		searchParms.addElement(new Parameter(fDatePicker.getSelectedDate()));
		searchParms.addElement(new Parameter(tDatePicker.getSelectedDate()));
		searchParms.addElement(new Parameter(rPreisVon.getValue()));
		searchParms.addElement(new Parameter(rPreisBis.getValue()));
		searchParms.addElement(new Parameter(rKontakt.getValue()));
		
		/* get Rechnung objects from server according to searchParms */
		rechnungen = controller.getProxy().searchRechnung(searchParms);
		rResultCount.setValue(Integer.toString(rechnungen.size()));
		
		/* display rechnungen in table */
		controller.setTableRechnungSuche(rechnungen);
	}
	
	public ChangeListener<Boolean> invoiceSearchListener = new ChangeListener<Boolean>()
	{
	    @Override
	    public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue)
	    {
	        if (!newPropertyValue)
	        {
	        	searchInvoices();
	        }
	    }
	};
	
	
	
}
