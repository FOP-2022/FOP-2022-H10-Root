package h10;

import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Represents a list as a linked structure, where each element has a reference to its successor.
 *
 * @param <T> the type of the elements in this list
 * @author Nhan Huynh
 * @see ListItem
 */
public class MyLinkedList<T> {

    /**
     * The head (pointer) of this list.
     */
    public ListItem<T> head;

    /**
     * Removes the elements from the list that satisfy the predicate.
     *
     * @param predT predT the predicate to determine which elements must be removed
     * @param fct   The function that maps from type T to U potential candidates
     * @param predU the predicate, which checks whether the mapped element is valid
     * @param <U>   the type of the list containing the removed mapped elements
     * @return a list that contains the removed mapped elements from this list that satisfy the predicate
     * @throws MyLinkedListException if the mapped element is invalid for removal
     */
    public <U> MyLinkedList<U> extractIteratively(Predicate<? super T> predT,
                                                  Function<? super T, ? extends U> fct,
                                                  Predicate<? super U> predU) throws MyLinkedListException {
        MyLinkedList<U> removed = new MyLinkedList<>();
        // Points to the last element of the list containing the removed elements and allows inserting elements at
        // the end of the list
        ListItem<U> tail = null;

        // Cannot extract if there are no elements
        if (head == null) {
            return removed;
        }

        // Extend the list item by one dummy node to simplify the check with the successor node
        ListItem<T> current = new ListItem<>();
        current.next = head;
        // The new head is stored in a variable to restore it
        ListItem<T> result = current;
        int index = 0;

        // We have to iterate over all elements
        while (current.next != null) {
            T key = current.next.key;
            // Skip iteration if predicate was not fulfilled
            if (!predT.test(key)) {
                current = current.next;
                index++;
                continue;
            }
            // Predicate was fulfilled
            U mapped = fct.apply(key);
            // Check mapped element if it still fulfills predicate
            if (!predU.test(mapped)) {
                throw new MyLinkedListException(index, mapped);
            }
            // Remove element
            current.next = current.next.next;

            // Insert element
            ListItem<U> item = new ListItem<>(mapped);
            if (tail == null) {
                removed.head = tail = item;
            } else {
                tail = tail.next = item;
            }
            index++;
        }

        // Restore head
        head = result.next;
        return removed;
    }

    /**
     * Removes the elements from the list that satisfy the predicate.
     *
     * @param predT predT the predicate to determine which elements must be removed
     * @param fct   The function that maps from type T to U potential candidates
     * @param predU the predicate, which checks whether the mapped element is valid
     * @param <U>   the type of the list containing the removed mapped elements
     * @return a list that contains the removed mapped elements from this list that satisfy the predicate
     * @throws MyLinkedListException if the mapped element is invalid for removal
     */
    public <U> MyLinkedList<U> extractRecursively(Predicate<? super T> predT,
                                                  Function<? super T, ? extends U> fct,
                                                  Predicate<? super U> predU) throws MyLinkedListException {
        // Extend the list item by one dummy node to simplify the check with the successor node
        ListItem<T> dummy = new ListItem<>();
        dummy.next = head;

        MyLinkedList<U> removed = extractRecursivelyHelper(predT, fct, predU, dummy, 0);

        // Restore head
        head = dummy.next;
        return removed;
    }

    /**
     * Removes the elements from the list that satisfy the predicate.
     *
     * @param predT predT the predicate to determine which elements must be removed
     * @param fct   The function that maps from type T to U potential candidates
     * @param predU the predicate, which checks whether the mapped element is valid
     * @param pSrc  the pointer to the current element of the list
     * @param index the current index of the element of the list
     * @param <U>   the type of the list containing the removed mapped elements
     * @return a list that contains the removed mapped elements from this list that satisfy the predicate
     * @throws MyLinkedListException if the mapped element is invalid for removal
     */
    // set to public for testExtractReallyRecursively in TutorTest_H2_1
    public <U> MyLinkedList<U> extractRecursivelyHelper(
        Predicate<? super T> predT,
        Function<? super T, ? extends U> fct,
        Predicate<? super U> predU,
        ListItem<T> pSrc, int index
    ) throws MyLinkedListException {
        if (pSrc.next == null) {
            return new MyLinkedList<>();
        }

        T key = pSrc.next.key;
        // Skip iteration if predicate was not fulfilled
        if (!predT.test(key)) {
            return extractRecursivelyHelper(predT, fct, predU, pSrc.next, index + 1);
        }

        // Predicate was fulfilled
        U mapped = fct.apply(key);
        // Check mapped element if it still fulfills predicate
        if (!predU.test(mapped)) {
            throw new MyLinkedListException(index, mapped);
        }

        MyLinkedList<U> removed = new MyLinkedList<>();
        // Remove element
        pSrc.next = pSrc.next.next;

        // Insert element
        removed.head = new ListItem<>(mapped);

        // Recursively determine the successor elements
        MyLinkedList<U> others = extractRecursivelyHelper(predT, fct, predU, pSrc, index + 1);
        removed.head.next = others.head;
        return removed;
    }

