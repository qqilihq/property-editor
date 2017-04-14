package de.philippkatz.swing.property;

import java.awt.Dimension;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.WindowConstants;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;

import de.philippkatz.swing.property.types.PropertyNode;

@SuppressWarnings("serial")
public class PropertiesEditorDemo extends JFrame implements TreeModelListener {

	private final PropertiesTreeTableModel treeTableModel;

	private static PropertyNode createSampleData() {

		Map<String, Object> root = new LinkedHashMap<>();
		root.put("string", "The quick brown fox");
		root.put("boolean", true);
		root.put("numbers", 42);

		Map<String, Object> languages = new LinkedHashMap<>();
		languages.put("DE", "German");
		languages.put("EN", "English");
		languages.put("FR", "French");
		root.put("languages", languages);

		root.put("fruits", Arrays.asList("apple", "orange", "grapefruit"));

		return PropertyNode.from(root);

	}

	private PropertiesEditorDemo() {
		setTitle("Properties Editor Demo");
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setMinimumSize(new Dimension(600, 400));

		treeTableModel = new PropertiesTreeTableModel(createSampleData());
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
		new PropertiesEditorDemo();
	}

}
