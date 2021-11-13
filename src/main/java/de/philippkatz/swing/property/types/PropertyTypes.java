package de.philippkatz.swing.property.types;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.JCheckBox;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

import org.jdesktop.swingx.JXTable.BooleanEditor;
import org.jdesktop.swingx.JXTable.NumberEditor;
import org.jdesktop.swingx.renderer.CheckBoxProvider;
import org.jdesktop.swingx.renderer.DefaultTableRenderer;
import org.jdesktop.swingx.renderer.FormatStringValue;
import org.jdesktop.swingx.treetable.MutableTreeTableNode;

import de.philippkatz.swing.property.PropertiesEditorConfig;

/**
 * Predefined property types.
 * 
 * @see PropertyType
 * @author Philipp Katz
 */
public final class PropertyTypes {

	public static final class ListType extends CollectionType<List<?>> {
		/**
		 * Create a new ListType.
		 * 
		 * @param name
		 *            The name in the UI.
		 */
		public ListType(String name) {
			super(List.class, name);
		}

		@Override
		public List<?> toObject(PropertyNode propertyNode) {
			List<Object> list = new ArrayList<>();
			for (Enumeration<? extends MutableTreeTableNode> en = propertyNode.children(); en.hasMoreElements();) {
				list.add(((PropertyNode) en.nextElement()).toObject());
			}
			return list;
		}

		@Override
		public PropertyNode fromObject(String key, Object object, PropertiesEditorConfig config) {
			PropertyNode propertyNode = new PropertyNode(key, this, null);
			for (Object item : (List<?>) object) {
				propertyNode.add(config.fromObject(item));
			}
			return propertyNode;
		}
	}

	public static final class MapType extends CollectionType<Map<?, ?>> {
		/**
		 * Create a new MapType.
		 * 
		 * @param name
		 *            The name in the UI.
		 */
		public MapType(String name) {
			super(Map.class, name);
		}

		@Override
		public Map<?, ?> toObject(PropertyNode propertyNode) {
			Map<String, Object> map = new LinkedHashMap<>();
			for (Enumeration<? extends MutableTreeTableNode> en = propertyNode.children(); en.hasMoreElements();) {
				PropertyNode current = (PropertyNode) en.nextElement();
				map.put(current.getKey(), current.toObject());
			}
			return map;
		}

		@Override
		public PropertyNode fromObject(String key, Object object, PropertiesEditorConfig config) {
			PropertyNode propertyType = new PropertyNode(key, this, null);
			for (Entry<?, ?> entry : ((Map<?, ?>) object).entrySet()) {
				propertyType.add(config.fromObject(entry.getKey().toString(), entry.getValue()));
			}
			return propertyType;
		}
	}

	public static final class StringType extends PrimitiveType<String> {
		/**
		 * Create a new StringType.
		 * 
		 * @param name
		 *            The name in the UI.
		 * @param defaultValue
		 *            The default value.
		 */
		public StringType(String name, String defaultValue) {
			super(String.class, name, defaultValue);
		}
	}

	public static final class BooleanType extends PrimitiveType<Boolean> {
		/**
		 * Create a new BooleanType.
		 * 
		 * @param name
		 *            The name in the UI.
		 * @param defaultValue
		 *            The default value.
		 */
		public BooleanType(String name, Boolean defaultValue) {
			super(Boolean.class, name, defaultValue);
		}

		// make rendering and editing of checkboxes align left

		@Override
		public TableCellRenderer getRenderer() {
			return new DefaultTableRenderer(new CheckBoxProvider(null, SwingConstants.LEFT));
		}

		@SuppressWarnings("serial")
		@Override
		public TableCellEditor getEditor() {
			return new BooleanEditor() {
				{
					JCheckBox checkBox = (JCheckBox) getComponent();
					checkBox.setHorizontalAlignment(SwingConstants.LEFT);
				}
			};
		}
	}

