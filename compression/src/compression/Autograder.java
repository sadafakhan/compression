package compression;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for CurDoublyLinkedList class
 *
 * @author cs62
 */
public class Autograder {
	private CurDoublyLinkedList<Integer> list;

	/**
	 * Sets up each test from scratch by creating a new empty list.
	 */
	@BeforeEach
	public void setUp() throws Exception {
		list = new CurDoublyLinkedList<Integer>();
	}

	/**
	 * Check the initial conditions of a new list
	 */
	@Test
	public void testCurDoublyLinkedList() {
		assertNull(list.current);
		assertEquals(0, list.size());
		assertFalse(list.isOffLeft());
		assertFalse(list.isOffRight());
	}

	/**
	 * When adding new nodes using .add(), first should
	 */
	@Test
	public void testFirst() {
		// 47
		// one element
		list.add(47);
		list.first();
		assertNotNull(list.current, "current not null after calling first().");
		assertEquals(47, (int) list.current.item, "list is: 47, first is 47.");
		// off right

		// 83 <=> 47

		list.add(83);
		list.next();
		list.first();
		assertNotNull(list.current, "current not null after insert.");
		assertEquals( 83, (int) list.current.item,
				"list is: 83 <=> 47, called next(), first(), then get current:");
	}

	@Test
	public void testFirstOff() {
		// 47
		list.add(47);
		// off right
		list.next();
		assertTrue(list.isOffRight(), "list should be off right after calling next()");
		// bring it back to first
		list.first();
		assertNotNull(list.current, "current not null after calling first() on non-empty list.");
		assertEquals(47, (int) list.current.item, "list is: 47, called next(), first(), then get current:");
		assertFalse(list.isOffRight(), "list should not be off right after calling first()");

		// off left
		list.back();
		assertTrue(list.isOffLeft(), "list should be off left after calling back()");

		// bring it back to first
		list.first();
		assertFalse(list.isOffLeft(), "list should not be off left after calling first()");
	}

	/**
	 * create an empty list, confirm first() throws IllegalStateException
	 */
	@Test
	public void testFirstEmpty() {
		assertThrows(IllegalStateException.class, () -> {list.first();} );
	}

	@Test
	public void testLast() {
		// 47
		list.add(47);
		list.first();
		assertNotNull(list.current, "current not null after calling last().");
		assertEquals(47, (int) list.current.item, "list is: 47, last is 47.");

		// 83 <=> 47
		list.add(83);
		list.last();

		assertNotNull(list.current);
		assertEquals(47, (int) list.current.item, "list is: 83 <=> 47, called last(), then get current:");
	}

	@Test
	public void testLastOff() {
		// 47
		list.add(47);
		// off right
		list.next();
		assertTrue(list.isOffRight(), "list should be off right after calling next()");
		// bring back to last
		list.last();
		assertNotNull(list.current, "current not null after calling last() on non-empty list.");
		assertEquals(47, (int) list.current.item, "list is: 47, called next(), lst(), then get current:");
		assertFalse(list.isOffRight(), "list should not be off right after calling last()");

		// off left
		list.back();
		assertTrue(list.isOffLeft(), "list should be off right after calling back()");
		// bring back to last
		list.last();
		assertFalse(list.isOffLeft(), "list should not be off left after calling last()");
	}

	// create an empty list, confirm last() throws IllegalStateException
	@Test
	public void testLastEmpty() {
		assertThrows(IllegalStateException.class, () -> {list.last();} );
	}

	// create an empty list, confirm next() throws IllegalStateException
	@Test
	public void testNextEmptyList() {
		assertThrows(IllegalStateException.class, () -> {list.next();} );

	}

	// Calls next() on a non-empty list where current is off the right side
	// Replace with whatever exception you throw when the precondition is
	// violated
	@Test
	public void testNextOffRightSide() {
		list.addFirst(47);
		list.next(); // this moves current off right
		// this should trigger an exception
		assertThrows(IllegalStateException.class, () -> {list.next();} );

	}

	// Calls next() on non-empty list where current is head
	@Test
	public void testNext() {
		// 83 <=> 47
		list.add(47);
		list.add(83);
		list.next();
		assertEquals( 47, (int) list.current.item,
				"list is: 83 <=> 47, called next() after add().");
		assertFalse(list.isOffRight(),
				"list is: 83 <=> 47, called next() after add(). list is not off right.");
		list.next();
		assertTrue(list.isOffRight(),
				"list is: 83 <=> 47, called next() and next() after add(). list should be off right.");

	}

	// create an empty list, confirm back() throws IllegalStateException
	@Test
	public void testBackEmptyList() {
		assertThrows(IllegalStateException.class, () -> {list.back();} );

	}

	// Calls back() on a non-empty list where current is off the left side
	@Test
	public void testBackOffLeftSide() {
		list.addFirst(47);
		list.back(); // this moves current off left
		assertThrows(IllegalStateException.class, () -> {list.back();} );
	}

