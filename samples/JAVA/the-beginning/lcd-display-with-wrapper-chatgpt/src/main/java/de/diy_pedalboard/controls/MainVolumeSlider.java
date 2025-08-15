package de.diy_pedalboard.controls;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import javax.sound.midi.*;
import java.util.ArrayList;
import java.util.List;

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
        // --- UI: device chooser + volume slider
        final Label deviceLabel = new Label("MIDI Output:");
        final ComboBox<MidiDevice.Info> deviceCombo
            = new ComboBox<>(FXCollections.observableArrayList(listOutputDevices()));
        deviceCombo.setCellFactory(cb -> new MidiInfoListCell());
        deviceCombo.setButtonCell(new MidiInfoListCell());

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
        List<MidiDevice.Info> outs = new ArrayList<>();
        for (MidiDevice.Info info : MidiSystem.getMidiDeviceInfo()) {
            try {
                MidiDevice dev = MidiSystem.getMidiDevice(info);
                // -1 means "unlimited", >0 means available, 0 means none
                if (dev.getMaxReceivers() != 0) {
                    outs.add(info);
                }
            } catch (MidiUnavailableException ignored) { }
        }
        return outs;
    }

    /** Open the device for the given Info and keep its Receiver as the current target. */
    private void openReceiverFor(final MidiDevice.Info info)
    {
        // Close any previously opened device/receiver
        closeCurrentReceiver();

        if (info == null) {
            receiver = null;
            return;
        }

        try {
            MidiDevice dev = MidiSystem.getMidiDevice(info);
            if (!dev.isOpen()) dev.open();
            Receiver r = dev.getReceiver();

            currentDevice = dev;
            receiver = r;
        } catch (MidiUnavailableException e) {
            e.printStackTrace();
            receiver = null;
            currentDevice = null;
        }
    }

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
        if (receiver != null) {
            receiver.close();
            receiver = null;
        }
        if (currentDevice != null && currentDevice.isOpen()) {
            currentDevice.close();
            currentDevice = null;
        }
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
