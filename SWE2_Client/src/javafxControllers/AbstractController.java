package javafxControllers;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import businessobjects.AbstractObject;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public abstract class AbstractController implements Initializable {
	public final String EDIT_LINE_PATH = "/fxml/InvoiceLineEditView.fxml";
	public final String VIEW_LINE_PATH = "/fxml/InvoiceLineJustView.fxml";
	public final String CREATE_LINE_PATH = "/fxml/InvoiceLineCreateView.fxml";
	public final String SEARCH_CONTACT_PATH = "/fxml/ContactSearchView.fxml";
	public final String INVOICE_PATH = "/fxml/InvoiceView.fxml";
	public final String ERROR_PATH = "/fxml/ErrorView.fxml";
	
	public final int SOCKET_NUMBER = 11111;
	
	private Stage stage;
	
	public AbstractController showNewDialog(String fxmlPath, AbstractController parent, AbstractObject model) {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource(fxmlPath));
		Stage stage = new Stage(StageStyle.DECORATED);
		stage.setTitle("Titel kommt noch");
		try {
			stage.setScene(new Scene((Pane) loader.load()));
		} catch (IOException e) {
			e.printStackTrace();
		}
		AbstractController controller = loader.getController();
		controller.setStage(stage);
		controller.setParent(parent);
		if(model != null) {
			controller.loadModel(model);
		}
		stage.show();
		return controller;
	}
	
	public AbstractController showErrorDialog(String errorMsg) {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource(ERROR_PATH));
		Stage stage = new Stage(StageStyle.DECORATED);
		stage.setTitle("Error");
		stage.initModality(Modality.APPLICATION_MODAL);
		try {
			stage.setScene(new Scene((Pane) loader.load()));
		} catch (IOException e) {
			e.printStackTrace();
		}
		ErrorController controller = loader.getController();
		controller.setStage(stage);
		controller.setMsg(errorMsg);
		stage.show();
		return controller;
	}
	
	public boolean serverConnection() {
		Socket socket = null;
		try {
			socket = new Socket("127.0.0.1", SOCKET_NUMBER);
			socket.close();
		} catch (UnknownHostException e) {
			showErrorDialog("Connection Error. Server might not be reachable.");
			return false;
		} catch (IOException e) {
			showErrorDialog("I/O Exception thwron. Computer might explode any second.");
			return false;
		}
		return true;
	}
	
	public void closeStage() {
		Stage stage = this.getStage();
		stage.close();
	}
	
	public Stage getStage() {
		return stage;
	}

	public void setStage(Stage temp) {
		stage = temp;
	}
	
	public void setParent(AbstractController parent) {
		throw new UnsupportedOperationException();
	}
	
	public void loadModel(AbstractObject model) {
		throw new UnsupportedOperationException();
	}
}
