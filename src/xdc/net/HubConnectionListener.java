package xdc.net;

public interface HubConnectionListener {

    void connected(HubConnection connection);

    void disconnected(HubConnection connection, String disconnectMessage);

    void hubInfoChanged(HubConnection connection, Hub hubInfo);

    void receivedSearch(HubConnection connection, Command searchCommand);

    void hubMessage(HubConnection connection, Command message);

    void privateChatMessage(HubConnection connection, Command message);

    void userArrived(HubConnection connection, User newUser);

    void userDisconnected(HubConnection connection, User disconnectedUser);

    void forceMove(HubConnection connection, Command moveCommand);

    void searchResult(HubConnection connection, Command result);

    void connectToMe(HubConnection connection, Command command);

    void reverseConnectToMe(HubConnection connection, Command command);

    void passwordRequired(HubConnection connection);
}
