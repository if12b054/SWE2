package applikation;

import gui.KontaktViewController;
import gui.MainController;
import gui.RechViewController;
import gui.RechZeileViewController;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
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
	
	public void showRechnungDialog(String fxmlPath, MainController parent) {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource(fxmlPath));
		Stage stage = new Stage(StageStyle.DECORATED);
		try {
			stage.setScene(new Scene((Pane) loader.load()));
		} catch (IOException e) {
			e.printStackTrace();
		}
		RechViewController controller = loader.getController();
		controller.setParent(parent);
		stage.show();
	}
	
	public void showKontaktDialog(String fxmlPath, MainController parent) {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource(fxmlPath));
		Stage stage = new Stage(StageStyle.DECORATED);
		try {
			stage.setScene(new Scene((Pane) loader.load()));
		} catch (IOException e) {
			e.printStackTrace();
		}
		KontaktViewController controller = loader.getController();
		controller.setParent(parent);
		stage.show();
	}
	
	public void showRechZeileDialog(String fxmlPath, RechViewController parent) {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource(fxmlPath));
		Stage stage = new Stage(StageStyle.DECORATED);
		try {
			stage.setScene(new Scene((Pane) loader.load()));
		} catch (IOException e) {
			e.printStackTrace();
		}
		RechZeileViewController controller = loader.getController();
		controller.setParent(parent);
		stage.show();
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
