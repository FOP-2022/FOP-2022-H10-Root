package h10.utils;

import h10.ListItem;
import h10.MyLinkedList;

/**
 * Substitute for the add method in MyLinkedList.
 *
 * @author Arianne Roselina Prananto
 */
public class MyLinkedListAdd<T> {

    /* *********************************************************************
     *                   Define add method for MyLinkedList                *
     **********************************************************************/

    /**
     * Adds the element to the end of this list.
     *
     * @param key the element to be added
     * @return {@code true} if this list changed as a result of the call
     */
    public boolean add(MyLinkedList<T> list, T key) {
        if (list.head == null) {
            list.head = new ListItem<>(key);
            return true;
        }

        ListItem<T> p = list.head;

        while (p.next != null) {
            p = p.next;
        }

        p.next = new ListItem<>(key);
        return true;
    }
}
