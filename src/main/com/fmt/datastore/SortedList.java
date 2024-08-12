package com.fmt.datastore;

import java.util.Comparator;
import java.util.Iterator;

public class SortedList<T> implements Iterable<T>{
    Node<T> head;
    Comparator<T> comp;

    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            Node<T> currentNode = head;
            @Override
            public boolean hasNext() {
                return currentNode != null;
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

    private SortedList(Node<T> head, Comparator<T> comp) {
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

    public void add(T item) {
    }

    public void remove(T item) {

    }

    private static class Node<T> {
        private Node<T> next;
        private final T item;

        public Node(T item, Node<T> next) {
            this.next = next;
            this.item = item;
        }

        public Node<T> getNext() {
            return this.next;
        }

        public void setNext(Node<T> next) {
            this.next = next;
        }
    }

}
