package frontend;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import backend.Graph;
import backend.Optimal;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class GUIController implements Initializable {

	Image img1 = new Image(getClass().getResourceAsStream("\\map.png"));
	Image img2 = new Image(getClass().getResourceAsStream("\\save.png"));

	@FXML private AnchorPane test;
	@FXML private TextArea showOn;
	@FXML private ComboBox<String> startCity;
	@FXML private ComboBox<String> goalCity;
	@FXML private ComboBox<String> choice;
	@FXML private Button run;
	@FXML private Button exit;
	@FXML private ImageView image;
	
	private void setImage(int num) {
		if (num != 3) {
			image.setImage(img1);
		} else {
			image.setImage(img2);			
		}
	}
	
	@FXML private void loadCities(ActionEvent event) {
		run.setDisable(false);
		goalCity.setDisable(false);
		goalCity.setDisable(false);
		startCity.setDisable(false);
		
		char c = choice.getValue().toString().charAt(0); 
		showOn.setText("");
		if (c == '1') {
			setImage(1);
			startCity.setItems(
					FXCollections.observableArrayList("Qalqilia", "Nablus", "Tulkarm", "Hebron", "Gaza", "Akko", "Jenin", "Haifa"));
			goalCity.setItems(
					FXCollections.observableArrayList("Qalqilia", "Nablus", "Tulkarm", "Hebron", "Gaza", "Akko", "Jenin", "Haifa"));
		} else if (c == '2') {
			setImage(2);
			startCity.setItems(
					FXCollections.observableArrayList("Qalqilia", "Nablus", "Tulkarm", "Hebron", "Gaza", "Akko", "Jenin", "Haifa"));

			goalCity.setItems(
					FXCollections.observableArrayList("Qalqilia", "Tulkarm"));			
		} else {
			setImage(3);
			startCity.setItems(
					FXCollections.observableArrayList(
							"Jenin", "Nablus", "Ramallah", "Salfit", "Qalqilia", "Tulkarm"
					));
			goalCity.setItems(
					FXCollections.observableArrayList(""));
			goalCity.setDisable(true);
		}
	}

	@FXML
	private void traverse(ActionEvent event) {
		showOn.clear();
		Graph.mappingCities();
		Graph.generateLinks();

		String from = "Tulkarm";
		String to = "Qalqilia";

		if (startCity.getValue() != null) {
			from = startCity.getValue().toString();
		} else {
			startCity.setValue(from);
		}
		
		if (goalCity.getValue() != null) {
			to = goalCity.getValue().toString();
		} else {
			goalCity.setValue(to);
		}

		int flag = 0;
		if (to.equals("Qalqilia")) {
			flag = 1;
		} else {
			flag = 2;
		}

		ArrayList<Object> s1 = Graph.dfs(from, to);
		ArrayList<Object> s2 = Graph.bfs(from, to);
		ArrayList<Object> s3 = Graph.uniform(from, to);
		ArrayList<Object> s4 = Graph.aStar(from, to, flag);

		char c = choice.getValue().toString().charAt(0);
		if (c == '1') {
			showOn.appendText("[Path from " + from + " to " + to + "]\n");
			showOn.appendText("\n=======================================================\n\n");
			showOn.appendText("{Depth First Search}\n");
			showOn.appendText("[Path]: " + s1.get(0) + "\n");
			showOn.appendText("[Expansion Nodes]: " + s1.get(1));
			showOn.appendText("\n\n=======================================================\n\n");
			showOn.appendText("{Breadth First Search}\n");
			showOn.appendText("[Path]: " + s2.get(0) + "\n");
			showOn.appendText("[Expansion Nodes]: " + s2.get(1));
			showOn.appendText("\n\n=======================================================\n\n");
			showOn.appendText("{Uniform Search}\n");
			showOn.appendText("[Path]: " + s3.get(1) + "\n");
			showOn.appendText("[Expansion Nodes]: " + s3.get(2) + "\n");
			showOn.appendText("Minimum Cost=" + s3.get(0));
			showOn.appendText("\n\n=======================================================\n\n");

		} else if (c == '2') {
			showOn.appendText("[Path from " + from + " to " + to + "]\n");
			showOn.appendText("\n=======================================================\n\n");
			showOn.appendText("{Uniform Search}\n");
			showOn.appendText("[Path]: " + s3.get(1) + "\n");
			showOn.appendText("[Expansion Nodes]: " + s3.get(2) + "\n");
			showOn.appendText("[Expansion Nodes Length]: " + ((List<String>) s3.get(2)).size() + "\n");
			showOn.appendText("Minimum Cost=" + s3.get(0));
			showOn.appendText("\n\n=======================================================\n\n");
			showOn.appendText("{A* Search}\n");
			showOn.appendText("[Path]: " + s4.get(1) + "\n");
			showOn.appendText("[Expansion Nodes]: " + s4.get(2) + "\n");
			showOn.appendText("[Expansion Nodes Length]: " + ((List<String>) s4.get(2)).size() + "\n");
			showOn.appendText("Minimum Cost=" + s4.get(0));
			showOn.appendText("\n\n=======================================================\n\n");
		} else {
			goalCity.setValue(null);
			new Optimal();
			String[] out = Optimal.go(from);
			showOn.appendText("Start City: " + from + "\n");
			showOn.appendText("=======================================================\n\n");
			showOn.appendText("{Optimal Path For Timing}\n");
			showOn.appendText("Cost=" + (int)(Double.parseDouble(out[0])) + "\n" + out[2]);
			showOn.appendText("\n=======================================================\n\n");
			showOn.appendText("{Optimal Path For Distance}\n");
			showOn.appendText("Cost=" + out[1] + "\n" + out[3] + "\n");
		}
		showOn.setFont(new Font(20));
		showOn.setEditable(false);
		showOn.getScrollLeft();
		showOn.getScrollTop();
	}

	private void setAlgorithm() {
		choice.setItems(FXCollections.observableArrayList(
				"1) DFS, BFS, Uniform Search",
				"2) A*, Uniform Search",
				"3) Optimal1, Optimal2")
		);
	}
	
	@FXML private void exit(ActionEvent event) throws IOException {		
		Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		stage.close();		
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		setAlgorithm();
		setImage(1);
		startCity.setDisable(true);
		goalCity.setDisable(true);
		run.setDisable(true);
	}
}