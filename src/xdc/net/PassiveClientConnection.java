package xdc.net;

import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.net.Socket;

/**
 * This class is used to connect directly to a client.
 * This is what you want to do on uploads and downloads (including filelist downloads).
 *
 */
public class PassiveClientConnection extends ClientConnection implements Runnable {
    private static Logger logger = Logger.getLogger(PassiveClientConnection.class);

    private String host;
    private int port;

    private Socket socket;
    private CommandWriter writer;

    public PassiveClientConnection(User localUser, String host, int port) {
        super(localUser);
        this.host = host;
        this.port = port;
    }

    public void connect() {
        new Thread(this).start();
    }

    public void disconnect() {
        disconnect("Disconnected");
    }

    public boolean isConnected() {
        return socket != null;
    }

    private void disconnect(String message) {
        fireDisconnected(message);
        try {
            if (socket != null) {
                socket.close();
                socket = null;
            }
        } catch (Exception e) {
            logger.warn("Error disconnecting (ignored)", e);
        }
    }

    public void run() {
        try {
            socket = new Socket(host, port);
            socket.setReceiveBufferSize(RECEIVE_BUFFER_SIZE);
            writer = new CommandWriter(socket.getOutputStream());

            writer.writeCommand(Command.createMyNickCommand(getLocalUser()));
            writer.writeCommand(Command.createLockCommand());

            fireConnected();

        } catch (Throwable e) {
            disconnect(e.getMessage());
        }
    }
}
