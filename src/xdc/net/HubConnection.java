package xdc.net;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.Socket;
import java.util.*;

public class HubConnection extends Thread {
    private Logger logger = Logger.getLogger(HubConnection.class);

    private User remoteUser;
    private Hub hub;
    private Socket socket;
    private CommandReader reader;
    private CommandWriter writer;
    private List listeners;
    private Map usersByNick;
    private Set operatorUserNicks;
    private boolean disconnected;

    public HubConnection(User remoteUser, Hub hub) {
        this.remoteUser = remoteUser;
        this.hub = hub;
        this.listeners = new ArrayList();
        this.usersByNick = new HashMap();
        this.operatorUserNicks = new HashSet();
    }

    public Hub getHub() {
        return hub;
    }

    public void addListener(HubConnectionListener listener) {
        this.listeners.add(listener);
    }

    public void connect() {
        this.start();
    }

    public void disconnect() {
        disconnect("Disconnected");
    }

    public void sendHubChatMessage(String message) {
        try {
            sendCommand(Command.createHubMessage(remoteUser, message));
        } catch (IOException e) {
            disconnect(e.toString());
        }
    }

    public void sendPrivateChatMessage(User toUser, String message) {
        try {
            sendCommand(Command.createPrivateChatMessage(remoteUser, toUser, message));
        } catch (IOException e) {
            disconnect(e.toString());
        }
    }

    public void search(String searchText) {
        try {
            Search search = new Search(remoteUser.getNick(),
                                       false,
                                       false,
                                       0,
                                       1,
                                       Arrays.asList(new String[] { searchText }));
            sendCommand(Command.createSearchCommand(search));
        } catch (IOException e) {
            disconnect(e.toString());
        }
    }

    public void run() {
        try {
            logger.debug("Connecting to " + hub.getHost() + ":" + hub.getPort());
            socket = new Socket(hub.getHost(), hub.getPort());
            socket.setTcpNoDelay(true);
            reader = new CommandReader(socket.getInputStream());
            writer = new CommandWriter(socket.getOutputStream());
            logger.debug("Connected to " + hub.getHost() + ":" + hub.getPort());
            fireConnected();
            Command command;
            while ((command = reader.readCommand()) != null) {
                processCommand(command);
            }

        } catch (Exception e) {
            if (!disconnected) {
                logger.error("Connection error", e);
                disconnect(e.toString());
            }
        }
    }

    private void disconnect(String message) {
        this.disconnected = true;
        try {
            if (socket != null) {
                socket.close();
            }
        } catch (Exception e) {
            logger.error("Disconnect error", e);
        }
        fireDisconnected(message);
    }

    private void processCommand(Command command) throws IOException {
        if (command.isHubMessage()) {
            fireHubMessage(command.getArgs());

        } else if (command.isPrivateChatCommand()) {
            firePrivateChatMessage(command);

        } else if (command.isHubNameCommand()) {
            hub.setName(command.getArgs());
            fireHubInfoChanged();

        } else if (command.isLockCommand()) {
            String lock = command.getArgs().substring(0, command.getArgs().toUpperCase().indexOf(" PK="));
            String key = ValidationKey.getValidationKeyFromLock(lock);
            sendCommand(Command.createKeyCommand(key));
            sendCommand(Command.createValidateNickCommand(remoteUser.getNick()));

        } else if (command.isSearchCommand()) {
            fireSearchReceived(new Search(command));

        } else if (command.isHelloCommand()) {
            if (command.getArgs().equals(remoteUser.getNick())) {
                sendCommand(Command.createVersionCommand("1.2"));
                sendCommand(Command.createGetNickListCommand());
                sendCommand(Command.createMyInfoCommand(remoteUser));
            }

        } else if (command.isNickListCommand()) {
            StringTokenizer st = new StringTokenizer(command.getArgs(), "$$");
            while (st.hasMoreTokens()) {
                String nick = st.nextToken();
                User user = createAndGetUserByNick(nick);
                sendCommand(Command.createGetInfoCommand(remoteUser, user));
            }

        } else if (command.isOpListCommand()) {
            StringTokenizer st = new StringTokenizer(command.getArgs(), "$$");
            while (st.hasMoreTokens()) {
                String nick = st.nextToken();
                User op = createAndGetUserByNick(nick);
                op.setOperator(true);
                operatorUserNicks.add(nick);
                fireUserModified(op);
            }

        } else if (command.isMyInfoCommand()) {
            User decodedUser = decodeUser(command);
            User hubUser = createAndGetUserByNick(decodedUser.getNick());
            hubUser.setDescription(decodedUser.getDescription());
            hubUser.setEmail(decodedUser.getEmail());
            hubUser.setSharedSize(decodedUser.getSharedSize());
            hubUser.setSpeed(decodedUser.getSpeed());
            hubUser.setSpeedCode(decodedUser.getSpeedCode());
            fireUserModified(hubUser);

        } else if (command.isForceMoveCommand()) {
            fireForceMove(command);

        } else if (command.isSearchResultCommand()) {
            try {
                fireSearchResult(new SearchResult(command));
            } catch (Exception e) {
                logger.warn("Discarded unparsable search result: " + command, e);
            }

        } else if (command.isConnectToMeCommand()) {
            fireConnectToMe(command);

        } else if (command.isReverseConnectToMeCommand()) {
            fireReverseConnectToMe(command);

        } else if (command.isPasswordRequiredCommand()) {
            firePasswordRequired();

        } else if (command.isQuitCommand()) {
            User disconnectedUser = (User) usersByNick.get(command.getArgs());
            if (disconnectedUser != null) {
                operatorUserNicks.remove(disconnectedUser.getNick());
                removeHubUser(disconnectedUser);
                fireUserDisconnected(disconnectedUser);
            }

        } else {
            logger.warn("Got unhandled command: '" + command + "'");
        }
    }

