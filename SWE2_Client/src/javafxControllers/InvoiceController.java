package javafxControllers;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;

import proxy.Proxy;
import eu.schudt.javafx.controls.calendar.DatePicker;
import ObserverPattern.Observer;
import ObserverPattern.Subject;
import applikation.AbstractController;
import businessobjects.AbstractObject;
import businessobjects.Article;
import businessobjects.Contact;
import businessobjects.Invoice;
import businessobjects.InvoiceLine;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
/**
 * Controller for creating new or viewing old recites, gets created, if user clicks
 * on "New" button in MainController(recite tab), or if he searches for old recites
 * and double clicks on a result
 * Is subject when it comes to changes on the MWSt ComboBox
 * @author Victor
 *
 */
public class InvoiceController extends AbstractController implements Subject {
	public MainController parent;
	private DatePicker fDatePicker;
	private Proxy proxy;
	private ArrayList<Observer> MWStList = new ArrayList<Observer>(); //List which gets affected if MWSt ComboBox changes values
	
	@FXML private Button btnSave, btnAdd, btnFind;
	@FXML private Pane pFaelligkeit;
	@FXML private TextField tfKunde;
	@FXML private TextArea taMessage, taComment;
	@FXML private TableView<InvoiceLine> tableRechnungszeilen;
	@FXML private ImageView imgRechKundeInput, imgRechnKundeDelete;
	@FXML private ComboBox<String> cbMWSt;
	@FXML private TextField tfRStrasse, tfROrt, tfRPLZ, tfRLand;
	@FXML private TextField tfLStrasse, tfLOrt, tfLPLZ, tfLLand;
	
	private static ObservableList<InvoiceLine> rechnungszeilen = FXCollections.observableArrayList();
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
		                InvoiceLine rZeile = (InvoiceLine) tableRechnungszeilen.getSelectionModel().getSelectedItem();
		                if(rZeile != null) {
		                	showNewDialog("/fxml/InvoiceLineView.fxml", InvoiceController.this, rZeile);
		                }
		            }
		        }
		    }
		});
		cbMWSt.setEditable(true);
		cbMWSt.getEditor().textProperty().addListener( new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable,
					String oldValue, String newValue) {
				notifyObserver();
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
		InvoiceLineController invLineWindow = (InvoiceLineController)showNewDialog("/fxml/InvoiceLineView.fxml", this, null); //need to change
		invLineWindow.setOnClose();
		register(invLineWindow);
	}
	
	public void addRechnungszeileToTable(InvoiceLine rechnungszeile) {
		rechnungszeilen.add(rechnungszeile);
		tableRechnungszeilen.setItems(rechnungszeilen);
	}
	
	@Override
	public void setParent(AbstractController parent) {
		this.parent = (MainController) parent;
	}
	
	public String getMWSt() {
		return this.cbMWSt.getValue();
	}
	
	public MainController getParent() {
		return this.parent;
	}
	
	public ObservableList<InvoiceLine> getRechnungszeilen() {
		return rechnungszeilen;
	}
	
	@Override
	public void loadModel(AbstractObject model) {
		Invoice rModel = (Invoice) model;
		//TODO set fields here
		//disable most fields
	}

	public int getrZeileCount() {
		return rZeileCount;
	}

	public void setrZeileCount(int rZeileCount) {
		this.rZeileCount = rZeileCount;
	}

	@Override
	public void register(Observer o) {
		MWStList.add(o);
	}

	@Override
	public void unregister(Observer o) {
		MWStList.remove(o);
	}

	@Override
	public void notifyObserver() {
		for(Observer o : MWStList)
		{
			o.update();
		}
	}
}
