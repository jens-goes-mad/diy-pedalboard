package de.diy_pedalboard.controls;

import com.fazecast.jSerialComm.SerialPort;

public class Sample_LCD
{
    public static void main(final String[] args)
    {
        final SerialPort[] ports = SerialPort.getCommPorts();
        System.out.println("Available Ports:");
        for (SerialPort port : ports) {
            System.out.println(port.getSystemPortName());
        }
    }
}