    private void removeHubUser(User user) {
        usersByNick.remove(user.getNick());
    }

    private User createAndGetUserByNick(String nick) {
        User user = (User) usersByNick.get(nick);
        if (user == null) {
            user = new User(nick);
            user.setOperator(operatorUserNicks.contains(user.getNick()));
            usersByNick.put(nick, user);
            fireUserArrived(user);
        }
        return user;
    }

    private User decodeUser(Command command) {
        String userInfo = command.getArgs();

        /* Strip "$ALL " away */
        userInfo = userInfo.substring(5, userInfo.length());

        StringTokenizer st = new StringTokenizer(userInfo, "$", true);
        String nickAndDesc = st.nextToken();
        st.nextToken(); /* Skip delimiter */
        int i = nickAndDesc.indexOf(" ");
        User user = new User(nickAndDesc.substring(0, i));
        user.setDescription(nickAndDesc.substring(i + 1));
        st.nextToken(); /* unknown info. */
        st.nextToken(); /* Skip delimiter */

        String rawSpeed = st.nextToken();
        st.nextToken(); /* Skip delimiter */
        user.setSpeed(rawSpeed.substring(0, rawSpeed.length() - 1));

        char c = rawSpeed.charAt(rawSpeed.length() - 1);
        user.setSpeedCode(c);

        if (st.countTokens() > 3) {
            user.setEmail(st.nextToken());
        }
        st.nextToken(); /* Skip delimiter */

        try {
            if (st.countTokens() > 1) {
                user.setSharedSize(Long.parseLong(st.nextToken()));
            }
        } catch (NumberFormatException e) {
            user.setSharedSize(0);
        }
        return user;
    }

    private void sendCommand(Command command) throws IOException {
        writer.writeCommand(command);
    }

    private void fireHubInfoChanged() {
        for (Iterator i = listeners.iterator(); i.hasNext();) {
            HubConnectionListener listener = (HubConnectionListener) i.next();
            listener.hubInfoChanged(this, getHub());
        }
    }

    private void fireSearchReceived(Search search) {
        for (Iterator i = listeners.iterator(); i.hasNext();) {
            HubConnectionListener listener = (HubConnectionListener) i.next();
            listener.receivedSearch(this, search);
        }
    }

    private void fireHubMessage(String message) {
        for (Iterator i = listeners.iterator(); i.hasNext();) {
            HubConnectionListener listener = (HubConnectionListener) i.next();
            listener.hubMessage(this, message);
        }
    }

    private void firePrivateChatMessage(Command command) {
        String args = command.getArgs();
        String FROM = "From: ";
        int authorStart = args.indexOf(FROM);
        if (authorStart  != -1) {
            authorStart += FROM.length();
            int authorEnd = args.indexOf("$", authorStart);
            String authorNick = args.substring(authorStart, authorEnd).trim();
            String message = args.substring(authorEnd + 1);
            User author = createAndGetUserByNick(authorNick);
            for (Iterator i = listeners.iterator(); i.hasNext();) {
                HubConnectionListener listener = (HubConnectionListener) i.next();
                listener.privateChatMessage(this, author, message);
            }
        }
    }

    private void fireUserArrived(User newUser) {
        for (Iterator i = listeners.iterator(); i.hasNext();) {
            HubConnectionListener listener = (HubConnectionListener) i.next();
            listener.userArrived(this, newUser);
        }
    }

    private void fireUserModified(User user) {
        for (Iterator i = listeners.iterator(); i.hasNext();) {
            HubConnectionListener listener = (HubConnectionListener) i.next();
            listener.userModified(this, user);
        }
    }

    private void fireUserDisconnected(User newUser) {
        for (Iterator i = listeners.iterator(); i.hasNext();) {
            HubConnectionListener listener = (HubConnectionListener) i.next();
            listener.userDisconnected(this, newUser);
        }
    }

    private void fireForceMove(Command moveCommand) {
        for (Iterator i = listeners.iterator(); i.hasNext();) {
            HubConnectionListener listener = (HubConnectionListener) i.next();
            listener.forceMove(this, moveCommand);
        }
    }

    private void fireSearchResult(SearchResult result) {
        for (Iterator i = listeners.iterator(); i.hasNext();) {
            HubConnectionListener listener = (HubConnectionListener) i.next();
            listener.searchResult(this, result);
        }
    }

    private void fireConnectToMe(Command command) {
        for (Iterator i = listeners.iterator(); i.hasNext();) {
            HubConnectionListener listener = (HubConnectionListener) i.next();
            listener.connectToMe(this, command);
        }
    }

    private void fireReverseConnectToMe(Command command) {
        for (Iterator i = listeners.iterator(); i.hasNext();) {
            HubConnectionListener listener = (HubConnectionListener) i.next();
            listener.reverseConnectToMe(this, command);
        }
    }

    private void firePasswordRequired() {
        for (Iterator i = listeners.iterator(); i.hasNext();) {
            HubConnectionListener listener = (HubConnectionListener) i.next();
            listener.passwordRequired(this);
        }
    }

    private void fireConnected() {
        for (Iterator i = listeners.iterator(); i.hasNext();) {
            HubConnectionListener listener = (HubConnectionListener) i.next();
            listener.connected(this);
        }
    }

    private void fireDisconnected(String message) {
        for (Iterator i = listeners.iterator(); i.hasNext();) {
            HubConnectionListener listener = (HubConnectionListener) i.next();
            listener.disconnected(this, message);
        }
    }

    public String toString() {
        return getHub().getName();
    }
}
