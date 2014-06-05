package javafxControllers;

import java.net.URL;
import java.util.ResourceBundle;

import proxy.Proxy;
import businessobjects.AbstractObject;
import businessobjects.Contact;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafxModels.ContactModel;

/**
 * @author Victor
 *
 */
public class ContactController extends AbstractController {
	private String title = "Kontakt";
	
	@FXML private Button btnKontakte, btnDelete;
	@FXML private TextField tfTitel, tfVname, tfNname, tfGebdatum, tfFirma;
	@FXML private TextField tfFname, tfUID;
	@FXML private TextField tfStrasse, tfOrt, tfPLZ, tfLand;
	@FXML private ImageView imgContactValid;
	
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
	 * exit application on hyperlink-click, first close all InvoiceLineControllers,
	 * then clos InvoiceController
	 * @param event
	 */
	@FXML protected void doExit(ActionEvent event) {
		model.exit();
	}
	
	/**
	 * @called 	when save button gets clicked
	 * @action 	activates function in model to check fields for errors and 
	 * 			then send data along if none exist
	 * @param 	event
	 */
	@FXML private void doSave(ActionEvent event) {
		if(Proxy.serverConnection()) {
			model.upsertContact();
		} else {
			showErrorDialog("Connection Error. Server might not be reachable.");
		}
		if(Proxy.serverConnection()) {
			getParent().contactModel.searchKontakts();
		} else {
			showErrorDialog("Connection Error. Server might not be reachable.");
		}
	}
	
	/**
	 * happens with click on "Finden" button, search for firm reference
	 * @param event
	 */
	@FXML private void doContactSearch(ActionEvent event) {
		if(Proxy.serverConnection()) {
			model.findFirm();
		} else {
			showErrorDialog("Connection Error. Server might not be reachable.");
		}
	}
	
	@FXML private void clear(ActionEvent event) {
		model.clear();
	}
	
	@Override
	public void setParent(AbstractController parent) {
		this.parent = (MainController)parent;
	}
	
	@Override
	public void loadModel(AbstractObject contact) {
		Contact kModel = (Contact) contact;
		model.loadModel(kModel);
	}
	
	@Override
	public String getTitle() {
		return this.title;
	}
	
	@Override
	public void setFoundContact(Contact contact) {
		model.setContactReference(contact);
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
				doContactSearch(null);
	        }
	    }
	};
	
	public MainController getParent() {
		return this.parent;
	}
	
	public void setImgContactValid(Image newImg) {
		imgContactValid.setImage(newImg);
	}
}
