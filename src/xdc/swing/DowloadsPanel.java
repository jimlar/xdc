package xdc.swing;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.util.ArrayList;

public class DowloadsPanel extends JPanel {
    private DownloadsTableModel downloadsTableModel;

    public DowloadsPanel() {
        super(new BorderLayout());

        JTable downloadsTable = new JTable();
        downloadsTableModel = new DownloadsTableModel();
        downloadsTable.setModel(downloadsTableModel);

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Downloads", new JScrollPane(downloadsTable));
        add(tabbedPane, BorderLayout.CENTER);
    }

    private class DownloadsTableModel extends AbstractTableModel {
        private java.util.List downloads = new ArrayList();

        public int getRowCount() {
            return downloads.size();
        }

        public int getColumnCount() {
            return 2;
        }

        public Object getValueAt(int rowIndex, int columnIndex) {
            return "";
        }

        public String getColumnName(int column) {
            switch (column) {
                case 0:
                    return "File";
                case 1:
                    return "Size";
                default:
                    return "";
            }
        }

        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return false;
        }
    }
}
