package xdc.swing;

import xdc.net.HubConnection;
import xdc.net.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class MessageSessionPanel extends JPanel {
    private User withUser;
    private JTextArea messagesTextArea;

    public MessageSessionPanel(HubConnection connection) {
        this(connection, null);
    }

    public MessageSessionPanel(final HubConnection connection, final User withUser) {
        super(new BorderLayout());
        this.withUser = withUser;

        messagesTextArea = new JTextArea();
        add(new JScrollPane(messagesTextArea), BorderLayout.CENTER);

        final JTextField chatInput = new JTextField();
        add(chatInput, BorderLayout.SOUTH);
        chatInput.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (!isPrivateSession()) {
                    connection.sendHubChatMessage(chatInput.getText());
                } else {
                    connection.sendPrivateChatMessage(withUser, chatInput.getText());

                }
                chatInput.setText("");
            }
        });
    }

    public void addMessage(String message) {
        messagesTextArea.append(message + "\n");
    }

    private boolean isPrivateSession() {
        return withUser != null;
    }
}
