package xdc.net;

import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

public abstract class ClientConnection {
    protected static final int RECEIVE_BUFFER_SIZE = 32 * 1024;

    private User localUser;
    private List listeners;

    protected ClientConnection(User localUser) {
        this.localUser = localUser;
        this.listeners = new ArrayList();
    }

    public abstract void connect();
    public abstract void disconnect();
    public abstract boolean isConnected();

    public User getLocalUser() {
        return localUser;
    }

    public void addConnectionListener(ClientConnectionListener listener) {
        this.listeners.add(listener);
    }

    protected void fireDisconnected(String message) {
        for (Iterator i = listeners.iterator(); i.hasNext();) {
            ClientConnectionListener listener = (ClientConnectionListener) i.next();
            listener.disconnected(this, message);
        }
    }

    protected void fireConnected() {
        for (Iterator i = listeners.iterator(); i.hasNext();) {
            ClientConnectionListener listener = (ClientConnectionListener) i.next();
            listener.connected(this);
        }
    }
}
