package xdc.net;

import java.io.*;

public class CommandReader {
    private static final char COMMAND_SEPARATOR = '|';
    private static final String COMMAND_PREFIX_SEPARATOR = " ";

    private InputStreamReader in;

    public CommandReader(InputStream in) throws IOException {
        this.in = new InputStreamReader(in, "iso-8859-1");
    }

    /**
     * Read the next command
     * @return the command or null if end of stream
     */
    public Command readCommand() throws IOException {
        Command command = null;
        while (command == null) {
            StringBuffer result = new StringBuffer(20);
            int readChar = in.read();
            if (readChar == -1) {
                return null;
            }

            while (readChar != -1 && readChar != COMMAND_SEPARATOR) {
                result.append((char) readChar);
                readChar = in.read();
            }
            command = decodeCommand(result.toString());
        }
        return command;
    }

    private Command decodeCommand(String commandString) {
        int commandTypeEnd = commandString.indexOf(COMMAND_PREFIX_SEPARATOR);
        String commandType = commandString;
        String commandArgs = null;

        if (commandTypeEnd != -1) {
            commandType = commandString.substring(0, commandTypeEnd);
            commandArgs = commandString.substring(commandTypeEnd + 1);
        }

        if (commandType.startsWith("$")) {
            return new Command(false, commandType.substring(1), commandArgs);
        } else if (commandType.startsWith("<")) {
            if (commandType.length() > 1) {
                return new Command(true, commandType.substring(1, commandType.length() - 1), commandArgs);
            }
        }
        return null;
    }
}
