package xdc.net;

import java.net.Socket;
import java.io.*;

public class HubConnection extends Thread {
    private Hub hub;
    private Socket socket;
    private CommandReader reader;

    public HubConnection(Hub hub) throws IOException {
        this.hub = hub;
        connect();
        this.start();
    }

    public void run() {
        try {
            Command command;
            while ((command = reader.readCommand()) != null) {
                if (!command.isChatMessage()) {
                    System.out.println("Got command: " + command);
                } else {
                    System.out.println("Got message from " + command.getCommand());
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void connect() throws IOException {
        socket = new Socket(hub.getHost(), hub.getPort());
        socket.setTcpNoDelay(true);
        reader = new CommandReader(socket.getInputStream());
    }
}
