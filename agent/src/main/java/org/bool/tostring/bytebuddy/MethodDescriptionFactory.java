package org.bool.tostring.bytebuddy;

import net.bytebuddy.description.method.MethodDescription;

public class MethodDescriptionFactory {

    public static MethodDescription.InDefinedShape constructor(Class<?> cls, Class<?>... argTypes) {
        try {
            return new MethodDescription.ForLoadedConstructor(cls.getConstructor(argTypes));
        } catch (NoSuchMethodException e) {
            throw asRuntimeException(e);
        }
    }

    public static MethodDescription.InDefinedShape method(Class<?> cls, String methodName, Class<?>... argTypes) {
        try {
            return new MethodDescription.ForLoadedMethod(cls.getMethod(methodName, argTypes));
        } catch (NoSuchMethodException e) {
            throw asRuntimeException(e);
        }
    }

    static <T extends Exception> T asRuntimeException(Exception e) throws T {
        throw (T) e;
    }
}
