package javafxControllers;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import proxy.Proxy;
import applikation.AbstractController;
import applikation.InputChecks;
import businessobjects.AbstractObject;
import businessobjects.Article;
import businessobjects.Contact;
import businessobjects.Invoice;
import businessobjects.InvoiceLine;
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
import javafxModels.ContactModel;

public class ContactController extends AbstractController {
	@FXML private Button btnKontakte, btnKontakteSuche, btnDelete;
	@FXML private TextField tfTitel, tfVname, tfNname, tfGebdatum, tfFirma;
	@FXML private TextField tfFname, tfUID;
	@FXML private TextField tfStrasse, tfOrt, tfPLZ, tfLand;
	@FXML private Label kontaktError;
	@FXML private ImageView imgFirmaInput, imgDelete;
	
	/* zum Ausblenden der jeweiligen Pane bei Eingabe in die andere */
	@FXML private Pane firmaPane;
	@FXML private Pane personPane;
	
	private ContactModel model = new ContactModel();
	private MainController parent;
	
	Contact kontakt;
	private String errorMsg;
	
	@Override
	public void initialize(URL url, ResourceBundle resources) {
		model.setController(this);
		
		/* set images */
		imgDelete.setImage(parent.getBin());
		
		/* bidirectional bind to model */
		tfVname.textProperty().bindBidirectional(model.vornameProperty());
		tfNname.textProperty().bindBidirectional(model.nachnameProperty());
		tfFirma.textProperty().bindBidirectional(model.firmaProperty());
		tfGebdatum.textProperty().bindBidirectional(model.geburtsdatumProperty());
		tfTitel.textProperty().bindBidirectional(model.titelProperty());
		tfFname.textProperty().bindBidirectional(model.firmennameProperty());
		tfUID.textProperty().bindBidirectional(model.UIDProperty());
		
		/* can only create new person OR firm*/
		personPane.disableProperty().bind(model.disableEditPersonBinding());
		firmaPane.disableProperty().bind(model.disableEditFirmaBinding());
		
		/* if firma-field becomes unfocused -> validate firm */
		tfFirma.focusedProperty().addListener(model.isFirma);
	}
	
	/**
	 * On button click "
	 * @param event
	 */
	@FXML private void doSave(ActionEvent event) {
		createKontakt();
	}
	
	/**
	 * happens with click on "Finden" button, search for firm reference
	 * @param event
	 */
	@FXML private void doKontaktSearch(ActionEvent event) {
		
	}
	
	/**
	 * Creates new Kontakt-Object and sends it over to proxy, which will send it to server,
	 * where the new "Kontakt" is inserted into the database
	 * */
	public void createKontakt() {
		
		/* error-checking */
//		if((errorMsg = InputChecks.saveKontaktError()) != null)
//		{
//			kontaktError.setText(errorMsg);
//			errorMsg = null;
//		}
//		else {
			Contact k;
			
			/* ist eine Person */
			if(tfVname.getText() != null) {
				k = new Contact(tfFirma.getText(), tfVname.getText(), tfNname.getText(), tfTitel.getText(), tfGebdatum.getText());
			}
			/* ist eine Firma */
			else {
				k = new Contact(tfUID.getText(), tfFname.getText());
			}
			k.setAdresse(tfStrasse.getText(), tfPLZ.getText(), tfOrt.getText(), tfLand.getText());
			
			/* send to proxy */
			parent.getProxy().insertKontakt(k);
//		}
	}
	
	@FXML private void clearFirmaField(ActionEvent event) {
		tfFirma.setText(null);
		imgFirmaInput.setImage(parent.getEmptyImg());
		System.out.println("DELETE");
	}
	
	@Override
	public void setParent(AbstractController parent) {
		this.parent = (MainController)parent;
	}
	
	public MainController getParent() {
		return this.parent;
	}
	
	@Override
	public void loadModel(AbstractObject model) {
		Contact kModel = (Contact) model;
		//set fields here
		switch(kModel.typProperty().get())
		{
		case "Person":
			System.out.println("It's a Person!");
			tfTitel.setText(kModel.getTitel());
			tfVname.setText(kModel.getVorname());
			tfNname.setText(kModel.getNachname());
			tfGebdatum.setText(kModel.getGeburtsdatum());
			tfFirma.setText(kModel.getFirma());
			
			break;
		case "Firma":
			System.out.println("It's a Firm!");
			break;
		}
//		tfStrasse.setText(kModel.getAdresse().get(0)); 
//		tfPLZ.setText(kModel.getAdresse().get(1)); 
//		tfOrt.setText(kModel.getAdresse().get(2)); 
//		tfLand.setText(kModel.getAdresse().get(3)); 
	}
	
	public void setFirmaFoundImg(Image newImg) {
		imgFirmaInput.setImage(newImg);
	}
}
