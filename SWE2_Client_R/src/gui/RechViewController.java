package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import applikation.AbstractController;
import businessobjects.Artikel;
import businessobjects.RechnungModel;
import businessobjects.RechnungZeileModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class RechViewController extends AbstractController {
	public MainController parent;
	@FXML private Button btnSave, btnAdd, btnFind;
	@FXML private TextField tfKunde;
	@FXML private TextArea taMessage, taComment;
	@FXML private TableView<RechnungZeileModel> tableRechnungszeilen;
	@FXML private ImageView imgRechKundeInput, imgRechnKundeDelete;
	
	private static ObservableList<RechnungZeileModel> rechnungszeilen = FXCollections.observableArrayList();
	
	@Override
	public void initialize(URL url, ResourceBundle resources) {
		
	}
	
	/**
	 * happens onClick on save-button, recite data gets sent
	 * to server and saved
	 * @param event
	 */
	@FXML private void doSave(ActionEvent event) {
		
	}
	
	@FXML private void openRechZeile(ActionEvent event) throws IOException {	
		showRechZeileDialog("/fxml/RechZeileView.fxml", this);
	}
	
	public void addRechnungszeileToTable(RechnungZeileModel rechnungszeile) {
		rechnungszeilen.add(rechnungszeile);
		tableRechnungszeilen.setItems(rechnungszeilen);
	}
	
	public void setParent(MainController parent) {
		this.parent = parent;
	}
	
	public MainController getParent() {
		return this.parent;
	}
}
