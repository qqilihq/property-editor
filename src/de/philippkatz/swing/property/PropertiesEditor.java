package de.philippkatz.swing.property;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.Border;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreePath;

import de.philippkatz.swing.property.types.PropertyNode;

@SuppressWarnings("serial")
public class PropertiesEditor extends JPanel {

	private final PropertiesTreeTable treeTable;

	private final PropertiesTreeTableModel treeTableModel;

	private JButton addButton;

	private JButton removeButton;

	public PropertiesEditor(PropertiesTreeTableModel treeTableModel) {

		Border padding = BorderFactory.createEmptyBorder(10, 10, 10, 10);
		setBorder(padding);
		setLayout(new GridBagLayout());

		this.treeTableModel = treeTableModel;
		treeTable = new PropertiesTreeTable(treeTableModel);

		treeTable.addTreeSelectionListener(new TreeSelectionListener() {
			@Override
			public void valueChanged(TreeSelectionEvent e) {
				updateButtons();
			}
		});

		treeTable.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				TreePath path = treeTable.getPathForLocation(e.getX(), e.getY());
				if (path == null) {
					treeTable.clearSelection();
				}
			}
		});

		treeTable.expandAll();

		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 1;
		c.weighty = 1;
		add(new JScrollPane(treeTable), c);

		c.fill = GridBagConstraints.NONE;
		c.anchor = GridBagConstraints.NORTHWEST;
		c.gridx++;
		c.weightx = 0;
		add(createButtons(), c);

		updateButtons();
	}

	private void updateButtons() {
		int[] selectedRows = treeTable.getSelectedRows();
		removeButton.setEnabled(selectedRows.length > 0);
	}

	private Component createButtons() {
		JPanel buttonPanel = new JPanel(new GridBagLayout());

		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridy = 0;

		addButton = new JButton("Add");
		addButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				PropertyNode newProperty = PropertyNode.string();

				if (treeTable.getSelectedRow() == -1) {

					// at the bottom of the root element
					treeTableModel.insertNodeInto(newProperty, (PropertyNode) treeTableModel.getRoot(),
							treeTableModel.getRoot().getChildCount());

				} else {

					TreePath path = treeTable.getPathForRow(treeTable.getSelectedRow());
					PropertyNode item = (PropertyNode) path.getPathComponent(path.getPathCount() - 1);

					if (item.getAllowsChildren()) {
						// if the selected item allows children, add child
						treeTableModel.insertNodeInto(newProperty, item, 0);

					} else {
						// else, add it as ancestor below the current selection
						treeTableModel.insertNodeInto(newProperty, (PropertyNode) item.getParent(),
								item.getParent().getIndex(item) + 1);
					}

				}

			}
		});
		buttonPanel.add(addButton, c);

		c.gridy++;
		removeButton = new JButton("Remove");
		buttonPanel.add(removeButton, c);
		removeButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int[] selectedRows = treeTable.getSelectedRows();
				for (int i = selectedRows.length - 1; i >= 0; i--) {
					TreePath path = treeTable.getPathForRow(selectedRows[i]);
					PropertyNode propertyToRemove = (PropertyNode) path.getPathComponent(path.getPathCount() - 1);
					treeTableModel.removeNodeFromParent(propertyToRemove);
				}
			}
		});
		return buttonPanel;
	}

}
