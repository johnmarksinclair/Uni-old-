import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.*;

// -------------------------------------------------------------------------
/**
 *  This class contains the methods of Doubly Linked List.
 *
 *  @author  
 *  @version 09/10/18 11:13:22
 */

/**
 * Class DoublyLinkedList: implements a *generic* Doubly Linked List.
 * 
 * @param <T> This is a type parameter. T is used as a class name in the
 *            definition of this class.
 *
 *            When creating a new DoublyLinkedList, T should be instantiated
 *            with an actual class name that extends the class Comparable. Such
 *            classes include String and Integer.
 *
 *            For example to create a new DoublyLinkedList class containing
 *            String data: DoublyLinkedList<String> myStringList = new
 *            DoublyLinkedList<String>();
 *
 *            The class offers a toString() method which returns a
 *            comma-separated sting of all elements in the data structure.
 * 
 *            This is a bare minimum class you would need to completely
 *            implement. You can add additional methods to support your code.
 *            Each method will need to be tested by your jUnit tests -- for
 *            simplicity in jUnit testing introduce only public methods.
 */
class DoublyLinkedList<T extends Comparable<T>> {

	/**
	 * private class DLLNode: implements a *generic* Doubly Linked List node.
	 */
	private class DLLNode {
		public final T data; // this field should never be updated. It gets its
								// value once from the constructor DLLNode.
		public DLLNode next;
		public DLLNode prev;

		/**
		 * Constructor
		 * 
		 * @param theData  : data of type T, to be stored in the node
		 * @param prevNode : the previous Node in the Doubly Linked List
		 * @param nextNode : the next Node in the Doubly Linked List
		 * @return DLLNode
		 */
		public DLLNode(T theData, DLLNode prevNode, DLLNode nextNode) {
			data = theData;
			prev = prevNode;
			next = nextNode;
		}
	}

	// Fields head and tail point to the first and last nodes of the list.
	private DLLNode head, tail;

	/**
	 * Constructor of an empty DLL
	 * 
	 * @return DoublyLinkedList
	 */
	public DoublyLinkedList() {
		head = null;
		tail = null;
	}

	/**
	 * Tests if the doubly linked list is empty
	 * 
	 * @return true if list is empty, and false otherwise
	 *
	 *         Worst-case asymptotic running time cost: Theta(1)
	 *
	 *         Justification: Regardless of the input size this method only checks
	 *         head once, giving it a Theta(1) worst case running time
	 */
	public boolean isEmpty() {
		return (head == null);
	}

	/**
	 * Inserts an element in the doubly linked list
	 * 
	 * @param pos  : The integer location at which the new data should be inserted
	 *             in the list. We assume that the first position in the list is 0
	 *             (zero). If pos is less than 0 then add to the head of the list.
	 *             If pos is greater or equal to the size of the list then add the
	 *             element at the end of the list.
	 * @param data : The new data of class T that needs to be added to the list
	 * @return none
	 *
	 *         Worst-case asymptotic running time cost: Theta(N)
	 *
	 *         Justification: worst case running
	 */
	public void insertBefore(int pos, T data) {
		if (isEmpty()) {
			firstNode(data);
		} else if (pos < 1) {
			insertAtFront(data);
		} else {
			DLLNode node = head;
			int index = 0;
			while (node != null) {
				if (index == pos) {
					DLLNode newNode = new DLLNode(data, node.prev, node);
					node.prev.next = newNode;
					node.prev = newNode;
					return;
				}
				node = node.next;
				index++;
			}
			insertAtEnd(data);
		}
		return;
	}

	public void firstNode(T data) {
		DLLNode node = new DLLNode(data, null, null);
		head = node;
		tail = node;
	}

	public void insertAtFront(T data) {
		DLLNode temp = head;
		DLLNode front = new DLLNode(data, null, temp);
		temp.prev = front;
		head = front;
	}

	public void insertAtEnd(T data) {
		DLLNode temp = tail;
		DLLNode end = new DLLNode(data, temp, null);
		temp.next = end;
		tail = end;
	}

	/**
	 * Returns the data stored at a particular position
	 * 
	 * @param pos : the position
	 * @return the data at pos, if pos is within the bounds of the list, and null
	 *         otherwise.
	 *
	 *         Worst-case asymptotic running time cost: Theta(N)
	 *
	 *         Justification: Worst case asymptotic running time would be Theta(N)
	 *         because worst case pos is the tail node and so this method will run n
	 *         times.
	 *
	 */
	public T get(int pos) {
		DLLNode node = head;
		int index = 0;
		while (node != null) {
			if (index == pos) {
				return node.data;
			}
			index++;
			node = node.next;
		}
		return null;
	}

	/**
	 * Deletes the element of the list at position pos. First element in the list
	 * has position 0. If pos points outside the elements of the list then no
	 * modification happens to the list.
	 * 
	 * @param pos : the position to delete in the list.
	 * @return true : on successful deletion, false : list has not been modified.
	 *
	 *         Worst-case asymptotic running time cost: Theta(N)
	 *
	 *         Justification: Linear while loop, worst case pos = tail index and
	 *         function will iterate through whole list.
	 */
	public boolean deleteAt(int pos) {
		DLLNode node = head;
		int index = 0;
		while (node != null) {
			if (pos == 0 && node.next == null && node.prev == null) { // only one node in list
				head = null;
				tail = null;
				return true;
			} else if (pos == 0) { // deleting first node in list (where list > 1 elements)
				head = head.next;
				head.prev = null;
				return true;
			} else if (pos == index) {
				if (node.next != null) { // if its a middle node
					node.next.prev = node.prev;
					node.prev.next = node.next;
					return true;
				} else if (node.next == null) { // deleting last node
					tail.prev.next = null;
					tail = tail.prev;
					return true;
				}
			}
			index++;
			node = node.next;
		}
		return false;
	}

