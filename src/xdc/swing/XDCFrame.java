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
    private JTabbedPane tabbedPane;
    private JPanel connectedHubsPanel;
    private User remoteUser;

    public XDCFrame() {
        super("XDC");

        remoteUser = new User("[SWE]Jim", 40L * 1024 * 1024 * 1024);
        Container contentPane = getContentPane();
        tabbedPane = new JTabbedPane();
        contentPane.setLayout(new BorderLayout());
        contentPane.add(tabbedPane, BorderLayout.CENTER);

        hubListTable = new JTable();
        hubListTableModel = new HubsTableModel();
        hubListTable.setModel(hubListTableModel);

        JPanel hubListPanel = new JPanel(new BorderLayout());
        hubListPanel.add(getConnectPanel(), BorderLayout.NORTH);
        hubListPanel.add(new JScrollPane(hubListTable), BorderLayout.CENTER);

        tabbedPane.add("Hubs", hubListPanel);

        connectedHubsPanel = new JPanel(new BorderLayout());
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

        tabbedPane.add("Connections", connectedHubsPanel);

        setDefaultCloseOperation(EXIT_ON_CLOSE);

        setSize(500, 500);
    }

    private void selectConnectionsTab() {
        tabbedPane.setSelectedComponent(connectedHubsPanel);
    }

    private void connect(Hub hub) {
        HubConnection connection = new HubConnection(remoteUser, hub);
        hubDetailsPanel.add(new ConnectionDetailsPanel(connection), hub.getHost());
        connectionsComboBoxModel.addElement(connection);
        connectionsComboBoxModel.setSelectedItem(connection);
        selectConnectionsTab();
        connection.connect();
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
