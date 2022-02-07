package h10;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
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
            if (!m.getName().equals("mixinIteratively")
                && !m.getName().equals("mixinRecursively")
                && !m.getName().equals("mixinRecursivelyHelper")) {
                continue;
            }
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
            if (!m.getName().equals("mixinIteratively")
                && !m.getName().equals("mixinRecursively")) {
                continue;
            }

            // is generic with type U
            assertEquals(1, m.getTypeParameters().length,
                         "mixin*-method is not generic");
            assertEquals("U", m.getTypeParameters()[0].getTypeName(),
                         "mixin*-method is generic with an incorrect type");


            // is public
            assertTrue(isPublic(m.getModifiers()));

            // all params are found
            var params = m.getParameters();
            assertEquals(4, params.length, "Parameters in mixin*-method are not complete");

            // param types are correct
            var paramTypes = Arrays.stream(params).map(x -> x.getParameterizedType().getTypeName())
                .collect(Collectors.toList());
            assertTrue(paramTypes.contains("h10.MyLinkedList<U>")
                       && (paramTypes.contains("java.util.function.BiPredicate<? super T, ? super U>")
                           || paramTypes.contains("java.util.function.BiPredicate<? super T,? super U>"))
                       && (paramTypes.contains("java.util.function.Function<? super U, ? extends T>")
                           || paramTypes.contains("java.util.function.Function<? super U,? extends T>"))
                       && paramTypes.contains("java.util.function.Predicate<? super U>"),
                       "Parameters in mixin*-method are incorrect");

            // return type is correct
            assertEquals(void.class, m.getReturnType(),
                         "Return type in mixin*-method is incorrect");

            // thrown exception type is correct
            assertEquals(MyLinkedListException.class, m.getExceptionTypes()[0],
                         "Thrown exception in mixin*-method is incorrect");
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
            if (!m.getName().equals("mixinRecursivelyHelper")) {
                continue;
            }

            // is generic with type U
            assertEquals(1, m.getTypeParameters().length,
                         "mixinRecursivelyHelper method is not generic");
            assertEquals("U", m.getTypeParameters()[0].getTypeName(),
                         "mixinRecursivelyHelper method is generic with an incorrect type");

            // is public
            assertTrue(isPrivate(m.getModifiers()));

            // all params are found
            var params = m.getParameters();
            assertEquals(7, params.length, "Parameters in mixinRecursivelyHelper method are not complete");

            // param types are correct
            var paramTypes = Arrays.stream(params).map(x -> x.getParameterizedType().getTypeName())
                .collect(Collectors.toList());
            assertTrue(paramTypes.contains("h10.MyLinkedList<U>")
                       && (paramTypes.contains("java.util.function.BiPredicate<? super T, ? super U>")
                           || paramTypes.contains("java.util.function.BiPredicate<? super T,? super U>"))
                       && (paramTypes.contains("java.util.function.Function<? super U, ? extends T>")
                           || paramTypes.contains("java.util.function.Function<? super U,? extends T>"))
                       && paramTypes.contains("java.util.function.Predicate<? super U>")
                       && paramTypes.contains("h10.ListItem<T>")
                       && paramTypes.contains("int"),
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
        var thisLists1 = TutorTest_Generators.generateThisListMixin1();
        var otherLists1 = TutorTest_Generators.generateOtherListMixin1WithoutExc();
        var thisLists2 = TutorTest_Generators.generateThisListMixin2();
        var otherLists2 = TutorTest_Generators.generateOtherListMixin2WithoutExc();

        // call test for the first list type (Integer, Integer[])
        helper1.testGeneralMixin(thisLists1, otherLists1, TutorTest_H2_Helper.MethodType.ITERATIVE,
                                 TutorTest_Generators.biPred1, TutorTest_Generators.fctMixin1,
                                 TutorTest_Generators.predU1);
        // call test for the second list type (String, Double)
        helper2.testGeneralMixin(thisLists2, otherLists2, TutorTest_H2_Helper.MethodType.ITERATIVE,
                                 TutorTest_Generators.biPred2, TutorTest_Generators.fctMixin2,
                                 TutorTest_Generators.predU2);
    }

    @Test
    public void testMixinIterativelyException() {
        var thisLists1 = TutorTest_Generators.generateThisListMixin1();
        var otherLists1 = TutorTest_Generators.generateOtherListMixin1WithExc();
        var thisLists2 = TutorTest_Generators.generateThisListMixin2();
        var otherLists2 = TutorTest_Generators.generateOtherListMixin2WithExc();

        // call test for the first list type (Integer, Integer[])
        helper1.testGeneralMixin(thisLists1, otherLists1, TutorTest_H2_Helper.MethodType.ITERATIVE,
                                 TutorTest_Generators.biPred1, TutorTest_Generators.fctMixin1,
                                 TutorTest_Generators.predU1);
        // call test for the second list type (String, Double)
        helper2.testGeneralMixin(thisLists2, otherLists2, TutorTest_H2_Helper.MethodType.ITERATIVE,
                                 TutorTest_Generators.biPred2, TutorTest_Generators.fctMixin2,
                                 TutorTest_Generators.predU2);
    }

    @Test
    public void testMixinRecursively() {
        var thisLists1 = TutorTest_Generators.generateThisListMixin1();
        var otherLists1 = TutorTest_Generators.generateOtherListMixin1WithoutExc();
        var thisLists2 = TutorTest_Generators.generateThisListMixin2();
        var otherLists2 = TutorTest_Generators.generateOtherListMixin2WithoutExc();

        // call test for the first list type (Integer, Integer[])
        helper1.testGeneralMixin(thisLists1, otherLists1, TutorTest_H2_Helper.MethodType.RECURSIVE,
                                 TutorTest_Generators.biPred1, TutorTest_Generators.fctMixin1,
                                 TutorTest_Generators.predU1);
        // call test for the second list type (String, Double)
        helper2.testGeneralMixin(thisLists2, otherLists2, TutorTest_H2_Helper.MethodType.RECURSIVE,
                                 TutorTest_Generators.biPred2, TutorTest_Generators.fctMixin2,
                                 TutorTest_Generators.predU2);
    }

    @Test
    public void testMixinRecursivelyException() {
        var thisLists1 = TutorTest_Generators.generateThisListMixin1();
        var otherLists1 = TutorTest_Generators.generateOtherListMixin1WithExc();
        var thisLists2 = TutorTest_Generators.generateThisListMixin2();
        var otherLists2 = TutorTest_Generators.generateOtherListMixin2WithExc();

        // call test for the first list type (Integer, Integer[])
        helper1.testGeneralMixin(thisLists1, otherLists1, TutorTest_H2_Helper.MethodType.RECURSIVE,
                                 TutorTest_Generators.biPred1, TutorTest_Generators.fctMixin1,
                                 TutorTest_Generators.predU1);
        // call test for the second list type (String, Double)
        helper2.testGeneralMixin(thisLists2, otherLists2, TutorTest_H2_Helper.MethodType.RECURSIVE,
                                 TutorTest_Generators.biPred2, TutorTest_Generators.fctMixin2,
                                 TutorTest_Generators.predU2);
    }

    @Test
    public void testMixinNoOtherMethods() {
        // TODO : test no other (unimplemented / new implemented) methods are used
    }

    @Test
    public void testMixinReallyIteratively() {
        // TODO : test if mixinIteratively has one loop only
    }

    @Test
    public void testMixinReallyRecursively() {
        var thisList = TutorTest_Generators.generateThisListMixinMockito();
        var otherList = TutorTest_Generators.generateOtherListMixinMockito();
        try {
            thisList.mixinRecursively(otherList, TutorTest_Generators.biPred1, TutorTest_Generators.fctMixin1,
                                      TutorTest_Generators.predU1);
            // TODO : ClassTransformer to change modifier method, then verify (but how?)
            /*Mockito.verify(thisList, Mockito.atLeast(2))
                .mixinRecursivelyHelper(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(),
                                        Mockito.any(), Mockito.any());*/
        } catch (Exception e) {
            // MyLinkedListException will never be thrown
            fail("mixinRecursively does not use recursion");
        }
    }
}
