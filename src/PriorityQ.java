/**
 * Helper class for a priority queue implemented as a heap. The methods are named using
 * heap conventions as this is how it is implemented (e.g. 'insert' rather than 'enqueue').
 * @author Daniel Hunt
 *
 */
import geometry.Event;

public class PriorityQ implements Cloneable {

	// the number of elements currently in the queue
	private int n;
	// the array used to store the elements
	private Event[] heap;


	/**
	 * Create a new PQ with the provided event.
	 * @param top	the provided event
	 */
	public PriorityQ(Event top) {
		n = 1;
		heap = new Event[5000];
		heap[0] = top;					
	}
	
	/**
	 * Create an empty PQ with the provided size.
	 * @param size the size of the PQ
	 */
	public PriorityQ(int size) {
		n=0;
		heap = new Event[size];				
	}

	/**
	 * Create an empty PQ.
	 */
	public PriorityQ() {
		n=0;
		heap = new Event[5000];
	}
	
	public int getNumElements()  {
		return n;
	}

	/**
	 * Insert the provided element into the PQ.
	 * @param p	the element to be inserted
	 */
	public void insert(Event p) {
		// if necessary, copy over to a new heap double the size to avoid overflow
		if( n == heap.length) {
			Event[] temp = new Event[heap.length*2];
			for(int i=0; i<n; i++) {
				temp[i] = heap[i];
			}
			heap = temp;
		}

		// otherwise just insert the element at the end and sift it up appropriately
		heap[n] = p;
		siftUp(n);
		n++;
	}

	/**
	 * Helper method for sorting the heap when a new element is added.
	 * @param index the starting point of the sift
	 */
	private void siftUp(int index) {
		if( index > 0 ) {
			int parent = (index - 1) / 2;
			if( heap[index].isBefore(heap[parent]) ) {
				Event temp = heap[parent];
				heap[parent] = heap[index];
				heap[index] = temp;
				siftUp( parent );
			}
		}
	}

	/**
	 * Helper method for sorting the heap when the top element is removed.
	 * @param index the starting point of the sift
	 */
	private void siftDown(int index) {
		int leftChild = 2 * index + 1;
		int rightChild = 2 * index + 2;

		// check if the children are outside the array bounds.
		if( rightChild >= n && leftChild >= n ) return;

		// determine the smallest child out of the left and right children.
		final int smallestChild = 
				heap[rightChild].isBefore(heap[leftChild]) ? rightChild : leftChild;

		// if the index should not come before one of its children, sift it down the heap
		if( heap[smallestChild].isBefore(heap[index]) ) {
			Event temp = heap[smallestChild];
			heap[smallestChild] = heap[index];
			heap[index] = temp;
			siftDown( smallestChild );
		}
	}

	/**
	 * Check if the PQ is empty.
	 * @return true if the PQ is empty
	 */
	public boolean isEmpty() {
		return n == 0;
	}

	/** 
	 * Searches for the event and changes the event value. This method should not be
	 * necessary, and is commented out for that reason. Needs to be properly implemented
	 * if uncommented.
	 * @param p1 the event to be changed

	public void change(Event p1, Event p2) {
		for(int i = 0; i<n; i++) {
			if( (heap[i].node == p.node) && (heap[i].length > p.length) ) {
				heap[i] = p;
				siftUp(i);
				return;
			}
		}
	}*/

	/**
	 * Extract and return the event at the top of the heap.
	 * @return the event currently at the top of the heap/PQ
	 */
	public Event extractMin() {
		if (isEmpty()) {
			return null;
		}

		// extract the min and swap the last element to the top
		Event min = heap[0];
		heap[0] = heap[n-1];
		n--;

		// heapify
		if( n>0 )
			siftDown(0);

		return min;
	}

	/**
	 * Return the event at the top of the heap without removing it.
	 * @return the event currently at the top of the heap/PQ
	 */
	public Event peekMin() {
		return heap[0];
	}
	
	public PriorityQ clone() {
		try {
			return (PriorityQ)super.clone();
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Return a copy of the heap. Used to print the points.
	 * @return
	 */
	public Event[] getHeap() {
		return heap;
	}
	


}