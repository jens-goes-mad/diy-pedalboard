package de.diy_pedalboard.controls.adapter;

import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortTimeoutException;
import lombok.SneakyThrows;

import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class SerialCommandAdapter
    extends Thread
{
    public interface ICommandHandler
    {
        String process(final String command);
    }

    private final String _portName;
    private final ICommandHandler _commandHandler;
    private SerialPort _serialPort = null;

    /**
     *
     * @param portName like /dev/cu.usbserial-1234
     */
    public SerialCommandAdapter(final String portName, final ICommandHandler commandHandler)
    {
        _portName = portName;
        _commandHandler = commandHandler;
    }

    @Override
    public void start()
    {
        _serialPort = SerialPort.getCommPort(_portName);
        _serialPort.setBaudRate(115200);
        _serialPort.setNumDataBits(8);
        _serialPort.setParity(SerialPort.NO_PARITY);
        _serialPort.setNumStopBits(SerialPort.ONE_STOP_BIT);
        _serialPort.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, 5000, 5000);

        if (!_serialPort.openPort()) {
            throw new IllegalStateException("Failed to open " + _portName);
        }

        super.start();
    }

    @Override
    @SneakyThrows
    public void run()
    {
        try (final InputStream in = _serialPort.getInputStream();
             final OutputStream out = _serialPort.getOutputStream()
        ) {
            // Write a test line
            out.write("SerialCommandAdapter ready\n".getBytes());
            out.flush();

            // Read loop (basic example)
            final StringBuffer buffer = new StringBuffer();
            final byte[] byteBuffer = new byte[256];
            int bytesRead = 0;
            while (_serialPort.isOpen()) {
                try {
                    while (0 <= (bytesRead = in.read(byteBuffer))) {
                        final String textReceived = new String(byteBuffer, 0, bytesRead, StandardCharsets.UTF_8);
                        int cmdSplitIdx = 0, lastSplitIdx = 0;
                        while (0 <= (cmdSplitIdx = textReceived.indexOf("\r", lastSplitIdx))) {
                            buffer.append(textReceived.substring(0, cmdSplitIdx));
                            _commandHandler.process(buffer.toString());
                            buffer.setLength(0);
                            lastSplitIdx = cmdSplitIdx + 1;
                        }

                        buffer.append(textReceived.substring(lastSplitIdx));
                    }
                } catch (final SerialPortTimeoutException e) {
                    // just ignore and check graceful shutdown
                }
            }
        } finally {
            _serialPort.closePort();
        }
    }

    public void shutdown()
    {
        try {
            _serialPort.closePort();
        } finally {
            //
        }
    }



    public static void main(final String[] args)
    {
        final ICommandHandler commandHandler = command -> {
            System.out.println(command);
            return null;
        };

        new SerialCommandAdapter(args[0], commandHandler).start();
    }
}
