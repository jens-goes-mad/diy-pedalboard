package de.diy_pedalboard.controls;

import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import lombok.Getter;
import lombok.experimental.Accessors;

@Accessors(prefix = "_")
public class JavaFXLCDDisplay
    extends VBox
    implements ILcdDisplay
{
    private static final int ROWS = 4;
    private static final int COLS = 20;
    private static final int VBOX_SPACING = 10;
    private static final int PADDING = 10;

    private final Text[] textRows = new Text[ROWS];
    private final StringBuilder[] rowData = new StringBuilder[ROWS];

    private int cursorRow = 0;
    private int cursorCol = 0;

    @Getter private final double _screenWidth;
    @Getter private final double _screenHeight;

    public JavaFXLCDDisplay()
    {
        this(24);
    }

    public JavaFXLCDDisplay(final int fontHeight)
    {
        super(VBOX_SPACING);

        final Font customFont = Font.loadFont(
            getClass().getResourceAsStream("/LCD5x7SegmentMonospace-Regular.otf"), fontHeight
        );

        final Text oneChar = new Text("A");
        oneChar.setFont(customFont);

        _screenWidth = oneChar.getLayoutBounds().getWidth() * 20 + 2 * PADDING;
        setMinWidth(_screenWidth);
        setMaxWidth(_screenWidth);
        _screenHeight = oneChar.getLayoutBounds().getHeight() * 4 + 2 * PADDING + 3 * VBOX_SPACING;
        setMinHeight(_screenHeight);
        setMaxHeight(_screenHeight);
        setStyle("-fx-background-color: blue; -fx-padding: " + PADDING + "px; -fx-border-radius: 0px; -fx-background-radius: 0px;");

        for (int i = 0; i < ROWS; i++) {
            final Text text = new Text(" ".repeat(COLS));
            text.setFont(customFont);

            textRows[i] = text;
            textRows[i].setFill(Color.WHITE);

            getChildren().add(textRows[i]);
            rowData[i] = new StringBuilder(" ".repeat(COLS));
        }
    }

    @Override
    public void clear()
    {
        for (int i = 0; i < ROWS; i++) {
            rowData[i] = new StringBuilder(" ".repeat(COLS));
            textRows[i].setText(rowData[i].toString());
        }
        cursorRow = 0;
        cursorCol = 0;
    }

    @Override
    public void setPosition(int col, int row)
    {
        if (row >= 0 && row < ROWS && col >= 0 && col < COLS) {
            cursorRow = row;
            cursorCol = col;
        }
    }

    @Override
    public void write(final String text)
    {
        int r = cursorRow;
        int c = cursorCol;

        for (char ch : text.toCharArray()) {
            if (r >= ROWS) break;

            if (c < COLS) {
                rowData[r].setCharAt(c, ch);
                c++;
            } else {
                r++;
                if (r >= ROWS) break;
                c = 0;
                rowData[r].setCharAt(c, ch);
                c++;
            }
        }

        for (int i = 0; i < ROWS; i++) {
            textRows[i].setText(rowData[i].toString());
        }

        cursorRow = r;
        cursorCol = c;
    }
}
