package h10;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.sourcegrade.jagr.api.rubric.TestForSubmission;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.stream.Collectors;

import static java.lang.reflect.Modifier.isPrivate;
import static java.lang.reflect.Modifier.isPublic;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Defines the JUnit test cases related to the class defined in the task H2.2.
 *
 * @author Arianne Roselina Prananto
 */
@TestForSubmission("h10")
@DisplayName("Criterion: H2.2")
public final class TutorTest_H2_2 {
    TutorTest_H2_Helper<Integer> helper1 = new TutorTest_H2_Helper<>();
    TutorTest_H2_Helper<String> helper2 = new TutorTest_H2_Helper<>();

    /* *********************************************************************
     *                               H2.2                                  *
     **********************************************************************/

    @Test
    public void testMixinMethodsExist() {
        Class<?> classH2 = null;
        try {
            classH2 = Class.forName("h10.MyLinkedList");
        } catch (ClassNotFoundException e) {
            fail("Class MyLinkedList does not exist");
        }

        int found = 0;
        for (Method m : classH2.getDeclaredMethods()) {
            if (!m.getName().equals("mixinIteratively") && !m.getName().equals("mixinRecursively") &&
                !m.getName().equals("mixinRecursivelyHelper"))
                continue;
            found++;
        }
        // methods are found
        assertEquals(3, found, "At least one mixin*-method does not exist");
    }

    @Test
    public void testMixinMethodsSignatures() {
        Class<?> classH2 = null;
        try {
            classH2 = Class.forName("h10.MyLinkedList");
        } catch (ClassNotFoundException e) {
            fail("Class MyLinkedList does not exist");
        }

        for (Method m : classH2.getDeclaredMethods()) {
            if (!m.getName().equals("mixinIteratively") && !m.getName().equals("mixinRecursively"))
                continue;

            // TODO : check method's generic type

            // is public
            assertTrue(isPublic(m.getModifiers()));

            // all params are found
            var params = m.getParameters();
            assertEquals(4, params.length, "Parameters in mixin*-methods are not complete");

            // param types are correct
            var paramTypes = Arrays.stream(params).map(x -> x.getParameterizedType().getTypeName())
                .collect(Collectors.toList());
            assertTrue(paramTypes.contains("h10.MyLinkedList<U>") &&
                       (paramTypes.contains("java.util.function.BiPredicate<? super T, ? super U>") ||
                        paramTypes.contains("java.util.function.BiPredicate<? super T,? super U>")) &&
                       (paramTypes.contains("java.util.function.Function<? super U, ? extends T>") ||
                        paramTypes.contains("java.util.function.Function<? super U,? extends T>")) &&
                       paramTypes.contains("java.util.function.Predicate<? super U>"),
                       "Parameters in mixin*-methods are incorrect");

            // return type is correct
            assertEquals(void.class, m.getReturnType(),
                         "Return type in mixin*-methods is incorrect");

            // thrown exception type is correct
            assertEquals(MyLinkedListException.class, m.getExceptionTypes()[0],
                         "Thrown exception in mixin*-methods is incorrect");
        }
    }

    @Test
    public void testMixinHelperMethod() {
        Class<?> classH2 = null;
        try {
            classH2 = Class.forName("h10.MyLinkedList");
        } catch (ClassNotFoundException e) {
            fail("Class MyLinkedList does not exist");
        }

        for (Method m : classH2.getDeclaredMethods()) {
            if (!m.getName().equals("mixinRecursivelyHelper")) continue;

            // TODO : check method's generic type

            // is public
            assertTrue(isPrivate(m.getModifiers()));

            // all params are found
            var params = m.getParameters();
            assertEquals(7, params.length, "Parameters in mixinRecursivelyHelper method are not complete");

            // param types are correct
            var paramTypes = Arrays.stream(params).map(x -> x.getParameterizedType().getTypeName())
                .collect(Collectors.toList());
            assertTrue(paramTypes.contains("h10.MyLinkedList<U>") &&
                       (paramTypes.contains("java.util.function.BiPredicate<? super T, ? super U>") ||
                        paramTypes.contains("java.util.function.BiPredicate<? super T,? super U>")) &&
                       (paramTypes.contains("java.util.function.Function<? super U, ? extends T>") ||
                        paramTypes.contains("java.util.function.Function<? super U,? extends T>")) &&
                       paramTypes.contains("java.util.function.Predicate<? super U>") &&
                       paramTypes.contains("h10.ListItem<T>") &&
                       paramTypes.contains("int"),
                       "Parameters in mixinRecursivelyHelper method are incorrect");

            // return type is correct
            assertEquals(void.class, m.getReturnType(),
                         "Return type in mixinRecursivelyHelper method is incorrect");

            // thrown exception type is correct
            assertEquals(MyLinkedListException.class, m.getExceptionTypes()[0],
                         "Thrown exception in mixinRecursivelyHelper method is incorrect");
        }
    }

