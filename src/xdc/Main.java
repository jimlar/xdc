package xdc;

import xdc.net.*;

import java.util.Iterator;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws Exception {
        PublicHubList list = new PublicHubList();
        User remote = new User("[BoStream]Jim");
        for (Iterator i = list.getHubs().iterator(); i.hasNext();) {
            Hub hub = (Hub) i.next();
            try {
                HubConnection connection = new HubConnection(remote, hub);
                connection.addListener(new HubConnectionListener() {
                    public void hubInfoChanged(HubConnection con, Hub hubInfo) {
                        System.out.println("New hub info: " + hubInfo);
                    }
                    public void receivedSearch(HubConnection con, Command searchCommand) {
                        System.out.println("Search: " + searchCommand);
                    }
                    public void hubMessage(HubConnection con, Command message) {
                        System.out.println("Hubmessage: " + message);
                    }
                    public void privateChatMessage(HubConnection con, Command message) {
                        System.out.println("Private message: " + message);
                    }
                    public void userArrived(HubConnection con, User newUser) {
                        System.out.println("User arrived: " + newUser);
                    }
                });
                connection.connect();
                return;
            } catch (IOException e) {
            }
        }
    }
}
