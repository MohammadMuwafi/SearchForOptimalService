package frontend;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class UI extends Application {

	@Override
	public void start(Stage primaryStage) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("GUI.fxml"));

		Scene scene = new Scene(root, 1200, 800);
		primaryStage.setTitle("Search Algorithms App");
		primaryStage.setFullScreen(true);
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	public static void main(String[] args) throws InterruptedException {
		launch(args);
	}
}