    @Test
    public void testMixinIteratively() {
        var thisList1 = helper1.generateThisListMixin1();
        var otherList1 = helper1.generateOtherListMixin1WithoutExc();
        var thisList2 = helper2.generateThisListMixin2();
        var otherList2 = helper2.generateOtherListMixin2WithoutExc();

        // first list
        for (int i = 0; i < thisList1.length; i++) {
            var thisListCopy = helper1.copyList(thisList1[i]);
            var otherListCopy = helper1.copyList(otherList1[i]);
            try {
                thisList1[i].mixinIteratively(otherList1[i], helper1.biPred1, helper1.fctMixin1, helper1.predU1);
            } catch (MyLinkedListException e) {
                fail("extractIteratively method fails with exception: " + e.getMessage());
            }
            try {
                helper1.expectedMixin(thisListCopy, otherListCopy, helper1.biPred1, helper1.fctMixin1, helper1.predU1);
                helper1.assertLinkedList(otherListCopy, otherList1[i]);
            } catch (MyLinkedListException e) {
                // never going to happen
            }
        }

        // second list
        for (int i = 0; i < thisList2.length; i++) {
            var thisListCopy = helper2.copyList(thisList2[i]);
            var otherListCopy = helper2.copyList(otherList2[i]);
            try {
                thisList2[i].mixinIteratively(otherList2[i], helper2.biPred2, helper2.fctMixin2, helper2.predU2);
            } catch (MyLinkedListException e) {
                fail("extractIteratively method fails with exception: " + e.getMessage());
            }
            try {
                helper2.expectedMixin(thisListCopy, otherListCopy, helper2.biPred2, helper2.fctMixin2, helper2.predU2);
                helper2.assertLinkedList(otherListCopy, otherList2[i]);
            } catch (MyLinkedListException e) {
                // never going to happen
            }
        }
    }

    @Test
    public void testMixinIterativelyException() {
        var thisList1 = helper1.generateThisListMixin1();
        var otherList1 = helper1.generateOtherListMixin1WithExc();
        var thisList2 = helper2.generateThisListMixin2();
        var otherList2 = helper2.generateOtherListMixin2WithExc();
        MyLinkedListException actualExc = null;

        // first list
        for (int i = 0; i < thisList1.length; i++) {
            var thisListCopy = helper1.copyList(thisList1[i]);
            var otherListCopy = helper1.copyList(otherList1[i]);
            try {
                thisList1[i].mixinIteratively(otherList1[i], helper1.biPred1, helper1.fctMixin1, helper1.predU1);
            } catch (MyLinkedListException e) {
                actualExc = e;
            }
            try {
                helper1.expectedMixin(thisListCopy, otherListCopy, helper1.biPred1, helper1.fctMixin1, helper1.predU1);
            } catch (MyLinkedListException e) {
                helper1.assertExceptionMessage(e, actualExc);
            }
        }

        // second list
        for (int i = 0; i < thisList2.length; i++) {
            var thisListCopy = helper2.copyList(thisList2[i]);
            var otherListCopy = helper2.copyList(otherList2[i]);
            try {
                thisList2[i].mixinIteratively(otherList2[i], helper2.biPred2, helper2.fctMixin2, helper2.predU2);
            } catch (MyLinkedListException e) {
                actualExc = e;
            }
            try {
                helper2.expectedMixin(thisListCopy, otherListCopy, helper2.biPred2, helper2.fctMixin2, helper2.predU2);
            } catch (MyLinkedListException e) {
                helper2.assertExceptionMessage(e, actualExc);
            }
        }
    }

