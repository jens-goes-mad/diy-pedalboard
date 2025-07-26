package de.diy_pedalboard.controls;

public interface ILcdDisplay {
    void clear();
    void setPosition(int col, int row);
    void write(String text);
}