	@Test
	public void testBack() {
		list.add(47);
		list.back();
		assertTrue( list.isOffLeft(),
				"list is: 47, called back() after add(). list should be off left.");

		list.add(83);
		list.next();
		list.back();
		assertEquals(83, (int) list.current.item,
				"list is: 83 <=> 47, called add(), back(), add(), next(), and back(). current at 83.");
		assertFalse( list.isOffLeft(),
				"list is: 83 <=> 47, called add(), back(), add(), next(), and back(). list is not off left.");
	}

	@Test
	public void testIsOffRight() {
		list.add(47);
		list.next();
		assertNull(list.current);
		assertTrue(list.isOffRight(), "list is: 47, called next() after add(). list should be off right.");
		assertFalse(list.isOffLeft());
	}

	@Test
	public void testIsOffLeft() {
		list.add(47);
		list.back();
		assertNull(list.current);
		assertTrue(list.isOffLeft(), "list is: 47, called back() after add(). list should be off left.");
		assertFalse(list.isOffRight());

	}

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
	@Test
	public void testCurrentValueEmptyList() {
		assertThrows(IllegalStateException.class, () -> {list.currentValue();} );
	}

	// Calls currentValue() on non-empty list where current is off right.
	@Test
	public void testCurrentValueIsOff() {
		list.addFirst(47);
		list.next(); // this moves current off right
		assertThrows(IllegalStateException.class, () -> {list.currentValue();} );
	}

	@Test
	public void testCurrentValue() {
		// 134 <=> 84 <=> 47
		list.add(47);
		list.add(84);
		list.add(134);

		list.first(); // current points at 134
		assertEquals(134, (int) list.currentValue(), "list is 134 <=> 84 <=> 47, called first().");

		list.next(); // current points at 84
		assertEquals(84, (int) list.currentValue(), "list is 134 <=> 84 <=> 47, called first(), next().");

		list.next(); // current points at 47
		assertEquals(47, (int) list.currentValue(), "list is 134 <=> 84 <=> 47, called first(), next(), next().");
	}

	@Test
	public void testAddAfterCurrent() {
		// 47
		list.add(47);
		list.first();
		assertEquals(1, (int) list.size(),
				"list is 47, called add(), first(). Size should be 1.");
		assertEquals(47, (int) list.current.item,
				"list is 47, called add(), first(). current should be 47.");

		// 47 <=> 83
		list.addAfterCurrent(83);

		assertEquals(2, (int) list.size(),
				"list is 47 <=> 83, called add(), first(), addAfterCurrent(). Size should be 2.");
		assertEquals(83, (int) list.current.item,
				"list is 47 <=> 83, called add(), first(), addAfterCurrent(). Current should be 83.");
		// new node
		// 47 <=> 134 <=> 83
		list.back();
		list.addAfterCurrent(134);

		assertEquals(3, (int) list.size(),
				"list is 47 <=> 83, called add(), first(), addAfterCurrent(), back(), addAfterCurrent(). Size should be 3.");
		assertEquals(134, (int) list.current.item,
				"list is 47 <=> 83, called add(), first(), addAfterCurrent(), back(), addAfterCurrent(). Current should be 134.");

	}

	@Test
	public void testAddAfterCurrentNull() {
		list.add(47);
		assertThrows(IllegalArgumentException.class, () -> {list.addAfterCurrent(null);} );
	}

	@Test
	public void testAddAfterCurrentIsOff() {
		list.add(47);
		list.next();
		assertThrows(IllegalStateException.class, () -> {list.addAfterCurrent(83);} );
	}

	@Test
	public void testRemoveCurrent() {
		list.add(47);
		list.add(83);
		list.last();
		list.removeCurrent();
		// when removing the last value, check that current becomes null
		// and the size of the list is 1 (having started with 2 elts)
		assertEquals((int) list.size(), 1,
				"list is 83, called add(47), add(83), last(), removeCurrent(). Size should be 1.");
		assertEquals(list.current, null,
				"list is 83, called add(47), add(83), last(), removeCurrent(). current should be null.");
		assertTrue(list.isOff(),
				"list is 83, called add(47), add(83), last(), removeCurrent(). current should be off.");

		list.first();
		list.removeCurrent();
		// when removing the the only value, check that the
		// current value becomes the next one
		assertEquals((int) list.size(), 0,
				"list should be empty. called add(47), add(83), last(), removeCurrent(), first(), removeCurrent().");
		assertEquals(list.current, null,
				"current should be null. called add(47), add(83), last(), removeCurrent(), first(), removeCurrent().");
		// assertTrue(list.isOff());

		// check that we can remove a value in the middle of a list
		// 47 <=> 134 <=> 83
		list.add(83);
		list.add(134);
		list.add(47);
		list.first();
		list.next();
		list.removeCurrent();

		assertEquals(83, (int) list.current.item, "list was 47 <=> 134 <=> 83 and removed 134. current at 83.");
		list.first();
		assertEquals(47, (int) list.current.item,
				"list was 47 <=> 134 <=> 83 and removed 134 and called first(). current at 47.");
	}

