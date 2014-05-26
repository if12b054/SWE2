package businessobjects;

import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ResultList extends AbstractObject {
	private ObservableList<Contact> results = FXCollections.observableArrayList();
	
	public ResultList(ObservableList<Contact> results) {
		this.setResults(results);
	}

	public ObservableList<Contact> getResults() {
		return results;
	}

	public void setResults(ObservableList<Contact> results) {
		this.results = results;
	}
}
