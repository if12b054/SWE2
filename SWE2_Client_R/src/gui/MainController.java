package gui;

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
import businessobjects.Artikel;
import businessobjects.KontaktModel;
import businessobjects.RechnungModel;
import businessobjects.RechnungZeileModel;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
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
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.util.StringConverter;

public class MainController extends AbstractController {
	
	/* Allgmein benörigte Daten */
	private static MainController firstInstance;
	private MainControllerModel model;
	private Proxy proxy;
	boolean isError = true; //because no input in beginning -> is an error
	
	private Image checkMark, noCheckMark, bin, emptyImg;
	
	/* for each table an ObservableList */
	private static ObservableList<KontaktModel> kontakte = FXCollections.observableArrayList();
	private static ObservableList<RechnungModel> rechnungen = FXCollections.observableArrayList();
	
	/* "Kontakte/Suche" Page: */
	@FXML private TextField tfSucheVorname, tfSucheNachname, tfSucheFirma;
	@FXML private TableView<KontaktModel> tableKontaktSuche;
	
	/* "Rechnungen/Suche" Page: */
	@FXML private Button btnSuche, btnKundeSuche;
	@FXML private TextField tfDatumVon, tfDatumBis, tfPreisVon, tfPreisBis, tfKontaktName;
	@FXML private ImageView imgKundeInput, imgKundeDelete;
	@FXML private TableView<RechnungModel> tableRechnungSuche;
	@FXML private Pane pDatumVon, pDatumBis;
	
	private DatePicker vonDatePicker, bisDatePicker;
	
	@FXML private void openKontaktSearch(ActionEvent event) throws IOException {
		showPriorityDialog("searchView.fxml", "Suche");
	}
	
	@FXML private void openNewRech(ActionEvent event) throws IOException {			
		showPriorityDialog("newRechView.fxml", "Neue Rechnungszeile");
	}
	
	
	
	/**
	 * Triggered on click on "Suche" Butten in Rechnungen/Suche subtab
	 * sends the current data from the textFields to Proxy, then receives the needed data in
	 * form of an ArrayList<Rechnungen>, and then displays that data in TableView
	 * @param event
	 */
	@FXML private void doRechnungenSuche(ActionEvent event) {
		/* clear current data from table */
		rechnungen.clear();
		
		/* get search parms from TextFields */
		Vector<String> searchParms = new Vector<String>();
		searchParms.addElement(tfDatumVon.getText());
		searchParms.addElement(tfDatumBis.getText());
		searchParms.addElement(tfPreisVon.getText());
		searchParms.addElement(tfPreisBis.getText());
		searchParms.addElement(tfKontaktName.getText());
		
		/* get Rechnung objects from server according to searchParms */
		ArrayList<RechnungModel> rechnungenResult = proxy.searchRechnung(searchParms);
		
		/* add results to observableList rechnungen */
		for (RechnungModel r : rechnungenResult)
			rechnungen.add(r);
		
		/* TEST */
		System.out.println("WORKING..");
		ArrayList<RechnungZeileModel> testRechnungszeilen = new ArrayList<RechnungZeileModel>();
		testRechnungszeilen.add(new RechnungZeileModel("Artikel", 1, 1, 1));
		String datum="10.10.2000", faelligkeit="11.11.2000", kunde="Gott", nachricht="Deree", kommentar="Könnt wichtig sein...";
		rechnungen.add(new RechnungModel(testRechnungszeilen, datum, faelligkeit, kunde, nachricht, kommentar));
		/* TEST-ENDE */
		
		/* display rechnungen in table */
		tableRechnungSuche.setItems(rechnungen);
	}
	
	/**
	 * happens on the fly, everytime a field on the Kontakt-page LOSES focus,
	 * this function is called 
	 * @param event
	 * @throws IOException
	 */
	private void doKontaktSuche(ActionEvent event) throws IOException {
		/* clear current data from table */
		kontakte.clear();
		
		
		
		/* get search parms from TextFields */
		Vector<Parameter> searchParms = new Vector<Parameter>();
		searchParms.addElement(new Parameter(vonDatePicker.getSelectedDate()));
		searchParms.addElement(new Parameter(bisDatePicker.getSelectedDate()));
		searchParms.addElement(new Parameter (tfPreisVon.getText()));
		searchParms.addElement(new Parameter(tfPreisBis.getText()));
		searchParms.addElement(new Parameter(tfKontaktName.getText()));
		
		/* get Rechnung objects from server according to searchParms */
		ArrayList<KontaktModel> kontakteResult = proxy.searchKontakt(searchParms);
		
		/* add results to observableList rechnungen */
		for (KontaktModel k : kontakteResult)
			kontakte.add(k);
		
		/* TEST */
		System.out.println("WORKING..");
		String typ="Person", firma="Smartass GmbH", vorname="Bart", nachname="Simpson";
		KontaktModel testKontakt = new KontaktModel(firma, vorname, nachname, null, null);
		kontakte.add(testKontakt);
		/* TEST-ENDE */
		
		/* display kontakte in table */
		tableKontaktSuche.setItems(kontakte);
	}
	
	@FXML private void doKontaktAnzeigen(ActionEvent event) throws IOException {
		
	}
	
	@FXML private void doKontaktBearbeiten(ActionEvent event) throws IOException {
		
	}
    
    public static MainController getInstance() {
        return firstInstance;
    }
	
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		firstInstance = this;
		model = new MainControllerModel();
		proxy = new Proxy();
		
		/* load images */
		checkMark = new Image("file:assets/check.png");
		noCheckMark = new Image("file:assets/nocheck.gif");
		bin = new Image("file:assets/bin.png");
		emptyImg = new Image("file:assets/transparent.png");
		
		/* Initialize the DatePickers */
		vonDatePicker = new DatePicker(Locale.GERMAN);
		vonDatePicker.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
		vonDatePicker.getCalendarView().todayButtonTextProperty().set("Today");
		vonDatePicker.getCalendarView().setShowWeeks(false);
		vonDatePicker.getStylesheets().add("fxml/datepicker.css");
		bisDatePicker = new DatePicker(Locale.GERMAN);
		bisDatePicker.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
		bisDatePicker.getCalendarView().todayButtonTextProperty().set("Today");
		bisDatePicker.getCalendarView().setShowWeeks(false);
		bisDatePicker.getStylesheets().add("fxml/datepicker.css");

		/* Add DatePickers to grid */
		pDatumVon.getChildren().add(vonDatePicker);
		pDatumBis.getChildren().add(bisDatePicker);
		
	}
	
	public Date alterDateType(String time) {
		try {
			SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
			Date parsed = (Date) format.parse(time);
			return parsed;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	/* GETTERs and SETTERs*/
	
	public Proxy getProxy() {
		return this.proxy;
	}
	
	ChangeListener<Date> dateListener = new ChangeListener<Date>() {
		@Override
		public void changed(ObservableValue<? extends Date> observable,
				Date oldValue, Date newValue) {
			if (newValue == null) {
				isError = true;
			} else {
				if (bisDatePicker.invalidProperty().get()) {
					isError = true;
			    }
			}
		}
	};
	
}