	@Test
	public void testRemoveCurrentEmpty() {	
		assertThrows(IllegalStateException.class, () -> {list.removeCurrent();} );
	}

	@Test
	public void testRemoveCurrentIsOff() {
		list.add(47);
		list.next();
		assertThrows(IllegalStateException.class, () -> {list.removeCurrent();} );
	}

	@Test
	public void testAddFirst() {
		list.addFirst(47);
		assertEquals(47, (int) list.currentValue(), "current should be 47 after addFirst(47).");

		list.addFirst(83);
		assertEquals(83, (int) list.currentValue(), "current should be (83) <=> 47 after addFirst(47), addFirst(83).");
		list.first();
		assertEquals(83, (int) list.currentValue(),
				"current should be (83) <=> 47 after addFirst(47), addFirst(83), first().");
	}

	@Test
	public void testAddFirstAfterOff() {
		list.addFirst(47);
		list.back();
		list.addFirst(83);
		assertFalse(list.isOffLeft(), "list should change to not off left after addFirst()");
		list.next();
		list.next();
		list.addFirst(134);
		assertFalse(list.isOffRight(), "list should change to not off right after addFirst()");
	}

	@Test
	public void testAddFirstNull() {
		list.add(47);
		assertThrows(IllegalArgumentException.class, () -> {list.addFirst(null);} );
	}

	@Test
	public void testRemoveFirst() {
		list.add(47);
		list.add(83);
		list.removeFirst();
		assertEquals(47, (int) list.current.item, "list was 83 <=> 47, and removed first");
	}

	@Test
	public void testRemoveFirstAfterOffLeft() {
		list.add(47);
		list.add(83);
		list.back();
		list.removeFirst();
		assertFalse(list.isOffLeft(), "list was 83 <=> 47. list should change to not off left after removeFirst()");
	}

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

	@Test
	public void testAddLastOff() {
		list.addLast(47);
		list.next();
		list.addLast(83);
		assertFalse(list.isOffRight(), "list should change to not off right after addLast()");

		list.back();
		list.back();
		list.addLast(134);
		assertFalse(list.isOffLeft(), "list should change to not off left after addLast()");
	}

	@Test
	public void testAddLastNull() {
		assertThrows(IllegalArgumentException.class, () -> {list.addLast(null);} );
	}

	@Test
	public void testRemoveLastAfterOffLeft() {
		list.add(47);
		list.add(83);
		list.back();
		list.removeLast();
		assertFalse(list.isOffLeft(), "list should change to not off left after removeLast()");
	}

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

	@Test
	public void testGetFirst() {
		list.add(47);
		assertEquals(47, (int) list.getFirst(), "list is 47, called getFirst().");

		list.add(83);
		assertEquals(83, (int) list.getFirst(), "list is 83<=>47, called getFirst().");

		list.next();
		assertEquals((int) list.currentValue(), 47, "list is 83<=>47, current at 47, getCurrent().");
		assertEquals(83, (int) list.getFirst(), "list is 83<=>47, current at 47, then called getFirst().");
		assertEquals((int) list.currentValue(), 83,
				"list is 83<=>47, current was at 47, then called getCurrent() after getFirst().");
		
		list.add(134);

		list.last();
		assertEquals(47, (int) list.current.item);
		assertEquals(134, (int) list.getFirst());
		assertEquals(134, (int) list.current.item);

	}

	@Test
	public void testGetFirstAfterOff() {
		list.add(47);
		list.back();
		assertEquals(47, (int) list.getFirst(), "list is 47. getFirst should change current to 47 after off left");
		assertFalse(list.isOffLeft(), "list should change to not off left after getFirst()");

		list.next();
		assertEquals(47, (int) list.getFirst(), "list is 47. getFirst should change current to 47 after off right");
		assertFalse(list.isOffRight(), "list should change to not off right after getFirst()");
	}

	@Test
	public void testGetLast() {
		list.add(47);
		assertEquals(47, (int) list.getLast(), "list is 47, called getLast().");

		list.add(83);
		assertEquals(47, (int) list.getLast(), "list is 83<=>47, current at 83, then called getLast().");
		assertEquals(47, (int) list.current.item,
				"list is 83<=>47, current was at 83, then called getLast(), current should be at 47.");
		list.add(134);

		assertEquals(134, (int) list.current.item);
		assertEquals(47, (int) list.getLast());
		assertEquals(47, (int) list.current.item);

	}

	@Test
	public void testGetLastAfterOff() {
		list.add(47);
		list.back();
		assertEquals(47, (int) list.getLast(), "list is 47. getLast should change current to 47 after off left");
		assertFalse(list.isOffLeft(), "list should change to not off left after getLast()");

		list.next();
		assertEquals(47, (int) list.getLast(), "list is 47. getLast should change current to 47 after off right");
		assertFalse(list.isOffRight(), "list should change to not off right after getLast()");
	}

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
