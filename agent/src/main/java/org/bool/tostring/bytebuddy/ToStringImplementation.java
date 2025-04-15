package org.bool.tostring.bytebuddy;

import net.bytebuddy.description.field.FieldDescription;
import net.bytebuddy.description.field.FieldList;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.description.type.TypeDescription;
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

public class ToStringImplementation implements Implementation, ByteCodeAppender {

    private final WithImplicitInvocationTargetType toString = MethodInvocation.invoke(MethodDescriptionFactory.method(Object.class, "toString"));

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
        List<StackManipulation> stack = instrumentedType.getSuperClass().represents(Object.class) && fields.isEmpty()
            ? constantString(prefix)
            : toStringMethod(instrumentedType, prefix, fields);
        return new Size(new StackManipulation.Compound(stack).apply(methodVisitor, context).getMaximalSize(), instrumentedMethod.getStackSize());
    }

    private List<StackManipulation> constantString(String prefix) {
        return Arrays.asList(new TextConstant(prefix + start + end), MethodReturn.REFERENCE);
    }

    private List<StackManipulation> toStringMethod(TypeDescription instrumentedType, String prefix, FieldList<? extends FieldDescription> fields) {
        List<StackManipulation> stack = new ArrayList<>(fields.size() * 5 + 11);
        stack.add(StringBuilderDefs.typeCreation());
        stack.add(Duplication.SINGLE);
        if (instrumentedType.getSuperClass().represents(Object.class)) {
            FieldDescription field = fields.get(0);
            fields = fields.subList(1, fields.size());
            stack.add(new TextConstant(prefix + start + field.getName() + definer));
            stack.add(StringBuilderDefs.constructor());
            stack.add(MethodVariableAccess.loadThis());
            stack.add(FieldAccess.forField(field).read());
            stack.add(StringBuilderDefs.append(field.getType().asErasure()));
        } else {
            stack.add(new TextConstant(prefix + start + "super" + definer));
            stack.add(StringBuilderDefs.constructor());
            stack.add(MethodVariableAccess.loadThis());
            stack.add(toString.special(instrumentedType.getSuperClass().asErasure()));
            stack.add(StringBuilderDefs.append(String.class));
        }
        for (FieldDescription field : fields) {
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
        final int prime = 31;
        int result = 1;
        result = prime * result + ((definer == null) ? 0 : definer.hashCode());
        result = prime * result + ((end == null) ? 0 : end.hashCode());
        result = prime * result + ((prefixResolver == null) ? 0 : prefixResolver.hashCode());
        result = prime * result + ((separator == null) ? 0 : separator.hashCode());
        result = prime * result + ((start == null) ? 0 : start.hashCode());
        return result;
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
        if (definer == null) {
            if (other.definer != null) {
                return false;
            }
        } else if (!definer.equals(other.definer)) {
            return false;
        }
        if (end == null) {
            if (other.end != null) {
                return false;
            }
        } else if (!end.equals(other.end)) {
            return false;
        }
        if (prefixResolver == null) {
            if (other.prefixResolver != null) {
                return false;
            }
        } else if (!prefixResolver.equals(other.prefixResolver)) {
            return false;
        }
        if (separator == null) {
            if (other.separator != null) {
                return false;
            }
        } else if (!separator.equals(other.separator)) {
            return false;
        }
        if (start == null) {
            if (other.start != null) {
                return false;
            }
        } else if (!start.equals(other.start)) {
            return false;
        }
        return true;
    }
}
