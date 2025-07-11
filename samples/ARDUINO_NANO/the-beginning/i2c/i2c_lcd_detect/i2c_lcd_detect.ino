#include <Wire.h>

void setup() {
  Wire.begin();
  Serial.begin(9600);
  Serial.println("Basic I2C Wire test starting...");

  for (int i=1; i < 127; i++) {
    Wire.beginTransmission(i); // Or 0x3F or any address you suspect
    byte result = Wire.endTransmission();

    if (result == 0) {
      Serial.print("Device found at ");
      Serial.println(i);
    } else {
      // Serial.print("No device. Error code: ");
      // Serial.println(result);
    }
  }
}

void loop() {}
