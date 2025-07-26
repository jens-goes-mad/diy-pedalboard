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

        display.setPosition(0, 0);
        display.write("Hello World");

        display.setPosition(5, 1);
        display.write("LCD via interface");

        display.setPosition(0, 3);
        display.write("Line 4 starts here");

        final Scene scene = new Scene(display, display.getScreenWidth(), display.getScreenHeight());
        primaryStage.setTitle("LCD Display Demo");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
