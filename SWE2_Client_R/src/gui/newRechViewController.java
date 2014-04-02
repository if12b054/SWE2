package gui;

import java.net.URL;
import java.util.ResourceBundle;

import applikation.AbstractController;
import businesslayer.RechnungszeileModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class newRechViewController extends AbstractController {
	@FXML private TextField tfArtikel, tfMenge, tfStuekPreis, tfMWSt;
	@FXML private Label lblNetto, lblBrutto;
	@FXML private Button btnAdd;
	
	RechnungszeileModel rechnungszeile;
	
	@Override
	public void initialize(URL url, ResourceBundle resources) {
		
	}
	
	@FXML private void doAdd(ActionEvent event) {
		
		System.out.println("1. " + tfArtikel.getText());
		System.out.println("2. " + Integer.parseInt(tfMenge.getText()));
		System.out.println("3. " + Float.parseFloat(tfMWSt.getText()));
		System.out.println("4. " + Float.parseFloat(tfStuekPreis.getText()));
		
		
		rechnungszeile = new RechnungszeileModel(tfArtikel.getText(), Integer.parseInt(tfMenge.getText()), Float.parseFloat(tfStuekPreis.getText()), Float.parseFloat(tfMWSt.getText()));
		MainController.getInstance().addRechToList(rechnungszeile);
		
		Stage stage = (Stage) btnAdd.getScene().getWindow();
		stage.close();
		
	}
}
