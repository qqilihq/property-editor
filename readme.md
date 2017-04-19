Hierarchical Property List Editor
=================================

![Run status](https://api.shippable.com/projects/58f65c38d1780a07007b7145/badge?branch=master)

![Demo](demo.gif)

An editor for hierarchical properties as Java Swing component. Shamelessly
inspired by Apple's Property List Editor in Xcode. It allows building data
structures based on `Map`s and `List`s and primitive types such as `Long`,
`Double`, and `Boolean`. It is originally created for the [KNIME Selenium
Nodes][1] but it could probably be of general interest.

The editor is built on top of SwingX.

Download
--------

Via Maven:

```xml
<dependency>
	<groupId>de.philippkatz.swing.property</groupId>
	<artifactId>property-editor</artifactId>
	<version>1.0</version>
</dependency>
```

Usage
-----

```java
// create a new component and pass the data
Map<String, Object> input = …
PropertiesTreeTableModel model = new PropertiesTreeTableModel(PropertyNode.from(input));
PropertiesEditor editor = new PropertiesEditor(model);

// access to the data; this is either a Map<String, Object> or a List<Object>
Object result = model.getData();
```

Full Working Example
-----------------------

Have a look at `PropertiesEditorDemo` for a more detailed version.

```java
public class PropertiesEditorDemo {

	private static PropertyNode createSampleData() {
		Map<String, Object> root = new LinkedHashMap<>();
		root.put("string", "The quick brown fox");
		root.put("boolean", true);
		root.put("numbers", 42);
		root.put("fruits", Arrays.asList("apple", "orange", "grapefruit"));
		return PropertyNode.from(root);
	}

	public static void main(String[] args) {
		JFrame frame = new JFrame();
		frame.setTitle("Properties Editor Demo");
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.setMinimumSize(new Dimension(600, 400));
		frame.add(new PropertiesEditor(new PropertiesTreeTableModel(createSampleData())));
		frame.setVisible(true);
	}

}
```

Contributing
------------

Pull requests are very welcome. Feel free to discuss bugs or new features by
opening a new [issue][2].

License
-------

[Apache License Version 2.0](https://www.apache.org/licenses/LICENSE-2.0.html)

- - -

Copyright (c) 2017 Philipp Katz

[1]: http://seleniumnodes.com
[2]: https://github.com/qqilihq/property-editor/issues
