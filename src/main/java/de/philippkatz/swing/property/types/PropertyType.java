package de.philippkatz.swing.property.types;

import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

import de.philippkatz.swing.property.PropertiesEditorConfig;
import de.philippkatz.swing.property.types.PropertyTypes.PrimitiveType;

/**
 * Property type description which can be used in the editor. This interface
 * defines the meta data of these types and conversion methods between Java
 * types and the corresponding nodes used within the editor.
 * 
 * @author Philipp Katz
 * @param <TYPE>
 *            The JavaType which corresponds with this property type.
 * @see PropertyTypes
 * @see PrimitiveType
 */
public interface PropertyType<TYPE> {

	/**
	 * @return <code>true</code> in case this is a collection type (i.e.
	 *         contains children).
	 */
	boolean isCollection();

	/**
	 * @return The default value of the property. <code>null</code> for
	 *         collection types.
	 */
	TYPE getDefaultValue();

	/**
	 * @return The underlying Java type.
	 */
	Class<? super TYPE> getType();

	/**
	 * Converts a Java object to a property node.
	 * 
	 * @param key
	 *            (optional) key.
	 * @param object
	 *            The object.
	 * @param config
	 *            The configuration.
	 * @return The property node.
	 */
	PropertyNode fromObject(String key, Object object, PropertiesEditorConfig config);

	/**
	 * Converts a PropertyNode to a Java object.
	 * 
	 * @param propertyNode
	 *            The property node.
	 * @return The Java object.
	 */
	TYPE toObject(PropertyNode propertyNode);

	// added in v2.1

	/**
	 * Allow to customize the table cell renderer.
	 * 
	 * @return The renderer, or <code>null</code> to use the default.
	 * @since 2.1
	 */
	default TableCellRenderer getRenderer() {
		return null;
	}

	/**
	 * Allow to customize the table cell editor.
	 * 
	 * @return The editor, or <code>null</code> to use the default.
	 * @since 2.1
	 */
	default TableCellEditor getEditor() {
		return null;
	}

}
