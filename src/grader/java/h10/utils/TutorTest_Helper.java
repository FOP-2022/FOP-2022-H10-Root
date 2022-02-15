package h10.utils;

import h10.MyLinkedListException;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

public class TutorTest_Helper {

    public static Class<?> getClass(String className) {
        Class<?> aClass = null;
        try {
            aClass = Class.forName("h10." + className);
        } catch (ClassNotFoundException e) {
            fail(TutorTest_Messages.classNotFound(className));
        }
        return aClass;
    }

    public static Class<?> getClassDontFail(String className) {
        Class<?> aClass;
        try {
            aClass = Class.forName("h10." + className);
        } catch (ClassNotFoundException e) {
            return null;
        }
        return aClass;
    }

    public static Method getMethod(String methodName, Class<?> theClass, Class<?>... args) {
       Method method = null;
        try {
            method = theClass.getDeclaredMethod(methodName, args);
        } catch (NoSuchMethodException | SecurityException e) {
            fail(TutorTest_Messages.methodNotFound(methodName));
        }
        return method;
    }

    public static Method getMethodDontFail(String methodName, Class<?> theClass, Class<?>... args) {
        Method method;
        try {
            method = theClass.getDeclaredMethod(methodName, args);
        } catch (NoSuchMethodException | SecurityException e) {
            return null;
        }
        return method;
    }

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
