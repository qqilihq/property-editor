package de.philippkatz.swing.property;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JTable;
import javax.swing.WindowConstants;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

import org.jdesktop.swingx.autocomplete.ComboBoxCellEditor;
import org.jdesktop.swingx.renderer.DefaultTableRenderer;

import de.philippkatz.swing.property.PropertiesEditorConfig.PropertiesEditorConfigBuilder;
import de.philippkatz.swing.property.types.PropertyNode;
import de.philippkatz.swing.property.types.PropertyType;
import de.philippkatz.swing.property.types.PropertyTypes;

@SuppressWarnings("serial")
public class PropertiesEditorDemo_CustomType extends JFrame implements TreeModelListener {

	private enum NamedColor {
		Black(Color.BLACK, "Black"), //
		White(Color.WHITE, "White"), //
		Red(Color.RED, "Red"), //
		Green(Color.GREEN, "Green"), //
		Blue(Color.BLUE, "Blue");

		final Color color;
		private final String name;

		private NamedColor(Color color, String name) {
			this.color = color;
			this.name = name;
		}

		@Override
		public String toString() {
			return name;
		}
	}

	private final PropertiesTreeTableModel treeTableModel;

	private static Object createSampleData() {

		Map<String, Object> root = new LinkedHashMap<>();
		root.put("string", "The quick brown fox");
		root.put("boolean", true);
		root.put("numbers", 42l);

		Map<String, Object> languages = new LinkedHashMap<>();
		languages.put("DE", "German");
		languages.put("EN", "English");
		languages.put("FR", "French");
		root.put("languages", languages);

		root.put("fruits", Arrays.asList("apple", "orange", "grapefruit"));

		Map<String, NamedColor> colors = new LinkedHashMap<>();
		colors.put("foreground", NamedColor.Black);
		colors.put("background", NamedColor.White);
		root.put("colors", colors);

		return root;

	}

	private PropertiesEditorDemo_CustomType() {
		setTitle("Properties Editor Demo");
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setMinimumSize(new Dimension(600, 400));
		getRootPane().setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		// the data types available in the editor can be configured as follows:

		PropertiesEditorConfigBuilder configBuilder = PropertiesEditorConfig.builder();

		configBuilder.addType(new PropertyTypes.MapType("Object"));
		configBuilder.addType(new PropertyTypes.ListType("Array"));

		configBuilder.addDefaultType(new PropertyTypes.StringType("String", ""));
		configBuilder.addType(new PropertyTypes.BooleanType("Boolean", true));
		configBuilder.addType(new PropertyTypes.DoubleType("Double", 0.));
		configBuilder.addType(new PropertyTypes.LongType("Long", 0l));
		// TODO use `PrimitiveType`
		configBuilder.addType(new PropertyType<NamedColor>() {
			@Override
			public boolean isCollection() {
				return false;
			}

			@Override
			public NamedColor getDefaultValue() {
				return null;
			}

			@Override
			public Class<? super NamedColor> getType() {
				return NamedColor.class;
			}

			@Override
			public PropertyNode fromObject(String key, Object object, PropertiesEditorConfig config) {
				return new PropertyNode(key, this, object);
			}

			@Override
			public NamedColor toObject(PropertyNode propertyNode) {
				return (NamedColor) propertyNode.getUserObject();
			}

			// XXX I had no idea that this needs to be overridden!

			@Override
			public String toString() {
				// return getType().getSimpleName();
				return "Color";
			}

			@Override
			public TableCellRenderer getRenderer() {
				return new DefaultTableRenderer() {
					@Override
					public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
							boolean hasFocus, int row, int column) {
						Component component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus,
								row, column);
						if (value instanceof NamedColor) {
							NamedColor color = (NamedColor) value;
							component.setForeground(color.color);
						}
						return component;
					}
				};
			}

			@Override
			public TableCellEditor getEditor() {
				JComboBox<NamedColor> comboBox = new JComboBox<>(NamedColor.values());
				return new ComboBoxCellEditor(comboBox);
			}
		});

		PropertiesEditorConfig config = configBuilder.build();

		treeTableModel = new PropertiesTreeTableModel(config, createSampleData());

		// treeTableModel = new PropertiesTreeTableModel(createSampleData());
		treeTableModel.addTreeModelListener(this);
		add(new PropertiesEditor(treeTableModel));

		setVisible(true);
	}

	@Override
	public void treeNodesChanged(TreeModelEvent e) {
		print();
	}

	@Override
	public void treeNodesInserted(TreeModelEvent e) {
		print();
	}

	@Override
	public void treeNodesRemoved(TreeModelEvent e) {
		print();
	}

	@Override
	public void treeStructureChanged(TreeModelEvent e) {
		print();
	}

	private void print() {
		System.out.println(treeTableModel.getData());
	}

	public static void main(String[] args) {
		new PropertiesEditorDemo_CustomType();
	}

}
