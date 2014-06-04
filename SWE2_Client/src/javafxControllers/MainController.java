package javafxControllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import eu.schudt.javafx.controls.calendar.DatePicker;
import proxy.Proxy;
import businessobjects.Contact;
import businessobjects.Invoice;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafxModels.MainContactModel;
import javafxModels.MainInvoiceModel;

public class MainController extends AbstractController {
	public final String INVOICE_PATH = "/fxml/InvoiceView.fxml";
	public final String CONTACT_PATH = "/fxml/ContactView.fxml";
	
	/* "Kontakte/Suche" Page: */
	@FXML private Label lblContactCount;
	@FXML private TextField tfFirstName, tfLastName, tfFirm;
	@FXML private TableView<Contact> tableContacts;
	
	/* "Rechnungen/Suche" Page: */
	@FXML private Label lblInvoiceCount;
	@FXML private TextField tfPriceFrom, tfPriceTill, tfContact;
	@FXML private ImageView imgContactValid;
	@FXML private TableView<Invoice> tableInvoices;
	@FXML private Pane pDateFrom, pDateTill;
	private Contact foundContact = null;
	
	@FXML private RadioButton radioFirm;
	
	/* Allgmein benörigte Daten */
	private MainContactModel contactModel;
	private MainInvoiceModel invoiceModel;
	private Proxy proxy;
	boolean isError = true; //because no input in beginning -> is an error
	private Image checkMark, noCheckMark, emptyImg;
	
	private DatePicker dpFrom, dpTill;
	
	/**
	 * opens contact window, either with empty textFields when creating new Contact or
	 * with textFields set, when editing old contact
	 * @param event
	 * @throws IOException
	 */
	@FXML private void doNewContact(ActionEvent event) throws IOException {
		showNewDialog(CONTACT_PATH, this, null);
	}
	
	/**
	 * opens recite window, with empty textFields
	 * @param event
	 * @throws IOException
	 */
	@FXML private void doNewInvoice(ActionEvent event) throws IOException {
		showNewDialog(INVOICE_PATH, this, null);
	}
	
	/**
	 * opens search window for reference on recite page
	 * @param event
	 */
	@FXML void doFindContact(ActionEvent event) {
		if(Proxy.serverConnection()) {
			invoiceModel.findContact();
		} else {
			showErrorDialog("Connection Error. Server might not be reachable.");
		}
	}
	
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		contactModel = new MainContactModel(this);
		invoiceModel = new MainInvoiceModel(this);
		proxy = new Proxy();
		
		/* load images */
		checkMark = new Image("file:assets/check.png");
		noCheckMark = new Image("file:assets/nocheck.gif");
		emptyImg = new Image("file:assets/transparent.png");
		
		/* bind contact data to model */
		tfFirstName.textProperty().bindBidirectional(contactModel.getkVorname());
		tfLastName.textProperty().bindBidirectional(contactModel.getkNachname());
		tfFirm.textProperty().bindBidirectional(contactModel.getkFirma());
		lblContactCount.textProperty().bindBidirectional(contactModel.getkResultCount());
		
		tfPriceFrom.textProperty().bindBidirectional(invoiceModel.priceFromProperty());
		tfPriceTill.textProperty().bindBidirectional(invoiceModel.priceTillProperty());
		tfContact.textProperty().bindBidirectional(invoiceModel.contactProperty());
		radioFirm.selectedProperty().bindBidirectional(invoiceModel.searchFirmProperty());
		
		tableContacts.setOnMouseClicked(new EventHandler<MouseEvent>() {
		    @Override
		    public void handle(MouseEvent mouseEvent) {
		        if(mouseEvent.getButton().equals(MouseButton.PRIMARY)){
		            if(mouseEvent.getClickCount() == 2){
		                System.out.println("Double clicked");
		                Contact kontakt = (Contact) tableContacts.getSelectionModel().getSelectedItem();
		                if(kontakt != null) {
		                	System.out.println("KONTAKT: " + kontakt.getNachname());
		                	showNewDialog(CONTACT_PATH, MainController.this, kontakt);
		                }
		            }
		        }
		    }
		});
		
