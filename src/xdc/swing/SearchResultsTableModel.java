package xdc.swing;

import xdc.net.SearchResult;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class SearchResultsTableModel extends AbstractTableModel {
    private List searchResults = new ArrayList();

    public void add(SearchResult result) {
        searchResults.add(result);
        fireTableRowsInserted(searchResults.size() - 1, searchResults.size() - 1);
    }

    public int getRowCount() {
        return searchResults.size();
    }

    public int getColumnCount() {
        return 4;
    }

    public String getColumnName(int column) {
        switch (column) {
            case 0:
                return "Name";
            case 1:
                return "Size";
            case 2:
                return "User";
            case 3:
                return "Slots";
        }
        return "";
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        SearchResult result = (SearchResult) searchResults.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return result.getFilename();
            case 1:
                return new Long(result.getFileSize());
            case 2:
                return result.getNick();
            case 3:
                return result.getFreeSlots() + "/" + result.getMaxSlots();
        }
        return "";
    }
}