	public static final class LongType extends NumberType<Long> {
		/**
		 * Create a new LongType.
		 * 
		 * @param name
		 *            The name in the UI.
		 * @param defaultValue
		 *            The default value.
		 */
		public LongType(String name, Long defaultValue) {
			super(Long.class, name, defaultValue);
		}
	}

	public static final class IntegerType extends NumberType<Integer> {
		/**
		 * Create a new IntegerType.
		 * 
		 * @param name
		 *            The name in the UI.
		 * @param defaultValue
		 *            The default value.
		 */
		public IntegerType(String name, Integer defaultValue) {
			super(Integer.class, name, defaultValue);
		}
	}

	public static final class DoubleType extends NumberType<Double> {
		/**
		 * Create a new DoubleType.
		 * 
		 * @param name
		 *            The name in the UI.
		 * @param defaultValue
		 *            The default value.
		 */
		public DoubleType(String name, Double defaultValue) {
			super(Double.class, name, defaultValue);
		}
	}

	public static final class FloatType extends NumberType<Float> {
		/**
		 * Create a new FloatType.
		 * 
		 * @param name
		 *            The name in the UI.
		 * @param defaultValue
		 *            The default value.
		 */
		public FloatType(String name, Float defaultValue) {
			super(Float.class, name, defaultValue);
		}
	}

	private static abstract class AbstractType<TYPE> implements PropertyType<TYPE> {
		protected final String name;
		protected final Class<? super TYPE> javaType;

		public AbstractType(Class<? super TYPE> javaType, String name) {
			this.javaType = javaType;
			this.name = name;
		}

		public AbstractType(Class<? super TYPE> javaType) {
			this(javaType, javaType.getSimpleName());
		}

		@Override
		public final Class<? super TYPE> getType() {
			return javaType;
		}

		@Override
		public final String toString() {
			return name;
		}
	}

	private static abstract class CollectionType<TYPE> extends AbstractType<TYPE> {
		protected CollectionType(Class<? super TYPE> javaType, String name) {
			super(javaType, name);
		}

		protected CollectionType(Class<? super TYPE> javaType) {
			super(javaType);
		}

		@Override
		public final boolean isCollection() {
			return true;
		}

		@Override
		public final TYPE getDefaultValue() {
			return null;
		}
	}

	/** @since 2.1 (public) */
	public static class PrimitiveType<TYPE> extends AbstractType<TYPE> {
		private final TYPE defaultValue;

		protected PrimitiveType(Class<TYPE> type, String name, TYPE defaultValue) {
			super(type, name);
			this.defaultValue = defaultValue;
		}

		protected PrimitiveType(Class<TYPE> type, TYPE defaultValue) {
			super(type);
			this.defaultValue = defaultValue;
		}

		@Override
		public final boolean isCollection() {
			return false;
		}

		@Override
		public final TYPE getDefaultValue() {
			return defaultValue;
		}

		@Override
		public final PropertyNode fromObject(String key, Object object, PropertiesEditorConfig config) {
			return new PropertyNode(key, this, object);
		}

		@SuppressWarnings("unchecked")
		@Override
		public final TYPE toObject(PropertyNode propertyNode) {
			return (TYPE) propertyNode.getUserObject();
		}
	}

	/** @since 2.1 */
	private static class NumberType<NUMBER extends Number> extends PrimitiveType<NUMBER> {
		public NumberType(Class<NUMBER> type, String name, NUMBER defaultValue) {
			super(type, name, defaultValue);
		}

		// make rendering and editing of numbers align left

		@Override
		public TableCellRenderer getRenderer() {
			return new DefaultTableRenderer(new FormatStringValue(), SwingConstants.LEFT);
		}

		@SuppressWarnings("serial")
		@Override
		public TableCellEditor getEditor() {
			return new NumberEditor() {
				{
					JTextField textField = (JTextField) getComponent();
					textField.setHorizontalAlignment(SwingConstants.LEFT);
				}
			};
		}
	}

	private PropertyTypes() {
		// not to be i'ted
	}

}
