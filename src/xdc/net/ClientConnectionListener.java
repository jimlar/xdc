package xdc.net;

public interface ClientConnectionListener {

    void connected(PassiveClientConnection connection);

    void disconnected(PassiveClientConnection connection, String message);
}
