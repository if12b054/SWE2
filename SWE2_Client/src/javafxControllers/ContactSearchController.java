package javafxControllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import businessobjects.Contact;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class ContactSearchController extends AbstractController {
	InvoiceController parent;
	
	/* "Kontakte/Suche" Page: */
	@FXML private Label lblContactCount;
	@FXML private TextField tfFirstName, tfLastName, tfFirm;
	@FXML private TableView<Contact> tableContacts;
	
	@FXML private void doKontaktSearch(ActionEvent event) throws IOException {
		/*
		 * Send Data to Server
		 * -> Server replies with List of Contacts and Firms which will get displayed
		 * -> on double click on a search result, it gets chosen, window closes
		 * 
		 * */
	}
	
	@FXML private void doNewContact(ActionEvent event) throws IOException {
		/*
		 * Send Data to Server
		 * -> Server replies with List of Contacts and Firms which will get displayed
		 * -> on double click on a search result, it gets chosen, window closes
		 * 
		 * */
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void setParent(AbstractController parent) {
		this.parent = (InvoiceController) parent;
	}
	
}
