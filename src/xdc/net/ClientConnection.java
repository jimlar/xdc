package xdc.net;

import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.net.Socket;

/**
 * This class is used to connect directly to a client.
 * This is what you want to do on uploads and downloads (including filelist downloads).
 *
 */
public class ClientConnection {
    private static final int RECEIVE_BUFFER_SIZE = 32 * 1024;

    private static Logger logger = Logger.getLogger(ClientConnection.class);

    private String host;
    private int port;
    private List listeners;

    private Socket socket;
    private CommandWriter writer;

    public ClientConnection(String host, int port) {
        this.host = host;
        this.port = port;
        this.listeners = new ArrayList();
    }

    public void addConnectionListener(ClientConnectionListener listener) {
        this.listeners.add(listener);
    }

    public void connect() {
        new Thread(new Runnable() {
            public void run() {
                try {
                    socket = new Socket(host, port);
                    socket.setReceiveBufferSize(RECEIVE_BUFFER_SIZE);
                    writer = new CommandWriter(socket.getOutputStream());

                    fireConnected();

                } catch (Throwable e) {
                    disconnect(e.getMessage());
                }
            }
        }).start();
    }

    public void disconnect() {
        disconnect("Disconnected");
    }

    private void disconnect(String message) {
        fireDisconnected(message);
        try {
            socket.close();
        } catch (Exception e) {
            logger.warn("Error disconnecting (ignored)", e);
        }
    }

    public void startDownload(SearchResult result, File localFile, boolean resumeIfPossible) throws IOException {
        startDownload(result.getFilename(), localFile, resumeIfPossible);
    }

    public void startDownload(String remoteFile, File localFile, boolean resumeIfPossible) throws IOException {
        if (!isConnected()) {
            throw new IOException("Not connected");
        }

        int resumeFromByte = 0;
        writer.writeCommand(Command.createGetFileCommand(remoteFile, resumeFromByte));

    }

    private boolean isConnected() {
        return socket != null;
    }

    private void fireDisconnected(String message) {
        for (Iterator i = listeners.iterator(); i.hasNext();) {
            ClientConnectionListener listener = (ClientConnectionListener) i.next();
            listener.disconnected(this, message);
        }
    }

    private void fireConnected() {
        for (Iterator i = listeners.iterator(); i.hasNext();) {
            ClientConnectionListener listener = (ClientConnectionListener) i.next();
            listener.connected(this);
        }
    }
}
