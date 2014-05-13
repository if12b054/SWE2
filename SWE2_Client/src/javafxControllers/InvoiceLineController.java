package javafxControllers;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import ObserverPattern.Observer;
import businessobjects.AbstractObject;
import businessobjects.Article;
import businessobjects.Contact;
import businessobjects.InvoiceLine;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Callback;
import javafxModels.InvoiceLineModel;

public class InvoiceLineController extends AbstractController implements Observer {
	protected InvoiceController parent;
	protected int existingID = -1; //if recite-line gets edited
	@FXML protected ComboBox<Article> cbArtikel;
	@FXML protected ComboBox<Integer> cbMenge;
	@FXML protected Label lblNetto, lblMWSt, lblBrutto, lblStueckPreis;
	
	protected ObservableList<Article> articles;
	protected InvoiceLineModel model = new InvoiceLineModel();
	
	/** 
	 * bindings created here 
	 */
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
	
	@Override
	public void loadModel(AbstractObject model) {
		InvoiceLine invLineModel = (InvoiceLine) model;
		
		existingID = invLineModel.getIdNumber();
		cbArtikel.setValue(invLineModel.getArticle());
		cbMenge.setValue(invLineModel.getMenge());
		lblNetto.setText(Double.toString(invLineModel.getNetto()));
		lblMWSt.setText(parent.getFunctions().getMWSt().toString());
		lblBrutto.setText(Double.toString(invLineModel.getBrutto()));
		lblStueckPreis.setText(Double.toString(invLineModel.getStueckPreis()));
		
		parent.getFunctions().register(this);
	}
	
	@Override
	public void setParent(AbstractController newParent) {
		parent = (InvoiceController) newParent;
		customInitialize();
	}
	
	/**
	 * exit application on hyperlink-click, first remove from MWStList
	 * (ObserverList) then close stage
	 * @param event
	 */
	@FXML protected void doExit(ActionEvent event) {
		close();
	}
	
	public void close() {
		//parent.getFunctions().getMWStList().remove(this);
		Stage stage = this.getStage();
		stage.close();
	}
	
	/**
	 * delete the current InvoiceLine
	 * @param event
	 */
	@FXML protected void doDelete(ActionEvent event) {
		
	}
	
	/**
	 * initialize all the elements, which need the parent connection and if necessary
	 * also the listeners
	 */
	public void customInitialize() {
		parent.getFunctions().register(this);
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
		lblMWSt.setText(parent.getFunctions().getMWSt().toString());
		cbArtikel.valueProperty().addListener(model.articleValueChanged);
		cbMenge.valueProperty().addListener(model.quantityValueChanged);
		lblMWSt.textProperty().addListener(model.taxValueChanged);
	}
	
	/**
	 * after stage gets created, this event is set, so when the window closes,
	 * it gets unregistered from observerlist
	 */
	public void setOnClose() {
		this.getStage().setOnHiding(new EventHandler<WindowEvent>() {
		      public void handle(WindowEvent event) {
		        parent.getFunctions().unregister(InvoiceLineController.this);
		      }
		});
	}
	
	/**
	 * On button click
	 * @param event
	 */
	@FXML protected void doSave(ActionEvent event) {
		/* get invoiceLine object from model */
		model.updateValues();
		InvoiceLine invoiceLine = model.getInvoiceLine();
		
		/* is a new invoice-line */
		if(existingID == -1) {
			invoiceLine.setIdNumber(parent.getFunctions().getInvoiceLineCount());
			parent.getFunctions().addInvoiceLineToTable(invoiceLine);
		}
		/* invoice-line already exists */
		else {
			int i = 0;
			for(InvoiceLine r : parent.getFunctions().getInvoiceLines()) {
				if(r.getIdNumber() == existingID) {
					invoiceLine.setIdNumber(existingID);
					parent.getFunctions().getInvoiceLines().remove(i);
					break;
				}
				i++;
			}
			parent.getFunctions().getInvoiceLines().add(invoiceLine);
		}
		Stage stage = (Stage) cbArtikel.getScene().getWindow();
		stage.close();
	}

	@Override
	public void update() {
		String newMWSt = Double.toString(parent.getFunctions().getMWSt());
		this.lblMWSt.setText(newMWSt);
	}
}
