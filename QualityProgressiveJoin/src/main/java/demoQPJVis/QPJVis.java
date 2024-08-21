package demoQPJVis;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JTextPane;
import java.awt.FlowLayout;
import javax.swing.JTextField;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import javax.swing.JScrollPane;

public class QPJVis {

	private JFrame frame;
	private JTextField textFieldSelect;
	private JTextField textFieldFrom;
	private JTextField textField;
	private JTextField textField_1;
	private JTextField textField_2;
	private JTextField textField_3;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					QPJVis window = new QPJVis();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public QPJVis() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setTitle("QualProJoinVis");
		frame.setBounds(100, 100, 750, 500);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JPanel panelSearch = new JPanel();
		panelSearch.setBounds(0, 6, 300, 460);
		frame.getContentPane().add(panelSearch);
		panelSearch.setLayout(null);
		
		JTextPane textPaneSearchPanel = new JTextPane();
		textPaneSearchPanel.setEditable(false);
		textPaneSearchPanel.setText("Search Panel");
		textPaneSearchPanel.setBounds(6, 6, 100, 16);
		panelSearch.add(textPaneSearchPanel);
		
		JTextPane textPaneSelect = new JTextPane();
		textPaneSelect.setEditable(false);
		textPaneSelect.setText("SELECT");
		textPaneSelect.setBounds(6, 34, 100, 16);
		panelSearch.add(textPaneSelect);
		
		textFieldSelect = new JTextField();
		textFieldSelect.setBounds(0, 50, 140, 30);
		panelSearch.add(textFieldSelect);
		textFieldSelect.setColumns(10);
		
		textFieldFrom = new JTextField();
		textFieldFrom.setColumns(10);
		textFieldFrom.setBounds(140, 50, 140, 30);
		panelSearch.add(textFieldFrom);
		
		JTextPane textPaneFrom = new JTextPane();
		textPaneFrom.setEditable(false);
		textPaneFrom.setText("FROM");
		textPaneFrom.setBounds(144, 34, 100, 16);
		panelSearch.add(textPaneFrom);
		
		JTextPane textPaneWhere = new JTextPane();
		textPaneWhere.setText("WHERE");
		textPaneWhere.setEditable(false);
		textPaneWhere.setBounds(6, 82, 100, 16);
		panelSearch.add(textPaneWhere);
		
		textField = new JTextField();
		textField.setColumns(10);
		textField.setBounds(0, 98, 170, 30);
		panelSearch.add(textField);
		
		JTextPane textPanelGroupBy = new JTextPane();
		textPanelGroupBy.setText("GROUP BY");
		textPanelGroupBy.setEditable(false);
		textPanelGroupBy.setBounds(170, 82, 100, 16);
		panelSearch.add(textPanelGroupBy);
		
		textField_1 = new JTextField();
		textField_1.setColumns(10);
		textField_1.setBounds(170, 98, 110, 30);
		panelSearch.add(textField_1);
		
		JTextPane textPaneNumPar = new JTextPane();
		textPaneNumPar.setText("# of Partitions:");
		textPaneNumPar.setEditable(false);
		textPaneNumPar.setBounds(6, 129, 100, 16);
		panelSearch.add(textPaneNumPar);
		
		JTextPane textPaneNumBatch = new JTextPane();
		textPaneNumBatch.setText("# of Batches:");
		textPaneNumBatch.setEditable(false);
		textPaneNumBatch.setBounds(110, 129, 90, 16);
		panelSearch.add(textPaneNumBatch);
		
		JTextPane textPaneNumBatch_1 = new JTextPane();
		textPaneNumBatch_1.setText("# of Batches:");
		textPaneNumBatch_1.setEditable(false);
		textPaneNumBatch_1.setBounds(204, 129, 90, 16);
		panelSearch.add(textPaneNumBatch_1);
		
		textField_2 = new JTextField();
		textField_2.setColumns(10);
		textField_2.setBounds(0, 144, 104, 30);
		panelSearch.add(textField_2);
		
		textField_3 = new JTextField();
		textField_3.setColumns(10);
		textField_3.setBounds(104, 144, 100, 30);
		panelSearch.add(textField_3);
		
		JPanel panelResult = new JPanel();
		panelResult.setBounds(300, 6, 450, 460);
		frame.getContentPane().add(panelResult);
		panelResult.setLayout(null);

	}
}
