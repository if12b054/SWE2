package javafxModels;

import java.util.Vector;

import utils.Parameter;
import businessobjects.Contact;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
