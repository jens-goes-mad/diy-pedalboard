package de.diy_pedalboard.controls;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class MainFourInOneRow
    extends Application
{
    private static final int PADDING = 10;
    private static final int FONT_HEIGHT = 18;


    @Override
    public void start(final Stage primaryStage)
    {
        final JavaFXLCDDisplay[] displays = new JavaFXLCDDisplay[] {
            new JavaFXLCDDisplay(FONT_HEIGHT),
            new JavaFXLCDDisplay(FONT_HEIGHT),
            new JavaFXLCDDisplay(FONT_HEIGHT),
            new JavaFXLCDDisplay(FONT_HEIGHT)
        };

        final JavaFXLCDDisplay display0 = displays[0];
        final double w = display0.getScreenWidth() * 4 + 5 * PADDING;
        final double h = display0.getScreenHeight() + 2 * PADDING;

        final HBox hbox = new HBox(PADDING);
        hbox.setStyle("-fx-background-color: black;");

        hbox.setSpacing(PADDING);
        hbox.setPadding(new Insets(PADDING, PADDING, PADDING, PADDING));
        for (int i = 0; i < displays.length; i++) {
            final JavaFXLCDDisplay display = displays[i];
            hbox.getChildren().add(display);

            display.setCursor(0, 0);
            display.print("Display: " + i);
        }

        final Scene scene = new Scene(hbox, w, h);

        primaryStage.setTitle("LCD Display Demo");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
