package xdc.swing;

import xdc.net.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class SearchPanel extends JPanel {
    private JTextArea results;

    public SearchPanel(final HubConnection connection) {
        super(new BorderLayout());

        JPanel inputPanel = new JPanel(new BorderLayout());
        final JTextField searchField = new JTextField();

        AbstractAction searchAction = new AbstractAction("Search") {
            public void actionPerformed(ActionEvent e) {
                connection.search(searchField.getText());
            }
        };

        searchField.setAction(searchAction);
        inputPanel.add(searchField, BorderLayout.CENTER);
        JButton searchButton = new JButton(searchAction);
        inputPanel.add(searchButton, BorderLayout.EAST);

        add(inputPanel, BorderLayout.NORTH);
        results = new JTextArea();
        add(results, BorderLayout.CENTER);

        connection.addListener(new HubConnectionAdapter() {
            public void searchResult(HubConnection con, SearchResult result) {
                results.append(result.getFilename() + "\n");
            }
        });
    }
}