		tableInvoices.setOnMouseClicked(new EventHandler<MouseEvent>() {
		    @Override
		    public void handle(MouseEvent mouseEvent) {
		        if(mouseEvent.getButton().equals(MouseButton.PRIMARY)){
		            if(mouseEvent.getClickCount() == 2){
		                Invoice invoice = (Invoice) tableInvoices.getSelectionModel().getSelectedItem();
		                if(invoice != null) {
		                	showNewDialog(INVOICE_PATH, MainController.this, invoice);
		                }
		            }
		        }
		    }
		});
		
		/* set prompt texts */
		tfFirstName.setPromptText("Vorname");
		tfLastName.setPromptText("Nachname");
		tfFirm.setPromptText("Firma");
		dpFrom.setPromptText("Von");
		dpTill.setPromptText("Bis");
		tfPriceFrom.setPromptText("Von");
		tfPriceTill.setPromptText("Bis");
		tfContact.setPromptText("Kontakt");
		radioFirm.setSelected(true);
		
		tfFirstName.addEventHandler(KeyEvent.KEY_PRESSED, contactHandler);
		tfLastName.addEventHandler(KeyEvent.KEY_PRESSED, contactHandler);
		tfFirm.addEventHandler(KeyEvent.KEY_PRESSED, contactHandler);
		
		dpFrom.addEventHandler(KeyEvent.KEY_PRESSED, invoiceHandler);
		dpTill.addEventHandler(KeyEvent.KEY_PRESSED, invoiceHandler);
		tfPriceFrom.addEventHandler(KeyEvent.KEY_PRESSED, invoiceHandler);
		tfPriceTill.addEventHandler(KeyEvent.KEY_PRESSED, invoiceHandler);
		tfContact.addEventHandler(KeyEvent.KEY_PRESSED, isContact);
	}
	
	@FXML private void doSearchContact(ActionEvent event) {
		if(Proxy.serverConnection()) {
			contactModel.searchKontakts();
		} else {
			showErrorDialog("Connection Error. Server might not be reachable.");
		}
	}
	
	@FXML private void doSearchInvoice(ActionEvent event) {
		if(Proxy.serverConnection()) {
			invoiceModel.searchInvoices();
		} else {
			showErrorDialog("Connection Error. Server might not be reachable.");
		}
	}
	
	private EventHandler<KeyEvent> contactHandler = new EventHandler<KeyEvent>()
	{
	    @Override
	    public void handle(KeyEvent event) {
			if (event.getCode() == KeyCode.ENTER)
	        {
				doSearchContact(null);
	        }
	    }
	};
	
	private EventHandler<KeyEvent> invoiceHandler = new EventHandler<KeyEvent>()
	{
	    @Override
	    public void handle(KeyEvent event) {
			if (event.getCode() == KeyCode.ENTER)
	        {
				doSearchInvoice(null);
	        }
	    }
	};
	
	/**
	 * Sets the image beside the firm field accordingly to input:
	 * null = empty image
	 * correct(checked with server) = check mark
	 * false(checked with server) = cross mark
	 */
	private EventHandler<KeyEvent> isContact = new EventHandler<KeyEvent>()
	{
	    @Override
	    public void handle(KeyEvent event) {
			if (event.getCode() == KeyCode.ENTER)
	        {
				doFindContact(null);
	        }
	    }
	};
	
	@Override
	public void setFoundContact(Contact contact) {
		invoiceModel.setContactReference(contact);
	}
	
	/* GETTERs and SETTERs*/
	
	public Proxy getProxy() {
		return this.proxy;
	}
	
	public void setTableKontaktSuche(ObservableList<Contact> kontakte) {
		tableContacts.setItems(kontakte);
	}
	
	public void setTableRechnungSuche(ObservableList<Invoice> rechnungen) {
		tableInvoices.setItems(rechnungen);
	}
	
	public void initDatePickers(DatePicker from, DatePicker till) {
		dpFrom = from;
		dpTill = till;
		pDateFrom.getChildren().add(dpFrom);
		pDateTill.getChildren().add(dpTill);
	}
	
	/* valid contact image handling */
	
	public Image getCheckMark() {
		return checkMark;
	}

	public Image getNoCheckMark() {
		return noCheckMark;
	}

	public Image getEmptyImg() {
		return emptyImg;
	}
	
	public void setImgContactValid(Image newImg) {
		imgContactValid.setImage(newImg);
	}
	
}
