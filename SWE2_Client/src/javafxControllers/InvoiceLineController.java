package javafxControllers;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import proxy.Proxy;
import utils.Observer;
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

public class InvoiceLineController extends AbstractController {
	private String title = "Rechnungszeile";
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
	public void setParent(AbstractController newParent) {
		parent = (InvoiceController) newParent;
		customInitialize();
	}
	
	@Override
	public String getTitle() {
		return this.title;
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
		Stage stage = this.getStage();
		stage.close();
	}
	
	/**
	 * delete the current InvoiceLine
	 * @param event
	 */
	@FXML protected void doDelete(ActionEvent event) {
		int i = 0;
		for(InvoiceLine r : parent.getModel().getInvoiceLines()) {
			if(r.getIdNumber() == existingID) {
				parent.getModel().getInvoiceLines().remove(i);
				break;
			}
			i++;
		}
		close();
	}
	
	/**
	 * initialize all the elements, which need the parent connection and if necessary
	 * also the listeners
	 */
	public void customInitialize() {
		/* get articles from db and set them, so only article-name gets displayed */
		if(Proxy.serverConnection()) {
			articles = parent.getParent().getProxy().getArticles();
		} else {
			showErrorDialog("Connection Error. Server might not be reachable.");
			close();
		}
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
		lblMWSt.setText(parent.getModel().getMWSt().toString());
		cbArtikel.valueProperty().addListener(model.articleValueChanged);
		cbMenge.valueProperty().addListener(model.quantityValueChanged);
		lblMWSt.textProperty().addListener(model.taxValueChanged);
	}
	
	/**
	 * On button click
	 * @param event
	 */
	@FXML protected void doSave(ActionEvent event) {
		/* get invoiceLine object from model */
		model.updateValues();
		InvoiceLine invoiceLine = model.getInvoiceLine();
		int i = 0;
		/* is a new invoice-line */
		if(existingID == -1) {
			boolean articleAlreadyAdded = false;
			for(InvoiceLine r : parent.getModel().getInvoiceLines()) {
				if(r.getArticle().getName().equals(invoiceLine.getArticle().getName())) {
					articleAlreadyAdded = true;
					r.setMenge(r.getMenge() + invoiceLine.getMenge());
					break;
				}
				i++;
			}
			if(!articleAlreadyAdded) {
				invoiceLine.setIdNumber(parent.getModel().getInvLineCnt());
				parent.getModel().addInvoiceLineToTable(invoiceLine);
			}
		}
		/* invoice-line already exists */
		else {
			for(InvoiceLine r : parent.getModel().getInvoiceLines()) {
				if(r.getIdNumber() == existingID) {
					invoiceLine.setIdNumber(existingID);
					parent.getModel().getInvoiceLines().remove(i);
					break;
				}
				i++;
			}
			parent.getModel().getInvoiceLines().add(invoiceLine);
		}
		Stage stage = (Stage) cbArtikel.getScene().getWindow();
		stage.close();
	}
	
	@Override
	public void loadModel(AbstractObject model) {
		InvoiceLine invLineModel = (InvoiceLine) model;
		
		existingID = invLineModel.getIdNumber();
		cbArtikel.setValue(invLineModel.getArticle());
		cbMenge.setValue(invLineModel.getMenge());
		lblNetto.setText(Double.toString(invLineModel.getNetto()));
		lblMWSt.setText(parent.getModel().getMWSt().toString());
		lblBrutto.setText(Double.toString(invLineModel.getBrutto()));
		lblStueckPreis.setText(Double.toString(invLineModel.getStueckPreis()));
	}
}