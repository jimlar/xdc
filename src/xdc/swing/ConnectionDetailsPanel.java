package xdc.swing;

import xdc.net.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class ConnectionDetailsPanel extends JPanel {
    private DefaultListModel userListModel;
    private JTextArea hubMessagesTextArea;

    public ConnectionDetailsPanel(final HubConnection con) {
        super(new BorderLayout());

        userListModel = new DefaultListModel();
        JList userList = new JList(userListModel);


        JPanel hubMessagesPanel = new JPanel(new BorderLayout());
        hubMessagesTextArea = new JTextArea();
        hubMessagesPanel.add(new JScrollPane(hubMessagesTextArea), BorderLayout.CENTER);

        final JTextField chatInput = new JTextField();
        hubMessagesPanel.add(chatInput, BorderLayout.SOUTH);
        chatInput.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                con.sendHubChatMessage(chatInput.getText());
                chatInput.setText("");
            }
        });

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                                              true,
                                              hubMessagesPanel,
                                              new JScrollPane(userList));

        splitPane.setDividerLocation(350);
        this.add(splitPane, BorderLayout.CENTER);
        this.add(new StatusBar(con), BorderLayout.SOUTH);

        con.addListener(new HubConnectionAdapter() {
            public void hubMessage(HubConnection connection, Command message) {
                hubMessagesTextArea.append(message + "\n");
            }
            public void privateChatMessage(HubConnection connection, Command message) {
                hubMessagesTextArea.append("Private message: " + message + "\n");
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
