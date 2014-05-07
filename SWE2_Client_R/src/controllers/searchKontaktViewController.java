package controllers;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class searchKontaktViewController {
	
	@FXML private TextField tfVorname, tfNachname, tfFirma;
	@FXML private Button btnSearch;
	
	@FXML private void doKontaktSearch(ActionEvent event) throws IOException {
		/*
		 * Send Data to Server
		 * -> Server replies with List of Contacts and Firms which will get displayed
		 * -> on double click on a search result, it gets chosen, window closes
		 * 
		 * */
	}
	
}
