package de.diy_pedalboard.controls;

public interface ILcdDisplay
{
    void clear();
    void setCursor(final int col, final int row);
    void print(final String text);
}
