package xdc.swing;

import org.apache.log4j.Logger;
import xdc.net.Hub;
import xdc.net.HubList;

import javax.swing.table.AbstractTableModel;
import java.io.IOException;

public class HubsTableModel extends AbstractTableModel {
    private Logger logger = Logger.getLogger(HubsTableModel.class);

    private HubList publicHubList;
    private Exception error;

    public HubsTableModel() {
        startReload();
    }

    public void setError(Exception error) {
        this.error = error;
    }

    public int getRowCount() {
        if (hasError()) {
            return 1;
        }
        return publicHubList != null ? publicHubList.getHubs().size() : 0;
    }

    private boolean hasError() {
        return error != null;
    }

    public int getColumnCount() {
        if (hasError()) {
            return 1;
        }
        return 3;
    }

    public String getColumnName(int column) {
        if (hasError()) {
            return " ";
        }

        switch (column) {
            case 0:
                return "Name";
            case 1:
                return "Users";
            case 2:
                return "Description";
        }
        return "";
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        if (hasError()) {
            return error.getMessage();
        }

        if (publicHubList == null) {
            return null;
        }

        Hub hub = (Hub) publicHubList.getHubs().get(rowIndex);
        switch (columnIndex) {
            case 0:
                return hub.getName();
            case 1:
                return new Integer(hub.getUsers());
            case 2:
                return hub.getDescription();
        }
        return "";
    }

    public void startReload() {
        new Thread(new Reloader()).start();
    }

    public Hub getHub(int row) {
        if (publicHubList != null) {
            return (Hub) publicHubList.getHubs().get(row);
        }
        return null;
    }

    private class Reloader implements Runnable {
        public void run() {
            try {
                setError(new Exception("Loading..."));
                fireTableStructureChanged();
                if (publicHubList == null) {
                    publicHubList = new HubList();
                } else {
                    publicHubList.refresh();
                }
                setError(null);
            } catch (IOException e) {
                logger.error("Cant load public hubs", e);
                setError(e);
            }
            fireTableStructureChanged();
        }
    }
}
