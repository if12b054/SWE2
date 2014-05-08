package controllers;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import proxy.Proxy;
import applikation.AbstractController;
import businessobjects.Artikel;
import businessobjects.RechnungZeileModel;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class RechZeileViewController extends AbstractController {
	private RechViewController parent;
	private int idNumber; //to check if row is being edited or created
	@FXML private ComboBox<String> cbArtikel, cbMenge;
	@FXML private Label lblNetto, lblMWSt, lblBrutto, lblStueckPreis;
	@FXML private Button btnSave;
	
	Proxy proxy; //reference to proxy in Main, for Server functions
	
	RechnungZeileModel rechnungszeile;
	private ArrayList<Artikel> dieArtikel;
	
	public void initialize() {
		dieArtikel = parent.getParent().getProxy().getArticles();
		cbMenge.getItems().addAll("1", "2","3", "4", "5", "6", "7", "8", "9", "10");
		//onchange listener for cbMenge
		cbMenge.setValue("1");
		lblMWSt.setText(parent.getMWSt());
		lblStueckPreis.setText("0");
		lblBrutto.setText("0");
		lblNetto.setText("0");
	}
	
	@Override
	public void setParent(AbstractController newParent) {
		parent = (RechViewController) newParent;
		initialize();
	}
	
	/**
	 * On button click "
	 * @param event
	 */
	@FXML private void doSave(ActionEvent event) {
		rechnungszeile = new RechnungZeileModel(
				cbArtikel.getValue(), 
				Integer.parseInt(cbMenge.getValue()), 
				Float.parseFloat(lblStueckPreis.getText()), 
				Float.parseFloat(lblMWSt.getText()));
		parent.addRechnungszeileToTable(rechnungszeile);
		
		Stage stage = (Stage) btnSave.getScene().getWindow();
		stage.close();
		
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		
	}
}
