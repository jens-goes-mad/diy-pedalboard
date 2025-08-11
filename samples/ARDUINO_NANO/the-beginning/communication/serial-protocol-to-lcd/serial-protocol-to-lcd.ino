#include <Wire.h>
#include <LiquidCrystal_I2C.h>

class LCD
{
private:
    unsigned int _address;
    unsigned int _rowCount;
    unsigned int _colCount;
    LiquidCrystal_I2C* _pLC;

public:
    LCD(unsigned int address, unsigned int rowCount, unsigned int colCount);
    virtual ~LCD();

    LiquidCrystal_I2C& getDriver() { return *_pLC; }
};

LCD::LCD(unsigned int address, unsigned int rowCount, unsigned int colCount)
{
    _address = address;
    _rowCount = rowCount;
    _colCount = colCount;

    _pLC = new LiquidCrystal_I2C(0x27, 16, 2);
    _pLC->init();
    _pLC->backlight();
    _pLC->clear();
    _pLC->blink();
}

LCD::~LCD()
{
    delete _pLC;
}

// ----

LCD* pg_LCD;
String g_inputBuffer = "";


void setup()
{
    // INIT DISPLAYS
    pg_LCD = new LCD(0x27, 16, 2);

    Wire.begin();
    Wire.setClock(400000); // Fast I2C

    // Serial.begin(57600);
    Serial.begin(115200);
}

void loop() 
{
    while (Serial.available()) {
        char c = Serial.read();
        if (c == '\r') {
            g_inputBuffer.trim();  // remove whitespace and newline chars
            if (g_inputBuffer.length() > 3) {
                handleCommand(g_inputBuffer);
            }
            g_inputBuffer = "";  // clear buffer after handling
        } else {
            g_inputBuffer += c;
        }
    }
}

void handleCommand(String cmd)
{
    String action = cmd.substring(0, 3);
    String display = cmd.substring(3, 4);
    if (action == "CLR") {
        // CLR[0-3|A]
        handleClear();
    } else {
        String args = cmd.substring(4);
        if (action == "STC") {
            // STC[0-3|A][0-9=row][00-99=col]
            handleSetCursor(args);
        } else if (action == "WRT") {
            // STC[0-3|A][text....\r]
            handleWrite(args);
        } else if (action == "BLK") {
            // BLK[0-3|A][0=off|1=on]
            handleBlink(args);
        }
    }
}

void handleClear()
{
    pg_LCD->getDriver().clear();
}

void handleSetCursor(String args)
{
    int row = args.substring(0, 1).toInt();
    int col = args.substring(1, 3).toInt();
    pg_LCD->getDriver().setCursor(col, row);
}

void handleWrite(String args)
{
    pg_LCD->getDriver().print(args);
}

void handleBlink(String args)
{
    bool status = args.substring(0, 1).toInt();
    if (status) {
        pg_LCD->getDriver().blink();
    } else {
        pg_LCD->getDriver().noBlink();
    }
}
