package xdc.net;

public interface HubConnectionListener {

    void connected(HubConnection connection);

    void disconnected(HubConnection connection, String disconnectMessage);

    void hubInfoChanged(HubConnection connection, Hub hubInfo);

    void receivedSearch(HubConnection connection, Search search);

    void hubMessage(HubConnection connection, String message);

    void privateChatMessage(HubConnection connection, User from, String message);

    void userArrived(HubConnection connection, User newUser);

    void userModified(HubConnection connection, User user);

    void userDisconnected(HubConnection connection, User disconnectedUser);

    void forceMove(HubConnection connection, String moveDestination);

    void searchResult(HubConnection connection, SearchResult result);

    void connectToMe(HubConnection connection, String connectTo);

    void reverseConnectToMe(HubConnection connection, String nick);

    void passwordRequired(HubConnection connection);
}
