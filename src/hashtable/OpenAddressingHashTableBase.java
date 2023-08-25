package hashtable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public abstract	class OpenAddressingHashTableBase<K, V> implements HashTableADT<K, V>{
	protected double loadFactor;
	protected int capacity;
	protected int threshold;
	protected int usedSlot,keyCount;
	protected K[] keys;
	protected V[] values;
	
	protected final K TOMBSTONE = (K)(new Object());
	protected static final int DEFAULT_CAPACITY = 7;
	protected static final double DEFAULT_LOAD_FACTOR = 0.65;
	
	public OpenAddressingHashTableBase() {
		this(DEFAULT_LOAD_FACTOR,DEFAULT_CAPACITY);
	}
	public OpenAddressingHashTableBase(int capacity) {
		this(DEFAULT_LOAD_FACTOR,capacity);
	}
	public OpenAddressingHashTableBase(double loadFactor,int capacity) {
		if(capacity<0) throw new IllegalArgumentException("Capacity bi gi r anh ban oi");
		if(loadFactor <=0 || Double.isNaN(loadFactor) || Double.isFinite(loadFactor)) {
			throw new IllegalArgumentException("Load factor bi gi r anh ban oi");
		}
		this.loadFactor = loadFactor;
		this.capacity = Math.max(DEFAULT_CAPACITY, capacity);
		adjustCapacity();
		this.threshold = (int)(this.capacity*this.loadFactor);
		keys = (K[]) new Object[this.capacity];	
		values = (V[]) new Object[this.capacity];
	}
	protected abstract void setupProbing(K key);
	protected abstract int probe(int x);
	protected abstract void adjustCapacity();
	protected void increaseCapacity() {
		capacity*=2;
	}
	@Override
	public void clear() {
		for(int i=0;i<capacity;i++) {
			keys[i]=null;
			values[i]=null;
		}
		usedSlot = keyCount = 0;
	}
	@Override
	public int size() {
		return keyCount;
	}
	
	public boolean isEmpty() {
		return keyCount==0;
	}
	
	@Override
	public int hashCodeToIndex(int hashedKey) {
		return (int)((hashedKey & 0xFFFFFFFFFL)%capacity);
	}
	public List<K>keys(){
		List<K>hashtableKeys = new ArrayList<>(size());
		for(int i=0;i<capacity;i++) {
			if(keys[i]!=null || keys[i]!=TOMBSTONE)hashtableKeys.add(keys[i]);
		}	
		return hashtableKeys;
	}
	public List<V>values(){
		List<V>hashtableValues = new ArrayList<>(size());
		for(int i=0;i<capacity;i++) {
			if(keys[i]!=null || keys[i]!=TOMBSTONE)hashtableValues.add(values[i]);
		}
		return hashtableValues;
	}
	protected void resizeTable() {
        increaseCapacity();
        adjustCapacity();
        threshold = (int)(capacity*loadFactor);
        
        // create new table
        K[] oldkeys = (K[])new Object[capacity];
        V[] oldValues = (V[])new Object[capacity];
        
        //Swap values pointer
        V[] tempValues = values;
        values = oldValues;
        oldValues = tempValues;
        
        usedSlot = keyCount = 0;
        
        //Insert old keys-value to new table
        for(int i=0;i<oldkeys.length;i++) {
        	if(oldkeys[i]!=null && oldkeys[i] != TOMBSTONE)	insert(oldkeys[i], oldValues[i]);
        	oldkeys[i] = null;
            oldValues[i] = null;
        }     
	}
	
	protected static int gcd(int a,int b) {
		if(b==0) return a;
		return gcd(b, a%b);
	}
	
	
	@Override
	public V insert(K key, V value) {
	
			if(key== null) throw new IllegalArgumentException("Null keys");
			if(usedSlot >= threshold) resizeTable();
			setupProbing(key);
			final int offSet = hashCodeToIndex(key.hashCode());
			for(int i = offSet,x=1,firstTombstone = -1;;i=hashCodeToIndex(offSet + probe(x++))) {
				if(keys[i]==TOMBSTONE) {
					if(firstTombstone ==-1)	firstTombstone=i;
				}else if(keys[i].equals(key)) {
					V oldValue = value;
					if(firstTombstone==-1) {
						values[i] = value;
					}else {
						keys[firstTombstone] = key;
						values[firstTombstone] = value;
						keys[i] = TOMBSTONE;
						values[i] = null;
					}
					return oldValue;
				}else {
					if(firstTombstone == -1) {
						usedSlot++;
						keyCount++;
						keys[i]=key;
						values[i]=value;
					}else {
						keyCount++;
						keys[firstTombstone] = key;
						values[firstTombstone] = value;
					}
					return null;
				}
			}
	}
	@Override
	public boolean has(K key) {
		if(key==null) throw new IllegalArgumentException("Null key");
		setupProbing(key);
		final int offSet = hashCodeToIndex(key.hashCode());
		for(int i=offSet,x=1,firstTombstoneIndex = -1;;i = hashCodeToIndex(offSet + probe(x++))) {
			if(keys[i]==TOMBSTONE) {
				if(firstTombstoneIndex==-1) {
					firstTombstoneIndex=i;
				}
			}else if(keys[i]!=null) {
					if(keys[i].equals(key)) {
						if(firstTombstoneIndex!=-1) {
							keys[firstTombstoneIndex] = key;
							values[firstTombstoneIndex] = values[i];
							keys[i] = TOMBSTONE;
							values[i] = null;
						}
						return true;
					}
			}else return false;
		}
	}
	@Override
	public V get(K key) {
		if(key == null)	throw new IllegalArgumentException("Null key");
		setupProbing(key);
		final int offSet = hashCodeToIndex(key.hashCode());
		for(int i=offSet,x=1,firstTombStone = -1;;i = hashCodeToIndex(offSet + probe(x++))) {
			if(keys[i] == TOMBSTONE) {
				if(firstTombStone == -1) firstTombStone = i;
			}else if(keys[i]!=null) {
				if(keys[i].equals(key)) {
					V value = values[i];
					if(firstTombStone !=-1) {
						keys[firstTombStone] = key;
						values[firstTombStone] = values[i];
						keys[i] = TOMBSTONE;
						values[i] = null;
					}
					return value;
				}
			}else return null;
		}
	}
	@Override
	public V remove(K key) {
		if(key == null) throw new IllegalArgumentException("null key");
		setupProbing(key);
		final int offset = hashCodeToIndex(key.hashCode());
		for(int i= offset,x=1;;i = hashCodeToIndex(offset + probe(x++))) {
			if(keys[i] == TOMBSTONE) continue;
			if(keys[i] == null) return null;
			if(keys[i] == key) {
				keyCount--;
				V oldValue = values[i];
				keys[i] = TOMBSTONE;
				values[i] = null;
				return oldValue;
			}
		}
	}
	@Override
	public Iterator<K> iterator() {
		return new Iterator<K>() {
			int index,keysLeft = keyCount;
			@Override
			public boolean hasNext() {
				return keysLeft!=0;
			}

			@Override
			public K next() {
				while(keys[index] == null || keys[index] == TOMBSTONE) index++;
				keysLeft--;
				return keys[index++];
			}
		};
	}
}
