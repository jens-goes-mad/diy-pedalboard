// ESP32-S3 Recovery Blink (Arduino CDC + NeoPixel)
// - Always brings up Serial (USB CDC) for logs
// - Blinks onboard RGB (WS2812)
// Libraries: Adafruit NeoPixel

#include <Adafruit_NeoPixel.h>

// Adjust to your board's RGB pin:
#ifndef RGB_LED_PIN
  #define RGB_LED_PIN 48   // DevKitC: 48, XIAO: 21, Feather S3: 33
#endif
#define NUM_PIXELS    1
#define BOOT_BTN_PIN  0

Adafruit_NeoPixel pixel(NUM_PIXELS, RGB_LED_PIN, NEO_GRB + NEO_KHZ800);

void setRGB(uint8_t r, uint8_t g, uint8_t b, uint8_t br=40) {
  pixel.setBrightness(br);
  pixel.setPixelColor(0, pixel.Color(r,g,b));
  pixel.show();
}
void blinkRGB(uint8_t r,uint8_t g,uint8_t b,uint32_t on_ms,uint32_t off_ms,uint8_t br=40){
  setRGB(r,g,b,br); delay(on_ms); setRGB(0,0,0,0); delay(off_ms);
}

void setup() {
  pinMode(BOOT_BTN_PIN, INPUT_PULLUP);
  pixel.begin();
  setRGB(0,0,0,0);

  // Built-in Arduino CDC (enable Tools → USB CDC On Boot = Enabled)
  Serial.begin(115200);
  unsigned long t0 = millis();
  while (!Serial && millis() - t0 < 1500) { blinkRGB(30,30,30,60,60,12); }

  Serial.println();
  Serial.println("=== ESP32-S3 Recovery Blink (Arduino CDC) ===");
  Serial.printf("RGB_LED_PIN=%d  BOOT_BTN_PIN=%d\n", RGB_LED_PIN, BOOT_BTN_PIN);

  // Mode cue
  if (digitalRead(BOOT_BTN_PIN) == LOW) {
    for (int i=0;i<4;i++) blinkRGB(0,180,180,120,120,32);  // Safe cue
  } else {
    for (int i=0;i<4;i++) blinkRGB(0,160,0,120,120,32);    // Normal cue
  }
}

void loop() {
  static uint32_t last=0;

  if (digitalRead(BOOT_BTN_PIN) == LOW) {
    blinkRGB(0,0,180,250,750,20);  // Safe: slow blue blink
  } else {
    blinkRGB(255,0,0,200,200,28);
    blinkRGB(0,255,0,200,200,28);
    blinkRGB(0,0,255,200,200,28);
    blinkRGB(255,255,255,60,240,16);
  }

  if (millis() - last > 2000) {
    last = millis();
    Serial.printf("Uptime: %lu ms\n", (unsigned long)millis());
  }
}
