package hashtable;

public class Node<K, V> {
	private int hash;
	private K key;
	private V value;
	public Node(K key, V value) {
		this.hash = key.hashCode();
		this.key = key;
		this.value = value;
	}
	public boolean equals(Node<K,V>other) {
		if(other.hash!=hash) return false;
		return this.key.equals(other.key);
	}
	public int getHash() {
		return hash;
	}
	public void setHash(int hash) {
		this.hash = hash;
	}
	public K getKey() {
		return key;
	}
	public void setKey(K key) {
		this.key = key;
	}
	public V getValue() {
		return value;
	}
	public void setValue(V value) {
		this.value = value;
	}
	
	@Override
	public String toString() {
		return "key = "+ key +", value= "+value;
	}
}
