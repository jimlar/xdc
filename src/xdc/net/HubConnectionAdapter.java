package xdc.net;

public class HubConnectionAdapter implements HubConnectionListener {
    public void connected(HubConnection connection) {}
    public void disconnected(HubConnection connection, String disconnectMessage) {}
    public void hubInfoChanged(HubConnection connection, Hub hubInfo) {}
    public void receivedSearch(HubConnection connection, Search search) {}
    public void hubMessage(HubConnection connection, String message) {}
    public void privateChatMessage(HubConnection connection, User from, String message) {}
    public void userArrived(HubConnection connection, User newUser) {}
    public void userModified(HubConnection connection, User user) {}
    public void userDisconnected(HubConnection connection, User disconnectedUser) {}
    public void forceMove(HubConnection connection, String moveDestination) {}
    public void searchResult(HubConnection connection, SearchResult result) {}
    public void connectToMe(HubConnection connection, String connectTo) {}
    public void reverseConnectToMe(HubConnection connection, String nick) {}
    public void passwordRequired(HubConnection connection) {}
}
