package applikation;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import businessobjects.AModel;
import controllers.KontaktViewController;
import controllers.MainController;
import controllers.RechViewController;
import controllers.RechZeileViewController;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public abstract class AbstractController implements Initializable {
	private Stage stage;

	public Stage getStage() {
		return stage;
	}

	public void setStage(Stage temp) {
		stage = temp;
	}

	@Override
	public void initialize(URL url, ResourceBundle resources) {
	}
	
	public void showNewDialog(String fxmlPath, AbstractController parent, AModel model) {
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
		controller.setParent(parent);
		if(model != null) {
			controller.loadModel(model);
		}
		stage.show();
	}
	
	public void setParent(AbstractController parent) {
		throw new UnsupportedOperationException();
	}
	
	public void loadModel(AModel model) {
		throw new UnsupportedOperationException();
	}
}
