package controllers;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import proxy.Proxy;
import applikation.AbstractController;
import businessobjects.AModel;
import businessobjects.Artikel;
import businessobjects.KontaktModel;
import businessobjects.RechnungZeileModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class RechZeileViewController extends AbstractController {
	private RechViewController parent;
	private int existingID = -1; //if recite-line gets edited
	@FXML private ComboBox<String> cbArtikel;
	@FXML private ComboBox<Integer> cbMenge;
	@FXML private Label lblNetto, lblMWSt, lblBrutto, lblStueckPreis;
	@FXML private Button btnSave;
	
	Proxy proxy; //reference to proxy in Main, for Server functions
	
	RechnungZeileModel rechnungszeile;
	private ArrayList<Artikel> dieArtikel;
	
	public void initialize() {
		dieArtikel = parent.getParent().getProxy().getArticles();
		cbMenge.getItems().addAll(1,2,3,4,5,6,7,8,9,10);
		//onchange listener for cbMenge and cbArtikel
		cbMenge.setValue(1);
		lblMWSt.setText(parent.getMWSt());
	}
	
	@Override
	public void setParent(AbstractController newParent) {
		parent = (RechViewController) newParent;
		initialize();
	}
	
	@Override
	public void loadModel(AModel model) {
		RechnungZeileModel rModel = (RechnungZeileModel) model;
		
		existingID = rModel.getIdNumber();
		cbArtikel.setValue(rModel.getArtikel());
		cbMenge.setValue(rModel.getMenge());
		lblNetto.setText(Float.toString(rModel.getNetto()));
		lblMWSt.setText(parent.getMWSt());
		lblBrutto.setText(Float.toString(rModel.getBrutto()));
		lblStueckPreis.setText(Float.toString(rModel.getStueckPreis()));
	}
	
	/**
	 * On button click "
	 * @param event
	 */
	@FXML private void doSave(ActionEvent event) {
		rechnungszeile = new RechnungZeileModel(
				cbArtikel.getValue(), 
				cbMenge.getValue(), 
				Float.parseFloat(lblStueckPreis.getText()), 
				Float.parseFloat(lblMWSt.getText()));
		
		System.out.println("ID: " + this.existingID);
		
		/* is a new recite-line */
		if(existingID == -1) {
			rechnungszeile.setIdNumber(parent.getrZeileCount());
			parent.addRechnungszeileToTable(rechnungszeile);
		}
		/* recite-line already exists */
		else {
			int i = 0;
			for(RechnungZeileModel r : parent.getRechnungszeilen()) {
				if(r.getIdNumber() == existingID) {
					rechnungszeile.setIdNumber(existingID);
					parent.getRechnungszeilen().remove(i);
					break;
				}
				i++;
			}
			parent.getRechnungszeilen().add(rechnungszeile);
		}
		
		Stage stage = (Stage) btnSave.getScene().getWindow();
		stage.close();
		
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		
	}
}
