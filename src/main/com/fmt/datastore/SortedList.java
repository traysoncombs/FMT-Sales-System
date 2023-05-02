package com.fmt.datastore;

import java.util.*;

/**
 * Class to maintain an ordering of objects.
 *
 * @param <T>
 */
public class SortedList<T> implements Iterable<T>{
    Node<T> head;
    Comparator<T> comp;

    int size = 0;

    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            Node<T> currentNode = head;
            @Override
            public boolean hasNext() {
                return currentNode.hasNext();
            }

            @Override
            public T next() {
                if (!hasNext()) throw new RuntimeException("Element out of bounds.");

                T tmp = currentNode.getNext().item;
                currentNode = currentNode.getNext();
                return tmp;
            }
        };
    }

    public SortedList(Comparator<T> comp) {
        this.head = null;
        this.comp = comp;
    }

    public SortedList(Node<T> head, Comparator<T> comp) {
        this.head = head;
        this.comp = comp;
    }

    public static<T> SortedList<T> buildSortedList(T[] data, Comparator<T> comp) {
        Node<T> head = new Node<>(data[0], null);
        return null;
    }

    /**
     * If type T extends comparable we simply create a Comparator that
     * calls T's compareTo and pass it to the constructor.
     *
     * TODO: UPDATE THIS AT SOME POINT
     *
     * @param data
     * @return
     * @param <T>
     */
    public static<T extends Comparable<T>> SortedList<T> buildSortedList(T[] data) {
        return buildSortedList(data, Comparator.naturalOrder());
    }

    /**
     * Returns the node at position pos.
     *
     * @param pos Index of node
     * @return Node
     */
    public Node<T> getNode(int pos) {
        if (pos - 1 > size || pos < 0) throw new ArrayIndexOutOfBoundsException();
        Node<T> currentNode = head;

        for (int i = 0; i < pos; i++) {
            currentNode = currentNode.getNext();
        }

        return currentNode;
    }

    /**
     * Adds an item to the list while maintaining the ordering.
     *
     * @param item Item
     */
    public void add(T item) {
        if (head == null) {
            size++;
            head = new Node<>(item, null);
            return;
        }

        Node<T> prevNode = null;
        Node<T> currentNode = head;

        // Loops until new item >= current item
        while (comp.compare(item, currentNode.getItem()) > 0) {
            if (!currentNode.hasNext()) {
                // In this case we reached the last element and
                // the new item is greater, so insert at end.
                currentNode.setNext(new Node<T>(item, null));
                return;
            }
            prevNode = currentNode;
            currentNode = currentNode.getNext();
        }

        if (prevNode == null) {
            // In this case we have a list of size 1
            // and the new item is less than the head.
            head = new Node<>(item, currentNode);
            return;
        }

        // Create new node with item and set it as next.
        Node<T> newNode = new Node<>(item, currentNode);
        prevNode.setNext(newNode);
        size++;
    }

    /**
     * Internal function to remove the node after prevNode.
     * If null is passed the head will be removed.
     *
     * @param prevNode Node before the one to be removed.
     */
    private void removeNextNode(Node<T> prevNode) {
        // null refers to the node before head.
        if (prevNode == null) {
            head = head.getNext();
            return;
        }
        prevNode.setNext(prevNode.getNext().getNext());
        size--;
    }

    /**
     * Removes an item from the list.
     *
     * @param item Item to be removed.
     */
    public void remove(T item) {
        Node<T> prevNode = null;
        Node<T> currentNode = head;

        while (currentNode.hasNext()) {
            if (currentNode.getItem().equals(item)) {
                removeNextNode(prevNode);
            }
            prevNode = currentNode;
            currentNode = currentNode.getNext();
        }
    }

    /**
     * Removes node at position pos.
     *
     * @param pos Position
     */
    public void remove(int pos) {
        removeNextNode(getNode(pos - 1));
    }

    public T[] toArray() {
        T[] ret = (T[]) new Object[size];

        if (head == null) return ret;
        Node<T> currentNode = head;

        for (int i = 0; i < size; i++) {
            ret[i] = currentNode.getItem();
            currentNode = currentNode.getNext();
        }

        return ret;
    }

    private static class Node<T> {
        private Node<T> next;
        private final T item;

        public Node(T item, Node<T> next) {
            this.next = next;
            this.item = item;
        }
        public boolean hasNext() {
            return next != null;
        }

        public Node<T> getNext() {
            return this.next;
        }

        public void setNext(Node<T> next) {
            this.next = next;
        }

        public T getItem() {
            return item;
        }
    }

    public static void main(String[] args) {
        SortedList<Integer> list = new SortedList<Integer>(Comparator.naturalOrder());
        Random rand = new Random();
        for (int i = 0; i <= 100; i++) {
            list.add(rand.nextInt(100));
        }
        List<Integer> lst = Arrays.asList(list.toArray());

        for (int i = 0; i < 99; i++) {
            assert lst.get(i) <= lst.get(i+1);
        }

        System.out.println(Arrays.toString(list.toArray()));

    }

}
