package xdc.swing;

import xdc.net.HubConnection;
import xdc.net.HubConnectionAdapter;

import javax.swing.*;
import java.awt.*;

public class StatusBar extends JPanel {
    private JLabel statusLabel;

    public StatusBar(HubConnection connection) {
        setLayout(new BorderLayout(0, 0));
        setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));

        JPanel innerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 1, 1));
        add(innerPanel, BorderLayout.CENTER);
        innerPanel.setBorder(BorderFactory.createLineBorder(Color.black));

        statusLabel = new JLabel("Connecting...");
        statusLabel.setFont(statusLabel.getFont().deriveFont(Font.PLAIN, 9));
        innerPanel.add(statusLabel);

        connection.addListener(new HubConnectionAdapter() {
            public void connected(HubConnection con) {
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        statusLabel.setText("Connected");
                    }
                });
            }

            public void disconnected(HubConnection con, final String disconnectMessage) {
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        statusLabel.setText("Disconnected: " + disconnectMessage);
                    }
                });
            }
        });
    }
}
