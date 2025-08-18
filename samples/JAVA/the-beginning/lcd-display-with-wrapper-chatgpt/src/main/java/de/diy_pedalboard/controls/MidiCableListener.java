package de.diy_pedalboard.controls;

import javax.sound.midi.*;

public class MidiCableListener
{
    public static void main(final String[] args) throws MidiUnavailableException
    {
        final MidiDevice.Info[] infos = MidiSystem.getMidiDeviceInfo();
        for (int i = 0; i < infos.length; i++) {
            final MidiDevice device = MidiSystem.getMidiDevice(infos[i]);
            System.out.printf("Device %d: %s (%s)\n", i, infos[i].getName(), infos[i].getDescription());
            // Check if device has transmitters or receivers
            if (device.getMaxTransmitters() != 0) {
                System.out.println(" - Has transmitters (can receive MIDI input)");
            }
            if (device.getMaxReceivers() != 0) {
                System.out.println(" - Has receivers (can send MIDI output)");
            }
        }
    }
}
