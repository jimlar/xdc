package xdc.net;

import java.io.*;

public class CommandWriter {
    private static final String COMMAND_SEPARATOR = "|";

    private OutputStreamWriter out;

    public CommandWriter(OutputStream out) throws IOException {
        this.out = new OutputStreamWriter(out, "iso-8859-1");
    }

    public void writeCommand(Command command) throws IOException {
        out.write(command.toString());
        out.write(COMMAND_SEPARATOR);
        out.flush();
    }
}
