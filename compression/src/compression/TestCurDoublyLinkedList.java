package compression;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 * Unit tests for CurDoublyLinkedList class
 * 
 * @author cs62
 */
public class TestCurDoublyLinkedList {
	CurDoublyLinkedList<Integer> list;

	@Before
	public void setUp() throws Exception {
		list = new CurDoublyLinkedList<Integer>();
	}

	// check the initial conditions of a new list
	@Test
	public void testCurDoublyLinkedList() {
		assertNull(list.current);
		assertEquals(0, list.size());
		assertFalse(list.isOffLeft());
		assertFalse(list.isOffRight());
	}

	// create a multi-element list, confirm first takes us to head
	@Test
	public void testFirst() {
		// 83 <=> 47
		list.add(47);
		list.add(83);
		list.first();
		assertNotNull(list.current);
		assertEquals(83, (int) list.current.item, 0);
	}

	// create an empty list, confirm first() throws IllegalStateException
	@Test(expected = IllegalStateException.class)
	public void testFirstEmpty() {
		list.first();
	}

	// create a multi-element list, confirm last takes us to tail
	@Test
	public void testLast() {
		// 83 <=> 47
		list.add(47);
		list.add(83);
		list.last();

		assertNotNull(list.current);
		assertEquals(47, (int) list.current.item, 0);
	}

	// create an empty list, confirm last() throws IllegalStateException
	@Test(expected = IllegalStateException.class)
	public void testLastEmpty() {
		list.last();
	}

	// create an empty list, confirm next() throws IllegalStateException
	@Test(expected = IllegalStateException.class)
	public void testNext_EmptyList() {
		list.next();
	}

	// Calls next() on a non-empty list where current is off the right side
	@Test(expected = IllegalStateException.class)
	public void testNext_OffRightSide() {
		list.addFirst(47);
		list.next(); // this moves current off right
		list.next(); // this should trigger an exception
	}

	// Calls next() on non-empty list where current is head
	@Test
	public void testNext() {
		// 83 <=> 47
		list.add(47);
		list.add(83);
		list.first();

		assertNotNull(list.current);
		assertEquals(83, (int) list.current.item, 0);

		list.next();
		assertEquals(47, (int) list.current.item, 0);
	}

	// calls back() on non-empty list where current is tail
	@Test
	public void testBack() {
		// 83 <=> 47
		list.add(47);
		list.add(83);
		list.last();

		assertNotNull(list.current);
		assertEquals(47, (int) list.current.item, 0);

		list.back();
		assertEquals(83, (int) list.current.item, 0);
	}

	// create an empty list, confirm back() throws IllegalStateException
	@Test(expected = IllegalStateException.class)
	public void testBack_EmptyList() {
		list.back();
	}

	// Calls back() on a non-empty list where current is off the left side
	@Test(expected = IllegalStateException.class)
	public void testBack_OffLeftSide() {
		list.addFirst(47);
		list.back(); // this moves current off left
		list.back(); // this should trigger an exception
	}

	// move to right of last element and test for offRight
	@Test
	public void testIsOffRight() {
		list.add(47);
		list.next();

		assertNull(list.current);
		assertTrue(list.isOffRight());
		assertFalse(list.isOffLeft());
	}

	// move to left of first element and test for offLeft
	@Test
	public void testIsOffLeft() {
		list.add(47);
		list.back();

		assertNull(list.current);
		assertTrue(list.isOffLeft());
		assertFalse(list.isOffRight());
	}

	// test off-left and off-right
	@Test
	public void testIsOff() {
		list.add(47);
		assertFalse(list.isOff());

		list.first();
		list.back();
		assertTrue(list.isOff());

		list.last();
		list.next();
		assertTrue(list.isOff());
	}

	// Calls currentValue() on an empty list.
	@Test(expected = IllegalStateException.class)
	public void testCurrentValue_EmptyList() {
		list.currentValue();
	}

	// Calls currentValue() on non-empty list where current is off right.
	@Test(expected = IllegalStateException.class)
	public void testCurrentValue_isOff() {
		list.addFirst(47);
		list.next(); // this moves current off right
		list.currentValue(); // this should trigger an exception
	}

	// check current value for first, middle, and last
	@Test
	public void testCurrentValue() {
		// 134 <=> 84 <=> 47
		list.add(47);
		list.add(84);
		list.add(134);

		list.first(); // current points at 134
		assertEquals(134, list.currentValue(), 0);

		list.next(); // current points at 84
		assertEquals(84, list.currentValue(), 0);

		list.next(); // current points at 47
		assertEquals(47, list.currentValue(), 0);
	}

