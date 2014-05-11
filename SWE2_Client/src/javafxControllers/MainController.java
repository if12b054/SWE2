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
import applikation.AbstractController;
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
import javafxModels.MainContactTabModel;
import javafxModels.MainInvoiceTabModel;

public class MainController extends AbstractController {
	
	/* "Kontakte/Suche" Page: */
	@FXML private Label lblKontaktCount;
	@FXML private TextField tfSucheVorname, tfSucheNachname, tfSucheFirma;
	@FXML private TableView<Contact> tableKontaktSuche;
	@FXML private Button btnNewKontakt;
	
	/* "Rechnungen/Suche" Page: */
	@FXML private Label lblRechnungCount;
	@FXML private Button btnKontaktSuche, btnNewRech;
	@FXML private TextField tfPreisVon, tfPreisBis, tfKontaktName;
	@FXML private ImageView imgKundeInput, imgKundeDelete;
	@FXML private TableView<Invoice> tableRechnungSuche;
	@FXML private Pane pDatumVon, pDatumBis;
	
	/* Allgmein benörigte Daten */
	private MainContactTabModel kModel;
	private MainInvoiceTabModel rModel;
	private Proxy proxy;
	boolean isError = true; //because no input in beginning -> is an error
	private Image checkMark, noCheckMark, bin, emptyImg;
	
	private DatePicker vonDatePicker, bisDatePicker;
	
	/**
	 * opens contact window, either with empty textFields when creating new Contact or
	 * with textFields set, when editing old contact
	 * @param event
	 * @throws IOException
	 */
	@FXML private void doNewKontakt(ActionEvent event) throws IOException {
		showNewDialog("/fxml/ContactView.fxml", this, null);
	}
	
	/**
	 * opens recite window, with empty textFields
	 * @param event
	 * @throws IOException
	 */
	@FXML private void doNewRech(ActionEvent event) throws IOException {
		showNewDialog("/fxml/InvoiceView.fxml", this, null);
	}
	
	/**
	 * opens search window for reference on recite page
	 * @param event
	 */
	@FXML void doKontaktSuche(ActionEvent event) {
		
	}
	
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		kModel = new MainContactTabModel(this);
		rModel = new MainInvoiceTabModel(this);
		proxy = new Proxy();
		
		/* load images */
		checkMark = new Image("file:assets/check.png");
		noCheckMark = new Image("file:assets/nocheck.gif");
		bin = new Image("file:assets/bin.png");
		emptyImg = new Image("file:assets/transparent.png");
		
		/* bind contact data to model */
		tfSucheVorname.textProperty().bindBidirectional(kModel.getkVorname());
		tfSucheNachname.textProperty().bindBidirectional(kModel.getkNachname());
		tfSucheFirma.textProperty().bindBidirectional(kModel.getkFirma());
		lblKontaktCount.textProperty().bindBidirectional(kModel.getkResultCount());
		
		/* add listeners from models */
		tfSucheVorname.focusedProperty().addListener(kModel.kontaktSearchListener);
		tfSucheNachname.focusedProperty().addListener(kModel.kontaktSearchListener);
		tfSucheFirma.focusedProperty().addListener(kModel.kontaktSearchListener);
		vonDatePicker.focusedProperty().addListener(rModel.rechSearchListener);
		bisDatePicker.focusedProperty().addListener(rModel.rechSearchListener);
		tfPreisVon.focusedProperty().addListener(rModel.rechSearchListener);
		tfPreisBis.focusedProperty().addListener(rModel.rechSearchListener);
		tfKontaktName.focusedProperty().addListener(rModel.rechSearchListener);
		
		tableKontaktSuche.setOnMouseClicked(new EventHandler<MouseEvent>() {
		    @Override
		    public void handle(MouseEvent mouseEvent) {
		        if(mouseEvent.getButton().equals(MouseButton.PRIMARY)){
		            if(mouseEvent.getClickCount() == 2){
		                System.out.println("Double clicked");
		                Contact kontakt = (Contact) tableKontaktSuche.getSelectionModel().getSelectedItem();
		                if(kontakt != null) {
		                	System.out.println("KONTAKT: " + kontakt.getNachname());
		                	showNewDialog("/fxml/ContactView.fxml", MainController.this, kontakt);
		                }
		            }
		        }
		    }
		});
		
		/* set prompt texts */
		tfSucheVorname.setPromptText("Vorname");
		tfSucheNachname.setPromptText("Nachname");
		tfSucheFirma.setPromptText("Firma");
		vonDatePicker.setPromptText("Von");
		bisDatePicker.setPromptText("Bis");
		tfPreisVon.setPromptText("Von");
		tfPreisBis.setPromptText("Bis");
		tfKontaktName.setPromptText("Kontakt");
	}
	
	/* GETTERs and SETTERs*/
	
	public Proxy getProxy() {
		return this.proxy;
	}
	
	public void setTableKontaktSuche(ObservableList<Contact> kontakte) {
		tableKontaktSuche.setItems(kontakte);
	}
	
	public void setTableRechnungSuche(ObservableList<Invoice> rechnungen) {
		tableRechnungSuche.setItems(rechnungen);
	}
	
	public void initDatePickers(DatePicker fDatePicker, DatePicker tDatePicker) {
		/* Add DatePickers to panes */
		this.vonDatePicker = fDatePicker;
		this.bisDatePicker = tDatePicker;
		pDatumVon.getChildren().add(vonDatePicker);
		pDatumBis.getChildren().add(bisDatePicker);
	}

	public Image getCheckMark() {
		return checkMark;
	}

	public Image getNoCheckMark() {
		return noCheckMark;
	}

	public Image getBin() {
		return bin;
	}

	public Image getEmptyImg() {
		return emptyImg;
	}
	
}