    /**
     * Merges this (target) list with the specified (source) list with the criterion that the
     * position of the insertion point of the elements from the source list depends on the
     * predicate. If the predicate is met, the element is added before the element of the target
     * list.
     *
     * @param otherList the source list to be merged with the destination list
     * @param biPred    the predicate, which decides where to insert the element
     * @param fct       the function that maps an element from the source list to a matching type of
     *                  the target list
     * @param predU     the predicate, which checks whether the source element is valid for
     *                  insertion
     * @param <U>       the type of the list which should be merged to this list
     * @throws MyLinkedListException if the source element is invalid for insertion
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
        ListItem<T> current = new ListItem<>();
        current.next = head;
        // The new head is stored in a variable to restore it
        ListItem<T> result = current;
        int index = 0;

        // We have to insert all elements from the other list
        while (others != null) {
            U key = others.key;
            // Check if source element should be inserted
            if (!predU.test(key)) {
                throw new MyLinkedListException(index, key);
            } else if (current.next != null) {
                // Case element is not the last element of the list
                T element = current.next.key;
                if (biPred.test(element, key)) {
                    T mapped = fct.apply(key);
                    ListItem<T> item = new ListItem<>(mapped);
                    // Element will be inserted before the element that fulfills the predicate
                    // Before -> New Element -> Predicate true element
                    item.next = current.next;
                    current.next = item;

                    // Update pointer since we added an element from the other list to this list
                    others = others.next;
                    current = current.next;
                    // Only increase index if we iterate through the target list
                    index++;
                }
            } else {
                // Case current == null only occurs if we reached the tail of the list (last element of the list)
                T mapped = fct.apply(key);
                current.next = new ListItem<>(mapped);
                others = others.next;
                index++;
            }

            // Update
            current = current.next;
        }

        // Restore head
        head = result.next;
    }

    /**
     * Merges this (target) list with the specified (source) list with the criterion that the
     * position of the insertion point of the elements from the source list depends on the
     * predicate. If the predicate is met, the element is added before the element of the target
     * list.
     *
     * @param otherList the source list to be merged with the destination list
     * @param biPred    the predicate, which decides where to insert the element
     * @param fct       the function that maps an element from the source list to a matching type of
     *                  the target list
     * @param predU     the predicate, which checks whether the source element is valid for
     *                  insertion
     * @param <U>       the type of the list which should be merged to this list
     * @throws MyLinkedListException if the source element is invalid for insertion
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
        ListItem<T> dummy = new ListItem<>();
        dummy.next = head;
        mixinRecursivelyHelper(otherList, biPred, fct, predU, otherList.head, dummy, 0);
        // Restore head
        head = dummy.next;
    }

    /**
     * Merges this (target) list with the specified (source) list with the criterion that the
     * position of the insertion point of the elements from the source list depends on the
     * predicate. If the predicate is met, the element is added before the element of the target
     * list.
     *
     * @param otherList the source list to be merged with the destination list
     * @param biPred    the predicate, which decides where to insert the element
     * @param fct       the function that maps an element from the source list to a matching type of
     *                  the target list
     * @param predU     the predicate, which checks whether the source element is valid for
     *                  insertion
     * @param pSrc      the pointer to the current element of the (source) list
     * @param pDest     the pointer to the current element of the (target) list
     * @param index     the current index of the element of the list
     * @param <U>       the type of the list which should be merged to this list
     * @throws MyLinkedListException if the source element is invalid for insertion
     */
    // set to public for testMixinReallyRecursively in TutorTest_H2_2
    public <U> void mixinRecursivelyHelper(MyLinkedList<U> otherList,
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
        // Check if source element should be inserted
        if (!predU.test(key)) {
            throw new MyLinkedListException(index, key);
        } else if (pDest.next != null) {
            T element = pDest.next.key;
            if (biPred.test(element, key)) {
                T mapped = fct.apply(key);
                ListItem<T> item = new ListItem<>(mapped);
                // Element will be inserted before the element that fulfills the predicate
                // Before -> New Element -> Predicate true element
                item.next = pDest.next;
                pDest.next = item;

                // Update pointer since we added an element from the other list to this list
                pSrc = pSrc.next;
                pDest = pDest.next;
                index++;
            }
        } else {
            // Case current == null only occurs if we reached the tail of the list
            T mapped = fct.apply(key);
            pDest.next = new ListItem<>(mapped);
            pSrc = pSrc.next;
            index++;
        }
        // Iterate over the next elements
        mixinRecursivelyHelper(otherList, biPred, fct, predU, pSrc, pDest.next, index);
    }

    /**
     * Adds the element to the end of this list.
     *
     * @param key the element to be added
     * @return {@code true} if this list changed as a result of the call
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
