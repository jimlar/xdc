package xdc.net;

public interface HubConnectionListener {

    void hubInfoChanged(HubConnection connection, Hub hubInfo);

    void receivedSearch(HubConnection connection, Command searchCommand);

    void hubMessage(HubConnection connection, Command message);

    void privateChatMessage(HubConnection connection, Command message);

    void userArrived(HubConnection connection, User newUser);
}
