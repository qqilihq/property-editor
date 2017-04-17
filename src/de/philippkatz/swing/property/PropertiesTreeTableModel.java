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
				fireChildrenChanged(currentNode);

			}

		} else {

			super.setValueAt(value, node, column);

		}

	}

	private void fireChildrenChanged(TreeTableNode node) {
		TreePath parentPath = new TreePath(getPathToRoot(node));
		for (int i = 0; i < node.getChildCount(); i++) {
			modelSupport.fireChildChanged(parentPath, i, node.getChildAt(i));
		}
	}
	
	public Object getData() {
		return ((PropertyNode) getRoot()).toObject();
	}
	
	@Override
	public void insertNodeInto(MutableTreeTableNode newChild, MutableTreeTableNode parent, int index) {
		super.insertNodeInto(newChild, parent, index);
		
		// make sure all children are updated; an array entry might change its
		// key from [9] to [10], and we need to re-draw the table in this case
		fireChildrenChanged(parent);
		
	}

}
