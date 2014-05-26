package javafxControllers;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import proxy.Proxy;
import applikation.InputChecks;
import businessobjects.AbstractObject;
import businessobjects.Article;
import businessobjects.Contact;
import businessobjects.Invoice;
import businessobjects.InvoiceLine;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafxModels.ContactModel;

/**
 * @author Victor
 *
 */
public class ContactController extends AbstractController {
	@FXML private Button btnKontakte, btnDelete;
	@FXML private TextField tfTitel, tfVname, tfNname, tfGebdatum, tfFirma;
	@FXML private TextField tfFname, tfUID;
	@FXML private TextField tfStrasse, tfOrt, tfPLZ, tfLand;
	@FXML private ImageView imgFirmaInput;
	
	/* zum Ausblenden der jeweiligen Pane bei Eingabe in die andere */
	@FXML private Pane firmaPane;
	@FXML private Pane personPane;
	
	public ContactModel model = new ContactModel();
	private MainController parent;
	
	@Override
	public void initialize(URL url, ResourceBundle resources) {
		model.setController(this);
		
		/* bidirectional bind to model */
		tfVname.textProperty().bindBidirectional(model.vornameProperty());
		tfNname.textProperty().bindBidirectional(model.nachnameProperty());
		tfFirma.textProperty().bindBidirectional(model.firmaProperty());
		tfGebdatum.textProperty().bindBidirectional(model.geburtsdatumProperty());
		tfTitel.textProperty().bindBidirectional(model.titelProperty());
		tfFname.textProperty().bindBidirectional(model.firmennameProperty());
		tfUID.textProperty().bindBidirectional(model.UIDProperty());
		tfStrasse.textProperty().bindBidirectional(model.streetProperty());
		tfPLZ.textProperty().bindBidirectional(model.PLZProperty());
		tfOrt.textProperty().bindBidirectional(model.cityProperty());
		tfLand.textProperty().bindBidirectional(model.countryProperty());
		
		/* can only create new person OR firm*/
		personPane.disableProperty().bind(model.disableEditPersonBinding());
		firmaPane.disableProperty().bind(model.disableEditFirmaBinding());
		
		/* if firma-field becomes unfocused -> validate firm */
		tfFirma.addEventHandler(KeyEvent.KEY_PRESSED, isFirma);
	}
	
	/**
	 * @called 	when save button gets clicked
	 * @action 	activates function in model to check fields for errors and 
	 * 			then send data along if none exist
	 * @param 	event
	 */
	@FXML private void doSave(ActionEvent event) {
		boolean success = model.upsertContact();
		if(!success) {
			showErrorDialog(model.errorMsg);
		}
	}
	
	/**
	 * happens with click on "Finden" button, search for firm reference
	 * @param event
	 */
	@FXML private void doContactSearch(ActionEvent event) {
		model.findFirm();
	}
	
	@FXML private void clearAll(ActionEvent event) {
		tfFirma.setText(null);
		imgFirmaInput.setImage(parent.getEmptyImg());
		System.out.println("Contact form cleared.");
	}
	
	@Override
	public void setParent(AbstractController parent) {
		this.parent = (MainController)parent;
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
	
	/**
	 * Sets the image beside the firm field accordingly to input:
	 * null = empty image
	 * correct(checked with server) = check mark
	 * false(checked with server) = cross mark
	 */
	private EventHandler<KeyEvent> isFirma = new EventHandler<KeyEvent>()
	{
	    @Override
	    public void handle(KeyEvent event) {
			if (event.getCode() == KeyCode.ENTER)
	        {
				model.findFirm();
	        }
	    }
	};
	
	public MainController getParent() {
		return this.parent;
	}
	
	public void setFirmaFoundImg(Image newImg) {
		imgFirmaInput.setImage(newImg);
	}
}
