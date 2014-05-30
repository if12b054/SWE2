package javafxControllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import businessobjects.AbstractObject;
import businessobjects.Contact;
import businessobjects.ResultList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

public class ContactSearchController extends AbstractController {
	String title = "Kontakt Finden";
	AbstractController parent = null;
	
	/* "Kontakte/Suche" Page: */
	@FXML private TableView<Contact> tableContacts;

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		
	}
	
	@Override
	public void loadModel(AbstractObject model) {
		ResultList resultList = (ResultList) model;
		tableContacts.setItems(resultList.getResults());
		tableContacts.setOnMouseClicked(new EventHandler<MouseEvent>() {
		    @Override
		    public void handle(MouseEvent mouseEvent) {
		        if(mouseEvent.getButton().equals(MouseButton.PRIMARY)){
		            if(mouseEvent.getClickCount() == 2){
		                Contact contact = tableContacts.getSelectionModel().getSelectedItem();
		                if(contact != null) {
		                	parent.setFoundContact(contact);
		                	closeStage();
		                }
		            }
		        }
		    }
		});
	}
	
	@Override
	public void setParent(AbstractController parent) {
		this.parent = parent;
	}
	
	@Override
	public String getTitle() {
		return this.title;
	}
	
}
