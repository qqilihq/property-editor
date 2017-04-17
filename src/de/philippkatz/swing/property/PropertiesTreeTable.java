package de.philippkatz.swing.property;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreePath;

import org.jdesktop.swingx.JXTreeTable;
import org.jdesktop.swingx.autocomplete.ComboBoxCellEditor;
import org.jdesktop.swingx.decorator.HighlighterFactory;
import org.jdesktop.swingx.treetable.TreeTableCellEditor;

import de.philippkatz.swing.property.types.ChildCount;
import de.philippkatz.swing.property.types.PropertyNode;
import de.philippkatz.swing.property.types.PropertyType;
import de.philippkatz.swing.property.types.PropertyTypes;

@SuppressWarnings("serial")
public final class PropertiesTreeTable extends JXTreeTable {

	/**
	 * Makes keys which are not editable (for entries within arrays) appear in a
	 * grey color.
	 */
	private final class KeyRenderer extends DefaultTreeCellRenderer {
		public KeyRenderer() {
			setOpenIcon(null);
			setClosedIcon(null);
			setLeafIcon(null);
		}

		@Override
		public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded,
				boolean leaf, int row, boolean hasFocus) {
			Component component = super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
			TreePath path = getPathForRow(row);
			if (path != null) {
				PropertyNode item = (PropertyNode) path.getLastPathComponent();
				if (((PropertyNode) item.getParent()).getType() == PropertyTypes.ARRAY) {
					component.setForeground(Color.GRAY);
				}
			}
			return component;
		}
	}

	/**
	 * Verifies upon {@link #stopCellEditing()} whether the entered key is
	 * unique within the parent (ie. there is no sibling this key already). In
	 * case of conflicts, the editor remains active and the user has to pick a
	 * different key.
	 */
	private final class UniqueKeyTreeTableCellEditor extends TreeTableCellEditor {
		private final int row;

		private UniqueKeyTreeTableCellEditor(JTree tree, int row) {
			super(tree);
			this.row = row;
		}

		@Override
		public boolean stopCellEditing() {

			String value = (String) delegate.getCellEditorValue();
			TreePath path = getPathForRow(row);
			PropertyNode item = (PropertyNode) path.getLastPathComponent();
			PropertyNode parent = (PropertyNode) item.getParent();
			int itemIdx = parent.getIndex(item);

			if (parent.getType() == PropertyTypes.OBJECT) {
				for (int childIdx = 0; childIdx < parent.getChildCount(); childIdx++) {
					PropertyNode child = (PropertyNode) parent.getChildAt(childIdx);
					if (childIdx == itemIdx) {
						continue;
					}
					if (child.getKey().equals(value)) {
						((JComponent) getComponent()).setBorder(new LineBorder(Color.RED));
						return false;
					}
				}
			}
			return super.stopCellEditing();
		}
	}

	/**
	 * Renders the number of children in a collection row.
	 */
	private static final class ChildCountRenderer extends DefaultTableCellRenderer {
		@Override
		protected void setValue(Object value) {
			if (value instanceof ChildCount) {
				int count = ((ChildCount) value).count;
				setText("(" + count + " items)");
			} else {
				throw new IllegalArgumentException(
						"Expected value to be of type " + ChildCount.class + ", but was " + value.getClass());
			}
		}

		@Override
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
				int row, int column) {
			Component component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
			component.setForeground(Color.GRAY);
			return component;
		}

	}

	private Class<?> editingClass;

	public PropertiesTreeTable(PropertiesTreeTableModel model) {
		setEditable(true);
		setDragEnabled(false);
		setColumnSelectionAllowed(false);
		setHighlighters(HighlighterFactory.createAlternateStriping());
		setRowHeight(20);
		setTreeTableModel(model);
		setDefaultEditor(PropertyType.class, new ComboBoxCellEditor(new JComboBox<>(PropertyTypes.ALL)));
		setDefaultRenderer(ChildCount.class, new ChildCountRenderer());
		setTreeCellRenderer(new KeyRenderer());
	}

	@Override
	public TableCellRenderer getCellRenderer(int row, int column) {
		editingClass = null;
		int modelColumn = convertColumnIndexToModel(column);
		if (modelColumn == 2) {
			editingClass = getModel().getValueAt(row, modelColumn).getClass();
			return getDefaultRenderer(editingClass);
		} else {
			return super.getCellRenderer(row, column);
		}
	}

	@Override
	public TableCellEditor getCellEditor(int row, int column) {
		editingClass = null;
		int modelColumn = convertColumnIndexToModel(column);
		// the hierarchical column which contains they keys; through the custom
		// cell editor we make sure, that no siblings have identical names
		if (modelColumn == getHierarchicalColumn()) {
			// http://stackoverflow.com/a/23832575
			JTree tree = (JTree) getCellRenderer(0, getHierarchicalColumn());
			return new UniqueKeyTreeTableCellEditor(tree, row);
		} else if (modelColumn == 2) {
			editingClass = getModel().getValueAt(row, modelColumn).getClass();
			return getDefaultEditor(editingClass);
		} else {
			return super.getCellEditor(row, column);
		}
	}

	@Override
	public Class<?> getColumnClass(int column) {
		return editingClass != null ? editingClass : super.getColumnClass(column);
	}

}