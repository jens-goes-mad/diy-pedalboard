const unsigned long C_LOOP_COUNT = 3000000;

void setup()
{
  pinMode(LED_BUILTIN, OUTPUT);
}


void loop() 
{
  unsigned long loopCounter = C_LOOP_COUNT;

  digitalWrite(LED_BUILTIN, HIGH);
  delay(1000);
  digitalWrite(LED_BUILTIN, LOW);
  delay(100);

  unsigned long startTime_ms = millis();

  while (loopCounter-- > 0) {
    if (loopCounter == C_LOOP_COUNT / 2) {
      digitalWrite(LED_BUILTIN, HIGH);
      digitalWrite(LED_BUILTIN, LOW);
    }
  }

  unsigned long endTime_ms = millis();
  unsigned long total_ms = endTime_ms - startTime_ms;

  while (total_ms > 0) {
    digitalWrite(LED_BUILTIN, HIGH);
    delay(2000);
    digitalWrite(LED_BUILTIN, LOW);
    delay(250);

    int blinkCount = (int)(total_ms % 10);
    for (int b = 0; b < blinkCount; b++) {
      digitalWrite(LED_BUILTIN, HIGH);
      delay(250);
      digitalWrite(LED_BUILTIN, LOW);
      delay(250);
    }

    delay(1000);

    total_ms = total_ms / 10;
  }
}
