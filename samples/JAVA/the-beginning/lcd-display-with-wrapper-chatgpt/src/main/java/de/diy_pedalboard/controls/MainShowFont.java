package de.diy_pedalboard.controls;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.apache.commons.lang3.StringUtils;

public class MainShowFont
    extends Application
{
    @Override
    public void start(final Stage primaryStage)
    {
        final JavaFXLCDDisplay display = new JavaFXLCDDisplay(16, 21, 24);

        for (int r = 0; r < 16; r++) {
            for (int c = 0; c < 16; c++) {
                display.setCursor(0, r);
                final int rn = r * 16;
                display.print(StringUtils.leftPad(Integer.toString(rn), 3) + ":");

                final int d = rn + c;
                display.setCursor(c + 5, r);
                final boolean hide = d < 32 || (d >= 128 && d < 160);
                display.print(Character.toString(hide ? 32 : d));
            }
        }

        display.setCursor(3, 0);
        display.setBlink(true);

        final Scene scene = new Scene(display, display.getScreenWidth(), display.getScreenHeight());
        primaryStage.setTitle("LCD Display Demo");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(final String[] args) { launch(args); }
}
