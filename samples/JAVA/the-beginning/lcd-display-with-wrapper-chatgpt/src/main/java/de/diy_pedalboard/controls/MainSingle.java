package de.diy_pedalboard.controls;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainSingle
    extends Application
{
    @Override
    public void start(final Stage primaryStage)
    {
        final JavaFXLCDDisplay display = new JavaFXLCDDisplay();

        display.setCursor(0, 0);
        display.print("Hello World");

        display.setCursor(5, 1);
        display.print("LCD via interface");

        display.setCursor(0, 3);
        display.print("Line 4 starts here");

        final Scene scene = new Scene(display, display.getScreenWidth(), display.getScreenHeight());
        primaryStage.setTitle("LCD Display Demo");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(final String[] args) {
        launch(args);
    }
}
