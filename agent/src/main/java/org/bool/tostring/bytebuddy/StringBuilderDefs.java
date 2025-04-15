package org.bool.tostring.bytebuddy;

import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.implementation.ToStringMethod;
import net.bytebuddy.implementation.bytecode.StackManipulation;
import net.bytebuddy.implementation.bytecode.TypeCreation;
import net.bytebuddy.implementation.bytecode.member.MethodInvocation;

public class StringBuilderDefs extends ToStringMethod {
    
    private static final StackManipulation TYPE_CREATION = TypeCreation.of(TypeDescription.ForLoadedType.of(StringBuilder.class));

    private static final StackManipulation CONSTRUCTOR = MethodInvocation.invoke(MethodDescriptionFactory.constructor(StringBuilder.class, String.class));

    private static final StackManipulation TO_STRING = MethodInvocation.invoke(MethodDescriptionFactory.method(StringBuilder.class, "toString"));

    StringBuilderDefs(PrefixResolver prefixResolver) {
        super(prefixResolver);
    }

    public static StackManipulation typeCreation() {
        return TYPE_CREATION;
    }

    public static StackManipulation constructor() { 
        return CONSTRUCTOR;
    }

    public static StackManipulation toStringMethod() { 
        return TO_STRING;
    }

    public static StackManipulation append(Class<?> argType) {
        return append(TypeDescription.ForLoadedType.of(argType));
    }

    public static StackManipulation append(TypeDescription argType) {
        if (argType.represents(boolean.class)) {
            return ValueConsumer.BOOLEAN;
        } else if (argType.represents(char.class)) {
            return ValueConsumer.CHARACTER;
        } else if (argType.represents(byte.class)
                || argType.represents(short.class)
                || argType.represents(int.class)) {
            return ValueConsumer.INTEGER;
        } else if (argType.represents(long.class)) {
            return ValueConsumer.LONG;
        } else if (argType.represents(float.class)) {
            return ValueConsumer.FLOAT;
        } else if (argType.represents(double.class)) {
            return ValueConsumer.DOUBLE;
        } else if (argType.represents(String.class)) {
            return ValueConsumer.STRING;
        } else if (argType.isAssignableTo(CharSequence.class)) {
            return ValueConsumer.CHARACTER_SEQUENCE;
        } else if (argType.represents(boolean[].class)) {
            return ValueConsumer.BOOLEAN_ARRAY;
        } else if (argType.represents(byte[].class)) {
            return ValueConsumer.BYTE_ARRAY;
        } else if (argType.represents(short[].class)) {
            return ValueConsumer.SHORT_ARRAY;
        } else if (argType.represents(char[].class)) {
            return ValueConsumer.CHARACTER_ARRAY;
        } else if (argType.represents(int[].class)) {
            return ValueConsumer.INTEGER_ARRAY;
        } else if (argType.represents(long[].class)) {
            return ValueConsumer.LONG_ARRAY;
        } else if (argType.represents(float[].class)) {
            return ValueConsumer.FLOAT_ARRAY;
        } else if (argType.represents(double[].class)) {
            return ValueConsumer.DOUBLE_ARRAY;
        } else if (argType.isArray()) {
            return argType.getComponentType().isArray()
                ? ValueConsumer.NESTED_ARRAY
                : ValueConsumer.REFERENCE_ARRAY;
        } else {
            return ValueConsumer.OBJECT;
        }
    }
}
