package xdc;

import xdc.net.*;

import java.util.Iterator;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws Exception {
        PublicHubList list = new PublicHubList();

        for (Iterator i = list.getHubs().iterator(); i.hasNext();) {
            Hub hub = (Hub) i.next();
            try {
                HubConnection connection = new HubConnection(hub);
                return;
            } catch (IOException e) {
            }
        }
    }
}
