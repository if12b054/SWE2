package javafxControllers;

import java.net.URL;
import java.util.ResourceBundle;

import applikation.AbstractController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class ErrorController extends AbstractController{
	@FXML private Label lblErrorMsg;
	@FXML private Button btnClose;
	
	@FXML private void closeDialog(ActionEvent event) {
		Stage stage = (Stage) btnClose.getScene().getWindow();
		stage.close();
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
	}
	
	public void setMsg(String msg) {
		lblErrorMsg.setText(msg);
	}
}
