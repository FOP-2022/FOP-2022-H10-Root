package h10;

import java.util.List;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;

public class MyLinkedList<T> {
    public h10.ListItem<T> head;

    /**
     * This method iteratively removes all items from the list where Predicate predT is true and returns a MyLinkedList<U> with the function result of each item in their original order
     *
     * @param predT predT Predicate which determines if item is to be removed
     * @param fct Function which converts ListItem<T> to ListItem<U>
     * @param predU Predicate which determines if an item is invalid and an Exception has to be thrown
     * @param <U> Type of resulting list
     * @return MyLinkedList<U>, containing the result of fct applied to each removed item in the order of all items in the original list
     * @throws MyLinkedListException if the result of predU is false and an Exception has to be thrown
     */
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

    /**
     * This method, by calling a helper function, recursively removes all items from the list where Predicate predT is true
     * and returns a MyLinkedList<U> with the function result of each item in their original order
     *
     * @param predT predT Predicate which determines if item is to be removed
     * @param fct Function which converts ListItem<T> to ListItem<U>
     * @param predU Predicate which determines if an item is invalid and an Exception has to be thrown
     * @param <U> Type of resulting list
     * @return MyLinkedList<U>, containing the result of fct applied to each removed item in the order of all items in the original list
     * @throws MyLinkedListException if the result of predU is false and an Exception has to be thrown
     */
    public <U> MyLinkedList<U> extractRecursively(Predicate<T> predT, Function<T, U> fct, Predicate<U> predU) throws MyLinkedListException {
        //call recursive helper method
        return extractRecursivelyHelper(predT, fct, predU, this.head, 0);
    }

    /**
     * This method recursively removes all items from the list where Predicate predT is true and returns a MyLinkedList<U> with the function result of each item in their original order
     * @param predT predT Predicate which determines if item is to be removed
     * @param fct Function which converts ListItem<T> to ListItem<U>
     * @param predU Predicate which determines if an item is invalid and an Exception has to be thrown
     * @param pSrc pointer to current item in the source list
     * @param index current index in the list
     * @param <U> Type of resulting list
     * @return MyLinkedList<U>, containing the current result of fct applied to each removed item in the order of all items in the original list
     * @throws MyLinkedListException if the result of predU is false and an Exception has to be thrown
     */
    private <U> MyLinkedList<U> extractRecursivelyHelper(Predicate<T> predT, Function<T, U> fct, Predicate<U> predU, ListItem<T> pSrc, int index) throws MyLinkedListException{
        //set up destination list
        MyLinkedList<U> destinationList = new MyLinkedList<U>();

        //go through the list recursively until end is reached
        if(pSrc.next != null) {
            destinationList = extractRecursivelyHelper(predT, fct, predU, pSrc.next, index+1);
        }

        //if the end is reached, check if last item has to be added to destination list (and be removed from src list) and go one step back
        if (pSrc.next == null) {
            if (predT.test(pSrc.key)) {
                U key = fct.apply(pSrc.key);

                //test if exception has to be thrown
                if (predU.test(key) == false) {
                    throw new MyLinkedListException(index, key);
                }

                //create item to be added to the list
                ListItem<U> item = new ListItem<U>(key);

                //if destination list is empty, create new head
                if (destinationList.head == null) {
                    destinationList.head = item;
                } else { //else make item the new head
                    item.next = destinationList.head;
                    destinationList.head = item;
                }

                return destinationList;
            }
            return destinationList;
        }

        //fill up destination list
        if (predT.test(pSrc.key)) {

            U key = fct.apply(pSrc.key);

            //test if exception has to be thrown
            if (predU.test(key) == false) {
                throw new MyLinkedListException(index, key);
            }

            ListItem<U> item = new ListItem<U>(fct.apply(pSrc.key));
            //if destination list is empty, create new head
            if (destinationList.head == null) {
                destinationList.head = item;
            } else { //else make item the new head
                item.next = destinationList.head;
                destinationList.head = item;
            }
        }

        //remove items from source list
        //handle edge case if head has to be removed: make next item the new head
        if (pSrc == head && predT.test(pSrc.key)) {
            head = head.next;
        } else if (predT.test(pSrc.next.key)) { //else check if last item was added to destination list and remove it accordingly
            pSrc.next = pSrc.next.next;
        }

        return destinationList;
    }

