package stack;

import java.util.EmptyStackException;
import java.util.Iterator;

import linkedList.DefaultDoublyLinkedList;
import linkedList.DoublyLinkedList;

public class LinkedListBasedStack<T> implements StackADT<T>{
	private final DoublyLinkedList<T> list = new DefaultDoublyLinkedList<>();
	public LinkedListBasedStack() {
		
	}
	public LinkedListBasedStack(T element) {
		push(element);
	}
	
	@Override
	public Iterator<T> iterator() {
		return list.iterator();
	}

	@Override
	public void push(T element) {
		
		list.add(element);
	}

	@Override
	public T pop() {
		
		if(isEmpty()) throw new EmptyStackException();
		return list.removeLast();
	}

	@Override
	public T top() {
		if(isEmpty()) throw new EmptyStackException();
		return list.peekLast();
	}

	@Override
	public int size() {
		return list.size();
	}

	@Override
	public boolean isEmpty() {
		return list.size()==0;
	}
	
}
