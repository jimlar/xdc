package xdc.net;

import org.apache.log4j.Logger;

import java.io.*;

class CommandWriter {
    private static final String COMMAND_SEPARATOR = "|";

    private Logger logger = Logger.getLogger(CommandWriter.class);
    private OutputStreamWriter out;

    public CommandWriter(OutputStream out) throws IOException {
        this.out = new OutputStreamWriter(out, "iso-8859-1");
    }

    public void writeCommand(Command command) throws IOException {
        logger.debug("Sending: " + command);
        out.write(command.toString());
        out.write(COMMAND_SEPARATOR);
        out.flush();
    }
}
