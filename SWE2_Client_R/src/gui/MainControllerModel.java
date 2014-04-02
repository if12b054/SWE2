package gui;

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

public class MainControllerModel {
	
	/* "Kontakte" Page: */
	private StringProperty titel = new SimpleStringProperty();
	private StringProperty vorname = new SimpleStringProperty();
	private StringProperty nachname = new SimpleStringProperty();
	private StringProperty geburtsdatum = new SimpleStringProperty();
	private StringProperty firma = new SimpleStringProperty();
	private StringProperty firmenname = new SimpleStringProperty();
	private StringProperty UID = new SimpleStringProperty();
	
	private BooleanBinding isFirma = new BooleanBinding() {
		@Override
		protected boolean computeValue() {
			return !Utils.isNullOrEmpty(getFirmenname());
		}
	};

	private BooleanBinding disableEditFirma = new BooleanBinding() {
		@Override
		protected boolean computeValue() {
			return !Utils.isNullOrEmpty(getVorname())
					|| !Utils.isNullOrEmpty(getNachname())
					|| !Utils.isNullOrEmpty(getGeburtsdatum())
					|| !Utils.isNullOrEmpty(getFirma())
					|| !Utils.isNullOrEmpty(getTitel());
		}
	};

	private BooleanBinding disableEditPerson = new BooleanBinding() {
		@Override
		protected boolean computeValue() {
			return !Utils.isNullOrEmpty(getFirmenname())
					|| !Utils.isNullOrEmpty(getUID());
		}
	};

	public MainControllerModel() {
		ChangeListener<String> canEditListener = new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable,
					String oldValue, String newValue) {
				isFirma.invalidate();
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
	
	/* returning BINDINGS */

	public final BooleanBinding isFirmaBinding() {
		return isFirma;
	}

	public BooleanBinding disableEditPersonBinding() {
		return disableEditPerson;
	}

	public BooleanBinding disableEditFirmaBinding() {
		return disableEditFirma;
	}
	
	/* GETTERS and SETTERS */

	public String getTitel() {
		return titel.get();
	}

	public void setTitel(String titel) {
		this.titel.set(titel);
	}
	
	public String getVorname() {
		return vorname.get();
	}

	public void setVorname(String vorname) {
		this.vorname.set(vorname);
	}

	public String getNachname() {
		return nachname.get();
	}

	public void setNachname(String nachname) {
		this.nachname.set(nachname);
	}
	
	public String getFirma() {
		return firma.get();
	}

	public void setFirma(String firma) {
		this.firma.set(firma);
	}
	
	public String getGeburtsdatum() {
		return geburtsdatum.get();
	}

	public void setGeburtsdatum(String geburtsdatum) {
		this.geburtsdatum.set(geburtsdatum);
	}

	public String getFirmenname() {
		return firmenname.get();
	}

	public void setFirmenname(String firmenname) {
		this.firmenname.set(firmenname);
	}

	public String getUID() {
		return UID.get();
	}

	public void setUID(String UID) {
		this.UID.set(UID);
	}

	public boolean isFirma() {
		return isFirma.get();
	}

	public boolean disableEditPerson() {
		return disableEditPerson.get();
	}

	public boolean disableEditFirma() {
		return disableEditFirma.get();
	}
	
}
