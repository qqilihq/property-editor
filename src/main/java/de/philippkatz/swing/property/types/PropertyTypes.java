package de.philippkatz.swing.property.types;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

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
	}

	public static final class LongType extends PrimitiveType<Long> {
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

	public static final class IntegerType extends PrimitiveType<Integer> {
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

	public static final class DoubleType extends PrimitiveType<Double> {
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

	public static final class FloatType extends PrimitiveType<Float> {
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
		public Class<? super TYPE> getType() {
			return javaType;
		}

		@Override
		public String toString() {
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

	private static class PrimitiveType<TYPE> extends AbstractType<TYPE> {
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
		public TYPE getDefaultValue() {
			return defaultValue;
		}

		@Override
		public PropertyNode fromObject(String key, Object object, PropertiesEditorConfig config) {
			return new PropertyNode(key, this, object);
		}

		@SuppressWarnings("unchecked")
		@Override
		public TYPE toObject(PropertyNode propertyNode) {
			return (TYPE) propertyNode.getUserObject();
		}
	}

	private PropertyTypes() {
		// not to be i'ted
	}

}
