package xdc.net;

import org.apache.log4j.Logger;

import java.net.URL;
import java.net.HttpURLConnection;
import java.util.*;
import java.io.*;

public class HubList {
    private static final String DCPP_HUBLIST_URL = "http://dcplusplus.sourceforge.net/PublicHubList.config";
    private static final String PUBLIC_HUBLIST_URL = "http://www.neo-modus.com/PublicHubList.config";

    private Logger logger = Logger.getLogger(HubList.class);
    private List hubs;

    public HubList() throws IOException {
        refresh();
    }

    public List getHubs() {
        return hubs;
    }

    public void refresh() throws IOException {
        this.hubs = new ArrayList();

        loadHubList(new URL(PUBLIC_HUBLIST_URL));
        loadHubList(new URL(DCPP_HUBLIST_URL));

        logger.debug("Found " + hubs.size() + " hubs");
    }

    private void loadHubList(URL url) throws IOException {
        logger.debug("Retrieving hublist from " + url);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

        String line;
        while ((line = reader.readLine()) != null) {
            StringTokenizer st = new StringTokenizer(line, "|");
            if (st.countTokens() > 3) {
                String name = st.nextToken();
                String host = st.nextToken().trim();
                String description = st.nextToken();
                int users = 0;
                try {
                    users = Integer.parseInt(st.nextToken());
                } catch (NumberFormatException e) {}

                Hub hub = new Hub(name, host, description, users);
                hubs.add(hub);
            }
        }
        reader.close();
        connection.disconnect();
    }
}
