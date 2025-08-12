package de.diy_pedalboard.controls;

import de.diy_pedalboard.controls.adapter.SerialCommandAdapter;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.util.List;

public class MainFourInOneRowPlusSerial
    extends Application
{
    private static final int ROWS = 4;
    private static final int COLS = 20;
    private static final int PADDING = 10;
    private static final int FONT_HEIGHT = 18;


    @Override
    public void start(final Stage primaryStage)
    {
        final JavaFXLCDDisplay[] displays = new JavaFXLCDDisplay[] {
            new JavaFXLCDDisplay(ROWS, COLS, FONT_HEIGHT),
            new JavaFXLCDDisplay(ROWS, COLS, FONT_HEIGHT),
            new JavaFXLCDDisplay(ROWS, COLS, FONT_HEIGHT),
            new JavaFXLCDDisplay(ROWS, COLS, FONT_HEIGHT)
        };

        final JavaFXLCDDisplay display0 = displays[0];
        final double w = display0.getScreenWidth() * ROWS + 5 * PADDING;
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

        final List<String> args = getParameters().getRaw();
        final LcdCommandService lcdCommandService = new LcdCommandService(displays);
        final SerialCommandAdapter.ICommandHandler commandHandler = command -> {
            lcdCommandService.process(command);
            return null;
        };
        final SerialCommandAdapter serialCommandAdapter = new SerialCommandAdapter(args.get(0), commandHandler);
        Runtime.getRuntime().addShutdownHook(new Thread(serialCommandAdapter::shutdown));
        serialCommandAdapter.start();


        final Scene scene = new Scene(hbox, w, h);

        primaryStage.setTitle("LCD Display Demo");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
