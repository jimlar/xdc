package xdc.swing;

import xdc.net.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class ConnectionDetailsPanel extends JPanel {
    private HubConnection connection;
    private DefaultListModel userListModel;
    private JTextArea hubMessagesTextArea;

    public ConnectionDetailsPanel(final HubConnection connection) {
        super(new BorderLayout());
        this.connection = connection;
        userListModel = new DefaultListModel();
        JList userList = new JList(userListModel);


        JPanel hubMessagesPanel = new JPanel(new BorderLayout());
        hubMessagesTextArea = new JTextArea();
        hubMessagesPanel.add(new JScrollPane(hubMessagesTextArea), BorderLayout.CENTER);

        final JTextField chatInput = new JTextField();
        hubMessagesPanel.add(chatInput, BorderLayout.SOUTH);
        chatInput.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                connection.sendHubChatMessage(chatInput.getText());
                chatInput.setText("");
            }
        });

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                                              true,
                                              hubMessagesPanel,
                                              new JScrollPane(userList));

        splitPane.setDividerLocation(350);
        this.add(splitPane, BorderLayout.CENTER);
        this.add(new StatusBar(connection), BorderLayout.SOUTH);

        connection.addListener(new HubConnectionAdapter() {
            public void hubMessage(HubConnection con, Command message) {
                hubMessagesTextArea.append(message + "\n");
            }
            public void privateChatMessage(HubConnection con, Command message) {
                hubMessagesTextArea.append("Private message: " + message + "\n");
            }
            public void userArrived(HubConnection con, User newUser) {
                addUser(newUser);
            }
            public void userDisconnected(HubConnection con, User disconnectedUser) {
                removeUser(disconnectedUser);
            }
        });
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
