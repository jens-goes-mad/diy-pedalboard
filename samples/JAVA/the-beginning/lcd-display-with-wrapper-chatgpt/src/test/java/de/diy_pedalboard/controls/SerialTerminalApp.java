package de.diy_pedalboard.controls;

import com.fazecast.jSerialComm.SerialPort;

import java.io.OutputStream;
import java.util.Scanner;

public class SerialTerminalApp
{
    private static SerialPort currentPort = null;
    private static int defaultBaudRate = 115200;

    public static void main(final String[] args)
    {
        System.out.println("=== Serial Terminal App ===");
        System.out.println("Commands:");
        System.out.println("  list                       - List available serial ports");
        System.out.println("  use <port> [baud]          - Select and open a port (default baud 115200)");
        System.out.println("  cmd <string>               - Send command to selected port");
        System.out.println("  exit / quit                - Exit the program");
        System.out.println();

        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.print("> ");
            String line = scanner.nextLine().trim();
            if (line.isEmpty()) continue;

            String[] tokens = line.split("\\s+", 2);
            String command = tokens[0];

            switch (command.toLowerCase()) {
                case "list":
                    listPorts();
                    break;

                case "use":
                    if (tokens.length < 2) {
                        System.out.println("Usage: use <port> [baud]");
                        break;
                    }
                    usePort(tokens[1]);
                    break;

                case "cmd":
                    if (tokens.length < 2) {
                        System.out.println("Usage: cmd <text>");
                        break;
                    }
                    sendCommand(tokens[1]);
                    break;

                case "exit":
                case "quit":
                    closePort();
                    System.out.println("Exiting.");
                    return;

                default:
                    System.out.println("Unknown command. Type 'list', 'use', 'cmd', or 'exit'.");
            }
        }
    }

    private static void listPorts()
    {
        SerialPort[] ports = SerialPort.getCommPorts();
        System.out.println("Available ports:");
        for (SerialPort port : ports) {
            System.out.println("  " + port.getSystemPortName() + " - " + port.getDescriptivePortName());
        }
    }

    private static void usePort(String input)
    {
        String[] parts = input.split("\\s+");
        String portName = parts[0];
        int baud = (parts.length > 1) ? Integer.parseInt(parts[1]) : defaultBaudRate;

        closePort(); // Close existing port first

        SerialPort port = SerialPort.getCommPort(portName);
        port.setBaudRate(baud);
        if (port.openPort()) {
            currentPort = port;
            System.out.println("Opened port " + portName + " @ " + baud + " baud.");
        } else {
            System.err.println("Failed to open port: " + portName);
        }
    }

    private static void sendCommand(String message)
    {
        if (currentPort == null || !currentPort.isOpen()) {
            System.err.println("No port selected. Use 'use <port>' first.");
            return;
        }
        try {
            OutputStream out = currentPort.getOutputStream();
            out.write(message.getBytes());
            out.write('\r');
            out.flush();
            System.out.println("Sent: " + message);
        } catch (Exception e) {
            System.err.println("Error sending command: " + e.getMessage());
        }
    }

    private static void closePort()
    {
        if (currentPort != null && currentPort.isOpen()) {
            currentPort.closePort();
            System.out.println("Closed port.");
        }
    }
}
