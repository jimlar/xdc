package xdc.net;

import java.util.*;

public class Search {
    /**
     * If it is an active search then host and port are used as an UDP address to
     * send the replies to.
     * Else the nick and hub is used for the replies.
     */
    private boolean activeSearch;


    private String host;
    private int port;

    private String nick;
    private boolean sizeMatters;
    private boolean sizeAtLeast;
    private long size;

    /** 1=any,2=audio,3=compressed,4=document,5=exe,6=picture,7=videos,8=folder */
    private int type;
    private List searchWords;

    Search(Command command) {
        if (!command.isSearchCommand()) {
            throw new IllegalArgumentException("Command is not a search command: " + command);
        }
        /* Command args is a string like "a?b?c?d?eeeee" where
        /* a is F is size doesn't matter, else T */
        /* b is F if size is "at least", else T (at most) */
        /* c is the size in byte */
        /* d is data type:
        /* and eeee is the pattern to find (words separated by $)*/

        String data = command.getArgs();

        int i = data.indexOf(" ");
        host = data.substring(0, i);
        data = data.substring(i + 1);
        if (host.startsWith("Hub:")) {
            nick = host.substring("Hub:".length() + 1);
        } else {
            i = host.indexOf(":");
            port = Integer.parseInt(host.substring(i + 1));
            host = host.substring(0, i);
        }

        sizeMatters = data.substring(0, 1).equalsIgnoreCase("f");
        data = data.substring(2);
        sizeAtLeast = data.substring(0, 1).equalsIgnoreCase("f");
        data = data.substring(2);

        i = data.indexOf("?");
        size = Long.parseLong(data.substring(0, i));
        data = data.substring(i + 1);

        i = data.indexOf("?");
        type = Integer.parseInt(data.substring(0, i));
        data = data.substring(i + 1);

        searchWords = new ArrayList();
        StringTokenizer st = new StringTokenizer(data, "$");
        while (st.hasMoreTokens()) {
            searchWords.add(st.nextToken());
        }
    }

    public Search(String nick, boolean sizeMatters, boolean sizeAtLeast, long size, int type, List searchWords) {
        this(false, null, -1, nick, sizeMatters, sizeAtLeast, size, type, searchWords);
    }

    public Search(String host, int port, boolean sizeMatters, boolean sizeAtLeast, long size, int type, List searchWords) {
        this(true, host, port, null, sizeMatters, sizeAtLeast, size, type, searchWords);
    }

    private Search(boolean activeSearch, String host, int port, String nick, boolean sizeMatters, boolean sizeAtLeast, long size, int type, List searchWords) {
        this.activeSearch = activeSearch;
        this.host = host;
        this.port = port;
        this.nick = nick;
        this.sizeMatters = sizeMatters;
        this.sizeAtLeast = sizeAtLeast;
        this.size = size;
        this.type = type;
        this.searchWords = searchWords;
    }

    public boolean isActiveSearch() {
        return activeSearch;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public String getNick() {
        return nick;
    }

    public boolean isSizeMatters() {
        return sizeMatters;
    }

    public boolean isSizeAtLeast() {
        return sizeAtLeast;
    }

    public long getSize() {
        return size;
    }

    /** 1=any,2=audio,3=compressed,4=document,5=exe,6=picture,7=videos,8=folder */
    public int getType() {
        return type;
    }

    public List getSearchWords() {
        return searchWords;
    }

    public String toString() {
        /* Command args is a string like "a?b?c?d?eeeee" where
        /* a is F is size doesn't matter, else T */
        /* b is F if size is "at least", else T (at most) */
        /* c is the size in byte */
        /* d is data type:
        /* and eeee is the pattern to find (words separated by $)*/
        String result = "";
        if (isActiveSearch()) {
            result += host + ":" + port;
        } else {
            result += "Hub:" + nick;
        }
        result += " ";

        result += (sizeMatters ? "T" : "F") + "?";
        result += (sizeAtLeast ? "T" : "F") + "?";
        result += size + "?";
        result += type + "?";

        for (Iterator i = searchWords.iterator(); i.hasNext();) {
            String word = (String) i.next();
            result += word;
            if (i.hasNext()) {
                result += "$";
            }
        }
        return result;
    }
}
