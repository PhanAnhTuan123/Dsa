package tree;

public class Node<T extends Comparable<T>>{
	private T data;
	public T getData() {
		return data;
	}
	public void setData(T data) {
		this.data = data;
	}
	private Node<T> left,right;
	public Node(T data, Node<T> left, Node<T> right) {
		
		this.data = data;
		this.left = left;
		this.right = right;
	}
	public String toString() {
		return data.toString();
	}
	public Node<T> getLeft() {
		return left;
	}
	public void setLeft(Node<T> left) {
		this.left = left;
	}
	public Node<T> getRight() {
		return right;
	}
	public void setRight(Node<T> right) {
		this.right = right;
	}
	
}
