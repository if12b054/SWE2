package javafxModels;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Vector;

import eu.schudt.javafx.controls.calendar.DatePicker;
import businessobjects.Contact;
import businessobjects.Invoice;
import applikation.InputChecks;
import applikation.Parameter;
import applikation.Utils;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafxControllers.MainController;

public class MainInvoiceTabModel {
	
	private MainController controller;
	private DatePicker fDatePicker, tDatePicker;
	private boolean isValidContact = false;
	
	/* "Kontakte" Page: */
	private StringProperty rPreisVon = new SimpleStringProperty();
	private StringProperty rPreisBis = new SimpleStringProperty();
	private StringProperty rKontakt = new SimpleStringProperty();
	private StringProperty rResultCount = new SimpleStringProperty();
	
	public MainInvoiceTabModel(MainController controller) {
		this.controller = controller;
		
		/* Initialize and create the DatePickers */
		fDatePicker = new DatePicker(Locale.GERMAN);
		fDatePicker.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
		fDatePicker.getCalendarView().todayButtonTextProperty().set("Today");
		fDatePicker.getCalendarView().setShowWeeks(false);
		fDatePicker.getStylesheets().add("fxml/datepicker.css");
		tDatePicker = new DatePicker(Locale.GERMAN);
		tDatePicker.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
		tDatePicker.getCalendarView().todayButtonTextProperty().set("Today");
		tDatePicker.getCalendarView().setShowWeeks(false);
		tDatePicker.getStylesheets().add("fxml/datepicker.css");
		controller.initDatePickers(fDatePicker, tDatePicker);
	}
	
	/**
	 * happens on the fly, everytime a field on the Rechnung-page LOSES focus,
	 * this function is called 
	 * @param event
	 */
	private void searchRechnungen() {
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
	
	public ChangeListener<Boolean> rechnungSearchListener = new ChangeListener<Boolean>()
	{
	    @Override
	    public void changed(ObservableValue<? extends Boolean> arg0, 
	    		Boolean oldPropertyValue, 
	    		Boolean newPropertyValue)
	    {
	        if (!newPropertyValue)
	        {
	        	String errorMsg = InputChecks.searchRechnungError(
	        			fDatePicker.invalidProperty().getValue(), 
	        			tDatePicker.invalidProperty().getValue(), 
	        			rPreisVon.getValue(), 
	        			rPreisBis.getValue(), 
	        			isValidContact);
	        	if(errorMsg == null) {
	        		searchRechnungen();
	        	} else {
	        		//set error Msg
	        	}
	        }
	    }
	};
	
	public ChangeListener<Boolean> rechSearchListener = new ChangeListener<Boolean>()
	{
	    @Override
	    public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue)
	    {
	        if (!newPropertyValue)
	        {
	        	searchRechnungen();
	        }
	    }
	};
	
	
	
}
