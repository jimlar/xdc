package xdc.swing;

import xdc.net.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class XDCFrame extends JFrame {
    private JTable hubListTable;
    private HubsTableModel hubListTableModel;
    private JTabbedPane tabbedPane;
    private User remoteUser;

    private ConnectionsPanel connectionsPanel;

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

        connectionsPanel = new ConnectionsPanel();

        setDefaultCloseOperation(EXIT_ON_CLOSE);

        setSize(500, 500);
    }

    private void selectConnectionsTab() {
        tabbedPane.setSelectedComponent(connectionsPanel);
    }

    private void connect(Hub hub) {
        HubConnection connection = new HubConnection(remoteUser, hub);
        connectionsPanel.addConnection(connection);
        boolean isAdded = false;
        Component[] components = tabbedPane.getComponents();
        if (components != null) {
            for (int i = 0; i < components.length; i++) {
                if (connectionsPanel.equals(components[i])) {
                    isAdded = true;
                }
            }
        }

        if (!isAdded) {
            tabbedPane.add("Connections", connectionsPanel);
        }
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
