package ciic4020.set;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class StaticSet<E> implements Set<E> {

	// current number of elements in set
	private int currentSize;

	// array of elements
	private E elements[];

	private static final int DEFAULT_SET_SIZE = 10;

	public StaticSet(int maxCapacity) {
		if (maxCapacity < 1)
			throw new IllegalArgumentException("Max capacity must be at least 1");
		this.currentSize = 0;
		this.elements = (E[]) new Object[maxCapacity];
	}

	private class SetIterator<T> implements Iterator<T> {
		private int currentPosition;

		public SetIterator() {
			this.currentPosition = 0;
		}

		@Override
		public boolean hasNext() {
			return this.currentPosition < size();
		}

		@Override
		public T next() {
			if (this.hasNext()) {
				T result = (T) elements[this.currentPosition++];
				return result;
			}
			else
				throw new NoSuchElementException();
		}
	}

	@Override
	public boolean add(E obj) {
		if (this.isMember(obj))
			return false;
		else {
			if (this.size() == this.elements.length)
				throw new IllegalStateException("The set is full.");
			else {
				this.elements[this.currentSize++] = obj;
				return true;
			}
		}
	}

	@Override
	public boolean isMember(E obj) {
		for (int i = 0; i < this.size(); i++)
			if (this.elements[i].equals(obj))
				return true;
		return false;
	}

	@Override
	public boolean remove(E obj) {
		for (int i = 0; i < this.size(); i++)
			if (this.elements[i].equals(obj))
			{
				this.elements[i] = this.elements[--this.currentSize];
				this.elements[this.currentSize] = null;
				return true;
			}
		return false;
	}

	@Override
	public boolean isEmpty() {
		return this.size() == 0;
	}

	@Override
	public int size() {
		return this.currentSize;
	}

	@Override
	public void clear() {
		for (int i = 0; i < this.size(); i++)
			this.elements[i] = null;
		this.currentSize = 0;

	}

	@Override
	public Set<E> union(Set<E> S2) {
		Set<E> S3 = new StaticSet<E>(DEFAULT_SET_SIZE);
		// Copy S1
		for (Object obj : this)
			S3.add((E) obj);

		// Copy elements of S2 not already in S1
		for (Object obj : S2)
		{
			E e = (E) obj;
			if (!S3.isMember(e))
				S3.add(e);
		}

		return S3;
	}

	@Override
	public Set<E> difference(Set<E> S2) {
		Set<E> S3 = new StaticSet<E>(DEFAULT_SET_SIZE);
		for (Object obj : this)
		{
			E e = (E) obj;
			if (!S2.isMember(e))
				S3.add(e);
		}
		return S3;
	}

	@Override
	public Set<E> intersection(Set<E> S2) {
		return this.difference(this.difference(S2));
	}

	@Override
	public boolean isSubSet(Set<E> S2) {
		for (Object obj : this)
			if (!S2.isMember((E) obj))
				return false;
		return true;
	}

	@Override
	public boolean equals(Set<E> S2) {

		if (this.size() != S2.size())
			return false;

//		for (Object obj : this)
//			if(!S2.isMember((E) obj)) {
//				return false;
//			}
//		return true;
		for (int i = 0; i < this.size(); i++) {

			if(!S2.isMember(this.elements[i]))
				return false;
		}

		return true;
	}


	@Override
	public Set<Set<E>> singletonSets() {

		Set<Set<E>> singleton = new StaticSet<Set<E>>(this.size());

		for (int i = 0; i < this.size(); i++) {

			//Creat an empty set of size 1
			Set<E> temp = new StaticSet<>(1);
			// add element[i] to temp set
			temp.add(this.elements[i]);
			//add the set to singleton
			singleton.add(temp);
		}

		return singleton;
	}


	@Override
	public Iterator<E> iterator() { return new SetIterator<E>(); }


	public static boolean checkDisjoint (Object[] sets){

		StaticSet result = new StaticSet(sets.length);

		for (int i = 0; i < sets.length; i++) {
			for (int j = i + 1; j < sets.length - 1; j ++){
				result.add(((StaticSet)sets[i]).intersection((StaticSet)sets[j]));
			}
		}
		return result.isEmpty();
	}

}