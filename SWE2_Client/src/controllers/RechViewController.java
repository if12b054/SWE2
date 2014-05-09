package controllers;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;

import proxy.Proxy;
import eu.schudt.javafx.controls.calendar.DatePicker;
import applikation.AbstractController;
import businessobjects.AModel;
import businessobjects.Artikel;
import businessobjects.KontaktModel;
import businessobjects.RechnungModel;
import businessobjects.RechnungZeileModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class RechViewController extends AbstractController {
	public MainController parent;
	private DatePicker fDatePicker;
	private Proxy proxy;
	
	@FXML private Button btnSave, btnAdd, btnFind;
	@FXML private Pane pFaelligkeit;
	@FXML private TextField tfKunde;
	@FXML private TextArea taMessage, taComment;
	@FXML private TableView<RechnungZeileModel> tableRechnungszeilen;
	@FXML private ImageView imgRechKundeInput, imgRechnKundeDelete;
	@FXML private ComboBox<String> boxMWSt;
	@FXML private TextField tfRStrasse, tfROrt, tfRPLZ, tfRLand;
	@FXML private TextField tfLStrasse, tfLOrt, tfLPLZ, tfLLand;
	
	private static ObservableList<RechnungZeileModel> rechnungszeilen = FXCollections.observableArrayList();
	private int rZeileCount = 0;
	
	@Override
	public void initialize(URL url, ResourceBundle resources) {
		fDatePicker = new DatePicker(Locale.GERMAN);
		fDatePicker.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
		fDatePicker.getCalendarView().todayButtonTextProperty().set("Today");
		fDatePicker.getCalendarView().setShowWeeks(false);
		fDatePicker.getStylesheets().add("fxml/datepicker.css");
		pFaelligkeit.getChildren().add(fDatePicker);
		
		tableRechnungszeilen.setOnMouseClicked(new EventHandler<MouseEvent>() {
		    @Override
		    public void handle(MouseEvent mouseEvent) {
		        if(mouseEvent.getButton().equals(MouseButton.PRIMARY)){
		            if(mouseEvent.getClickCount() == 2){
		                System.out.println("Double clicked");
		                RechnungZeileModel rZeile = (RechnungZeileModel) tableRechnungszeilen.getSelectionModel().getSelectedItem();
		                if(rZeile != null) {
		                	showNewDialog("/fxml/RechZeileView.fxml", RechViewController.this, rZeile);
		                }
		            }
		        }
		    }
		});
	}
	
	/**
	 * happens onClick on save-button, recite data gets sent
	 * to server and saved
	 * @param event
	 */
	@FXML private void doSave(ActionEvent event) {
		
	}
	
	@FXML private void openRechZeile(ActionEvent event) throws IOException {	
		rZeileCount++;
		showNewDialog("/fxml/RechZeileView.fxml", this, null); //need to change
	}
	
	public void addRechnungszeileToTable(RechnungZeileModel rechnungszeile) {
		rechnungszeilen.add(rechnungszeile);
		tableRechnungszeilen.setItems(rechnungszeilen);
	}
	
	@Override
	public void setParent(AbstractController parent) {
		this.parent = (MainController) parent;
	}
	
	public String getMWSt() {
		return this.boxMWSt.getValue();
	}
	
	public MainController getParent() {
		return this.parent;
	}
	
	public ObservableList<RechnungZeileModel> getRechnungszeilen() {
		return rechnungszeilen;
	}
	
	@Override
	public void loadModel(AModel model) {
		RechnungModel rModel = (RechnungModel) model;
		//set fields here
	}

	public int getrZeileCount() {
		return rZeileCount;
	}

	public void setrZeileCount(int rZeileCount) {
		this.rZeileCount = rZeileCount;
	}
	
	public void setTabel(ObservableList<RechnungZeileModel> list) {
		tableRechnungszeilen.setItems(list);
	}
}
