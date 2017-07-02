package System;

import java.util.ArrayList;

import user.UserInterface;

public class MinHeap {

	private ArrayList<UserInterface> list;
	private Compare compare;

	public MinHeap(Compare compare) {
		this.list = new ArrayList<UserInterface>();
		this.compare = compare;
	}

	public void insert(UserInterface item) {

		list.add(item);
		int i = list.size() - 1;
		int parent = parent(i);

		while (parent != i && compare.comp(list.get(i), list.get(parent)) < 0) {

			swap(i, parent);
			i = parent;
			parent = parent(i);
		}
		
		if(list.size() > 20)
			extractMin();
	}

	public void buildHeap() {

		for (int i = list.size() / 2; i >= 0; i--) {
			minHeapify(i);
		}
	}

	public UserInterface extractMin() {

		if (list.size() == 0) {
			throw new IllegalStateException("MinHeap is EMPTY");
			
		} else if (list.size() == 1) {
			UserInterface min = list.remove(0);
			return min;
		}

		// remove the last item ,and set it as new root
		UserInterface min = list.get(0);
		UserInterface lastItem = list.remove(list.size() - 1);
		list.set(0, lastItem);

		// bubble-down until heap property is maintained
		minHeapify(0);

		// return min key
		return min;
	}

	private void minHeapify(int i) {

		int left = left(i);
		int right = right(i);
		int smallest = -1;

		// find the smallest key between current node and its children.
		if (left <= list.size() - 1 && compare.comp(list.get(left), list.get(i)) < 0) {
			smallest = left;
		} else {
			smallest = i;
		}

		if (right <= list.size() - 1 && compare.comp(list.get(right), list.get(smallest)) < 0) {
			smallest = right;
		}

		// if the smallest key is not the current key then bubble-down it.
		if (smallest != i) {

			swap(i, smallest);
			minHeapify(smallest);
		}
	}

	public UserInterface getMin() {

		return list.get(0);
	}

	public boolean isEmpty() {

		return list.size() == 0;
	}

	private int right(int i) {

		return 2 * i + 2;
	}

	private int left(int i) {

		return 2 * i + 1;
	}

	private int parent(int i) {

		if (i % 2 == 1) {
			return i / 2;
		}

		return (i - 1) / 2;
	}

	private void swap(int i, int parent) {

		UserInterface temp = list.get(parent);
		list.set(parent, list.get(i));
		list.set(i, temp);
	}

}