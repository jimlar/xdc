package xdc.swing;

import xdc.net.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class XDCFrame extends JFrame {
    private JTable hubListTable;
    private HubsTableModel hubListTableModel;
    private DefaultComboBoxModel connectionsComboBoxModel;
    private JPanel hubDetailsPanel;

    public XDCFrame() {
        super("XDC");

        Container contentPane = getContentPane();
        JTabbedPane tabbedPane = new JTabbedPane();
        contentPane.add(tabbedPane);

        hubListTable = new JTable();
        hubListTableModel = new HubsTableModel();
        hubListTable.setModel(hubListTableModel);

        JPanel hubListPanel = new JPanel(new BorderLayout());
        hubListPanel.add(getConnectPanel(), BorderLayout.NORTH);
        hubListPanel.add(new JScrollPane(hubListTable), BorderLayout.CENTER);

        tabbedPane.add("Public Hubs", hubListPanel);

        JPanel connectedHubsPanel = new JPanel(new BorderLayout());
        final CardLayout connectedHubsCardLayout = new CardLayout();
        hubDetailsPanel = new JPanel(connectedHubsCardLayout);
        connectedHubsPanel.add(hubDetailsPanel, BorderLayout.CENTER);
        connectionsComboBoxModel = new DefaultComboBoxModel();
        JComboBox connectionsComboBox = new JComboBox(connectionsComboBoxModel);
        connectedHubsPanel.add(connectionsComboBox, BorderLayout.NORTH);
        connectionsComboBox.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                HubConnection con = (HubConnection) connectionsComboBoxModel.getSelectedItem();
                connectedHubsCardLayout.show(hubDetailsPanel,
                                             con.getHub().getHost());
            }
        });

        tabbedPane.add("Connected Hubs", connectedHubsPanel);

        pack();
    }

    private void connect(Hub hub) {
        HubConnection connection = new HubConnection(getClientUser(), hub);
        hubDetailsPanel.add(createHubDetailsPanel(connection), hub.getHost());
        connectionsComboBoxModel.addElement(connection);
        connectionsComboBoxModel.setSelectedItem(connection);
    }

    private Component createHubDetailsPanel(HubConnection con) {
        JPanel details = new JPanel(new BorderLayout());
        final JTextArea textArea = new JTextArea();
        details.add(new JScrollPane(textArea), BorderLayout.CENTER);
        con.addListener(new HubConnectionListener() {
            public void hubInfoChanged(HubConnection connection, Hub hubInfo) {
            }
            public void receivedSearch(HubConnection connection, Command searchCommand) {
                textArea.append("Search: " + searchCommand + "\n");
            }
            public void hubMessage(HubConnection connection, Command message) {
                textArea.append("Hub message: " + message + "\n");
            }
            public void privateChatMessage(HubConnection connection, Command message) {
                textArea.append("Private message: " + message + "\n");
            }
            public void userArrived(HubConnection connection, User newUser) {
                textArea.append("User arrived: " + newUser + "\n");
            }
            public void userDisconnected(HubConnection connection, User disconnectedUser) {
                textArea.append("User disconnected: " + disconnectedUser + "\n");
            }
            public void forceMove(HubConnection connection, Command moveCommand) {
                textArea.append("Force move: " + moveCommand + "\n");
            }
            public void searchResult(HubConnection connection, Command result) {
                textArea.append("Search result: " + result + "\n");
            }
            public void connectToMe(HubConnection connection, Command command) {
                textArea.append("Connect to me: " + command + "\n");
            }
            public void reverseConnectToMe(HubConnection connection, Command command) {
                textArea.append("Reverse connect to me: " + command + "\n");
            }
            public void passwordRequired(HubConnection connection) {
                textArea.append("Password required\n");
            }
        });
        con.connect();
        return details;
    }

    private User getClientUser() {
        User remote = new User("[SWE]Jim");
        remote.setSharedSize(40 * 1024 * 1024 * 1024);
        return remote;
    }

    private JPanel getConnectPanel() {
        JPanel connectPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton connectButton = new JButton(new AbstractAction("Connect") {
            public void actionPerformed(ActionEvent e) {
                Hub hub = hubListTableModel.getHub(hubListTable.getSelectedRow());
                if (hub != null) {
                    connect(hub);
                }
            }
        });
        connectPanel.add(connectButton);
        JButton refreshButton = new JButton(new AbstractAction("Refresh") {
            public void actionPerformed(ActionEvent e) {
                hubListTableModel.startReload();
            }
        });
        connectPanel.add(refreshButton);
        return connectPanel;
    }
}
