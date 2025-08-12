package de.diy_pedalboard.controls;

public class LcdCommandService
{
    private final ILcdDisplay[] _displays;

    public LcdCommandService(final ILcdDisplay[] displays)
    {
        _displays = displays;
    }

    public void process(final String command)
    {
        if (null != command && command.length() > 3) {
            try {
                System.out.println(command);

                final String ucCommand = command.toUpperCase();
                final String displayIndexStr = ucCommand.substring(3, 4);

                final int startIndex, endIndex;
                if ("A".equals(displayIndexStr)) {
                    startIndex = 0; endIndex = _displays.length;
                } else {
                    startIndex = Integer.parseInt(displayIndexStr);
                    endIndex = startIndex + 1;
                }

                if (ucCommand.startsWith("CLR")) {
                    // STCd
                    for (int i = startIndex; i < endIndex; i++) {
                        _displays[i].clear();
                    }
                } else if (ucCommand.startsWith("STC") && ucCommand.length() == 7) {
                    // STCdrcc
                    final int row = Integer.parseInt(ucCommand.substring(4, 5));
                    final int col = Integer.parseInt(ucCommand.substring(5, 7));
                    for (int i = startIndex; i < endIndex; i++) {
                        _displays[i].setCursor(col, row);
                    }
                } else if (ucCommand.startsWith("WRT")) {
                    // WRTdt...
                    for (int i = startIndex; i < endIndex; i++) {
                        _displays[i].print(ucCommand.substring(4));
                    }
                } else if (ucCommand.startsWith("BLK")) {
                    // BLKdn
                    final boolean on = "1".equals(ucCommand.substring(4, 5));
                    for (int i = startIndex; i < endIndex; i++) {
                        _displays[i].setBlink(on);
                    }
                }
            } catch (final Exception e) {
                System.err.println("unable to process command: " + command);
            }
        }
    }
}
