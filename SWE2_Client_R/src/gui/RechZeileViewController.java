package gui;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import proxy.Proxy;
import applikation.AbstractController;
import businessobjects.Artikel;
import businessobjects.RechnungZeileModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class RechZeileViewController extends AbstractController {
	private int idNumber; //to check if row is being edited or created
	@FXML private ComboBox<String> cbArtikel;
	@FXML private TextField tfMenge;
	@FXML private Label lblNetto, lblMWSt, lblBrutto, lblStuekPreis;
	@FXML private Button btnSave;
	
	Proxy proxy; //reference to proxy in Main, for Server functions
	
	RechViewController parent;
	
	RechnungZeileModel rechnungszeile;
	private ArrayList<Artikel> dieArtikel;
	
	@Override
	public void initialize(URL url, ResourceBundle resources) {
		proxy = MainController.getInstance().getProxy();
		dieArtikel = proxy.getArticles();
	}
	
	public void initParent(RechViewController newParent) {
		parent = newParent;
	}
	
	/**
	 * On button click "
	 * @param event
	 */
	@FXML private void doSave(ActionEvent event) {
		
		System.out.println("1. " + cbArtikel.getValue());
		System.out.println("2. " + Integer.parseInt(tfMenge.getText()));
		
		
		rechnungszeile = new RechnungZeileModel(
				cbArtikel.getValue(), 
				Integer.parseInt(tfMenge.getText()), 
				Float.parseFloat(lblStuekPreis.getText()), 
				Float.parseFloat(lblMWSt.getText()));
		parent.addRechnungszeileToTable(rechnungszeile);
		
		//Stage stage = (Stage) btnAdd.getScene().getWindow();
		//stage.close();
		
	}
}
