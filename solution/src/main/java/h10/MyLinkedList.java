package h10;

import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * An object of this class represents a Linked List.
 *
 * @param <T> Type ListItem Objects saved in the LinkedList
 */
public class MyLinkedList<T> {
    public h10.ListItem<T> head;

    /**
     * This method iteratively removes all items from the list where Predicate predT is true and returns a
     * MyLinkedList.
     *
     * @param predT predT Predicate which determines if item is to be removed
     * @param fct   Function which converts ListItem with key of type T to List item with key of type U
     * @param predU Predicate which determines if an item is invalid and an Exception has to be thrown
     * @param <U>   Type of resulting list
     *
     * @return MyLinkedList, containing the result of fct applied to each removed item in original order
     *
     * @throws MyLinkedListException if the result of predU is false and an Exception has to be thrown
     */
    public <U> MyLinkedList<U> extractIteratively(Predicate<? super T> predT,
                                                  Function<? super T, ? extends U> fct,
                                                  Predicate<? super U> predU) throws MyLinkedListException {
        // set up source and destination lists
        MyLinkedList<U> dest = new MyLinkedList<>();

        // set up pointers
        ListItem<T> prev = null;
        ListItem<T> current = this.head;
        ListItem<T> next = this.head.next;
        ListItem<U> pDest = dest.head;

        // save current index for potentially thrown exceptions
        int index = 0;

        // iterate through the list
        while (current != null) {

            // test if current item has to be removed
            if (predT.test(current.key)) {
                // save key of current element to add to list or potentially throw an exception
                U key = fct.apply(current.key);

                // test if exception has to be thrown
                if (!predU.test(key)) {
                    throw new MyLinkedListException(index, key);
                }

                // create item to be added to destination list
                ListItem<U> item = new ListItem<>(key);

                // add item to destination list
                if (dest.head == null) { //if current item is list head, make next item the head and adjust pointer
                    dest.head = item;
                    pDest = dest.head;
                } else { //else add it to the list normally
                    pDest.next = item;
                    pDest = pDest.next;
                }

                // cut item from source list
                if (current == this.head) { //if source list is empty, make item the head
                    if (this.head.next != null) { //check if removal of head would make list empty
                        this.head = this.head.next;
                        current = this.head;
                        next = this.head.next;
                    } else { // if this is the case, set pointers accordingly
                        this.head = null;
                        current = null;
                        next = null;
                    }

                } else { // else cut it from the list normally
                    if (next != null) {
                        prev.next = next;
                        current = next;
                        next = next.next;
                    } else {
                        prev.next = null;
                        current = null;
                    }

                }

            } else { // if current item does not have to be removed, iterate
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

    /**
     * This method, by calling a helper function, recursively removes all items from the list where Predicate predT is
     * true.
     *
     * @param predT predT Predicate which determines if item is to be removed
     * @param fct   Function which converts ListItem with key of type T to ListItem with key of type U
     * @param predU Predicate which determines if an item is invalid and an Exception has to be thrown
     * @param <U>   Type of resulting list
     *
     * @return MyLinkedList, containing the result of fct applied to each removed item in original order
     *
     * @throws MyLinkedListException if the result of predU is false and an Exception has to be thrown
     */
    public <U> MyLinkedList<U> extractRecursively(Predicate<? super T> predT,
                                                  Function<? super T, ? extends U> fct,
                                                  Predicate<? super U> predU) throws MyLinkedListException {
        // call recursive helper method
        return extractRecursivelyHelper(predT, fct, predU, this.head, 0);
    }

    /**
     * Recursively removes items from the list where Predicate predT is true and returns list with the items in original
     * order.
     *
     * @param predT predT Predicate which determines if item is to be removed
     * @param fct   Function which converts ListItem with key type T to ListItem with key type U
     * @param predU Predicate which determines if an item is invalid and an Exception has to be thrown
     * @param pSrc  pointer to current item in the source list
     * @param index current index in the list
     * @param <U>   Type of resulting list
     *
     * @return MyLinkedList, containing the current result of fct applied to each removed item in original order
     *
     * @throws MyLinkedListException if the result of predU is false and an Exception has to be thrown
     */
    private <U> MyLinkedList<U> extractRecursivelyHelper(Predicate<? super T> predT,
                                                         Function<? super T, ? extends U> fct,
                                                         Predicate<? super U> predU,
                                                         ListItem<T> pSrc, int index) throws MyLinkedListException {
        //set up destination list
        MyLinkedList<U> destinationList = new MyLinkedList<>();

        // go through the list recursively until end is reached
        if (pSrc.next != null) {
            destinationList = extractRecursivelyHelper(predT, fct, predU, pSrc.next, index + 1);
        }

        // if the end is reached, check if last item has to be added to destination list (and be removed from src list)
        if (pSrc.next == null) {
            if (predT.test(pSrc.key)) {
                U key = fct.apply(pSrc.key);

                // test if exception has to be thrown
                if (!predU.test(key)) {
                    throw new MyLinkedListException(index, key);
                }

                // create item to be added to the list
                ListItem<U> item = new ListItem<>(key);

                // if destination list is empty, create new head
                if (destinationList.head != null) { //else make item the new head
                    item.next = destinationList.head;
                }
                destinationList.head = item;

                return destinationList;
            }
            return destinationList;
        }

        // fill up destination list
        if (predT.test(pSrc.key)) {

            U key = fct.apply(pSrc.key);

            // test if exception has to be thrown
            if (!predU.test(key)) {
                throw new MyLinkedListException(index, key);
            }

            ListItem<U> item = new ListItem<>(fct.apply(pSrc.key));
            // if destination list is empty, create new head
            if (destinationList.head != null) { //else make item the new head
                item.next = destinationList.head;
            }
            destinationList.head = item;
        }

        // remove items from source list
        // handle edge case if head has to be removed: make next item the new head
        if (pSrc == head && predT.test(pSrc.key)) {
            head = head.next;
        } else if (predT.test(pSrc.next.key)) { // else check if last item was added to destination list and remove it accordingly
            pSrc.next = pSrc.next.next;
        }

        return destinationList;
    }

    /**
     * This method merges two MyLinkedLists by adding the result of fct applied to each item of otherList to current
     * list.
     *
     * @param otherList Source list from which items are to be mixed into the target list
     * @param biPred    Predicate to check if second parameter has to be inserted at position of first parameter
     * @param fct       Function to convert item of type U to type T
     * @param predU     Predicate to determine if item in source list is a valid item to be inserted into target list
     * @param <U>       Type of the source list
     *
     * @throws MyLinkedListException if the result of predU is false, and parameter is not valid to be inserted into
     *                               target list
     */
    public <U> void mixinIteratively(MyLinkedList<U> otherList,
                                     BiPredicate<? super T, ? super U> biPred,
                                     Function<? super U, ? extends T> fct,
                                     Predicate<? super U> predU) throws MyLinkedListException {
        // Cannot merge other list if its empty
        if (otherList.head == null) {
            return;
        }

        ListItem<U> others = otherList.head;
        // Extend the list item by one dummy node to simplify the check with the successor node
        ListItem<T> current = new ListItem<>(null);
        current.next = head;
        // The new head is stored in a variable to restore it
        ListItem<T> result = current;
        int index = 0;

        // We have to insert all elements from the other list
        while (others != null) {
            U key = others.key;
            if (!predU.test(key)) {
                throw new MyLinkedListException(index, key);
            } else if (current.next != null) {
                T element = current.next.key;
                if (biPred.test(element, key)) {
                    T mapped = fct.apply(key);
                    ListItem<T> item = new ListItem<>(mapped);
                    item.next = current.next;
                    current.next = item;

                    // Update pointer since we added an element from the other list to this list
                    others = others.next;
                    current = current.next;
                }
            } else {
                // Case current == null only occurs if we reached the tail of the list
                T mapped = fct.apply(key);
                current.next = new ListItem<>(mapped);
                others = others.next;
            }

            // Update
            index++;
            current = current.next;
        }

        // Restore head
        head = result.next;
    }

    /**
     * This method merges two MyLinkedLists by calling a helper function.
     *
     * @param otherList Source list from which items are to be mixed into the target list
     * @param biPred    Predicate to check if second parameter has to be inserted at position of first parameter
     * @param fct       Function to convert item of type U to type T
     * @param predU     Predicate to determine if item in source list is a valid item to be inserted into target list
     * @param <U>       Type of the source list
     *
     * @throws MyLinkedListException if result of predU is false and its parameter is valid to be inserted into target
     *                               list
     */
    public <U> void mixinRecursively(MyLinkedList<U> otherList,
                                     BiPredicate<? super T, ? super U> biPred,
                                     Function<? super U, ? extends T> fct,
                                     Predicate<? super U> predU) throws MyLinkedListException {
        // Cannot merge other list if its empty
        if (otherList.head == null) {
            return;
        }
        // Extend the list item by one dummy node to simplify the check with the successor node
        ListItem<T> dummy = new ListItem<>(null);
        dummy.next = head;
        mixinRecursivelyHelper(otherList, biPred, fct, predU, otherList.head, dummy, 0);
        // Restore head
        head = dummy.next;
    }

    /**
     * This method merges two MyLinkedLists by adding the result of fct applied to each item of otherList to current
     * list.
     *
     * @param otherList Source list from which items are to be mixed into the target list
     * @param biPred    Predicate to check if second parameter has to be inserted at position of first parameter
     * @param fct       Function to convert item of type U to type T
     * @param predU     Predicate to determine if item in source list is a valid item to be inserted into target list
     * @param pSrc      pointer to current item at source list
     * @param pDest     pointer to current item at target list
     * @param index     current index at source list
     * @param <U>       Type of the source list
     *
     * @throws MyLinkedListException if result of predU is false and its parameter is not valid to be inserted into
     *                               target list
     */
    private <U> void mixinRecursivelyHelper(MyLinkedList<U> otherList,
                                            BiPredicate<? super T, ? super U> biPred,
                                            Function<? super U, ? extends T> fct,
                                            Predicate<? super U> predU,
                                            ListItem<U> pSrc,
                                            ListItem<T> pDest,
                                            int index) throws MyLinkedListException {
        // We have to insert all elements from the other list until there are no elements left
        if (pSrc == null) {
            return;
        }
        U key = pSrc.key;
        if (!predU.test(key)) {
            throw new MyLinkedListException(index, key);
        } else if (pDest.next != null) {
            T element = pDest.next.key;
            if (biPred.test(element, key)) {
                T mapped = fct.apply(key);
                ListItem<T> item = new ListItem<>(mapped);
                item.next = pDest.next;
                pDest.next = item;

                // Update pointer since we added an element from the other list to this list
                pSrc = pSrc.next;
                pDest = pDest.next;
            }
        } else {
            // Case current == null only occurs if we reached the tail of the list
            T mapped = fct.apply(key);
            pDest.next = new ListItem<>(mapped);
            pSrc = pSrc.next;
        }
        // Iterate over the next elements
        mixinRecursivelyHelper(otherList, biPred, fct, predU, pSrc, pDest.next, index + 1);
    }

    /**
     * This method adds a new list item with parameter "key" as its key value to the list.
     *
     * @param key key value of item to be added
     *
     * @return boolean stating if the item has been added to the list successfully
     */
    public boolean add(T key) {
        if (head == null) {
            head = new ListItem<>(key);
            return true;
        }

        ListItem<T> p = head;

        while (p.next != null) {
            p = p.next;
        }

        p.next = new ListItem<>(key);
        return true;
    }
}