    /**
     * This method merges two MyLinkedLists by adding the result of fct applied to each item of otherList to the list the current objects represents
     * @param otherList Source list from which items are to be mixed into the target list
     * @param biPred Predicate to check if second parameter has to be inserted at position of first parameter
     * @param fct Function to convert item of type U to type T
     * @param predU Predicate to determine if item in source list is a valid item to be inserted into target list
     * @param <U> Type of the source list
     * @throws MyLinkedListException if the result of predU is false, and therefore its parameter is not a valid item to be inserted into target list
     */
    public <U> void mixinIteratively(MyLinkedList<U> otherList, BiPredicate<T, U> biPred, Function<U, T> fct, Predicate<U> predU) throws MyLinkedListException {
        //return if the source list is empty
        if (otherList.head == null) {
            return;
        }

        //set up pointers
        ListItem<T> pCurrent = this.head;
        ListItem<T> pPrevious = null;
        ListItem<U> pSource = otherList.head;

        //create index variable for potentially throwing exceptions
        int index = 0;

        //iterate over the source list
        while (pSource != null) {
            //check if exception has to be thrown
            if (predU.test(pSource.key) == false) {
                throw new MyLinkedListException(index, pSource.key);
            }

            //create ListItem to be inserted into target list
            ListItem<T> item = new ListItem<T>(fct.apply(pSource.key));

            //first handle case if position is currently at head of target list but nothing is to be inserted
            if (pPrevious == null && pCurrent != null && !biPred.test(pCurrent.key, pSource.key)) {
                pPrevious = pCurrent;
                pCurrent = pCurrent.next;
                continue;
            }
            //handle case if target list is empty or end of target list is reached
            if (pCurrent == null || pPrevious == null) {
                //if target list is empty, make item the new head
                if (pPrevious == null && pCurrent == null) {
                    item.next = this.head;
                    pCurrent = this.head;
                    pPrevious = item;
                    this.head = item;
                }
                else if (pPrevious == null && biPred.test(pCurrent.key, pSource.key)) {
                    item.next = this.head;
                    pCurrent = this.head;
                    pPrevious = item;
                    this.head = item;

                } else if (pPrevious != null){ //if end of target list is reached, add item to the end
                    pPrevious.next = item;
                    pPrevious = pPrevious.next;
                }
            } else if (biPred.test(pCurrent.key, pSource.key)) { //if target list is not empty nor the end is reached, check if item has to be inserted at current position
                //insert item at current position
                item.next = pCurrent;
                pPrevious.next = item;

            } else { //else iterate on target list without changing the list
                pPrevious = pCurrent;
                pCurrent = pCurrent.next;
                continue;
            }
            //increase index variable
            index++;
            pSource = pSource.next;
        }
    }

    /**
     * This method merges two MyLinkedLists by calling a helper function
     * @param otherList Source list from which items are to be mixed into the target list
     * @param biPred Predicate to check if second parameter has to be inserted at position of first parameter
     * @param fct Function to convert item of type U to type T
     * @param predU Predicate to determine if item in source list is a valid item to be inserted into target list
     * @param <U> Type of the source list
     * @throws MyLinkedListException if the result of predU is false, and therefore its parameter is not a valid item to be inserted into target list
     */
    public <U> void mixinRecursively(MyLinkedList<U> otherList, BiPredicate<T, U> biPred, Function<U, T> fct, Predicate<U> predU) throws MyLinkedListException {
        //if source list is empty, return without changing the target list
        if (otherList.head == null) {
            return;
        }

        mixinRecursivelyHelper(otherList, biPred, fct, predU, otherList.head, this.head, 0);
    }

    /**
     * This method merges two MyLinkedLists by adding the result of fct applied to each item of otherList to the list the current objects represents
     * @param otherList Source list from which items are to be mixed into the target list
     * @param biPred Predicate to check if second parameter has to be inserted at position of first parameter
     * @param fct Function to convert item of type U to type T
     * @param predU Predicate to determine if item in source list is a valid item to be inserted into target list
     * @param pSrc pointer to current item at source list
     * @param pDest pointer to current item at target list
     * @param index current index at source list
     * @param <U> Type of the source list
     * @throws MyLinkedListException if the result of predU is false, and therefore its parameter is not a valid item to be inserted into target list
     */
    private <U> void mixinRecursivelyHelper(MyLinkedList<U> otherList, BiPredicate<T, U> biPred, Function<U, T> fct, Predicate<U> predU, ListItem<U> pSrc, ListItem<T> pDest, int index) throws MyLinkedListException {
        //if items from source list have been inserted, return
        if (pSrc == null) {
            return;
        }

        //check if exception is to be thrown
        if (predU.test(pSrc.key) == false) {
            throw new MyLinkedListException(index, pSrc.key);
        }

        //create item for current element in source list
        ListItem<T> item = null;

        //handle edge case if current item in target list is the head and item has to be inserted as new head
        if (pDest == this.head && biPred.test(pDest.key, pSrc.key)) {
            item = new ListItem<>(fct.apply(pSrc.key));
            item.next = this.head;
            this.head = item;
            mixinRecursivelyHelper(otherList, biPred, fct, predU, pSrc.next, pDest, index+1);
        } else if (pDest.next != null && biPred.test(pDest.next.key, pSrc.key)) { //check if item at pSrc has to be inserted at position of pDest.next and potentially add it to the list if current item has to be inserted at next position in target list, add it to the list
            item = new ListItem<>(fct.apply(pSrc.key));
            item.next = pDest.next;
            pDest.next = item;
            mixinRecursivelyHelper(otherList, biPred, fct, predU, pSrc.next, pDest.next, index+1);
        } else if (pDest.next == null) { //if end of the list is reached, insert item at pSrc at the end of pDest
            item = new ListItem<>(fct.apply(pSrc.key));
            pDest.next = item;
            mixinRecursivelyHelper(otherList, biPred, fct, predU, pSrc.next, pDest.next, index+1);
        } else { //if position at pDest is not the correct position for pSrc, go to next item in target list
            mixinRecursivelyHelper(otherList, biPred, fct, predU, pSrc, pDest.next, index);
        }

    }

    /**
     * This method adds a new list item with parameter "key" as its key value to the list
     * @param key key value of item to be added
     * @return boolean stating if the item has been added to the list successfully
     */
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
