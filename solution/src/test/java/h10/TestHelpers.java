package h10;

public class TestHelpers {

    public static MyLinkedList<int[]> createList1() {
        MyLinkedList<int[]> testList2 = new MyLinkedList<>();

        int[] arr1 = {1, 5, 10};
        int[] arr2 = {2, 3, 5};
        int[] arr3 = {10, 100, 1000};
        int[] arr4 = {5, 6, 7};
        int[] arr5 = {10, 15, 20};
        int[] arr6 = {7, 8, 9};

        testList2.add(arr1);
        testList2.add(arr2);
        testList2.add(arr3);
        testList2.add(arr4);
        testList2.add(arr5);
        testList2.add(arr6);

        return testList2;
    }

    public static MyLinkedList<int[]> createList2() {
        MyLinkedList<int[]> testList1 = new MyLinkedList<>();

        int[] arr1 = {1, 5, 10};
        int[] arr2 = {2, 3, 5};
        int[] arr3 = {10, 100, 1000};
        int[] arr4 = {5, 6, 7};
        int[] arr5 = {10, 15, 20};
        int[] arr6 = {7, 8, 9};

        testList1.add(arr2);
        testList1.add(arr1);
        testList1.add(arr4);
        testList1.add(arr3);
        testList1.add(arr6);
        testList1.add(arr5);

        return testList1;
    }

    public static MyLinkedList<int[]> createList3() {
        MyLinkedList<int[]> testList3 = new MyLinkedList<>();

        int[] arr1 = {1, 5, 10};
        int[] arr2 = {2, 3, 5};
        int[] arr3 = {10, 100, 1000};
        int[] arr4 = {5, 6, 7};
        int[] arr6 = {7, 8, 9};
        int[] arrException = {-1, -2, -3};

        testList3.add(arr1);
        testList3.add(arr2);
        testList3.add(arr3);
        testList3.add(arr4);
        testList3.add(arrException);
        testList3.add(arr6);

        return testList3;
    }

    public static MyLinkedList<int[]> createSourceResult() {
        MyLinkedList<int[]> result = new MyLinkedList<>();

        int[] arr1 = {1, 5, 10};
        int[] arr2 = {2, 3, 5};
        int[] arr3 = {10, 100, 1000};
        int[] arr4 = {5, 6, 7};
        int[] arr5 = {10, 15, 20};
        int[] arr6 = {7, 8, 9};

        result.add(arr2);
        result.add(arr4);
        result.add(arr6);

        return result;
    }

    public static MyLinkedList<Integer> createDestinationResult() {
        MyLinkedList<Integer> result = new MyLinkedList<>();

        int i1 = 16;
        int i2 = 1110;
        int i3 = 45;

        result.add(i1);
        result.add(i2);
        result.add(i3);

        return result;
    }


}
