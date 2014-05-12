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
import javafx.beans.binding.Bindings;
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
import javafx.util.converter.DoubleStringConverter;
import javafx.util.converter.NumberStringConverter;
import javafxModels.InvoiceLineModel;

public class InvoiceLineController extends AbstractController implements Observer {
	private InvoiceController parent;
	private int existingID = -1; //if recite-line gets edited
	@FXML private ComboBox<Article> cbArtikel;
	@FXML private ComboBox<Integer> cbMenge;
	@FXML private Label lblNetto, lblMWSt, lblBrutto, lblStueckPreis;
	@FXML private Button btnSave;
	
	InvoiceLine rechnungszeile;
	private ObservableList<Article> articles;
	InvoiceLineModel model = new InvoiceLineModel();
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {	
		cbArtikel.valueProperty().bindBidirectional(model.articleProperty());
		cbMenge.valueProperty().bindBidirectional(model.quantityProperty());
		lblNetto.textProperty().bindBidirectional(model.nettoProperty());
		lblMWSt.textProperty().bindBidirectional(model.salesTaxProperty());
		lblBrutto.textProperty().bindBidirectional(model.bruttoProperty());
		lblStueckPreis.textProperty().bindBidirectional(model.unitPriceProperty());
		
		cbMenge.getItems().addAll(1,2,3,4,5,6,7,8,9,10);
		cbMenge.getSelectionModel().selectFirst();
	}
	
	/**
	 * initialize all the elements, which need the parent connection
	 */
	public void customInitialize() {
		/* get articles from db and set them, so only article-name gets displayed */
		articles = parent.getParent().getProxy().getArticles();
		cbArtikel.getItems().addAll(articles);
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
		lblMWSt.setText(parent.getMWSt().toString());
		cbArtikel.valueProperty().addListener(model.articleValueChanged);
		cbMenge.valueProperty().addListener(model.quantityValueChanged);
		lblMWSt.textProperty().addListener(model.salesTaxValueChanged);
	}
	
	@Override
	public void setParent(AbstractController newParent) {
		parent = (InvoiceController) newParent;
		customInitialize();
	}
	
	@Override
	public void loadModel(AbstractObject model) {
		InvoiceLine invLineModel = (InvoiceLine) model;
		
		existingID = invLineModel.getIdNumber();
		cbArtikel.setValue(invLineModel.getArticle());
		cbMenge.setValue(invLineModel.getMenge());
		lblNetto.setText(Double.toString(invLineModel.getNetto()));
		lblMWSt.setText(parent.getMWSt().toString());
		lblBrutto.setText(Double.toString(invLineModel.getBrutto()));
		lblStueckPreis.setText(Double.toString(invLineModel.getStueckPreis()));
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
	public void update() {
		String newMWSt = Double.toString(parent.getMWSt());
		this.lblMWSt.setText(newMWSt);
	}
}
