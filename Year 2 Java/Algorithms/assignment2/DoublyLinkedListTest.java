package assignment2;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

//-------------------------------------------------------------------------
/**
 * Test class for Doubly Linked List
 *
 * @author
 * @version 13/10/16 18:15
 */
@RunWith(JUnit4.class)
public class DoublyLinkedListTest {
	// ~ Constructor ........................................................
	@Test
	public void testConstructor() {
		new DoublyLinkedList<Integer>();
	}

	// ~ Public Methods ........................................................

	// ----------------------------------------------------------
	/**
	 * Check if the insertBefore works
	 */
	@Test
	public void testInsertBefore() {
		// test non-empty list
		DoublyLinkedList<Integer> testDLL = new DoublyLinkedList<Integer>();
		testDLL.insertBefore(0, 1);
		testDLL.insertBefore(1, 2);
		testDLL.insertBefore(2, 3);

		testDLL.insertBefore(0, 4);
		assertEquals("Checking insertBefore to a list containing 3 elements at position 0", "4,1,2,3",
				testDLL.toString());
		testDLL.insertBefore(1, 5);
		assertEquals("Checking insertBefore to a list containing 4 elements at position 1", "4,5,1,2,3",
				testDLL.toString());
		testDLL.insertBefore(2, 6);
		assertEquals("Checking insertBefore to a list containing 5 elements at position 2", "4,5,6,1,2,3",
				testDLL.toString());
		testDLL.insertBefore(-1, 7);
		assertEquals(
				"Checking insertBefore to a list containing 6 elements at position -1 - expected the element at the head of the list",
				"7,4,5,6,1,2,3", testDLL.toString());
		testDLL.insertBefore(7, 8);
		assertEquals(
				"Checking insertBefore to a list containing 7 elemenets at position 8 - expected the element at the tail of the list",
				"7,4,5,6,1,2,3,8", testDLL.toString());
		testDLL.insertBefore(700, 9);
		assertEquals(
				"Checking insertBefore to a list containing 8 elements at position 700 - expected the element at the tail of the list",
				"7,4,5,6,1,2,3,8,9", testDLL.toString());

		// test empty list
		testDLL = new DoublyLinkedList<Integer>();
		testDLL.insertBefore(0, 1);
		assertEquals(
				"Checking insertBefore to an empty list at position 0 - expected the element at the head of the list",
				"1", testDLL.toString());
		testDLL = new DoublyLinkedList<Integer>();
		testDLL.insertBefore(10, 1);
		assertEquals(
				"Checking insertBefore to an empty list at position 10 - expected the element at the head of the list",
				"1", testDLL.toString());
		testDLL = new DoublyLinkedList<Integer>();
		testDLL.insertBefore(-10, 1);
		assertEquals(
				"Checking insertBefore to an empty list at position -10 - expected the element at the head of the list",
				"1", testDLL.toString());
	}

	@Test
	public void testIsEmpty() {
		DoublyLinkedList<Integer> testDLL = new DoublyLinkedList<Integer>();
		assertTrue("", testDLL.isEmpty());
		testDLL.firstNode(1);
		assertFalse("", testDLL.isEmpty());
	}

	@Test
	public void testGet() {
		DoublyLinkedList<Integer> testDLL = new DoublyLinkedList<Integer>();
		assertNull("", testDLL.get(0));
		testDLL.firstNode(1);
		assertEquals("", Integer.valueOf(1), testDLL.get(0));
		testDLL.insertAtEnd(2);
		testDLL.insertAtEnd(3);
		assertNull("", testDLL.get(3));
	}

	@Test
	public void testDeleteAt() {
		DoublyLinkedList<Integer> testDLL = new DoublyLinkedList<Integer>();
		testDLL.firstNode(1);
		assertTrue("", testDLL.deleteAt(0));
		testDLL.firstNode(1);
		testDLL.insertAtEnd(2);
		testDLL.insertAtEnd(3);
		testDLL.insertAtEnd(4);
		testDLL.insertAtEnd(5);
		assertTrue("", testDLL.deleteAt(0));
		assertTrue("", testDLL.deleteAt(2));
		assertTrue("", testDLL.deleteAt(1));
		assertFalse("", testDLL.deleteAt(7));
	}
	
	@Test
	public void testReverse() {
		DoublyLinkedList<Integer> testDLL = new DoublyLinkedList<Integer>();
		testDLL.firstNode(1);
		assertEquals("", "1", testDLL.toString());
		testDLL.insertAtEnd(2);
		testDLL.insertAtEnd(3);
		testDLL.reverse();
		assertEquals("", "3,2,1", testDLL.toString());
	}

	@Test
	public void testMakeUnique() {
		DoublyLinkedList<Integer> testDLL = new DoublyLinkedList<Integer>();
		testDLL.firstNode(1);
		testDLL.insertAtEnd(2);
		testDLL.insertAtEnd(1);
		testDLL.makeUnique();
		assertEquals("", "1,2", testDLL.toString());
		testDLL = new DoublyLinkedList<Integer>();
		testDLL.firstNode(2);
		testDLL.insertAtEnd(2);
		testDLL.insertAtEnd(2);
		testDLL.insertAtEnd(1);
		testDLL.insertAtEnd(1);
		testDLL.insertAtEnd(1);
		testDLL.makeUnique();
		testDLL.insertBefore(1, 3);
		assertEquals("", "2,3,1", testDLL.toString());
	}

	@Test
	public void testPush() {
		DoublyLinkedList<Integer> testDLL = new DoublyLinkedList<Integer>();
		testDLL.push(1);
		testDLL.push(2);
		testDLL.push(3);
		assertEquals("", "3,2,1", testDLL.toString());
	}

	@Test
	public void testPop() {
		DoublyLinkedList<Integer> testDLL = new DoublyLinkedList<Integer>();
		assertNull("", testDLL.pop());
		testDLL.push(1);
		testDLL.push(2);
		testDLL.push(3);
		testDLL.pop();
		assertEquals("", "2,1", testDLL.toString());
	}

	@Test
	public void testEnqueue() {
		DoublyLinkedList<Integer> testDLL = new DoublyLinkedList<Integer>();
		testDLL.firstNode(1);
		testDLL.enqueue(2);
		testDLL.enqueue(3);
		assertEquals("", "3,2,1", testDLL.toString());
	}

	@Test
	public void testDequeue() {
		DoublyLinkedList<Integer> testDLL = new DoublyLinkedList<Integer>();
		assertNull("", testDLL.dequeue());
		testDLL.firstNode(1);
		testDLL.dequeue();
		assertEquals("", "", testDLL.toString());
		testDLL.firstNode(1);
		testDLL.insertAtEnd(2);
		testDLL.insertAtEnd(3);
		testDLL.dequeue();
		assertEquals("", "1,2", testDLL.toString());
	}
}