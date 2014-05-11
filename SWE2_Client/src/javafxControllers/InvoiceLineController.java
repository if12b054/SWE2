package javafxControllers;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import proxy.Proxy;
import ObserverPattern.Observer;
import applikation.AbstractController;
import businessobjects.AbstractObject;
import businessobjects.Article;
import businessobjects.Contact;
import businessobjects.InvoiceLine;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Callback;

public class InvoiceLineController extends AbstractController implements Observer {
	private InvoiceController parent;
	private int existingID = -1; //if recite-line gets edited
	@FXML private ComboBox<Article> cbArtikel;
	@FXML private ComboBox<Integer> cbMenge;
	@FXML private Label lblNetto, lblMWSt, lblBrutto, lblStueckPreis;
	@FXML private Button btnSave;
	
	Proxy proxy; //reference to proxy in Main, for Server functions
	
	InvoiceLine rechnungszeile;
	private ObservableList<Article> dieArtikel;

	public void initialize() {
		dieArtikel = parent.getParent().getProxy().getArticles();
//		for(int i=0; i<=dieArtikel.size(); i++) {
//			
//		}
		cbArtikel.getItems().addAll(dieArtikel);
		
		cbArtikel.getSelectionModel().selectFirst(); //select the first element
        
        cbArtikel.setCellFactory(new Callback<ListView<Article>,ListCell<Article>>(){
 
            @Override
            public ListCell<Article> call(ListView<Article> p) {
                 
                final ListCell<Article> cell = new ListCell<Article>(){
 
                    @Override
                    protected void updateItem(Article t, boolean bln) {
                        super.updateItem(t, bln);
                         
                        if(t != null){
                            setText(t.getName());
                        }else{
                            setText(null);
                        }
                    }
  
                };
                 
                return cell;
            }
        });
		
		cbMenge.getItems().addAll(1,2,3,4,5,6,7,8,9,10);
		//onchange listener for cbMenge and cbArtikel
		cbMenge.setValue(1);
		lblMWSt.setText(parent.getMWSt().toString());
	}
	
	@Override
	public void setParent(AbstractController newParent) {
		parent = (InvoiceController) newParent;
		initialize();
	}
	
	@Override
	public void loadModel(AbstractObject model) {
		InvoiceLine rModel = (InvoiceLine) model;
		
		existingID = rModel.getIdNumber();
		cbArtikel.setValue(rModel.getArticle());
		cbMenge.setValue(rModel.getMenge());
		lblNetto.setText(Float.toString(rModel.getNetto()));
		lblMWSt.setText(parent.getMWSt().toString());
		lblBrutto.setText(Float.toString(rModel.getBrutto()));
		lblStueckPreis.setText(Float.toString(rModel.getStueckPreis()));
	}
	
	/**
	 * after stage gets created, this event is set, so when the window closes,
	 * it gets unregistered from observerlist
	 */
	public void setOnClose() {
		Stage stage = this.getStage();
		this.getStage().setOnHiding(new EventHandler<WindowEvent>() {
		      public void handle(WindowEvent event) {
		        parent.unregister(InvoiceLineController.this);
		      }
		});
	}
	
	/**
	 * On button click "
	 * @param event
	 */
	@FXML private void doSave(ActionEvent event) {
		rechnungszeile = new InvoiceLine(
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
			for(InvoiceLine r : parent.getRechnungszeilen()) {
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
		
	}

	@Override
	public void update() {
		lblMWSt.setText(parent.getMWSt().toString());
	}
}
