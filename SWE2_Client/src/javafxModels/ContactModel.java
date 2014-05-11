package javafxModels;

import businessobjects.Contact;
import applikation.Utils;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafxControllers.ContactController;

public class ContactModel {
	
	private ContactController controller;
	
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
	
	/* GUI functions */
	
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
	
	/* saving data and error checking */
	
	/**
	 * Creates new Kontakt-Object and sends it over to proxy, which will send it to server,
	 * where the new "Kontakt" is inserted into the database
	 * */
	public void createKontakt() {
		Contact k;
		
		/* contact is person */
		if(vorname.get() != null) {
			k = new Contact(firma.get(), vorname.get(), nachname.get(), titel.get(), geburtsdatum.get());
		}
		/* contact is firm */
		else {
			k = new Contact(UID.get(), firmenname.get());
		}
		k.setAdresse(street.get(), PLZ.get(), city.get(), country.get());
		
		/* send to proxy */
		controller.getParent().getProxy().insertKontakt(k);
	}
	
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
}
