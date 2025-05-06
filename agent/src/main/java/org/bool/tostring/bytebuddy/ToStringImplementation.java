package org.bool.tostring.bytebuddy;

import net.bytebuddy.description.field.FieldDescription;
import net.bytebuddy.description.field.FieldList;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.description.type.TypeDescription.Generic;
import net.bytebuddy.dynamic.scaffold.InstrumentedType;
import net.bytebuddy.implementation.Implementation;
import net.bytebuddy.implementation.ToStringMethod.PrefixResolver;
import net.bytebuddy.implementation.bytecode.ByteCodeAppender;
import net.bytebuddy.implementation.bytecode.Duplication;
import net.bytebuddy.implementation.bytecode.StackManipulation;
import net.bytebuddy.implementation.bytecode.constant.TextConstant;
import net.bytebuddy.implementation.bytecode.member.FieldAccess;
import net.bytebuddy.implementation.bytecode.member.MethodInvocation;
import net.bytebuddy.implementation.bytecode.member.MethodInvocation.WithImplicitInvocationTargetType;
import net.bytebuddy.implementation.bytecode.member.MethodReturn;
import net.bytebuddy.implementation.bytecode.member.MethodVariableAccess;
import net.bytebuddy.jar.asm.MethodVisitor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class ToStringImplementation implements Implementation, ByteCodeAppender {

    private static final WithImplicitInvocationTargetType toStringInvocation = MethodInvocation.invoke(MethodDescriptionFactory.method(Object.class, "toString"));

    private final PrefixResolver prefixResolver;

    private final String start;

    private final String separator;

    private final String definer;

    private final String end;

    public ToStringImplementation() {
        this(" {", "}");
    }

    public ToStringImplementation(String start, String end) {
        this(PrefixResolver.Default.SIMPLE_CLASS_NAME, start, ", ", "=", end);
    }

    public ToStringImplementation(PrefixResolver prefixResolver, String start, String separator, String definer, String end) {
        this.prefixResolver = prefixResolver;
        this.start = start;
        this.separator = separator;
        this.definer = definer;
        this.end = end;
    }

    @Override
    public InstrumentedType prepare(InstrumentedType instrumentedType) {
        return instrumentedType;
    }

    @Override
    public ByteCodeAppender appender(Target implementationTarget) {
        return this;
    }

    @Override
    public Size apply(MethodVisitor methodVisitor, Implementation.Context context, MethodDescription instrumentedMethod) {
        TypeDescription instrumentedType = context.getInstrumentedType();
        FieldList<? extends FieldDescription> fields = instrumentedType.getDeclaredFields();
        String prefix = prefixResolver.resolve(instrumentedType);
        Generic superClass = instrumentedType.getSuperClass();
        TypeDescription superType = superClass != null && !superClass.represents(Object.class)
            ? superClass.asErasure()
            : null;
        List<StackManipulation> stack = superClass == null && fields.isEmpty()
            ? constantString(prefix)
            : toStringMethod(superType, prefix, fields);
        return new Size(new StackManipulation.Compound(stack).apply(methodVisitor, context).getMaximalSize(), instrumentedMethod.getStackSize());
    }

    private List<StackManipulation> constantString(String prefix) {
        return Arrays.asList(new TextConstant(prefix + start + end), MethodReturn.REFERENCE);
    }

    private List<StackManipulation> toStringMethod(TypeDescription superType, String prefix, FieldList<? extends FieldDescription> fields) {
        List<StackManipulation> stack = new ArrayList<>(fields.size() * 5 + 11);
        stack.add(StringBuilderDefs.typeCreation());
        stack.add(Duplication.SINGLE);
        if (superType == null) {
            FieldDescription field = fields.get(0);
            stack.add(new TextConstant(prefix + start + field.getName() + definer));
            stack.add(StringBuilderDefs.constructor());
            stack.add(MethodVariableAccess.loadThis());
            stack.add(FieldAccess.forField(field).read());
            stack.add(StringBuilderDefs.append(field.getType().asErasure()));
        } else {
            stack.add(new TextConstant(prefix + start + "super" + definer));
            stack.add(StringBuilderDefs.constructor());
            stack.add(MethodVariableAccess.loadThis());
            stack.add(toStringInvocation.special(superType));
            stack.add(StringBuilderDefs.append(String.class));
        }
        for (FieldDescription field : superType == null ? fields.subList(1, fields.size()) : fields) {
            stack.add(new TextConstant(separator + field.getName() + definer));
            stack.add(StringBuilderDefs.append(String.class));
            stack.add(MethodVariableAccess.loadThis());
            stack.add(FieldAccess.forField(field).read());
            stack.add(StringBuilderDefs.append(field.getType().asErasure()));
        }
        stack.add(new TextConstant(end));
        stack.add(StringBuilderDefs.append(String.class));
        stack.add(StringBuilderDefs.toStringMethod());
        stack.add(MethodReturn.REFERENCE);
        return stack;
    }

    @Override
    public int hashCode() {
        return Objects.hash(definer, end, prefixResolver, separator, start);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        ToStringImplementation other = (ToStringImplementation) obj;
        return Objects.equals(definer, other.definer) && Objects.equals(end, other.end)
            && Objects.equals(prefixResolver, other.prefixResolver) && Objects.equals(separator, other.separator)
            && Objects.equals(start, other.start);
    }
}
