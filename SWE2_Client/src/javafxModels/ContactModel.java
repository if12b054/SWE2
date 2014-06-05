package javafxModels;

import java.util.ArrayList;

import proxy.Proxy;
import utils.ErrorCheckUtils;
import businessobjects.AbstractObject;
import businessobjects.Contact;
import businessobjects.ResultList;
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
import javafx.stage.Stage;
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
	
	private boolean settingReference = false;

	public ContactModel() {
		/* if firm field gets modified manually, reference object gets deleted */
		firma.addListener(delContactReference);
		UID.addListener(canEditListener);
		titel.addListener(canEditListener);
		vorname.addListener(canEditListener);
		nachname.addListener(canEditListener);
		firmenname.addListener(canEditListener);
		geburtsdatum.addListener(canEditListener);
		firma.addListener(canEditListener);
	}
	
	public void exit() {
		Stage stage = controller.getStage();
		stage.close();
	}
	
	public void findFirm() {
		ObservableList<Contact> results = controller.getParent().getProxy().findFirm(firma.get());
		
		if(results == null || results.isEmpty()) {
			//no Contact found
			firmReference = null;
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
	
	/* saving data and error checking */
	
	/**
	 * Checks for errors, then creates new Contact-Object and sends it over to proxy, which will send it to server,
	 * where the new "Contact" is inserted into the database
	 * */
	public void upsertContact() {
		Contact newContact;
		
		/* contact is person */
		if(disableEditFirma.get()) {
			if(!validPerson())
				return;
			newContact = new Contact(firmReference, vorname.get(), nachname.get(), titel.get(), geburtsdatum.get());
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
		controller.getParent().getProxy().upsertContact(newContact);
		
		
		exit();
	}
	
	public void loadModel(Contact contact) {
		curContact = contact;
		//set fields here
		switch(contact.typProperty().get())
		{
		case "Person":
			System.out.println("It's a Person!");
			titel.set(contact.getTitel());
			vorname.set(contact.getVorname());
			nachname.set(contact.getNachname());
			geburtsdatum.set(contact.getGeburtsdatum());
			firma.set(contact.getFirma());
			firmReference = contact.getFirmaRef();
			break;
		case "Firma":
			System.out.println("It's a Firm!");
			UID.set(contact.getUid());
			firmenname.set(contact.getFirma());
			break;
		}
		
		street.set(contact.getAdresse().get(0)); 
		PLZ.set(contact.getAdresse().get(1)); 
		city.set(contact.getAdresse().get(2)); 
		country.set(contact.getAdresse().get(3)); 
	}
	
	public boolean validPerson() {
		if(ErrorCheckUtils.isNullOrEmpty(titel.get())) {
			controller.showErrorDialog("No title entered!");
			return false;
		} else if(ErrorCheckUtils.isNullOrEmpty(vorname.get())) {
			controller.showErrorDialog("No first name entered!");
			return false;
		} else if(ErrorCheckUtils.isNullOrEmpty(titel.get())) {
			controller.showErrorDialog("No last name entered!");
			return false;
		} else if(ErrorCheckUtils.convertToDate(geburtsdatum.get()) == null) {
			controller.showErrorDialog("Wrong DateFormat! E.g. 2000-05-13");
			return false;
		}
		if(!validAdress())
			return false;
		return true;
	}
	
	public boolean validFirm() {
		if(!ErrorCheckUtils.isInteger(UID.get())) {
			controller.showErrorDialog("Wrong UID!");
			return false;
		}
		if(!validAdress())
			return false;
		return true;
	}
	
	public boolean validAdress() {
		if(ErrorCheckUtils.isNullOrEmpty(street.get())) {
			controller.showErrorDialog("No street entered!");
			return false;
		} else if(ErrorCheckUtils.isNullOrEmpty(PLZ.get())) {
			controller.showErrorDialog("No postcode entered!");
			return false;
		} else if(!ErrorCheckUtils.isInteger(PLZ.get())) {
			controller.showErrorDialog("Wrong postcode format! Only numbers(0-9) allowed!");
			return false;
		}else if(ErrorCheckUtils.isNullOrEmpty(city.get())) {
			controller.showErrorDialog("No city entered!");
			return false;
		} else if(ErrorCheckUtils.isNullOrEmpty(country.get())) {
			controller.showErrorDialog("No country entered!");
			return false;
		} 
		return true;
	}
	
	/* GUI bindings - can only create person OR firm */
	
	private BooleanBinding disableEditFirma = new BooleanBinding() {
		@Override
		protected boolean computeValue() {
			return !ErrorCheckUtils.isNullOrEmpty(vorname.get())
					|| !ErrorCheckUtils.isNullOrEmpty(nachname.get())
					|| !ErrorCheckUtils.isNullOrEmpty(geburtsdatum.get())
					|| !ErrorCheckUtils.isNullOrEmpty(firma.get())
					|| !ErrorCheckUtils.isNullOrEmpty(titel.get());
		}
	};

	private BooleanBinding disableEditPerson = new BooleanBinding() {
		@Override
		protected boolean computeValue() {
			return !ErrorCheckUtils.isNullOrEmpty(firmenname.get())
					|| !ErrorCheckUtils.isNullOrEmpty(UID.get());
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
	
	/* getter and setter */

	public void setController(ContactController controller) {
		this.controller = controller;
	}
	
	public Contact getFirmReference() {
		return firmReference;
	}
	
	public void setContactReference(Contact contact) {
		this.firmReference = contact;
		firma.set(firmReference.getFirma());
		controller.setImgContactValid(controller.getParent().getCheckMark());
	}
	
	public void clear() {
		titel.set(null);
		vorname.set(null);
		nachname.set(null);
		geburtsdatum.set(null);
		firma.set(null);
		firmenname.set(null);
		UID.set(null);
		street.set(null);
		PLZ.set(null);
		city.set(null);
		country.set(null);
		firmReference = null;
	}
	
	/* listener */
	private ChangeListener<String> canEditListener = new ChangeListener<String>() {
		@Override
		public void changed(ObservableValue<? extends String> observable,
				String oldValue, String newValue) {
			disableEditPerson.invalidate();
			disableEditFirma.invalidate();
		}
	};
	
	private ChangeListener<String> delContactReference = new ChangeListener<String>() {
		@Override
		public void changed(ObservableValue<? extends String> observable,
				String oldValue, String newValue) {
			if(!settingReference) {
				firmReference = null;
				controller.setImgContactValid(controller.getParent().getNoCheckMark());
			}
		}
	};
}
