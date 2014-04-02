package gui;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Timer;

import applikation.AbstractController;
import applikation.Utils;
import businesslayer.Businesslayer;
import businesslayer.Kunde;
import businesslayer.RechnungModel;
import businesslayer.RechnungszeileModel;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
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
import javafx.stage.Modality;
import javafx.util.StringConverter;

public class MainController extends AbstractController {
	
	private static MainController firstInstance;
	
	private MainControllerModel model;
	
	@FXML private TitledPane firmaPane;
	@FXML private TitledPane personPane;
	
	/* "Kontakte" Page: */
	@FXML private Button btnKontakte, btnKontakteSuche, btnDelete;
	@FXML private TextField tfTitel, tfVname, tfNname, tfGebdatum, tfFirma;
	@FXML private TextField tfFname, tfUID;
	@FXML private TextField tfRAdresse, tfROrt, tfRPLZ;
	@FXML private TextField tfLadresse, tfLOrt, tfLPLZ;
	@FXML private ImageView imgFirmaInput, imgDelete;
	
	/* "Rechnungen" Page: */
	@FXML private Button btnRechnungen, btnHinzufuegen, btnRechKundeSuche;
	@FXML private TextField tfKunde;
	@FXML private TextArea taNachricht, taKommentar;
	@FXML private TableView<RechnungszeileModel> tableRechnungen;
	@FXML private ImageView imgRechKundeInput, imgRechnKundeDelete;
	
	/* "Suche" Page: */
	@FXML private Button btnSuche, btnKundeSuche;
	@FXML private TextField tfPreisVon, tfPreisBis, tfKundeName;
	@FXML private ImageView imgKundeInput, imgKundeDelete;
	@FXML private TableView<RechnungModel> tableSuche;
	
	@FXML private void emptyFirmaField(ActionEvent event) {
		tfFirma.setText(null);
		imgFirmaInput.setImage(emptyImg);
		System.out.println("DELETE");
	}
	
	@FXML private void openKontaktSearch(ActionEvent event) throws IOException {
		showPriorityDialog("searchView.fxml", "Suche");
	}
	
	@FXML private void openNewRech(ActionEvent event) throws IOException {			
		showPriorityDialog("newRechView.fxml", "Neue Rechnungszeile");
	}
	
	private static ObservableList<RechnungModel> rechnungen = FXCollections.observableArrayList();
	
	@FXML private void showRechnungen(ActionEvent event) {
		rechnungen.clear();
		/* get values from fields and perform search(via server) HERE*/
		ArrayList<RechnungszeileModel> searchAll = new ArrayList<RechnungszeileModel>();
		Businesslayer b = new Businesslayer();
		
		String action = "search/Rechnung";
		searchAll = b.searchRechnung(action);
		
		
		/*just a test if display is working*/
		System.out.println("WORKING..");
		ArrayList<RechnungszeileModel> testRechnungszeilen = new ArrayList<RechnungszeileModel>();
		testRechnungszeilen.add(new RechnungszeileModel("Artikel", 1, 1, 1));
		String datum="10.10.2000", faelligkeit="11.11.2000", kunde="Gott", nachricht="Deree", kommentar="Könnt wichtig sein...";
		
		
		rechnungen.add(new RechnungModel(testRechnungszeilen, datum, faelligkeit, kunde, nachricht, kommentar));
		tableSuche.setItems(rechnungen);
	}
	
	/* General */
	private boolean rightSearchKontakte=false, rightSearchRechnungen=false, rightSearchSuche=false; //check for correct input in searches, when confirming a save or something
	private Image checkMark, noCheckMark, bin, emptyImg;
	private static ObservableList<RechnungszeileModel> rechnungszeilen = FXCollections.observableArrayList();
    
    public static MainController getInstance() {
        return firstInstance;
    }
	
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		firstInstance = this;
		
		model = new MainControllerModel();

		tfVname.textProperty().bindBidirectional(model.vornameProperty());
		tfNname.textProperty().bindBidirectional(model.nachnameProperty());
		tfFirma.textProperty().bindBidirectional(model.firmaProperty());
		tfGebdatum.textProperty().bindBidirectional(model.geburtsdatumProperty());
		tfTitel.textProperty().bindBidirectional(model.titelProperty());
		tfFname.textProperty().bindBidirectional(model.firmennameProperty());
		tfUID.textProperty().bindBidirectional(model.UIDProperty());
		
		personPane.disableProperty().bind(model.disableEditPersonBinding());
		firmaPane.disableProperty().bind(model.disableEditFirmaBinding());
		
		/* load images */
		checkMark = new Image("file:assets/check.png");
		noCheckMark = new Image("file:assets/nocheck.gif");
		bin = new Image("file:assets/bin.png");
		emptyImg = new Image("file:assets/transparent.png");
		
		/* set images */
		imgDelete.setImage(bin);
		
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
		        		rightSearchKontakte=false;
		        		return;
		        	}
		        	//DB ABFRAGE ob es die Firma wirklich gibt!
		        	//falls ja:
		        	imgFirmaInput.setImage(checkMark);
		        	
		        	//sonst
		        	//imgFirmaInput.setImage(noCheckMark);
		        	
		        	
		        }
		    }
		});
		
	}
	
	public void insertKontakt() {
		Kunde k = new Kunde();
		
		k.setTitel(tfTitel.getText());
		k.setVorname(tfVname.getText());
		k.setNachname(tfNname.getText());
		
		
		k.setGeburtsdatum(alterDateType(tfGebdatum.getText()));
		k.setFirma(tfFirma.getText());
		k.setFirmenName(tfFname.getText());
		
		String action = "insert/Kontakt"; 
		
		Businesslayer b = new Businesslayer();
		b.insertKontakt(k, action);
		
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
	
	public void addRechToList(RechnungszeileModel rechnungszeile) {
		rechnungszeilen.add(rechnungszeile);
		tableRechnungen.setItems(rechnungszeilen);
	}
	
}
