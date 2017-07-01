package de.philippkatz.swing.property;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class PropertiesTreeTableModelTest {

	@Test
	public void treeNodesChangedShouldOnlyBeCalledOnceWhenInvokingFireChildrenChanged() {
		PropertiesTreeTableModel model = new PropertiesTreeTableModel(Arrays.asList(1, 2, 3, 4));
		TreeModelListenerMock listenerMock = new TreeModelListenerMock();
		model.addTreeModelListener(listenerMock);
		model.fireChildrenChanged(model.getRoot());
		assertEquals(1, listenerMock.getListenerCallCount());
		assertArrayEquals(new int[] { 0, 1, 2, 3 }, listenerMock.getLatestEvent().getChildIndices());
	}

	@Test(expected = IllegalArgumentException.class)
	public void constructorThrowsExceptionOnNoCollectionType() {
		new PropertiesTreeTableModel(1);
	}

	@Test
	public void changingBetweenPrimitiveTypesShouldTriggerListener() {
		Map<String, Object> data = new HashMap<>();
		data.put("key", "value");

		PropertiesTreeTableModel model = new PropertiesTreeTableModel(data);
		TreeModelListenerMock listenerMock = new TreeModelListenerMock();
		model.addTreeModelListener(listenerMock);

		model.setValueAt(model.getConfig().getType(Integer.class), model.getChild(model.getRoot(), 0), 1);

		assertEquals(1, listenerMock.getListenerCallCount());
		assertArrayEquals(new int[] { 0 }, listenerMock.getLatestEvent().getChildIndices());
	}

}
