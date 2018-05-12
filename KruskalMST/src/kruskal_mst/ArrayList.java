package kruskal_mst;
/**
 * Contains information about the ArrayList class
 * 
 * @author Samantha Ryan
 * @param <E>
 *            generic parameter
 */
@SuppressWarnings("unchecked")
public class ArrayList<E>  {
	/** Initial capacity for the List */
	public static final int INIT_SIZE = 5;
	private E[] list;
	private int size;

	/**
	 * Constructs an ArrayList with a default size of 0 and a default capacity
	 * as INIT_SIZE
	 */
	public ArrayList() {
		size = 0; 
		list = (E[]) new Object[INIT_SIZE];
	}
 
	/** 
	 * gets the element at a certain index
	 * @return element at a certain index
	 */
	
	public E get(int index) {
		if (index < 0 || index >= size()) {
			throw new IndexOutOfBoundsException();
		}
		return list[index];
	}

	/**
	 * returns the number of elements in the list
	 * @return size of list
	 */
	public int size() {
		return size;
	}

	/**
	 * Adds an element to the list at a specific index If index is larger than
	 * size capacity, then the list capacity is doubled
	 */
	
	public void add(int i, Object element) {
		if (size == list.length) {
			throw new IllegalArgumentException();
		}
		// Check valid index
		if (i < 0 || i > size()) {
			throw new IndexOutOfBoundsException();
		} 

		// Check null element
		if (element == null) { 
			throw new NullPointerException();
		}

		// Check duplicate element
		for (int k = 0; k < size(); k++) {
			if (list[k].equals(element)) {
				throw new IllegalArgumentException();
			}
		}

		if (i == size()) {
			list[i] = (E) element;
		} else {
			for (int k = size(); k > i; k--) {
				list[k] = list[k - 1];
			}
			list[i] = (E) element;
		}
		size++;
		if (size() == list.length) {
			growArray();
		}
	}

	/**
	 * Removes an element from the list at a given index
	 */
	
	public E remove(int i) {
		if (i < 0 || i >= size()) {
			throw new IndexOutOfBoundsException();
		}

		E removed = list[i];

		if (i == size() - 1) {
			list[i] = null;
			size--;
			return removed;
		} else {

			for (int k = i; k < size(); k++) {
				list[k] = list[k + 1];
			}
		}
		list[size() - 1] = null;
		size--;
		return removed;
	}

	/**
	 * Doubles the capacity of  the Array
	 */
	private void growArray() {
		E[] temp = (E[]) new Object[list.length * 2];
		for (int i = 0; i < list.length; i++) {
			temp[i] = list[i];
		}
		list = temp;
	}

	/**
	 * Sets an index to a specific element without altering the rest of the list
	 */
	
	public E set(int index, E element) {
		
		if (element == null) {
			throw new NullPointerException();
		}
		if (index < 0 || index >= size()){
			throw new IndexOutOfBoundsException();
		}
		
		for (int i = 0; i < size(); i++){
			if (list[i].equals(element)){
				throw new IllegalArgumentException();
			}
		}
		E oldElement = list[index];
		list[index] = element;
		return oldElement;

	}

}
