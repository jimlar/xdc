package xdc.swing;

import xdc.net.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

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

        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        splitPane.setOneTouchExpandable(true);
        splitPane.setTopComponent(tabbedPane);
        splitPane.setBottomComponent(new DowloadsPanel());
        splitPane.setDividerLocation(400);

        contentPane.add(splitPane, BorderLayout.CENTER);

        hubListTable = new JTable();
        hubListTableModel = new HubsTableModel();
        hubListTable.setModel(hubListTableModel);
        hubListTable.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() > 1) {
                    Hub hub = hubListTableModel.getHub(hubListTable.getSelectedRow());
                    if (hub != null) {
                        connect(hub);
                    }
                }
            }
        });

        JPanel hubListPanel = new JPanel(new BorderLayout());
        hubListPanel.add(getConnectPanel(), BorderLayout.NORTH);
        hubListPanel.add(new JScrollPane(hubListTable), BorderLayout.CENTER);

        tabbedPane.add("Hubs", hubListPanel);

        connectionsPanel = new ConnectionsPanel();
        tabbedPane.add("Connections", connectionsPanel);

        setDefaultCloseOperation(EXIT_ON_CLOSE);

        setSize(500, 500);
    }

    private void selectConnectionsTab() {
        tabbedPane.setSelectedComponent(connectionsPanel);
    }

    private void connect(Hub hub) {
        HubConnection connection = new HubConnection(remoteUser, hub);
        connectionsPanel.addConnection(connection);
        selectConnectionsTab();
        connection.connect();
    }

    private JPanel getConnectPanel() {
        JPanel connectPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
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