	// create list w/two, add after first, confirm correct order
	@Test
	public void testAddAfterCurrent() {
		// 47
		list.add(47);
		list.first();
		assertEquals(1, list.size());
		assertEquals(47, (int) list.current.item, 0);

		// 47 <=> 83
		list.addAfterCurrent(83);
		assertEquals(2, list.size());
		assertEquals(83, (int) list.current.item, 0); // current should point to new node

		// 47 <=> 60 <=> 83
		list.first();
		list.addAfterCurrent(60);
		assertEquals(3, list.size());
		assertEquals(60, (int) list.current.item, 0);
		list.first();
		assertEquals(47, (int) list.current.item, 0);
		list.last();
		assertEquals(83, (int) list.current.item, 0);
	}

	// position to middle, remove, check successor and contents
	@Test
	public void testRemoveCurrent() {
		// 134 <=> 84 <=> 47
		list.add(47);
		list.add(84);
		list.add(134);

		list.next();
		assertEquals(84, (int) list.current.item);

		list.removeCurrent();
		assertEquals(47, (int) list.current.item);

		assertEquals(2, list.size());
		list.first();
		assertEquals(134, (int) list.current.item);
		list.next();
		assertEquals(47, (int) list.current.item);
	}

	// position to end, addFirst, confirm where it was added
	@Test
	public void testAddFirst() {
		// 134 <=> 84 <=> 47
		list.add(47);
		list.add(84);

		list.last();
		assertEquals(47, (int) list.current.item);

		list.addFirst(134);
		assertEquals(3, list.size());
		assertEquals(134, (int) list.current.item);

		list.first();
		assertEquals(134, (int) list.current.item);
		list.next();
		assertEquals(84, (int) list.current.item);
		list.next();
		assertEquals(47, (int) list.current.item);
	}

	// create 3 item list, remove first element
	@Test
	public void testRemoveFirst() {
		// 134 <=> 84 <=> 47
		list.add(47);
		list.add(84);
		list.add(134);

		list.last();
		assertEquals(47, (int) list.current.item);

		assertEquals(134, (int) list.removeFirst());
		assertEquals(84, (int) list.current.item);

		assertEquals(2, list.size());
		list.last();
		assertEquals(47, (int) list.current.item);
		list.back();
		assertEquals(84, (int) list.current.item);
	}

	// create list, add to end, confirm correct list
	@Test
	public void testAddLast() {
		// 84 <=> 47 <=> 134
		list.add(47);
		list.add(84);

		assertEquals(84, (int) list.current.item);

		list.addLast(134);
		assertEquals(3, list.size());
		assertEquals(134, (int) list.current.item);

		list.first();
		assertEquals(84, (int) list.current.item);
		list.next();
		assertEquals(47, (int) list.current.item);
		list.next();
		assertEquals(134, (int) list.current.item);
	}

	// create three element list, current to first, remove last
	@Test
	public void testRemoveLast() {
		// 134 <=> 84 <=> 47
		list.add(47);
		list.add(84);
		list.add(134);

		assertEquals(134, (int) list.current.item);

		list.removeLast();
		assertNull(list.current);
		assertTrue(list.isOffRight());
		assertFalse(list.isOffLeft());

		assertEquals(2, list.size());
		list.first();
		assertEquals(134, (int) list.current.item);
		list.next();
		assertEquals(84, (int) list.current.item);
	}

	// create three element list and getFirst (where that is not current)
	@Test
	public void testGetFirst() {
		// 134 <=> 84 <=> 47
		list.add(47);
		list.add(84);
		list.add(134);

		list.last();
		assertEquals(47, (int) list.current.item);
		assertEquals(134, (int) list.getFirst());
		assertEquals(134, (int) list.current.item);
	}

	// create three element list and getLast (where that is not current)
	@Test
	public void testGetLast() {
		// 134 <=> 84 <=> 47
		list.add(47);
		list.add(84);
		list.add(134);

		assertEquals(134, (int) list.current.item);
		assertEquals(47, (int) list.getLast());
		assertEquals(47, (int) list.current.item);
	}

	// clear list, confirm it is empty, and not off
	@Test
	public void testClear() {
		list.add(47);
		list.add(83);
		list.clear();
		assertNull(list.current);
		assertEquals(0, list.size());
		assertFalse(list.isOffLeft());
		assertFalse(list.isOffRight());
	}
}
