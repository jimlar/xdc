package xdc.net;

import java.io.IOException;
import java.net.Socket;
import java.util.*;

public class HubConnection extends Thread {
    private User user;
    private Hub hub;
    private Socket socket;
    private CommandReader reader;
    private CommandWriter writer;
    private List listeners;


    public HubConnection(User user, Hub hub) throws IOException {
        this.user = user;
        this.hub = hub;
        this.listeners = new ArrayList();
    }

    public User getUser() {
        return user;
    }

    public Hub getHub() {
        return hub;
    }

    public void addListener(HubConnectionListener listener) {
        this.listeners.add(listener);
    }

    public void connect() throws IOException {
        socket = new Socket(hub.getHost(), hub.getPort());
        socket.setTcpNoDelay(true);
        reader = new CommandReader(socket.getInputStream());
        writer = new CommandWriter(socket.getOutputStream());
        this.start();
    }

    public void run() {
        try {
            Command command;
            while ((command = reader.readCommand()) != null) {
                processCommand(command);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void processCommand(Command command) throws IOException {
        if (command.isHubMessage()) {
            fireHubMessage(command);

        } else if (command.isPrivateChatCommand()) {
            firePrivateChatMessage(command);

        } else if (command.isHubNameCommand()) {
            hub.setName(command.getArgs());
            fireHubInfoChanged();

        } else if (command.isLockCommand()) {
            String lock = command.getArgs().substring(0, command.getArgs().toUpperCase().indexOf(" PK="));
            String key = ValidationKey.getValidationKeyFromLock(lock);
            sendCommand(Command.createKeyCommand(key));
            sendCommand(Command.createValidateNickCommand(user.getNick()));

        } else if (command.isSearchCommand()) {
            fireSearchReceived(command);

        } else if (command.isHelloCommand()) {
            if (command.getArgs().equals(user.getNick())) {
                sendCommand(Command.createVersionCommand("1.2"));
                sendCommand(Command.createGetNickListCommand());
                sendCommand(Command.createMyInfoCommand(user));
            }

        } else if (command.isNickListCommand()) {
            StringTokenizer st = new StringTokenizer(command.getArgs(), "$$");
            while (st.hasMoreTokens()) {
                String nick = st.nextToken();
                sendCommand(Command.createGetInfoCommand(user, nick));
            }

        } else if (command.isMyInfoCommand()) {
            User newUser = decodeUser(command);
            fireUserArrived(newUser);

        } else {
            System.out.println("Got unhandled command: '" + command.getCommand() + "', args='" + command.getArgs() + "'");
        }
    }

    private User decodeUser(Command command) {
        String userInfo = command.getArgs();

        /* Strip "$ALL " away */
        userInfo = userInfo.substring(5, userInfo.length());

        StringTokenizer st = new StringTokenizer(userInfo, "$");
        String nickAndDesc = st.nextToken();
        int i = nickAndDesc.indexOf(" ");
        User user = new User(nickAndDesc.substring(0, i));
        user.setDescription(nickAndDesc.substring(i + 1));
        st.nextToken(); // unknown info.

        String rawSpeed = st.nextToken();
        user.setSpeed(rawSpeed.substring(0, rawSpeed.length() - 1));

        char c = rawSpeed.charAt(rawSpeed.length() - 1);
        user.setSpeedCode(c);

        if (st.countTokens() > 1) {
            user.setEmail(st.nextToken());
        }

        user.setSharedSize(Long.parseLong(st.nextToken()));
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

    private void fireSearchReceived(Command command) {
        for (Iterator i = listeners.iterator(); i.hasNext();) {
            HubConnectionListener listener = (HubConnectionListener) i.next();
            listener.receivedSearch(this, command);
        }
    }

    private void fireHubMessage(Command command) {
        for (Iterator i = listeners.iterator(); i.hasNext();) {
            HubConnectionListener listener = (HubConnectionListener) i.next();
            listener.hubMessage(this, command);
        }
    }

    private void firePrivateChatMessage(Command command) {
        for (Iterator i = listeners.iterator(); i.hasNext();) {
            HubConnectionListener listener = (HubConnectionListener) i.next();
            listener.privateChatMessage(this, command);
        }
    }

    private void fireUserArrived(User newUser) {
        for (Iterator i = listeners.iterator(); i.hasNext();) {
            HubConnectionListener listener = (HubConnectionListener) i.next();
            listener.userArrived(this, newUser);
        }
    }
}
