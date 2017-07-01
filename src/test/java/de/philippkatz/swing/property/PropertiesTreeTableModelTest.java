package de.philippkatz.swing.property;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import javax.swing.event.TreeModelEvent;

import org.junit.Test;

public class PropertiesTreeTableModelTest {

	@Test
	public void treeNodesChangedShouldOnlyBeCalledOnceWhenInvokingFireChildrenChanged() {
		PropertiesTreeTableModel model = new PropertiesTreeTableModel(Arrays.asList(1, 2, 3, 4));

		int[] listenerCallCount = { 0 };
		TreeModelEvent[] listenerEvent = { null };

		model.addTreeModelListener(new TreeModelAdapter() {
			public void treeNodesChanged(TreeModelEvent e) {
				listenerCallCount[0]++;
				listenerEvent[0] = e;
			};
		});
		model.fireChildrenChanged(model.getRoot());
		assertEquals(1, listenerCallCount[0]);
		assertArrayEquals(new int[] { 0, 1, 2, 3 }, listenerEvent[0].getChildIndices());
	}

	@Test(expected = IllegalArgumentException.class)
	public void constructorThrowsExceptionOnNoCollectionType() {
		new PropertiesTreeTableModel(1);
	}

}
