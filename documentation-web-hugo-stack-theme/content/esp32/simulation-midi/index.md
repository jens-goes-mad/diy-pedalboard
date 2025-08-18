---
title: Midi simulation (Transceiver)
links:
  - title: Writing a MIDI transceiver in JavaFX 
    description: a blog about software and hardware development of a DIY pedal board
menu:
    main:
        parent: "esp32"
        weight: 80
layout: "article"
toc: true
draft: false
tags: ["software", "esp32", "simulation", "midi"]
categories: ["midi"]
imageSuppress: "Page"
shortSummary: "
A JavaFX MIDI transceiver application that sends Volume Change messages by dragging a slider.
"
---
# MIDI simulation  

As you already might have noticed I love simulations. To test our MIDI connection and message handling we need a simple
Application which queries the `javax.sound` subsystem for receivers and let us send midi messages. And because I am to
lazy to type, my sidekick steps in:

<span class="chatgpt">Can you sketch a JavaFX app which has one slider and sends midi volume messages on channel 1?</span>

## Midi Transmitter

    public class MainVolumeSlider
    extends Application
    {
        private static final int MIDI_CHANNEL = 0; // Channel 1 → index 0
        private static final int CC_VOLUME = 7;    // Channel Volume (MSB)
    
        private Receiver receiver;                 // currently selected receiver
        private MidiDevice currentDevice;          // keep a handle so we can close it
    
        @Override
        public void start(final Stage stage)
        {

Let's create a dropdown with all MIDI receivers

            // --- UI: device chooser + volume slider
            final Label deviceLabel = new Label("MIDI Output:");
            final ComboBox<MidiDevice.Info> deviceCombo
                = new ComboBox<>(FXCollections.observableArrayList(listOutputDevices()));
            deviceCombo.setCellFactory(cb -> new MidiInfoListCell());
            deviceCombo.setButtonCell(new MidiInfoListCell());
    
And a slider which send Volume Change message values between 0..127

            final Label valueLabel = new Label("Volume: 100");
            final Slider slider = new Slider(0, 127, 100);
            slider.setMajorTickUnit(16);
            slider.setMinorTickCount(3);
            slider.setShowTickMarks(true);
            slider.setShowTickLabels(true);
            slider.setBlockIncrement(1);
            slider.setSnapToTicks(true);
    
            // Send CC on change
            final ChangeListener<Number> sliderListener = (obs, oldV, newV) -> {
                int v = newV.intValue();
                valueLabel.setText("Volume: " + v);
                sendCC(CC_VOLUME, v);
            };
            slider.valueProperty().addListener(sliderListener);
    
            // When a device is selected, open it and send the current slider value
            deviceCombo.getSelectionModel().selectedItemProperty().addListener((obs, oldInfo, newInfo) -> {
                openReceiverFor(newInfo);
                sendCC(CC_VOLUME, (int) slider.getValue());
            });
    
            // Pick the first available device if any; else fall back to default system receiver
            if (!deviceCombo.getItems().isEmpty()) {
                deviceCombo.getSelectionModel().selectFirst();
            } else {
                // As a fallback, try default system receiver (often routes to the default synth)
                try {
                    receiver = MidiSystem.getReceiver();
                } catch (MidiUnavailableException e) {
                    receiver = null;
                }
            }
    
            final Label title = new Label("MIDI Volume (Ch 1, CC 7)");
            final VBox root = new VBox(12, title, deviceLabel, deviceCombo, slider, valueLabel);
            root.setPadding(new Insets(16));
    
            stage.setTitle("MIDI Volume → Specific Output");
            stage.setScene(new Scene(root, 460, 220));
            stage.show();
        }
    
        /** Enumerate MIDI devices that can provide a Receiver (maxReceivers != 0). */
        private List<MidiDevice.Info> listOutputDevices()
        {
            ...
        }
    
        /** Open the device for the given Info and keep its Receiver as the current target. */
        private void openReceiverFor(final MidiDevice.Info info)
        {
            ...
        }
    
whenever slider is dragged a value is send as MIDI message to listener(s)

        private void sendCC(int controller, int value)
        {
            if (receiver == null) return;
            try {
                ShortMessage msg = new ShortMessage();
                msg.setMessage(ShortMessage.CONTROL_CHANGE, MIDI_CHANNEL, controller, value);
                receiver.send(msg, -1);
            } catch (InvalidMidiDataException e) {
                e.printStackTrace();
            }
        }
    
        private void closeCurrentReceiver()
        {
            ...
        }
    
        @Override
        public void stop()
        {
            closeCurrentReceiver();
        }
    
        // Small helper to show nicer names in the ComboBox
        private static class MidiInfoListCell extends javafx.scene.control.ListCell<MidiDevice.Info>
        {
            @Override protected void updateItem(final MidiDevice.Info item, final boolean empty)
            {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getName() + " — " + item.getVendor() + " (" + item.getDescription() + ")");
                }
            }
        }
    
        public static void main(final String[] args)
        {
            launch(args);
        }
    }

