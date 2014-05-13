package javafxControllers;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.Vector;

import eu.schudt.javafx.controls.calendar.DatePicker;
import proxy.Proxy;
import applikation.Parameter;
import applikation.Utils;
import applikation.InputChecks;
import businessobjects.AbstractObject;
import businessobjects.Article;
import businessobjects.Contact;
import businessobjects.Invoice;
import businessobjects.InvoiceLine;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.StringConverter;
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
		initDatePickers();
		
		/* bind contact data to model */
		tfFirstName.textProperty().bindBidirectional(contactModel.getkVorname());
		tfLastName.textProperty().bindBidirectional(contactModel.getkNachname());
		tfFirm.textProperty().bindBidirectional(contactModel.getkFirma());
		lblContactCount.textProperty().bindBidirectional(contactModel.getkResultCount());
		
		/* add listeners from models */
		tfFirstName.focusedProperty().addListener(contactModel.kontaktSearchListener);
		tfLastName.focusedProperty().addListener(contactModel.kontaktSearchListener);
		tfFirm.focusedProperty().addListener(contactModel.kontaktSearchListener);
		dpFrom.focusedProperty().addListener(invoiceModel.invoiceSearchListener);
		dpTill.focusedProperty().addListener(invoiceModel.invoiceSearchListener);
		tfPriceFrom.focusedProperty().addListener(invoiceModel.invoiceSearchListener);
		tfPriceTill.focusedProperty().addListener(invoiceModel.invoiceSearchListener);
		tfContact.focusedProperty().addListener(invoiceModel.invoiceSearchListener);
		
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
		
		/* set prompt texts */
		tfFirstName.setPromptText("Vorname");
		tfLastName.setPromptText("Nachname");
		tfFirm.setPromptText("Firma");
		dpFrom.setPromptText("Von");
		dpTill.setPromptText("Bis");
		tfPriceFrom.setPromptText("Von");
		tfPriceTill.setPromptText("Bis");
		tfContact.setPromptText("Kontakt");
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
	
	public void initDatePickers() {
		/* Initialize and create the DatePickers */
		dpFrom = new DatePicker(Locale.GERMAN);
		dpFrom.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
		dpFrom.getCalendarView().todayButtonTextProperty().set("Today");
		dpFrom.getCalendarView().setShowWeeks(false);
		dpFrom.getStylesheets().add("fxml/datepicker.css");
		dpTill = new DatePicker(Locale.GERMAN);
		dpTill.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
		dpTill.getCalendarView().todayButtonTextProperty().set("Today");
		dpTill.getCalendarView().setShowWeeks(false);
		dpTill.getStylesheets().add("fxml/datepicker.css");

		pDateFrom.getChildren().add(dpFrom);
		pDateTill.getChildren().add(dpTill);
	}

	public Image getCheckMark() {
		return checkMark;
	}

	public Image getNoCheckMark() {
		return noCheckMark;
	}

	public Image getEmptyImg() {
		return emptyImg;
	}
	
}
