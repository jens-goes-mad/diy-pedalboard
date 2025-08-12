package de.diy_pedalboard.controls;

public interface ILcdDisplay
{
    void clear();
    void setCursor(final int col, final int row);
    void setBlink(final boolean blink);     // true = on, false = off

    void print(final String text);
}
