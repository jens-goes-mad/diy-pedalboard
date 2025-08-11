package de.diy_pedalboard.controls;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.util.concurrent.atomic.AtomicBoolean;

@Accessors(prefix = "_")
public class JavaFXLCDDisplay
    extends VBox
    implements ILcdDisplay
{
    private static final int VBOX_SPACING = 10;
    private static final int PADDING = 10;

    private final int _rowCount;
    private final int _colCount;

    private final Text[] _textRows;
    private final StringBuilder[] _rowData;
    private final Timeline _timeline;
    private final AtomicBoolean _isBlinking = new AtomicBoolean(false);
    private final AtomicBoolean _isBlinkingOn = new AtomicBoolean(true);

    private int _cursorRow = 0;
    private int _cursorCol = 0;

    @Getter private final double _screenWidth;
    @Getter private final double _screenHeight;

    public JavaFXLCDDisplay()
    {
        this(4, 20, 24);
    }

    public JavaFXLCDDisplay(final int rowCount, final int colCount)
    {
        this(rowCount, colCount,24);
    }

    public JavaFXLCDDisplay(final int rowCount, final int colCount, final int fontHeight)
    {
        super(VBOX_SPACING);

        _rowCount = rowCount;
        _colCount = colCount;

        _textRows = new Text[_rowCount];
        _rowData = new StringBuilder[_rowCount];

        final Font customFont = Font.loadFont(
            getClass().getResourceAsStream("/LCD5x7SegmentMonospace-Regular.otf"), fontHeight
        );

        final Text oneChar = new Text("A");
        oneChar.setFont(customFont);

        _screenWidth = oneChar.getLayoutBounds().getWidth() * _colCount + 2 * PADDING;
        setMinWidth(_screenWidth);
        setMaxWidth(_screenWidth);
        _screenHeight = oneChar.getLayoutBounds().getHeight() * _rowCount + 2 * PADDING + (_rowCount-1) * VBOX_SPACING;
        setMinHeight(_screenHeight);
        setMaxHeight(_screenHeight);
        setStyle("-fx-background-color: blue; -fx-padding: " + PADDING + "px; -fx-border-radius: 0px; -fx-background-radius: 0px;");

        for (int i = 0; i < _rowCount; i++) {
            _rowData[i] = new StringBuilder(" ".repeat(_colCount));

            final Text text = new Text(_rowData[i].toString());
            text.setFont(customFont);

            _textRows[i] = text;
            _textRows[i].setFill(Color.WHITE);

            getChildren().add(_textRows[i]);
        }

        _timeline = new Timeline(
            new KeyFrame(Duration.millis(500), e -> {
                // do something every 500 ms on the FX thread
                if (_isBlinking.get()) {
                    if (_isBlinkingOn.get()) {
                        final StringBuilder row = new StringBuilder(_rowData[_cursorRow].toString());
                        row.setCharAt(_cursorCol, (char) 245);
                        _textRows[_cursorRow].setText(row.toString());
                    } else {
                        _textRows[_cursorRow].setText(_rowData[_cursorRow].toString());
                    }

                    final boolean prev = _isBlinkingOn.get();
                    _isBlinkingOn.compareAndExchange(prev, !prev);
                }
            })
        );
        _timeline.setCycleCount(Animation.INDEFINITE);
        _timeline.play();
    }

    @Override
    public void clear()
    {
        for (int i = 0; i < _rowCount; i++) {
            _rowData[i] = new StringBuilder(" ".repeat(_colCount));
            _textRows[i].setText(_rowData[i].toString());
        }
        _cursorRow = 0;
        _cursorCol = 0;
    }

    @Override
    public void setCursor(int col, int row)
    {
        if (row >= 0 && row < _rowCount && col >= 0 && col < _colCount) {
            _cursorRow = row;
            _cursorCol = col;
        }
    }

    public void setBlink(final boolean blink)
    {
        _isBlinking.set(blink);
    }

    @Override
    public void print(final String text)
    {
        int r = _cursorRow;
        int c = _cursorCol;

        for (char ch : text.toCharArray()) {
            if (r >= _rowCount) break;

            if (c < _colCount) {
                _rowData[r].setCharAt(c, ch);
                c++;
            } else {
                r++;
                if (r >= _rowCount) break;
                c = 0;
                _rowData[r].setCharAt(c, ch);
                c++;
            }
        }

        for (int i = 0; i < _rowCount; i++) {
            _textRows[i].setText(_rowData[i].toString());
        }

        _cursorRow = r;
        _cursorCol = c;
    }
}
