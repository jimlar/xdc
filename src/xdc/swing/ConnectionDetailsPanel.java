package xdc.swing;

import xdc.net.*;

import javax.swing.*;
import java.awt.*;

public class ConnectionDetailsPanel extends JPanel {
    private DefaultListModel userListModel;

    public ConnectionDetailsPanel(HubConnection con) {
        super(new BorderLayout());

        userListModel = new DefaultListModel();
        JList userList = new JList(userListModel);

        final JTextArea textArea = new JTextArea();
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                                              true,
                                              new JScrollPane(textArea),
                                              new JScrollPane(userList));

        splitPane.setDividerLocation(350);
        this.add(splitPane, BorderLayout.CENTER);
        this.add(new StatusBar(con), BorderLayout.SOUTH);

        con.addListener(new HubConnectionAdapter() {
            public void hubMessage(HubConnection connection, Command message) {
                textArea.append(message + "\n");
            }
            public void privateChatMessage(HubConnection connection, Command message) {
                textArea.append("Private message: " + message + "\n");
            }
            public void userArrived(HubConnection connection, User newUser) {
                addUser(newUser);
            }
            public void userDisconnected(HubConnection connection, User disconnectedUser) {
                removeUser(disconnectedUser);
            }
        });
    }

    private void removeUser(final User disconnectedUser) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                userListModel.removeElement(disconnectedUser.getNick());
            }
        });
    }

    private void addUser(final User newUser) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                userListModel.addElement(newUser.getNick());
            }
        });
    }

}
