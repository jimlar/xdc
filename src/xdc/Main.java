package xdc;

import xdc.net.*;

import java.util.*;
import java.io.IOException;

import org.apache.log4j.Logger;

public class Main {
    public static void main(String[] args) throws Exception {
        final Logger logger = Logger.getLogger(Main.class);

        PublicHubList list = new PublicHubList();
        User remote = new User("[SWE]Jim");
        remote.setSharedSize(40 * 1024 * 1024 * 1024);
        List hubs = list.getHubs();
        Collections.reverse(hubs);
        for (Iterator i = hubs.iterator(); i.hasNext();) {
            Hub hub = (Hub) i.next();
            try {
                HubConnection connection = new HubConnection(remote, hub);
                connection.addListener(new HubConnectionListener() {
                    public void hubInfoChanged(HubConnection con, Hub hubInfo) {
                        logger.debug("New hub info: " + hubInfo);
                    }
                    public void receivedSearch(HubConnection con, Command searchCommand) {
                        logger.debug("Search: " + searchCommand);
                    }
                    public void hubMessage(HubConnection con, Command message) {
                        logger.debug("Hubmessage: " + message);
                    }
                    public void privateChatMessage(HubConnection con, Command message) {
                        logger.debug("Private message: " + message);
                    }
                    public void userArrived(HubConnection con, User newUser) {
                        if (newUser.isOperator()) {
                            logger.debug("Operator arrived: " + newUser);
                        } else {
                            logger.debug("User arrived: " + newUser);
                        }
                    }
                    public void userDisconnected(HubConnection con, User disconnectedUser) {
                        logger.debug("User disconnected: " + disconnectedUser);
                    }
                    public void forceMove(HubConnection con, Command moveCommand) {
                        logger.debug("Force move: " + moveCommand);
                    }
                    public void searchResult(HubConnection con, Command result) {
                        logger.debug("Search result: " + result);
                    }
                    public void connectToMe(HubConnection con, Command command) {
                        logger.debug("Connect to me: " + command);
                    }
                    public void reverseConnectToMe(HubConnection con, Command command) {
                        logger.debug("Reverse connect to me: " + command);
                    }
                    public void passwordRequired(HubConnection con) {
                        logger.debug("Password required");
                    }
                });
                connection.connect();
                return;
            } catch (IOException e) {
            }
        }
    }
}
