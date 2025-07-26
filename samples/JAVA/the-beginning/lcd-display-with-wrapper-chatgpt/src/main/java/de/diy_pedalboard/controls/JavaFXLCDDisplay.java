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

    private final Text[] _textRows = new Text[ROWS];
    private final StringBuilder[] _rowData = new StringBuilder[ROWS];

    private int _cursorRow = 0;
    private int _cursorCol = 0;

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

            _textRows[i] = text;
            _textRows[i].setFill(Color.WHITE);

            getChildren().add(_textRows[i]);
            _rowData[i] = new StringBuilder(" ".repeat(COLS));
        }
    }

    @Override
    public void clear()
    {
        for (int i = 0; i < ROWS; i++) {
            _rowData[i] = new StringBuilder(" ".repeat(COLS));
            _textRows[i].setText(_rowData[i].toString());
        }
        _cursorRow = 0;
        _cursorCol = 0;
    }

    @Override
    public void setCursor(int col, int row)
    {
        if (row >= 0 && row < ROWS && col >= 0 && col < COLS) {
            _cursorRow = row;
            _cursorCol = col;
        }
    }

    @Override
    public void print(final String text)
    {
        int r = _cursorRow;
        int c = _cursorCol;

        for (char ch : text.toCharArray()) {
            if (r >= ROWS) break;

            if (c < COLS) {
                _rowData[r].setCharAt(c, ch);
                c++;
            } else {
                r++;
                if (r >= ROWS) break;
                c = 0;
                _rowData[r].setCharAt(c, ch);
                c++;
            }
        }

        for (int i = 0; i < ROWS; i++) {
            _textRows[i].setText(_rowData[i].toString());
        }

        _cursorRow = r;
        _cursorCol = c;
    }
}
