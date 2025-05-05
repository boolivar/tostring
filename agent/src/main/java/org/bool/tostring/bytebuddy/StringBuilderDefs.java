package org.bool.tostring.bytebuddy;

import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.implementation.ToStringMethod;
import net.bytebuddy.implementation.bytecode.StackManipulation;
import net.bytebuddy.implementation.bytecode.TypeCreation;
import net.bytebuddy.implementation.bytecode.member.MethodInvocation;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class StringBuilderDefs extends ToStringMethod {
    
    private static final StackManipulation stringBuilderTypeCreation = TypeCreation.of(TypeDescription.ForLoadedType.of(StringBuilder.class));

    private static final StackManipulation stringBuilderConstructor = MethodInvocation.invoke(MethodDescriptionFactory.constructor(StringBuilder.class, String.class));

    private static final StackManipulation stringBuilderToStringMethod = MethodInvocation.invoke(MethodDescriptionFactory.method(StringBuilder.class, "toString"));

    private static final Map<Class<?>, ValueConsumer> representsMapping = defaultRepresentsMapping();

    StringBuilderDefs(PrefixResolver prefixResolver) {
        super(prefixResolver);
    }

    public static StackManipulation typeCreation() {
        return stringBuilderTypeCreation;
    }

    public static StackManipulation constructor() { 
        return stringBuilderConstructor;
    }

    public static StackManipulation toStringMethod() { 
        return stringBuilderToStringMethod;
    }

    public static StackManipulation append(Class<?> argType) {
        return append(TypeDescription.ForLoadedType.of(argType));
    }

    public static StackManipulation append(TypeDescription argType) {
        for (Map.Entry<Class<?>, ValueConsumer> e : representsMapping.entrySet()) {
            if (argType.represents(e.getKey())) {
                return e.getValue();
            }
        }
        if (argType.isAssignableTo(CharSequence.class)) {
            return ValueConsumer.CHARACTER_SEQUENCE;
        }
        if (argType.isArray()) {
            return Objects.requireNonNull(argType.getComponentType()).isArray()
                ? ValueConsumer.NESTED_ARRAY
                : ValueConsumer.REFERENCE_ARRAY;
        }
        return ValueConsumer.OBJECT;
    }

    private static Map<Class<?>, ValueConsumer> defaultRepresentsMapping() {
        Map<Class<?>, ValueConsumer> mapping = new HashMap<>();

        mapping.put(boolean.class, ValueConsumer.BOOLEAN);
        mapping.put(char.class, ValueConsumer.CHARACTER);
        mapping.put(byte.class, ValueConsumer.INTEGER);
        mapping.put(short.class, ValueConsumer.INTEGER);
        mapping.put(int.class, ValueConsumer.INTEGER);
        mapping.put(long.class, ValueConsumer.LONG);
        mapping.put(float.class, ValueConsumer.FLOAT);
        mapping.put(double.class, ValueConsumer.DOUBLE);
        mapping.put(String.class, ValueConsumer.STRING);

        mapping.put(boolean[].class, ValueConsumer.BOOLEAN_ARRAY);
        mapping.put(char[].class, ValueConsumer.CHARACTER_ARRAY);
        mapping.put(byte[].class, ValueConsumer.BYTE_ARRAY);
        mapping.put(short[].class, ValueConsumer.SHORT_ARRAY);
        mapping.put(int[].class, ValueConsumer.INTEGER_ARRAY);
        mapping.put(long[].class, ValueConsumer.LONG_ARRAY);
        mapping.put(float[].class, ValueConsumer.FLOAT_ARRAY);
        mapping.put(double[].class, ValueConsumer.DOUBLE_ARRAY);

        return mapping;
    }
}
