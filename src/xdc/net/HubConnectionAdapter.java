package xdc.net;

public class HubConnectionAdapter implements HubConnectionListener {
    public void connected(HubConnection connection) {}
    public void disconnected(HubConnection connection, String disconnectMessage) {}
    public void hubInfoChanged(HubConnection connection, Hub hubInfo) {}
    public void receivedSearch(HubConnection connection, Command searchCommand) {}
    public void hubMessage(HubConnection connection, Command message) {}
    public void privateChatMessage(HubConnection connection, Command message) {}
    public void userArrived(HubConnection connection, User newUser) {}
    public void userDisconnected(HubConnection connection, User disconnectedUser) {}
    public void forceMove(HubConnection connection, Command moveCommand) {}
    public void searchResult(HubConnection connection, Command result) {}
    public void connectToMe(HubConnection connection, Command command) {}
    public void reverseConnectToMe(HubConnection connection, Command command) {}
    public void passwordRequired(HubConnection connection) {}
}