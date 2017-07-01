package de.philippkatz.swing.property;

import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;

class TreeModelListenerMock implements TreeModelListener {

	private int listenerCallCount = 0;
	private TreeModelEvent latestEvent;

	@Override
	public final void treeStructureChanged(TreeModelEvent e) {
		record(e);
	}

	@Override
	public final void treeNodesRemoved(TreeModelEvent e) {
		record(e);
	}

	@Override
	public final void treeNodesInserted(TreeModelEvent e) {
		record(e);
	}

	@Override
	public final void treeNodesChanged(TreeModelEvent e) {
		record(e);
	}

	private void record(TreeModelEvent e) {
		listenerCallCount++;
		latestEvent = e;
	}

	public int getListenerCallCount() {
		return listenerCallCount;
	}

	public TreeModelEvent getLatestEvent() {
		return latestEvent;
	}

}