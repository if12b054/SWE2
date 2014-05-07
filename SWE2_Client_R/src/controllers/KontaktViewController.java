package gui;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import proxy.Proxy;
import applikation.AbstractController;
import applikation.InputChecks;
import businessobjects.Artikel;
import businessobjects.KontaktModel;
import businessobjects.RechnungZeileModel;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class KontaktViewController extends AbstractController {
	@FXML private Button btnKontakte, btnKontakteSuche, btnDelete;
	@FXML private TextField tfTitel, tfVname, tfNname, tfGebdatum, tfFirma;
	@FXML private TextField tfFname, tfUID;
	@FXML private TextField tfRStrasse, tfROrt, tfRPLZ, tfRLand;
	@FXML private TextField tfLStrasse, tfLOrt, tfLPLZ, tfLLand;
	@FXML private Label kontaktError;
	@FXML private ImageView imgFirmaInput, imgDelete;
	
	/* zum Ausblenden der jeweiligen Pane bei Eingabe in die andere */
	@FXML private Pane firmaPane;
	@FXML private Pane personPane;
	
	private KontaktViewModel model = new KontaktViewModel();
	private Image checkMark, noCheckMark, bin, emptyImg;
	private MainController parent;
	
	KontaktModel kontakt;
	private String errorMsg;
	
	@Override
	public void initialize(URL url, ResourceBundle resources) {
		/* load images */
		checkMark = new Image("file:assets/check.png");
		noCheckMark = new Image("file:assets/nocheck.gif");
		bin = new Image("file:assets/bin.png");
		emptyImg = new Image("file:assets/transparent.png");
		
		/* set images */
		imgDelete.setImage(bin);
		
		/* bidirectional bind to model */
		tfVname.textProperty().bindBidirectional(model.vornameProperty());
		tfNname.textProperty().bindBidirectional(model.nachnameProperty());
		tfFirma.textProperty().bindBidirectional(model.firmaProperty());
		tfGebdatum.textProperty().bindBidirectional(model.geburtsdatumProperty());
		tfTitel.textProperty().bindBidirectional(model.titelProperty());
		tfFname.textProperty().bindBidirectional(model.firmennameProperty());
		tfUID.textProperty().bindBidirectional(model.UIDProperty());
		
		personPane.disableProperty().bind(model.disableEditPersonBinding());
		firmaPane.disableProperty().bind(model.disableEditFirmaBinding());
		
		/* if firma-field becomes unfocused -> validate firm */
		tfFirma.focusedProperty().addListener(new ChangeListener<Boolean>()
		{
		    @Override
		    public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue)
		    {
		        if (!newPropertyValue)
		        {
		        	if(tfFirma.getText() == null || tfFirma.getText().isEmpty()) {
		        		imgFirmaInput.setImage(emptyImg);
		        	}
		        	else if(parent.getProxy().isFirma(tfFirma.getText())) {
		        		imgFirmaInput.setImage(checkMark);
		        	}
		        	else {
		        		imgFirmaInput.setImage(noCheckMark);
		        	}
		        }
		    }
		});
	}
	
	/**
	 * On button click "
	 * @param event
	 */
	@FXML private void doSave(ActionEvent event) {
		
		createKontakt();
		//Stage stage = (Stage) btnAdd.getScene().getWindow();
		//stage.close();
	}
	
	@FXML private void doKontaktSearch(ActionEvent event) {
		
		createKontakt();
		//Stage stage = (Stage) btnAdd.getScene().getWindow();
		//stage.close();
	}
	
	/**
	 * Creates new Kontakt-Object and sends it over to proxy, which will send it to server,
	 * where the new "Kontakt" is inserted into the database
	 * */
	public void createKontakt() {
		
		/* error-checking */
		if((errorMsg = InputChecks.saveKontaktError()) != null)
		{
			kontaktError.setText(errorMsg);
			errorMsg = null;
		}
		else {
			KontaktModel k;
			
			/* ist eine Person */
			if(tfVname != null) {
				k = new KontaktModel(tfFirma.getText(), tfVname.getText(), tfNname.getText(), tfTitel.getText(), tfGebdatum.getText());
			}
			/* ist eine Firma */
			else {
				k = new KontaktModel(tfUID.getText(), tfFname.getText());
			}
			k.setLieferadresse(tfLStrasse.getText(), tfLPLZ.getText(), tfLOrt.getText(), tfLLand.getText());
			k.setRechnungsadresse(tfRStrasse.getText(), tfRPLZ.getText(), tfROrt.getText(), tfRLand.getText());
			
			/* send to proxy */
			parent.getProxy().insertKontakt(k);
		}
	}
	
	@FXML private void clearFirmaField(ActionEvent event) {
		tfFirma.setText(null);
		imgFirmaInput.setImage(emptyImg);
		System.out.println("DELETE");
	}
	
	public void setParent(MainController parent) {
		this.parent = parent;
	}
}
