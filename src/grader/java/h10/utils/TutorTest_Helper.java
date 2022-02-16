package h10.utils;

import h10.MyLinkedListException;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * Define some methods that are used throughout the test.
 *
 * @author Arianne Roselina Prananto
 */
public class TutorTest_Helper {

    /**
     * Get a class if it exists, throw an error otherwise.
     *
     * @param className the class' name
     * @return the class
     */
    public static Class<?> getClass(String className) {
        Class<?> aClass = null;
        try {
            aClass = Class.forName(className);
        } catch (ClassNotFoundException e) {
            fail(TutorTest_Messages.classNotFound(className));
        }
        return aClass;
    }

    /**
     * Get a class if it exists, but do not throw an error otherwise.
     *
     * @param className the class' name
     * @return the class
     */
    public static Class<?> getClassDontFail(String className) {
        Class<?> aClass;
        try {
            aClass = Class.forName(className);
        } catch (ClassNotFoundException e) {
            return null;
        }
        return aClass;
    }

    /**
     * Get a method if it exists, throw an error otherwise.
     *
     * @param methodName the method's name
     * @return the method
     */
    public static Method getMethod(String methodName, Class<?> theClass, Class<?>... args) {
        Method method = null;
        try {
            method = theClass.getDeclaredMethod(methodName, args);
        } catch (NoSuchMethodException | SecurityException e) {
            fail(TutorTest_Messages.methodNotFound(methodName));
        }
        return method;
    }

    /**
     * Get a method if it exists, but do not throw an error otherwise.
     *
     * @param methodName the method's name
     * @return the method
     */
    public static Method getMethodDontFail(String methodName, Class<?> theClass, Class<?>... args) {
        Method method;
        try {
            method = theClass.getDeclaredMethod(methodName, args);
        } catch (NoSuchMethodException | SecurityException e) {
            return null;
        }
        return method;
    }

    /**
     * Get the constructor of MyLinkedListException class if it exists, throw an error otherwise.
     *
     * @param aClass the class (MyLinkedListException)
     * @return the constructor of MyLinkedListException
     */
    public static Constructor<MyLinkedListException> getMyLinkedListExceptionConstructor(Class<?> aClass) {
        int exceptions = 0;
        Constructor<?> constructor = null;
        try {
            constructor = aClass.getDeclaredConstructor(Integer.class, Object.class);
        } catch (NoSuchMethodException | SecurityException e) {
            exceptions++;
        }
        try {
            constructor = aClass.getDeclaredConstructor(Object.class, Integer.class);
        } catch (NoSuchMethodException | SecurityException e) {
            exceptions++;
        }

        // does exist
        assertTrue(exceptions < 2, TutorTest_Messages.methodNotFound(aClass.getSimpleName()));
        return (Constructor<MyLinkedListException>) constructor;
    }
}
