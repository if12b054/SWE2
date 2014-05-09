package applikation;

import java.io.IOException;
import businessobjects.AbstractObject;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public abstract class AbstractController implements Initializable {
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
		controller.setParent(parent);
		controller.setStage(stage);
		if(model != null) {
			controller.loadModel(model);
		}
		stage.show();
		return controller;
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
