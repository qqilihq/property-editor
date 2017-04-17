package de.philippkatz.swing.property;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

import org.jdesktop.swingx.JXTreeTable;
import org.jdesktop.swingx.autocomplete.ComboBoxCellEditor;
import org.jdesktop.swingx.decorator.HighlighterFactory;

import de.philippkatz.swing.property.types.ChildCount;
import de.philippkatz.swing.property.types.PropertyType;
import de.philippkatz.swing.property.types.PropertyTypes;

@SuppressWarnings("serial")
public final class PropertiesTreeTable extends JXTreeTable {

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

		setOpenIcon(null);
		setClosedIcon(null);
		setLeafIcon(null);

		setDefaultEditor(PropertyType.class, new ComboBoxCellEditor(new JComboBox<>(PropertyTypes.ALL)));
		setDefaultRenderer(ChildCount.class, new ChildCountRenderer());

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
		if (modelColumn == 2) {
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