    @Test
    public void testMixinRecursively() {
        var thisList1 = helper1.generateThisListMixin1();
        var otherList1 = helper1.generateOtherListMixin1WithoutExc();
        var thisList2 = helper2.generateThisListMixin2();
        var otherList2 = helper2.generateOtherListMixin2WithoutExc();

        // first list
        for (int i = 0; i < thisList1.length; i++) {
            var thisListCopy = helper1.copyList(thisList1[i]);
            var otherListCopy = helper1.copyList(otherList1[i]);
            try {
                thisList1[i].mixinIteratively(otherList1[i], helper1.biPred1, helper1.fctMixin1, helper1.predU1);
            } catch (MyLinkedListException e) {
                fail("extractIteratively method fails with exception: " + e.getMessage());
            }
            try {
                helper1.expectedMixin(thisListCopy, otherListCopy, helper1.biPred1, helper1.fctMixin1, helper1.predU1);
                helper1.assertLinkedList(otherListCopy, otherList1[i]);
            } catch (MyLinkedListException e) {
                // never going to happen
            }
        }

        // second list
        for (int i = 0; i < thisList2.length; i++) {
            var thisListCopy = helper2.copyList(thisList2[i]);
            var otherListCopy = helper2.copyList(otherList2[i]);
            try {
                thisList2[i].mixinRecursively(otherList2[i], helper2.biPred2, helper2.fctMixin2, helper2.predU2);
            } catch (MyLinkedListException e) {
                fail("extractIteratively method fails with exception: " + e.getMessage());
            }
            try {
                helper2.expectedMixin(thisListCopy, otherListCopy, helper2.biPred2, helper2.fctMixin2, helper2.predU2);
                helper2.assertLinkedList(otherListCopy, otherList2[i]);
            } catch (MyLinkedListException e) {
                // never going to happen
            }
        }
    }

    @Test
    public void testMixinRecursivelyException() {
        var thisList1 = helper1.generateThisListMixin1();
        var otherList1 = helper1.generateOtherListMixin1WithExc();
        var thisList2 = helper2.generateThisListMixin2();
        var otherList2 = helper2.generateOtherListMixin2WithExc();
        MyLinkedListException actualExc = null;

        // first list
        for (int i = 0; i < thisList1.length; i++) {
            var thisListCopy = helper1.copyList(thisList1[i]);
            var otherListCopy = helper1.copyList(otherList1[i]);
            try {
                thisList1[i].mixinIteratively(otherList1[i], helper1.biPred1, helper1.fctMixin1, helper1.predU1);
            } catch (MyLinkedListException e) {
                actualExc = e;
            }
            try {
                helper1.expectedMixin(thisListCopy, otherListCopy, helper1.biPred1, helper1.fctMixin1, helper1.predU1);
            } catch (MyLinkedListException e) {
                helper1.assertExceptionMessage(e, actualExc);
            }
        }

        // second list
        for (int i = 0; i < thisList2.length; i++) {
            var thisListCopy = helper2.copyList(thisList2[i]);
            var otherListCopy = helper2.copyList(otherList2[i]);
            try {
                thisList2[i].mixinRecursively(otherList2[i], helper2.biPred2, helper2.fctMixin2, helper2.predU2);
            } catch (MyLinkedListException e) {
                actualExc = e;
            }
            try {
                helper2.expectedMixin(thisListCopy, otherListCopy, helper2.biPred2, helper2.fctMixin2, helper2.predU2);
            } catch (MyLinkedListException e) {
                helper2.assertExceptionMessage(e, actualExc);
            }
        }
    }

    @Test
    public void testMixinNoOtherMethods() {
        // TODO : test no other methods
    }

    @Test
    public void testMixinReallyIteratively() {
        // TODO : test really iteratively
    }

    @Test
    public void testMixinReallyRecursively() {
        // TODO : test really recursively
    }

}
