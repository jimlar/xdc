package xdc.swing;

import xdc.net.*;

import javax.swing.*;
import java.awt.*;

public class ConnectionDetailsPanel extends JPanel {
    private HubConnection connection;
    private DefaultListModel userListModel;
    private MessageSessionPanel hubMessageSessionPanel;
    private JTabbedPane tabbedPane;

    public ConnectionDetailsPanel(final HubConnection connection) {
        super(new BorderLayout());
        this.connection = connection;
        userListModel = new DefaultListModel();
        JList userList = new JList(userListModel);

        tabbedPane = new JTabbedPane(JTabbedPane.BOTTOM);
        this.hubMessageSessionPanel = new MessageSessionPanel(connection);
        tabbedPane.addTab("Messages", hubMessageSessionPanel);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                                              true,
                                              tabbedPane,
                                              new JScrollPane(userList));

        splitPane.setDividerLocation(350);
        this.add(splitPane, BorderLayout.CENTER);
        this.add(new StatusBar(connection), BorderLayout.SOUTH);

        connection.addListener(new HubConnectionAdapter() {
            public void hubMessage(HubConnection con, String message) {
                hubMessageSessionPanel.addMessage(message);
            }

            public void privateChatMessage(HubConnection con, User from, String message) {
                MessageSessionPanel sessionPanel = createAndGetMessageSession(from);
                sessionPanel.addMessage(message);
            }

            public void userArrived(HubConnection con, User newUser) {
                addUser(newUser);
            }

            public void userDisconnected(HubConnection con, User disconnectedUser) {
                removeUser(disconnectedUser);
            }
        });
    }

    private MessageSessionPanel createAndGetMessageSession(User from) {
        for (int i = 0; i < tabbedPane.getTabCount(); i++) {
            String title = tabbedPane.getTitleAt(i);
            Component component = tabbedPane.getComponentAt(i);
            if (title.equals(from.getNick()) && component instanceof MessageSessionPanel) {
                return (MessageSessionPanel) component;
            }
        }

        MessageSessionPanel sessionPanel = new MessageSessionPanel(connection, from);
        tabbedPane.addTab(from.getNick(), sessionPanel);
        super.validate();
        return sessionPanel;
    }

    public HubConnection getConnection() {
        return connection;
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
