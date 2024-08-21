package demoQPJVis;

import javax.swing.*;
import java.awt.*;

public class SearchPanel extends JFrame {
    public SearchPanel() {
        setTitle("QualProJoinVis Search Panel");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);

        // Main panel with grid layout for structuring form elements
        JPanel mainPanel = new JPanel(new GridLayout(0, 1));

        // Create the form panel
        JPanel formPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        addTextField(formPanel, "SELECT", "");
        addTextField(formPanel, "FROM", "");
        addTextField(formPanel, "WHERE", "");
        addTextField(formPanel, "GROUP BY", "");
        addTextField(formPanel, "# of Partitions:", "5");
        addTextField(formPanel, "# of Batches:", "35");
        addTextField(formPanel, "Error Bound: ", "0.1");
        addTextField(formPanel, "Result Store Path", "/home/qualProJoinVis/spatialJoin/results/");
        mainPanel.add(formPanel);

        // Adding radio buttons
        mainPanel.add(createRadioPanel("Partition Function", new String[]{"Hash Partition", "Grid Partition"}));
        mainPanel.add(createRadioPanel("Visualize Results", new String[]{"Yes", "No"}));
        mainPanel.add(createRadioPanel("Visualize Statistics", new String[]{"Yes", "No"}));

        // Button panel
        JPanel buttonPanel = new JPanel();
        JButton qpjSearchButton = new JButton("QPJSearch");
        JButton prismSearchButton = new JButton("PrismSearch");
        buttonPanel.add(qpjSearchButton);
        buttonPanel.add(prismSearchButton);
        mainPanel.add(buttonPanel);

        // Set main panel to the frame
        setContentPane(mainPanel);
    }

    private void addTextField(JPanel panel, String label, String placeholder) {
        JLabel jLabel = new JLabel(label);
        JTextField jTextField = new JTextField(10);
        jTextField.setText(placeholder);
        panel.add(jLabel);
        panel.add(jTextField);
    }

    private JPanel createRadioPanel(String title, String[] options) {
        JPanel radioPanel = new JPanel(new GridLayout(1, options.length));
        radioPanel.setBorder(BorderFactory.createTitledBorder(title));
        ButtonGroup group = new ButtonGroup();
        for (String option : options) {
            JRadioButton radioButton = new JRadioButton(option);
            group.add(radioButton);
            radioPanel.add(radioButton);
        }
        return radioPanel;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            SearchPanel frame = new SearchPanel();
            frame.setVisible(true);
        });
    }
}
