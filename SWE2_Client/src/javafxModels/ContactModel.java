package javafxModels;

import java.util.ArrayList;

import proxy.Proxy;
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

	public ContactModel() {
		/* if firm field gets modified manually, reference object gets deleted */
		firma.addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable,
					String oldValue, String newValue) {
				firmReference = null;
				controller.setFirmaFoundImg(controller.getParent().getNoCheckMark());
			}
		});
		
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
	
	public void exit() {
		Stage stage = controller.getStage();
		stage.close();
	}
	
	public void findFirm() {
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
		curContact.setId(controller.getParent().getProxy().upsertContact(newContact));
		exit();
	}
	
	public void loadModel(Contact contact) {
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
		if(Utils.isNullOrEmpty(titel.get())) {
			controller.showErrorDialog("No title entered!");
			return false;
		} else if(Utils.isNullOrEmpty(vorname.get())) {
			controller.showErrorDialog("No first name entered!");
			return false;
		} else if(Utils.isNullOrEmpty(titel.get())) {
			controller.showErrorDialog("No last name entered!");
			return false;
		} else if(Utils.convertToDate(geburtsdatum.get()) == null) {
			controller.showErrorDialog("Wrong DateFormat! E.g. 2000-05-13");
			return false;
		}
		if(!validAdress())
			return false;
		return true;
	}
	
	public boolean validFirm() {
		if(!Utils.isInteger(UID.get())) {
			controller.showErrorDialog("Wrong UID!");
			return false;
		}
		if(!validAdress())
			return false;
		return true;
	}
	
	public boolean validAdress() {
		if(Utils.isNullOrEmpty(street.get())) {
			controller.showErrorDialog("No street entered!");
			return false;
		} else if(Utils.isNullOrEmpty(PLZ.get())) {
			controller.showErrorDialog("No postcode entered!");
			return false;
		} else if(!Utils.isInteger(PLZ.get())) {
			controller.showErrorDialog("Wrong postcode format! Only numbers(0-9) allowed!");
			return false;
		}else if(Utils.isNullOrEmpty(city.get())) {
			controller.showErrorDialog("No city entered!");
			return false;
		} else if(Utils.isNullOrEmpty(country.get())) {
			controller.showErrorDialog("No country entered!");
			return false;
		} 
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
	
	/* getter and setter */

	public void setController(ContactController controller) {
		this.controller = controller;
	}
	
	public Contact getFirmReference() {
		return firmReference;
	}

	public void setFirmReference(Contact firmReference) {
		this.firmReference = firmReference;
		firma.set(firmReference.getFirma());
		controller.setFirmaFoundImg(controller.getParent().getCheckMark());
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
}
