package javafxModels;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Vector;

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

public class MainContactModel {
	
	MainController controller;
	
	/* "Kontakte" Page: */
	private StringProperty kVorname = new SimpleStringProperty();
	private StringProperty kNachname = new SimpleStringProperty();
	private StringProperty kFirma = new SimpleStringProperty();
	private StringProperty kResultCount = new SimpleStringProperty();
	
	public MainContactModel(MainController controller) {
		this.controller = controller;
	}
	
	/**
	 * happens on the fly, everytime a field on the Kontakt-page LOSES focus,
	 * this function is called 
	 * @param event
	 */
	public void searchKontakts() {
		ObservableList<Contact> kontakte = FXCollections.observableArrayList();
		int results = 0;
		
		/* get search parms from TextFields */
		Vector<Parameter> searchParms = new Vector<Parameter>();
		searchParms.addElement(new Parameter (kVorname.getValue()));
		searchParms.addElement(new Parameter(kNachname.getValue()));
		searchParms.addElement(new Parameter(kFirma.getValue()));
		
		/* get Rechnung objects from server according to searchParms */
		kontakte = controller.getProxy().searchKontakt(searchParms);
		results = kontakte.size();
		
		/* display kontakte in table */
		kResultCount.setValue(Integer.toString(results));
		controller.setTableKontaktSuche(kontakte);
	}
	
	/* Listeners */
	public ChangeListener<Boolean> kontaktSearchListener = new ChangeListener<Boolean>()
	{
	    @Override
	    public void changed(ObservableValue<? extends Boolean> arg0, 
	    		Boolean oldPropertyValue, 
	    		Boolean newPropertyValue)
	    {
	        if (!newPropertyValue)
	        {
	        	String errorMsg = InputChecks.searchKontaktError(
	        			kVorname.getValue(), 
	        			kNachname.getValue(), 
	        			kFirma.getValue());
	        	if(errorMsg == null) {
	        		searchKontakts();
	        	} else {
	        		//set error Msg
	        	}
	        }
	    }
	};
	
	public StringProperty getkVorname() {
		return kVorname;
	}
	public void setkVorname(StringProperty kVorname) {
		this.kVorname = kVorname;
	}
	public StringProperty getkNachname() {
		return kNachname;
	}
	public void setkNachname(StringProperty kNachname) {
		this.kNachname = kNachname;
	}
	public StringProperty getkFirma() {
		return kFirma;
	}
	public void setkFirma(StringProperty kFirma) {
		this.kFirma = kFirma;
	}
	public StringProperty getkResultCount() {
		return kResultCount;
	}
	public void setkResultCount(StringProperty kResultCount) {
		this.kResultCount = kResultCount;
	}
	
	
	
}
