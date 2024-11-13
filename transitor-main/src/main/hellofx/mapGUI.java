package main.hellofx;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class mapGUI extends Application {
    private static final int WIDTH = 1280;
    private static final int HEIGHT = 800;
    private PostcodeInputPane locationInput = new PostcodeInputPane();

    @Override
    public void start(Stage primaryStage) {
        BorderPane mainPane = new BorderPane();
        mainPane.setCenter(locationInput.getMapLoader());

        VBox inputPaneContainer = setupInputPaneContainer();

        StackPane overlay = new StackPane();
        overlay.getChildren().addAll(mainPane, inputPaneContainer);
        StackPane.setAlignment(inputPaneContainer, Pos.TOP_RIGHT);
        StackPane.setMargin(inputPaneContainer, new Insets(10));

        Scene scene = new Scene(overlay, WIDTH, HEIGHT);
        setupStage(primaryStage, scene);
    }

    private VBox setupInputPaneContainer() {
        VBox inputPaneContainer = new VBox(locationInput);
        inputPaneContainer.setAlignment(Pos.TOP_RIGHT);
        inputPaneContainer.setMaxWidth(300);
        inputPaneContainer.setMaxHeight(150);
        inputPaneContainer.setStyle("-fx-border-radius: 10; -fx-background-radius: 10; -fx-border-color: black; -fx-border-width: 1; -fx-background-color: rgba(255, 255, 255, 0.9);");
        return inputPaneContainer;
    }

    private void setupStage(Stage primaryStage, Scene scene) {
        primaryStage.setScene(scene);
        primaryStage.setResizable(true);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
