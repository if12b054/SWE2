package controllers;

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

import models.MainControllerKontaktModel;
import models.MainControllerRechnungModel;
import eu.schudt.javafx.controls.calendar.DatePicker;
import proxy.Proxy;
import applikation.AbstractController;
import applikation.Parameter;
import applikation.Utils;
import applikation.InputChecks;
import businessobjects.AModel;
import businessobjects.Artikel;
import businessobjects.KontaktModel;
import businessobjects.RechnungModel;
import businessobjects.RechnungZeileModel;
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

public class MainController extends AbstractController {
	
	/* "Kontakte/Suche" Page: */
	@FXML private Label lblKontaktCount;
	@FXML private TextField tfSucheVorname, tfSucheNachname, tfSucheFirma;
	@FXML private TableView<KontaktModel> tableKontaktSuche;
	@FXML private Button btnNewKontakt;
	
	/* "Rechnungen/Suche" Page: */
	@FXML private Label lblRechnungCount;
	@FXML private Button btnKontaktSuche, btnNewRech;
	@FXML private TextField tfPreisVon, tfPreisBis, tfKontaktName;
	@FXML private ImageView imgKundeInput, imgKundeDelete;
	@FXML private TableView<RechnungModel> tableRechnungSuche;
	@FXML private Pane pDatumVon, pDatumBis;
	
	/* Allgmein benörigte Daten */
	private MainControllerKontaktModel kModel;
	private MainControllerRechnungModel rModel;
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
		showNewDialog("/fxml/KontaktView.fxml", this, null);
	}
	
	/**
	 * opens recite window, with empty textFields
	 * @param event
	 * @throws IOException
	 */
	@FXML private void doNewRech(ActionEvent event) throws IOException {
		showNewDialog("/fxml/RechView.fxml", this, null);
	}
	
	/**
	 * opens search window for reference on recite page
	 * @param event
	 */
	@FXML void doKontaktSuche(ActionEvent event) {
		
	}
	
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		kModel = new MainControllerKontaktModel(this);
		rModel = new MainControllerRechnungModel(this);
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
		                KontaktModel kontakt = (KontaktModel) tableKontaktSuche.getSelectionModel().getSelectedItem();
		                if(kontakt != null) {
		                	System.out.println("KONTAKT: " + kontakt.getNachname());
		                	showNewDialog("/fxml/KontaktView.fxml", MainController.this, kontakt);
		                }
		            }
		        }
		    }
		});
	}
	
	/* GETTERs and SETTERs*/
	
	public Proxy getProxy() {
		return this.proxy;
	}
	
	public void setTableKontaktSuche(ObservableList<KontaktModel> kontakte) {
		tableKontaktSuche.setItems(kontakte);
	}
	
	public void setTableRechnungSuche(ObservableList<RechnungModel> rechnungen) {
		tableRechnungSuche.setItems(rechnungen);
	}
	
	public void setDatePickers(DatePicker fDatePicker, DatePicker tDatePicker) {
		/* Add DatePickers to panes */
		this.vonDatePicker = fDatePicker;
		this.bisDatePicker = tDatePicker;
		pDatumVon.getChildren().add(vonDatePicker);
		pDatumBis.getChildren().add(bisDatePicker);
	}
	
}
