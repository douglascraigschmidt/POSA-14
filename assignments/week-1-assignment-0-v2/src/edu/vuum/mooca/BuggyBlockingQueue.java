package edu.vuum.mooca;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * @class BuggyBlockingQueue
 * 
 * @brief Defines a buggy version of the BlockingQueue interface that
 *        doesn't implement any synchronization mechanisms, so of
 *        course it will fail horribly, which is the intent!
 */
public class BuggyBlockingQueue<E> implements BlockingQueue<E> {
    /**
     * ArrayList doesn't provide any synchronization, so it will not
     * work correctly when called from multiple Java Threads.
     */
    private ArrayList<E> mList = null;

    /**
     * Constructor just creates an ArrayList of the appropriate size.
     */
    public BuggyBlockingQueue(int initialSize) {
        mList = new ArrayList<E>(initialSize);
    }

    /**
     * Returns the number of elements in this queue.
     */
    public int size() {
        return mList.size();
    }

    /**
     * Insert msg at the tail of the queue, but doesn't block if the
     * queue is full.
     */
    public void put(E msg) throws InterruptedException {
        mList.add(msg);
    }

    /**
     * Remove msg from the head of the queue, but doesn't block if the
     * queue is empty.
     */
    public E take() throws InterruptedException {
        return mList.remove(0);
    }

    /**
     * All these methods are inherited from the BlockingQueue
     * interface. They are defined as no-ops to ensure the "Buggyness"
     * of this class ;-)
     */
    public int drainTo(Collection<? super E> c) {
        return 0;
    }
    public int drainTo(Collection<? super E> c, int maxElements) {
        return 0;
    }
    public boolean contains(Object o) {
        return false;
    }
    public boolean remove(Object o) {
        return false;
    }
    public int remainingCapacity() {
        return 0;
    }
    public E poll() {
        return null;
    }
    public E poll(long timeout, TimeUnit unit) throws InterruptedException {
        return take();
    }
    public E peek() {
        return null;
    }
    public boolean offer(E e) {
        return false;
    }
    public boolean offer(E e, long timeout, TimeUnit unit) {
        try {
            put(e);
        }
        catch (InterruptedException ex) {
            // Just swallow this exception for this simple (buggy) test.
        }
        return true;
    }
    public boolean add(E e) {
        return false;
    }
    public E element() {
        return null;
    }
    public E remove() {
        return null;
    }
    public void clear() {
    }
    public boolean retainAll(Collection<?> collection) {
        return false;
    }
    public boolean removeAll(Collection<?> collection) {
        return false;
    }
    public boolean addAll(Collection<? extends E> collection) {
        return false;
    }
    public boolean containsAll(Collection<?> collection) {
        return false;
    }
    public Object[] toArray() {
        return null;
    }
    public <T> T[] toArray(T[] array) {
        return null;
    }
    public Iterator<E> iterator() {
        return null;
    }
    public boolean isEmpty() {
        return false;
    }
}
