package xdc.net;

public interface ClientConnectionListener {

    void connected(ClientConnection connection);

    void disconnected(ClientConnection connection, String message);
}
