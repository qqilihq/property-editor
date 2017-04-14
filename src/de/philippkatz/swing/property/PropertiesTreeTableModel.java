package de.philippkatz.swing.property;

import javax.swing.tree.TreePath;

import org.jdesktop.swingx.treetable.DefaultTreeTableModel;
import org.jdesktop.swingx.treetable.MutableTreeTableNode;
import org.jdesktop.swingx.treetable.TreeTableNode;

import de.philippkatz.swing.property.types.PropertyNode;
import de.philippkatz.swing.property.types.PropertyType;

public class PropertiesTreeTableModel extends DefaultTreeTableModel {

	private static final String[] COLUMN_NAMES = { "Key", "Type", "Value" };

	private static final Class<?>[] COLUMN_CLASSES = { String.class, PropertyType.class, Object.class };

	public PropertiesTreeTableModel(PropertyNode root) {
		super(root);
	}

	@Override
	public String getColumnName(int column) {
		return COLUMN_NAMES[column];
	}

	@Override
	public Class<?> getColumnClass(int column) {
		return COLUMN_CLASSES[column];
	}

	@Override
	public void setValueAt(Object value, Object node, int column) {

		if (column == 1) {

			PropertyNode currentNode = (PropertyNode) node;

			PropertyType<?> oldType = currentNode.getType();
			PropertyType<?> newType = (PropertyType<?>) value;

			if (oldType == newType) {
				return;
			}

			currentNode.setValueAt(newType, column);

			if (oldType.isCollection() && !newType.isCollection()) {

				// changed from collection to primitive; remove all children
				for (int i = currentNode.getChildCount() - 1; i >= 0; i--) {
					TreeTableNode nodeToRemove = currentNode.getChildAt(i);
					removeNodeFromParent((MutableTreeTableNode) nodeToRemove);
				}

			} else if (oldType.isCollection() && newType.isCollection()) {

				// changed from array to object or vice versa; trigger update
				// listener for all child nodes b/c the keys might have changed
				TreePath parentPath = new TreePath(getPathToRoot(currentNode));
				for (int i = 0; i < currentNode.getChildCount(); i++) {
					modelSupport.fireChildChanged(parentPath, i, currentNode.getChildAt(i));
				}

			}

		} else {

			super.setValueAt(value, node, column);

		}

	}
	
	public Object getData() {
		return ((PropertyNode) getRoot()).toObject();
	}

}
