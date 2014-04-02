package applikation;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
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
	
	
	

	private void show(String resource, String title, Modality m,
			String... cssList) throws IOException {
		FXMLLoader fl = new FXMLLoader();
		fl.setLocation(getClass().getResource(resource));
		fl.load();
		Parent root = fl.getRoot();

		Stage newStage = new Stage(StageStyle.DECORATED);
		newStage.initModality(m);
		newStage.initOwner(stage);
		Scene scene = new Scene(root);
//		scene.getStylesheets().add(
//				getClass().getResource("application.css").toExternalForm());
//		// more css by code
//		for (String css : cssList) {
//			scene.getStylesheets().add(
//					getClass().getResource(css).toExternalForm());
//		}

		newStage.setScene(scene);
		newStage.setTitle(title);
		newStage.show();
	}

	public void show(String resource, String title, String... cssList) throws IOException {
		show(resource, title, Modality.NONE, cssList);
	}

	public void showDialog(String resource, String title, String... cssList) throws IOException {
		show(resource, title, Modality.WINDOW_MODAL, cssList);
	}
	
	public void showPriorityDialog(String resource, String title, String... cssList) throws IOException {
		show(resource, title, Modality.APPLICATION_MODAL, cssList);
	}
}
