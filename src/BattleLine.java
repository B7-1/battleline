import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;

public class BattleLine extends Application {
	public static void main(String[] args) {
		System.out.println(Tactics.Darius);
		launch(args);
	}

	@Override
	public void start(Stage stage) throws Exception {
		Label label = new Label("Hello JavaFX!");
		BorderPane pane = new BorderPane();
		pane.setCenter(label);
		
		Scene scene = new Scene(pane, 640, 480);
		stage.setScene(scene);

		stage.show();
	}
}