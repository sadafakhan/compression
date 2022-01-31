
## Description

Program that compresses a table of strings to reduce the space it takes. 
Consider the following table of information that corresponds to a 3x4 image with only black pixels (k for short):

| | | | |
|-|-|-|-|
|k|k|k|k|
|k|k|k|k|
|k|k|k|k|

In actuality, we only need to record the following entry to impart the same information:

| | | | |
|-|-|-|-|
|k|-|-|-|
|-|-|-|-|
|-|-|-|-|

Rather than storing unnecessarily the entire table, this program will model it as a doubly linked list that consists of associations, where the key will correspond to a cell in the table (an object of type `RowOrderedPosn`) and the value to a pixel (or more generally to a String). To capture the table above, our doubly linked list would only contain a single node:

    <Association: Position: (0,0)=k>


Interestingly, if we choose to alter the second pixel (position 0, 1) to red (r):

| | | | |
|-|-|-|-|
|k|r|k|k|
|k|k|k|k|
|k|k|k|k|

we would now need to update our doubly linked list to encode this additional information. The contents of the doubly linked list would now be:

    <Association: Position: (0,0)=k>
    <Association: Position: (0,1)=r>
    <Association: Position: (0,2)=k>

If we were to update the (0,1) pixel back to black (k), our doubly linked list would need to contract back to:

    <Association: Position: (0,0)=k>


The program can become more complicated. For instance, suppose we have the following table of information (where we will imagine r corresponding to the color red, g to green and b to blue):

| | | | | |
|-|-|-|-|-|
|r|r|r|g|g|
|r|r|r|r|r|
|r|b|r|r|r|
|r|r|r|g|r|
|r|r|b|r|r|


Notice that we only need to record the following entries:

| | | | | |
|-|-|-|-|-|
|r|-|-|g|-|
|r|-|-|-|-|
|-|b|r|-|-|
|-|-|-|g|r|
|-|-|b|r|-|


Rather than recording this in a two-dimensional table, it will now generally be more efficient to keep this
information in a linear list of `Assocations` where it is assumed we sweep across an entire row before going
on to the next:

    <Association: Position: (0,0)=r>
    <Association: Position: (0,3)=g>
    <Association: Position: (1,0)=r>
    <Association: Position: (2,1)=b>
    <Association: Position: (2,2)=r>
    <Association: Position: (3,3)=g>
    <Association: Position: (3,4)=r>
    <Association: Position: (4,2)=b>
    <Association: Position: (4,3)=r>


## Classes


### `CurDoublyLinkedList`

`CurDoublyLinkedList` (short for 'extends the `DoublyLinkedList` class to include a `current` reference to a "current" node). 

The `CurDoublyLinkedList` (in addition to supporting the parent class's methods) supports the following methods:

* `first()`, `last()`,
* `next()`, `back()`,
* `isOffRight()`, `isOffLeft()`, `isOff()`
* `currentValue()`,
* `addFirst()`, `addLast()`, `removeFirst()`, `removeLast()`, `getFirst()`, `getLast()`, `addAfterCurrent(Object value)`, and `removeCurrent()`.

### `TestCurDoublyLinkedList`

This is a **`JUnit`** test class for the `CurDoublyLinkedList` class. 

### `RowOrderedPosn`

The `RowOrderedPosn` class represents a single cell in a row-ordered table. The constructor takes four parameters: the row of the entry in the table, the column of the entry in the table, the total number of rows in the table, and the total number of columns in the table. Thus,

    new RowOrderedPosn(0, 0, 5, 3)

represents the entry at location (0, 0) i.e. the upper-left corner in a table with 5 rows and 3 columns. This class also contains methods to return the next position after a given one (look at method `next()`) and to compare two positions in a table (look at methods `less()` and `greater()`). 


### `TwoDTable`
This interface represents a two-dimensional table. `CompressedTable` implements it. 


### `CompressedTable`

The `CompressedTable` class represents the compressed table. `CompressedTable` implements the `TwoDTable` interface. It has an instance variable `tableInfo` of type:
`CurDoublyLinkedList<Association<RowOrderedPosn, ValueType>>`. The instance variable `tableInfo` is a `CurDoublyLinkedList` where each node in the list is an `Association` whose key is an entry in the table of type `RowOrderedPosn` and whose value is of type `ValueType`. It supports the following method: 

* `updateInfo` 

1. Find the node of the list that encodes the position being updated. Not every position is in the list, only those representing changes to the table. If the node is not there, the method returns the node before the given position in the list. The class `RowOrderedPosn` not only encodes a position, but, because it also contains information on the number of rows and columns in the table, can determine if one position would come before or after another.

2. If the new information in the table is the same as that in the node found in step 1, then nothing needs to be done. Otherwise determine if the node represents exactly the position being updated.
If it is the same, update the value of the node, otherwise add a new node representing the new position.

3. If you are not careful you may accidentally change several positions in the table to the new value. Avoid changing several positions to the new value by putting in a new node representing the position immediately after the position with the new value. 

4. If there is already a node with this successor position then nothing needs to be done. Otherwise add a new node with the successor position and the original value. 

5. Eliminate consecutive items with the same value, essentially contracting the doubly linked list. Ideally this optimization will only take `O(1)` time each time something is updated in the table.


## `Compression`

Asks the user to specify the size of the table (number of rows, number of columns) and an initial value to start with (all separated by comma), e.g., 

    Provide #rows #cols defaultValue:
    4 4 k

It continuously asks the user 

    What's next?

and the user can specify whether they want to

    display

the underlying compressed table

    exit 

the application

or update a specific cell with a new value, e.g., 

    update 3 3 r
