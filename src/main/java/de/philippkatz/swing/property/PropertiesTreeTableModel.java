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

	private final PropertiesEditorConfig config;

	/**
	 * Create a new PropertiesTreeTableModel.
	 * 
	 * @param data
	 *            The data to show in the editor (must be a Map or List).
	 * @throws IllegalArgumentException
	 *             In case the data is not an instance of Map or List.
	 */
	public PropertiesTreeTableModel(Object data) {
		this(PropertiesEditorConfig.defaultConfig(), data);
	}

	/**
	 * Create a new PropertiesTreeTableModel.
	 * 
	 * @param config
	 *            The custom configuration.
	 * @param data
	 *            The data to show in the editor (must be a Map or List).
	 * @throws IllegalArgumentException
	 *             In case the data is not an instance of Map or List.
	 * @see PropertiesEditorConfig
	 */
	public PropertiesTreeTableModel(PropertiesEditorConfig config, Object data) {
		super(assertCollection(config.fromObject(data)));
		this.config = config;
	}

	private static PropertyNode assertCollection(PropertyNode propertyNode) {
		if (!propertyNode.getType().isCollection()) {
			throw new IllegalArgumentException("The data argument must be a Map or List.");
		}
		return propertyNode;
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

				// changed from list to map or vice versa; trigger update
				// listener for all child nodes b/c the keys might have changed
				fireChildrenChanged(currentNode);

			} else {

				// changed between primitives
				modelSupport.fireChildChanged(new TreePath(getPathToRoot(currentNode.getParent())),
						currentNode.getParent().getIndex(currentNode), currentNode);

			}

		} else {

			super.setValueAt(value, node, column);

		}

	}

	protected void fireChildrenChanged(TreeTableNode node) {
		TreePath parentPath = new TreePath(getPathToRoot(node));

		int[] indices = new int[node.getChildCount()];
		Object[] children = new Object[node.getChildCount()];

		for (int i = 0; i < node.getChildCount(); i++) {
			indices[i] = i;
			children[i] = node.getChildAt(i);
		}

		modelSupport.fireChildrenChanged(parentPath, indices, children);
	}
	
	public Object getData() {
		return ((PropertyNode) getRoot()).toObject();
	}
	
	@Override
	public void insertNodeInto(MutableTreeTableNode newChild, MutableTreeTableNode parent, int index) {
		super.insertNodeInto(newChild, parent, index);

		// make sure all children are updated; a list entry might change its
		// key from [9] to [10], and we need to re-draw the table in this case
		fireChildrenChanged(parent);

	}

	/**
	 * @return The editor configuration.
	 */
	public PropertiesEditorConfig getConfig() {
		return config;
	}

}
