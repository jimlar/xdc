package xdc.swing;

import xdc.net.HubConnection;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ConnectionsPanel extends JPanel {
    private DefaultComboBoxModel connectionsComboBoxModel;
    private JPanel hubDetailsPanel;
    private CardLayout connectedHubsCardLayout;
    private JButton disconnectButton;

    public ConnectionsPanel() {
        super(new BorderLayout());

        /* Card layout for details */
        connectedHubsCardLayout = new CardLayout();
        hubDetailsPanel = new JPanel(connectedHubsCardLayout);
        hubDetailsPanel.add(new JPanel(), "Dummy");
        add(hubDetailsPanel, BorderLayout.CENTER);

        /* Combobox */
        JPanel topPanel = new JPanel(new BorderLayout());
        connectionsComboBoxModel = new DefaultComboBoxModel();
        JComboBox connectionsComboBox = new JComboBox(connectionsComboBoxModel);
        topPanel.add(connectionsComboBox, BorderLayout.CENTER);
        connectionsComboBox.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                HubConnection con = getSelectedConnection();
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    showConnection(con);
                }
                disconnectButton.setEnabled(con != null);
            }
        });
        disconnectButton = new JButton(new AbstractAction("Disconnect") {
            public void actionPerformed(ActionEvent e) {
                HubConnection connection = getSelectedConnection();
                if (connection != null) {
                    connection.disconnect();
                    removeConnection(connection);
                }
            }
        });
        disconnectButton.setEnabled(false);
        topPanel.add(disconnectButton, BorderLayout.EAST);
        add(topPanel, BorderLayout.NORTH);
    }

    public void addConnection(HubConnection connection) {
        hubDetailsPanel.add(new ConnectionDetailsPanel(connection), connection.getHub().getHost());
        connectionsComboBoxModel.addElement(connection);
        connectionsComboBoxModel.setSelectedItem(connection);
    }

    private void removeConnection(HubConnection connection) {
        Component[] components = hubDetailsPanel.getComponents();
        if (components != null) {
            for (int i = 0; i < components.length; i++) {
                if (components[i] instanceof  ConnectionDetailsPanel) {
                    ConnectionDetailsPanel detailsPanel = (ConnectionDetailsPanel) components[i];
                    if (detailsPanel.getConnection().equals(connection)) {
                        hubDetailsPanel.remove(components[i]);
                    }
                }
            }
        }
        connectionsComboBoxModel.removeElement(connection);
        getParent().validate();
    }

    private void showConnection(HubConnection con) {
        connectedHubsCardLayout.show(hubDetailsPanel, con.getHub().getHost());
    }

    private HubConnection getSelectedConnection() {
        return (HubConnection) connectionsComboBoxModel.getSelectedItem();
    }
}