As usual find source on [github](https://github.com/jens-goes-mad/diy-pedalboard/blob/master/samples/JAVA/the-beginning/lcd-display-with-wrapper-chatgpt/src/main/java/de/diy_pedalboard/controls/MainVolumeSlider.java)

Alright, let’s fire it up and see transmitter and receiver in action. On the left side we have our
JavaFX application, on the right the [midi viewer](https://hautetechnique.com/midi/midiview/). 
You can see: as soon as the slider is moved, the viewer receives MIDI messages (in HEX): B0 07 XX

{{< youtube "MomxYb-5jv4" >}}

## MIDI 

Let's break down what we're seeing:
MIDI messages are divided into Channel Messages and System Messages indicated by the upper 4 bits (the nibble) 
of the first byte. For now, we'll focus on Channel Messages.

### MIDI Channel Messages

Channel messages consist of either two or three bytes. Here’s an overview:

| Status Byte (Hex) | Status Byte (Binary) | Message Type                | Data Byte 1        | Data Byte 2            | Description |
|-------------------|----------------------|-----------------------------|--------------------|------------------------|-------------|
| 0x8n              | 1000cccc             | Note Off                    | Note Number        | Release Velocity       | Turns a note off on channel. |
| 0x9n              | 1001cccc             | Note On                     | Note Number        | Velocity               | Turns a note on; velocity 0 is often treated as Note Off. |
| 0xAn              | 1010cccc             | Polyphonic Aftertouch       | Note Number        | Pressure Amount        | Per-note pressure after the key is pressed. |
| 0xBn              | 1011cccc             | Control Change (CC)         | Controller Number  | Controller Value       | Adjusts parameters like modulation, volume, pan, etc. |
| 0xCn              | 1100cccc             | Program Change              | Program Number     | *None*                 | Changes instrument/patch. |
| 0xDn              | 1101cccc             | Channel Pressure Aftertouch | Pressure Amount    | *None*                 | Pressure applied to all notes on the channel. |
| 0xEn              | 1110cccc             | Pitch Bend Change           | LSB                | MSB                    | 14-bit value, center = 8192. |

The upper 4 bits of the status byte define the message type (0x8..0xE = 0b1000..0b1110 = 8..14), which all describes 
Channel Messages. The lower 4 bits (cccc) select one of 16 possible channels (values 0..15 correspond to channel 1..16).
Each data byte always starts with a leading 0 bit (0b0vvvvvvv), therefore MIDI data values range from 0–127. 
Most of the Channel Messages are 3 byte messages (one status byte + two data bytes), 
except for Program Change and Channel Pressure Aftertouch, which only have one data byte.

Thus `0xB0 0x07 0x1C` means: Control Change (CC), on Channel 1, Value 28

Time to send these messages to [real hardware](/esp32/midi-class-compliance)!