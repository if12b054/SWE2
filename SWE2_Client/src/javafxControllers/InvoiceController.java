package javafxControllers;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.ResourceBundle;

import proxy.Proxy;
import eu.schudt.javafx.controls.calendar.DatePicker;
import businessobjects.AbstractObject;
import businessobjects.Contact;
import businessobjects.InvoiceLine;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafxModels.InvoiceModel;

/**
 * Controller for creating new or viewing old recites, gets created, if user clicks
 * on "New" button in MainController(recite tab), or if he searches for old recites
 * and double clicks on a result
 * Is subject(Observer Pattern) when it comes to changes on the MWSt ComboBox
 * @author Victor
 *
 */
public class InvoiceController extends AbstractController {
	private String title = "Rechnung";
	
	public MainController parent;
	
	private InvoiceModel model = new InvoiceModel(this);
	
	@FXML private Pane pFaelligkeit, paneData;
	@FXML private TextField tfKunde;
	@FXML private TextArea taMessage, taComment;
	@FXML private TableView<InvoiceLine> tableRechnungszeilen;
	@FXML private ImageView imgContactValid;
	@FXML private ComboBox<String> cbMWSt;
	@FXML private TextField tfRStrasse, tfROrt, tfRPLZ, tfRLand;
	@FXML private TextField tfLStrasse, tfLOrt, tfLPLZ, tfLLand;
	@FXML private Button btnAdd, btnClear, btnSave, btnFind;
	@FXML private RadioButton radioFirm, radioPerson;
	private DatePicker dpDeadLine;
	
	
	@Override
	public void initialize(URL url, ResourceBundle resources) {
		/* initialize DatePicker*/
		dpDeadLine = model.createDatePicker();
		pFaelligkeit.getChildren().add(dpDeadLine);
		
		/* bidirectional bind to functions */
		cbMWSt.valueProperty().bindBidirectional(model.MWStProperty());
		tfKunde.textProperty().bindBidirectional(model.contactProperty());
		taMessage.textProperty().bindBidirectional(model.messageProperty());
		taComment.textProperty().bindBidirectional(model.commentProperty());
		tfRStrasse.textProperty().bindBidirectional(model.invStreetProperty());
		tfROrt.textProperty().bindBidirectional(model.invCityProperty());
		tfRPLZ.textProperty().bindBidirectional(model.invPLZProperty());
		tfRLand.textProperty().bindBidirectional(model.invCountryProperty());
		tfLStrasse.textProperty().bindBidirectional(model.delStreetProperty());
		tfLOrt.textProperty().bindBidirectional(model.delCityProperty());
		tfLPLZ.textProperty().bindBidirectional(model.delPLZProperty());
		tfLLand.textProperty().bindBidirectional(model.delCountryProperty());
		radioFirm.selectedProperty().bindBidirectional(model.searchFirmProperty());
		radioPerson.selectedProperty().bindBidirectional(model.searchPersonProperty());
		
		/* on double click on table entry open that entry in new InvoiceLineView */
		tableRechnungszeilen.setOnMouseClicked(model.handleDoubleClick);
		
		/* initialize MWSt */
		cbMWSt.setEditable(true);
		cbMWSt.getItems().setAll("0.15","0.17","0.19","0.21","0.23","0.25");
		cbMWSt.getSelectionModel().selectFirst();
		cbMWSt.getEditor().textProperty().addListener(model.updateObservers);
		radioFirm.setSelected(true);
	}
	
	/**
	 * happens onClick on save-button, recite data gets sent
	 * to server and saved
	 * @param event
	 */
	@FXML private void doSave(ActionEvent event) {
		model.save();
	}
	
	/**
	 * exit application on hyperlink-click, first close all InvoiceLineControllers,
	 * then clos InvoiceController
	 * @param event
	 */
	@FXML protected void doExit(ActionEvent event) {
		model.exit();
	}
	
	@FXML private void doOpenInvoiceLine(ActionEvent event) throws IOException {	
		model.openInvoiceLine();
	}
	
	/**
	 * if in creating mode form gets cleared
	 * if in edit mode, printing happens
	 * @param event
	 */
	@FXML private void doClear(ActionEvent event) {	
		if(model.curInvoice != null) {
			model.print();
		} else {
			model.clear();
		}
	}
	
	@FXML private void doFindContact(ActionEvent event) {	
		model.findContact();
	}
	
	public void setEditMode() {
		cbMWSt.setDisable(true);
		dpDeadLine.setDisable(true);
		
		tfKunde.setEditable(false);
		radioFirm.disableProperty().set(true);
		radioPerson.disableProperty().set(true);
		taMessage.setEditable(false);
		taComment.setEditable(false);
		
		tfRStrasse.setEditable(false);
		tfROrt.setEditable(false);
		tfRPLZ.setEditable(false);
		tfRLand.setEditable(false);
		tfLStrasse.setEditable(false);
		tfLOrt.setEditable(false);
		tfLPLZ.setEditable(false);
		tfLLand.setEditable(false);
		
		btnAdd.disableProperty().set(true);
		btnSave.disableProperty().set(true);
		btnFind.disableProperty().set(true);
		btnClear.setText("Drucken");
	}
	
	public MainController getParent() {
		return this.parent;
	}
	
	@Override
	public void loadModel(AbstractObject object) {
		model.loadModel(object);
	}
	
	@Override
	public void setParent(AbstractController parent) {
		this.parent = (MainController) parent;
	}
	
	@Override
	public String getTitle() {
		return this.title;
	}
	
	@Override
	public void setFoundContact(Contact contact) {
		model.setContactReference(contact);
	}
	
	public void addInvoiceLineItems(ObservableList<InvoiceLine> newInvoiceLines) {
		tableRechnungszeilen.setItems(newInvoiceLines);
	}
	
	public InvoiceLine getSelectedInvoiceLine() {
		return tableRechnungszeilen.getSelectionModel().getSelectedItem();
	}
	
	public InvoiceModel getModel() {
		return model;
	}
	
	public void setImgContactValid(Image newImg) {
		imgContactValid.setImage(newImg);
	}
	
	public DatePicker getDatePicker() {
		return dpDeadLine;
	}
}
