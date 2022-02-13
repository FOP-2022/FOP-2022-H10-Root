package h10;

import h10.utils.TutorTest_Helper;
import h10.utils.TutorTest_Messages;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.sourcegrade.jagr.api.rubric.TestForSubmission;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Random;

import static java.lang.reflect.Modifier.isAbstract;
import static java.lang.reflect.Modifier.isPublic;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * Defines the JUnit test cases related to the class defined in the task H1.
 *
 * @author Arianne Roselina Prananto
 */
@TestForSubmission("h10")
@DisplayName("Criterion: H1")
public final class TutorTest_H1 {
    static final String className = "MyLinkedListException";

    /* *********************************************************************
     *                                 H1                                  *
     **********************************************************************/

    @Test
    public void testClassSignatures() {
        // class is found
        var classH1 = TutorTest_Helper.getClass(className);
        // is not abstract
        assertFalse(isAbstract(classH1.getModifiers()), TutorTest_Messages.classModifierIncorrect(className));
        // is public
        assertTrue(isPublic(classH1.getModifiers()), TutorTest_Messages.classModifierIncorrect(className));
        // is a direct extension from Exception
        assertEquals(Exception.class, classH1.getSuperclass(),
                     TutorTest_Messages.classExtendsIncorrect(className));
    }

    @Test
    public void testConstructor() {
        // class is found
        Class<?> classH1 = TutorTest_Helper.getClass(className);
        var constructor = TutorTest_Helper.getMyLinkedListExceptionConstructor(classH1);

        // is public
        assertTrue(isPublic(constructor.getModifiers()), TutorTest_Messages.methodModifierIncorrect(className));
    }

    @Test
    public void testMessage() {
        // class is found
        Class<?> classH1 = TutorTest_Helper.getClass(className);

        int numOfTests = 100;
        var nums = createManyRandomIntegers(numOfTests);
        var objs = createManyRandomObjects(numOfTests);

        for (int i = 0; i < numOfTests; i++) {
            var constructor = TutorTest_Helper.getMyLinkedListExceptionConstructor(classH1);
            try {
                assertEquals(constructor.newInstance(nums[i], objs[i]).getMessage(),
                             createCorrectMessage(nums[i], objs[i]),
                             String.format("Constructor %s returns wrong message", className));
            } catch (Exception e) {
                fail(String.format("Cannot create an object of class %s", className));
            }
        }
    }

    /* *********************************************************************
     *                        Some random generators                       *
     **********************************************************************/

    String createCorrectMessage(Integer num, Object obj) {
        return "(" + num + "," + obj.toString() + ")";
    }

    Integer[] createManyRandomIntegers(int numOfTests) {
        Integer[] nums = new Integer[numOfTests];
        Random rn = new Random();
        for (int i = 0; i < numOfTests; i++) {
            nums[i] = rn.nextInt();
        }
        return nums;
    }

    Object[] createManyRandomObjects(int numOfTests) {
        Object[] objs = new Object[numOfTests];
        Random rn = new Random();
        for (int i = 0; i < numOfTests; i++) {
            if (i % 2 == 0) {
                objs[i] = Integer.toHexString(rn.nextInt());
            } else {
                objs[i] = rn.nextInt();
            }
        }
        return objs;
    }
}
