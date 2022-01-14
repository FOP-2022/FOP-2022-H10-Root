package h10;

/**
 * This class contains methods to generate test lists for testing a MyLinkedList.
 */
public class TestHelpers {

    /**
     * This method creates the first test list for testExtract.
     *
     * @return first test list
     */
    public static MyLinkedList<int[]> createExtractList1() {
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

    /**
     * This method creates the second test list for testExtract.
     *
     * @return second test list
     */
    public static MyLinkedList<int[]> createExtractList2() {
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

    /**
     * This method creates the third test list for testExtract.
     *
     * @return third test list
     */
    public static MyLinkedList<int[]> createExtractList3() {
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

    /**
     * This method creates the resulting source test list for testExtract.
     *
     * @return source test list result
     */
    public static MyLinkedList<int[]> createExtractSourceResult() {
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

    /**
     * This method creates the resulting destination test list for testExtract.
     *
     * @return destination test list result
     */
    public static MyLinkedList<Integer> createExtractDestinationResult() {
        MyLinkedList<Integer> result = new MyLinkedList<>();

        int i1 = 16;
        int i2 = 1110;
        int i3 = 45;

        result.add(i1);
        result.add(i2);
        result.add(i3);

        return result;
    }

    /**
     * This method creates the first target list for testMixin.
     *
     * @return first target list
     */
    public static MyLinkedList<Number> createMixinTargetList1() {
        MyLinkedList<Number> lst = new MyLinkedList<Number>();

        lst.add(1.0);
        lst.add(3.0);
        lst.add(5.0);
        lst.add(7.0);

        return lst;
    }

    /**
     * This method creates the second target list for testMixin.
     *
     * @return second target list
     */
    public static MyLinkedList<Number> createMixinTargetList2() {
        MyLinkedList<Number> lst = new MyLinkedList<Number>();

        lst.add(0.0);
        lst.add(2.0);
        lst.add(4.0);
        lst.add(6.0);

        return lst;
    }

    /**
     * This method creates the third target list for testMixin.
     *
     * @return third target list
     */
    public static MyLinkedList<Number> createMixinTargetList3() {
        MyLinkedList<Number> lst = new MyLinkedList<Number>();

        lst.add(1.0);
        lst.add(3.0);
        lst.add(5.0);
        lst.add(7.0);

        return lst;
    }

    /**
     * This method creates the first source list for testMixin.
     *
     * @return first source list
     */
    public static MyLinkedList<String> createMixinSourceList1() {
        MyLinkedList<String> lst = new MyLinkedList<String>();

        lst.add("0");
        lst.add("2");
        lst.add("4");
        lst.add("6");

        return lst;
    }

    /**
     * This method creates the second source list for testMixin.
     *
     * @return second source list
     */
    public static MyLinkedList<String> createMixinSourceList2() {
        MyLinkedList<String> lst = new MyLinkedList<String>();

        lst.add("1");
        lst.add("3");
        lst.add("5");
        lst.add("7");

        return lst;
    }

    /**
     * This method creates the third source list for testMixin.
     *
     * @return third source list
     */
    public static MyLinkedList<String> createMixinSourceList3() {
        MyLinkedList<String> lst = new MyLinkedList<String>();

        lst.add("0");
        lst.add("2");
        lst.add("Exception");
        lst.add("6");

        return lst;
    }

    /**
     * This method creates the result list for testMixin.
     *
     * @return result list
     */
    public static MyLinkedList<Number> createMixinResult() {
        MyLinkedList<Number> lst = new MyLinkedList<Number>();

        lst.add(0.0);
        lst.add(1.0);
        lst.add(2.0);
        lst.add(3.0);
        lst.add(4.0);
        lst.add(5.0);
        lst.add(6.0);
        lst.add(7.0);

        return lst;
    }
}
