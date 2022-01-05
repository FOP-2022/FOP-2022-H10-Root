package h10;

import java.util.function.Function;
import java.util.function.Predicate;

public class MyLinkedList<T> {
    public h10.ListItem<T> head;

    public <U> MyLinkedList<U> extractIteratively(Predicate<T> predT, Function<T, U> fct, Predicate<U> predU) throws MyLinkedListException {
        //set up source and destination lists
        MyLinkedList<T> src = this;
        MyLinkedList<U> dest = new MyLinkedList<U>();

        //set up pointers
        ListItem<T> prev = null;
        ListItem<T> current = this.head;
        ListItem<T> next = this.head.next;
        ListItem<U> pDest = dest.head;

        //save current index for potentially thrown exceptions
        int index = 0;

        //iterate through the list
        while (current != null) {

            //test if current item has to be removed
            if (predT.test(current.key)) {
                //save key of current element to add to list or potentially throw an exception
                U key = fct.apply(current.key);

                //test if exception has to be thrown
                if (predU.test(key) == false) {
                    throw new MyLinkedListException(index, key);
                }

                //create item to be added to destination list
                ListItem<U> item = new ListItem<U>(key);

                //add item to destination list
                if (dest.head == null) { //if current item is list head, make next item the head and adjust pointer
                    dest.head = item;
                    pDest = dest.head;
                } else { //else add it to the list normally
                    pDest.next = item;
                    pDest = pDest.next;
                }

                //cut item from source list
                if (current == this.head) { //if source list is empty, make item the head
                    if (this.head.next != null) { //check if removal of head would make list empty
                        this.head = this.head.next;
                        current = this.head;
                        next = this.head.next;
                    } else { //if this is the case, set pointers accordingly
                        this.head = null;
                        current = null;
                        next = null;
                    }

                } else { //else cut it from the list normally
                    if (next != null) {
                        prev.next = next;
                        current = next;
                        next = next.next;
                    } else {
                        prev.next = null;
                        current = null;
                    }

                }

            } else { //if current item does not have to be remove, iterate
                prev = current;
                current = next;
                if (next != null) {
                    next = next.next;
                }
            }

            index++;
        }

        return dest;
    }

    public <U> MyLinkedList<U> extractRecursively(Predicate<T> predT, Function<T, U> fct, Predicate<U> predU) throws MyLinkedListException {
        //call recursive helper method
        return extractRecursivelyHelper(predT, fct, predU, 0);
    }

    private <U> MyLinkedList<U> extractRecursivelyHelper(Predicate<T> predT, Function<T, U> fct, Predicate<U> predU, int index) throws MyLinkedListException{
        //save current head for reconstruction after recursion
        ListItem<T> lstHead = head;

        //set up destination list
        MyLinkedList<U> dest = new MyLinkedList<U>();

        //go through the list recursively until end is reached
        if (head.next != null) {
            head = head.next;
            dest = extractRecursivelyHelper(predT, fct, predU, index+1);
        }

        //if the end is reached, check if last item has to be added to destination list (and be removed from src list) and go one step back
        if (lstHead == head) {
            if (predT.test(head.key)) {
                //save key of current element to add to list or potentially throw an exception
                U key = fct.apply(lstHead.key);

                //test if exception has to be thrown
                if (predU.test(key) == false) {
                    throw new MyLinkedListException(index, key);
                }

                //create item to be added to the list
                ListItem<U> item = new ListItem<U>(key);


                //if destination list is empty, create new head
                if (dest.head == null) {
                    dest.head = item;
                } else { //else make item the new head
                    item.next = dest.head;
                    dest.head = item;
                }
                //set head to null to not include last item in rebuilt source list
                head = null;
                return dest;
            }
            //set head.next to null to clarify that item is the last item in the list
            head.next = null;
            return dest;
        }

        //fill up destination list
        if (predT.test(lstHead.key)) {

            U key = fct.apply(lstHead.key);

            //test if exception has to be thrown
            if (predU.test(key) == false) {
                throw new MyLinkedListException(index, key);
            }

            ListItem<U> item = new ListItem<U>(fct.apply(lstHead.key));
            //if destination list is empty, create new head
            if (dest.head == null) {
                dest.head = item;
            } else { //else make item the new head
                item.next = dest.head;
                dest.head = item;
            }
        } else { //only include items that were not added to the destination list while rebuilding the source list
            lstHead.next = head;
            head = lstHead;
        }
        //return current destination list
        return dest;
    }

    public boolean add(T key) {
        if (head == null) {
            head = new ListItem<T>(key);
            return true;
        }

        ListItem<T> p = head;

        while(p.next != null) {
            p = p.next;
        }

        p.next = new ListItem<T>(key);
        return true;
    }
}
