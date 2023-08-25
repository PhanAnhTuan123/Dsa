package queue;

import java.util.Iterator;

import linkedList.DefaultDoublyLinkedList;
import linkedList.DoublyLinkedList;

public class LinkedListBasedQueue<T> implements QueueADT<T>{
	private final DoublyLinkedList<T>linkedlist = new DefaultDoublyLinkedList<>();
	
	public LinkedListBasedQueue() {
		
	}
	public LinkedListBasedQueue(T element) {
		enQueue(element);
	}
	@Override
	public Iterator<T> iterator() {
		return linkedlist.iterator();
	}

	@Override
	public void enQueue(T element) {
		linkedlist.add(element);
		
	}

	@Override
	public T deQueue() {
		if(isEmpty()) throw new RuntimeException("Queue empty");
		return linkedlist.removeFirst();
	}

	@Override
	public T peek() {
		if(isEmpty()) throw new RuntimeException("Queue empty");
		return linkedlist.peekFirst();
	}

	@Override
	public int size() {
		return linkedlist.size();
	}

	@Override
	public boolean isEmpty() {
		return size()==0;
	}
	
}