	/**
	 * Reverses the list. If the list contains "A", "B", "C", "D" before the method
	 * is called Then it should contain "D", "C", "B", "A" after it returns.
	 *
	 * Worst-case asymptotic running time cost: Theta(N)
	 *
	 * Justification: Another linear while loop, this method iterates n times.
	 */
	public void reverse() {
		DLLNode node = head;
		DLLNode tempNode = null;
		while (node != null) {
			tempNode = node.prev;
			node.prev = node.next;
			node.next = tempNode;
			node = node.prev;
		}
		if (tempNode != null) {
			head = tempNode.prev;
		}
	}

	/**
	 * Removes all duplicate elements from the list. The method should remove the
	 * _least_number_ of elements to make all elements uniqueue. If the list
	 * contains "A", "B", "C", "B", "D", "A" before the method is called Then it
	 * should contain "A", "B", "C", "D" after it returns. The relative order of
	 * elements in the resulting list should be the same as the starting list.
	 *
	 * Worst-case asymptotic running time cost: Theta(N^2)
	 *
	 * Justification: Nested while loops
	 */
	public void makeUnique() {
		DLLNode node1 = head;
		DLLNode node2 = null;
		int index1 = 0;
		int index2 = 0;
		while (node1 != null) {
			node2 = node1.next;
			index2 = index1 + 1;
			while (node2 != null) {
				if (node1.data.compareTo(node2.data) == 0) {
					deleteAt(index2);
					index2 = index1;
					node2 = node1;
				}
				index2++;
				node2 = node2.next;
			}
			index1++;
			node1 = node1.next;
		}
	}

	/*----------------------- STACK API 
	 * If only the push and pop methods are called the data structure should behave like a stack.
	 */

	/**
	 * This method adds an element to the data structure. How exactly this will be
	 * represented in the Doubly Linked List is up to the programmer.
	 * 
	 * @param item : the item to push on the stack
	 *
	 *             Worst-case asymptotic running time cost: Theta(1)
	 *
	 *             Justification: Regardless of the input size this method only runs once
	 */
	public void push(T item) {
		if (isEmpty()) {
			firstNode(item);
		} else {
			insertAtFront(item);
		}
	}

	/**
	 * This method returns and removes the element that was most recently added by
	 * the push method.
	 * 
	 * @return the last item inserted with a push; or null when the list is empty.
	 *
	 *         Worst-case asymptotic running time cost: Theta(N)
	 *
	 *         Justification: This method calls the deleteAt() method which has a 
	 *         worst case asymptotic running time of Theta(N)
	 */
	public T pop() {
		if (!isEmpty()) {
			T item = get(0);
			deleteAt(0);
			return item;
		} else {
			return null;
		}
	}

	/*----------------------- QUEUE API
	 * If only the enqueue and dequeue methods are called the data structure should behave like a FIFO queue.
	 */

	/**
	 * This method adds an element to the data structure. How exactly this will be
	 * represented in the Doubly Linked List is up to the programmer.
	 * 
	 * @param item : the item to be enqueued to the stack
	 *
	 *             Worst-case asymptotic running time cost: Theta(1)
	 *
	 *             Justification: Regardless of the input size this method only runs once
	 */
	public void enqueue(T item) {
		push(item);
	}

	/**
	 * This method returns and removes the element that was least recently added by
	 * the enqueue method.
	 * 
	 * @return the earliest item inserted with an equeue; or null when the list is
	 *         empty.
	 *
	 *         Worst-case asymptotic running time cost: Theta(1)
	 *
	 *         Justification: Regardless of the input size this method only runs once
	 */
	public T dequeue() {
		if (!isEmpty()) {
			if (head == tail) {
				T val = tail.data;
				head = null;
				tail = null;
				return val;
			} else {
				T val = tail.data;
				tail = tail.prev;
				tail.next = null;
				return val;
			}
		} else {
			return null;
		}
	}

	/**
	 * @return a string with the elements of the list as a comma-separated list,
	 *         from beginning to end
	 *
	 *         Worst-case asymptotic running time cost: Theta(n)
	 *
	 *         Justification: We know from the Java documentation that
	 *         StringBuilder's append() method runs in Theta(1) asymptotic time. We
	 *         assume all other method calls here (e.g., the iterator methods above,
	 *         and the toString method) will execute in Theta(1) time. Thus, every
	 *         one iteration of the for-loop will have cost Theta(1). Suppose the
	 *         doubly-linked list has 'n' elements. The for-loop will always iterate
	 *         over all n elements of the list, and therefore the total cost of this
	 *         method will be n*Theta(1) = Theta(n).
	 */
	public String toString() {
		StringBuilder s = new StringBuilder();
		boolean isFirst = true;

		// iterate over the list, starting from the head
		for (DLLNode iter = head; iter != null; iter = iter.next) {
			if (!isFirst) {
				s.append(",");
			} else {
				isFirst = false;
			}
			s.append(iter.data.toString());
		}

		return s.toString();
	}

}