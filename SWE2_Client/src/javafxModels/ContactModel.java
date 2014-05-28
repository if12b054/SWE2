package javafxModels;

import java.util.ArrayList;

import businessobjects.AbstractObject;
import businessobjects.Contact;
import businessobjects.ResultList;
import applikation.Utils;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafxControllers.ContactController;

public class ContactModel {
	private ContactController controller;
	public Contact curContact = null;
	public Contact firmReference = null;
	
	private StringProperty titel = new SimpleStringProperty();
	private StringProperty vorname = new SimpleStringProperty();
	private StringProperty nachname = new SimpleStringProperty();
	private StringProperty geburtsdatum = new SimpleStringProperty();
	private StringProperty firma = new SimpleStringProperty();
	private StringProperty firmenname = new SimpleStringProperty();
	private StringProperty UID = new SimpleStringProperty();
	private StringProperty street = new SimpleStringProperty();
	private StringProperty PLZ = new SimpleStringProperty();
	private StringProperty city = new SimpleStringProperty();
	private StringProperty country = new SimpleStringProperty();

	public ContactModel() {
		ChangeListener<String> canEditListener = new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable,
					String oldValue, String newValue) {
				disableEditPerson.invalidate();
				disableEditFirma.invalidate();
			}
		};
		UID.addListener(canEditListener);
		titel.addListener(canEditListener);
		vorname.addListener(canEditListener);
		nachname.addListener(canEditListener);
		firmenname.addListener(canEditListener);
		geburtsdatum.addListener(canEditListener);
		firma.addListener(canEditListener);
	}
	
	public void findFirm() {
		if(controller.serverConnection()) {
			ObservableList<Contact> results = controller.getParent().getProxy().findFirm(firmenname.get());
			
			if(results == null || results.isEmpty()) {
				//no Contact found
				firmReference = null;
				controller.setFirmaFoundImg(controller.getParent().getNoCheckMark());
			} else if(results.size() == 1) {
				//one Contact found
				setFirmReference(results.get(0));
			} else {
				//multiple Contacts found
				//open new dialog
				controller.showNewDialog(controller.SEARCH_CONTACT_PATH, controller, new ResultList(results));
			}
		}
	}
	
	/* saving data and error checking */
	
	/**
	 * Checks for errors, then creates new Contact-Object and sends it over to proxy, which will send it to server,
	 * where the new "Contact" is inserted into the database
	 * */
	public void upsertContact() {
		int id;
		Contact newContact;
		
		/* contact is person */
		if(disableEditFirma.get()) {
			if(!validPerson())
				return;
			newContact = new Contact(firma.get(), vorname.get(), nachname.get(), titel.get(), geburtsdatum.get());
		}
		/* contact is firm */
		else {
			if(!validFirm())
				return;
			newContact = new Contact(UID.get(), firmenname.get());
		}
		newContact.setAdresse(street.get(), PLZ.get(), city.get(), country.get());
		
		/* check if editing or creating */
		if(curContact != null) {
			newContact.setId(curContact.getId());
		}
		curContact = newContact;
		
		/* send to proxy */
		if(controller.serverConnection()) {
			id = controller.getParent().getProxy().upsertContact(newContact);
			curContact.setId(id);
		}
	}
	
	public boolean validPerson() {
		if(firmReference == null && firma.get() != null && !firma.get().isEmpty()) {
			controller.showErrorDialog("Not a valid Firm! Try to click on 'Finden' button.");
			return false;
		}
		if(!validAdress())
			return false;
		return true;
	}
	
	public boolean validFirm() {
		
		if(!validAdress())
			return false;
		return true;
	}
	
	public boolean validAdress() {
		return true;
	}
	
	/* GUI bindings - can only create person OR firm */
	
	private BooleanBinding disableEditFirma = new BooleanBinding() {
		@Override
		protected boolean computeValue() {
			return !Utils.isNullOrEmpty(vorname.get())
					|| !Utils.isNullOrEmpty(nachname.get())
					|| !Utils.isNullOrEmpty(geburtsdatum.get())
					|| !Utils.isNullOrEmpty(firma.get())
					|| !Utils.isNullOrEmpty(titel.get());
		}
	};

	private BooleanBinding disableEditPerson = new BooleanBinding() {
		@Override
		protected boolean computeValue() {
			return !Utils.isNullOrEmpty(firmenname.get())
					|| !Utils.isNullOrEmpty(UID.get());
		}
	};
	
	/* returning PROPERTIES */
	
	public final StringProperty titelProperty() {
		return titel;
	}
	
	public final StringProperty vornameProperty() {
		return vorname;
	}

	public final StringProperty nachnameProperty() {
		return nachname;
	}

	public final StringProperty firmennameProperty() {
		return firmenname;
	}
	
	public final StringProperty firmaProperty() {
		return firma;
	}
	
	public final StringProperty geburtsdatumProperty() {
		return geburtsdatum;
	}

	public final StringProperty UIDProperty() {
		return UID;
	}
	
	public final StringProperty streetProperty() {
		return street;
	}
	
	public final StringProperty PLZProperty() {
		return PLZ;
	}
	
	public final StringProperty cityProperty() {
		return city;
	}
	
	public final StringProperty countryProperty() {
		return country;
	}
	
	/* returning BINDINGS */

	public BooleanBinding disableEditPersonBinding() {
		return disableEditPerson;
	}

	public BooleanBinding disableEditFirmaBinding() {
		return disableEditFirma;
	}

	public void setController(ContactController controller) {
		this.controller = controller;
	}
	
	/* getter and setter */
	
	public Contact getFirmReference() {
		return firmReference;
	}

	public void setFirmReference(Contact firmReference) {
		this.firmReference = firmReference;
		firma.set(firmReference.getFirma());
		controller.setFirmaFoundImg(controller.getParent().getCheckMark());
	}
}
