package de.philippkatz.swing.property.types;

import org.jdesktop.swingx.treetable.AbstractMutableTreeTableNode;
import org.jdesktop.swingx.treetable.MutableTreeTableNode;

public class PropertyNode extends AbstractMutableTreeTableNode {
	
	public static final class ChildCount {
		public final int count;

		private ChildCount(int count) {
			this.count = count;
		}
	}


	private String key;

	private PropertyType<?> type;

	public static PropertyNode from(Object object) {
		return PropertyTypes.fromObject(null, object);
	}

	public static PropertyNode empty() {
		return new PropertyNode(null, PropertyTypes.OBJECT, null);
	}

	public static PropertyNode string() {
		return new PropertyNode(null, PropertyTypes.STRING, "");
	}

	PropertyNode(String key, PropertyType<?> type, Object value) {
		super(value);
		this.key = key;
		this.type = type;
	}

	public String getKey() {
		if (isInArray()) {
			return "[" + getParent().getIndex(this) + "]";
		} else {
			return key;
		}
	}

	private void setKey(String key) {
		this.key = key;
	}

	public PropertyType<?> getType() {
		return type;
	}

	private void setType(PropertyType<?> type) {
		this.type = type;
		userObject = type.getDefaultValue();
	}

	public Object toObject() {
		return getType().toObject(this);
	}

	/**
	 * @return <code>true</code> in case this node is contained within an array.
	 */
	private boolean isInArray() {
		if (getParent() == null) {
			return false;
		} else {
			PropertyNode parentProperty = (PropertyNode) getParent();
			return parentProperty.getType() == PropertyTypes.ARRAY;
		}
	}

	// AbstractMutableTreeTableNode overrides

	@Override
	public boolean getAllowsChildren() {
		return type.isCollection();
	}

	@Override
	public int getColumnCount() {
		return 3;
	}

	@Override
	public Object getValueAt(int column) {
		switch (column) {
		case 0:
			return getKey();
		case 1:
			return getType();
		case 2:
			if (type.isCollection()) {
				return new ChildCount(getChildCount());
			} else {
				return userObject;
			}
		default:
			throw new IllegalArgumentException("Invalid column: " + column);
		}
	}

	@Override
	public boolean isEditable(int column) {
		switch (column) {
		case 0:
			return !isInArray();
		case 1:
			return true;
		case 2:
			return !getAllowsChildren();
		default:
			throw new IllegalArgumentException("Invalid column: " + column);
		}
	}

	@Override
	public void setValueAt(Object aValue, int column) {
		switch (column) {
		case 0:
			setKey((String) aValue);
			return;
		case 1:
			setType((PropertyType<?>) aValue);
			return;
		case 2:
			setUserObject(aValue);
			return;
		default:
			throw new IllegalArgumentException("Invalid column: " + column);
		}
	}

	@Override
	public void setParent(MutableTreeTableNode newParent) {
		super.setParent(newParent);

		generateKey();
	}

	/**
	 * Generates a default key for this property
	 */
	private void generateKey() {
		if (getParent() != null && key == null) {
			for (int keyIndex = getParent().getChildCount();; keyIndex++) {
				String key = "item " + keyIndex;
				boolean taken = false;
				// check if key is not yet taken
				for (int childIndex = 0; childIndex < getParent().getChildCount(); childIndex++) {
					PropertyNode childNode = (PropertyNode) getParent().getChildAt(childIndex);
					if (key.equals(childNode.getKey())) {
						taken = true;
						break;
					}
				}
				if (!taken) {
					this.key = key;
					return;
				}
			}
		}
	}

	// Object overrides

	@Override
	public String toString() {
		return getKey();
	}

}
