package org.bool.tostring.bytebuddy;

import net.bytebuddy.agent.builder.AgentBuilder.Transformer;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.dynamic.DynamicType.Builder;
import net.bytebuddy.implementation.Implementation;
import net.bytebuddy.utility.JavaModule;

import java.security.ProtectionDomain;
import java.util.Objects;

import static net.bytebuddy.matcher.ElementMatchers.isToString;

public class ToStringTransformer implements Transformer {

    private final Implementation implementation;

    public ToStringTransformer(Implementation implementation) {
        this.implementation = implementation;
    }

    @Override
    public Builder<?> transform(DynamicType.Builder<?> builder, TypeDescription typeDescription,
            ClassLoader classLoader, JavaModule module, ProtectionDomain protectionDomain) {
        return builder.method(isToString()).intercept(implementation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(implementation);
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
        ToStringTransformer other = (ToStringTransformer) obj;
        return Objects.equals(implementation, other.implementation);
    }
}
