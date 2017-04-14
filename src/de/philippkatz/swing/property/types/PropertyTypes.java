package de.philippkatz.swing.property.types;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.jdesktop.swingx.treetable.MutableTreeTableNode;

public class PropertyTypes {

	public static final PropertyType<List<?>> ARRAY = new CollectionProperty<List<?>>(List.class) {
		@Override
		public List<?> toObject(PropertyNode propertyNode) {
			List<Object> list = new ArrayList<>();
			for (Enumeration<? extends MutableTreeTableNode> en = propertyNode.children(); en.hasMoreElements();) {
				list.add(((PropertyNode) en.nextElement()).toObject());
			}
			return list;
		}

		@Override
		public PropertyNode fromObject(String key, Object object) {
			PropertyNode propertyNode = new PropertyNode(key, this, null);
			for (Object item : (List<?>) object) {
				propertyNode.add(PropertyTypes.fromObject(null, item));
			}
			return propertyNode;
		}
	};

	public static final PropertyType<Map<?, ?>> OBJECT = new CollectionProperty<Map<?, ?>>(Map.class) {
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
		public PropertyNode fromObject(String key, Object object) {
			PropertyNode propertyType = new PropertyNode(key, this, null);
			for (Entry<?, ?> entry : ((Map<?, ?>) object).entrySet()) {
				propertyType.add(PropertyTypes.fromObject(entry.getKey().toString(), entry.getValue()));
			}
			return propertyType;
		}
	};

	public static final PropertyType<String> STRING = new PrimitiveProperty<>(String.class, "");
	public static final PropertyType<Boolean> BOOLEAN = new PrimitiveProperty<>(Boolean.class, true);
	public static final PropertyType<Long> LONG = new PrimitiveProperty<>(Long.class, 0l);
	public static final PropertyType<Integer> INTEGER = new PrimitiveProperty<>(Integer.class, 0);
	public static final PropertyType<Double> DOUBLE = new PrimitiveProperty<>(Double.class, 0.0);
	public static final PropertyType<Float> FLOAT = new PrimitiveProperty<>(Float.class, 0.0f);

	/** All known property types. */
	public static final PropertyType<?>[] ALL = { ARRAY, OBJECT, STRING, BOOLEAN, LONG, INTEGER, DOUBLE, FLOAT };

	private static abstract class AbstractProperty<TYPE> implements PropertyType<TYPE> {
		protected final Class<? super TYPE> javaType;

		public AbstractProperty(Class<? super TYPE> javaType) {
			this.javaType = javaType;
		}

		@Override
		public Class<? super TYPE> getType() {
			return javaType;
		}

		@Override
		public String toString() {
			return javaType.getSimpleName();
		}
	}

	private static abstract class CollectionProperty<TYPE> extends AbstractProperty<TYPE> {
		protected CollectionProperty(Class<? super TYPE> javaType) {
			super(javaType);
		}

		@Override
		public boolean isCollection() {
			return true;
		}

		@Override
		public TYPE getDefaultValue() {
			return null;
		}
	}

	private static class PrimitiveProperty<TYPE> extends AbstractProperty<TYPE> {
		private final TYPE defaultValue;

		protected PrimitiveProperty(Class<TYPE> type, TYPE defaultValue) {
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
		public PropertyNode fromObject(String key, Object object) {
			return new PropertyNode(key, this, object);
		}

		@SuppressWarnings("unchecked")
		@Override
		public TYPE toObject(PropertyNode propertyNode) {
			return (TYPE) propertyNode.getUserObject();
		}
	}

	static PropertyNode fromObject(String key, Object object) {
		for (PropertyType<?> type : ALL) {
			if (type.getType().isAssignableFrom(object.getClass())) {
				return type.fromObject(key, object);
			}
		}
		throw new IllegalArgumentException("Unsupported type: " + object.getClass().getName());
	}

}
