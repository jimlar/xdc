package xdc.net;

import java.net.Socket;
import java.io.*;

public class HubConnection extends Thread {
    private Hub hub;
    private Socket socket;
    private CommandReader reader;
    private CommandWriter writer;

    public HubConnection(Hub hub) throws IOException {
        this.hub = hub;
        connect();
        this.start();
    }

    public void run() {
        try {
            Command command;
            while ((command = reader.readCommand()) != null) {
                processCommand(command);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void processCommand(Command command) throws IOException {
        if (command.isChatMessage()) {
            System.out.println("Got chat message from: " + command.getCommand());

        } else if (command.isHubNameCommand()) {
            hub.setName(command.getArgs());

        } else if (command.isLockCommand()) {
            String lock = command.getArgs().substring(0, command.getArgs().toUpperCase().indexOf(" PK="));
            String key = ValidationKey.getValidationKeyFromLock(lock);
            sendCommand(Command.createKeyCommand(key));
            sendCommand(Command.createValidateNickCommand(getMyNick()));

        } else {
            System.out.println("Got unhandled command: '" + command.getCommand() + "', args='" + command.getArgs() + "'");
        }
    }

    private void sendCommand(Command command) throws IOException {
        writer.writeCommand(command);
    }

    private String getMyNick() {
        return "[Bostream]Jim";
    }

    private void connect() throws IOException {
        socket = new Socket(hub.getHost(), hub.getPort());
        socket.setTcpNoDelay(true);
        reader = new CommandReader(socket.getInputStream());
        writer = new CommandWriter(socket.getOutputStream());
    }
}
