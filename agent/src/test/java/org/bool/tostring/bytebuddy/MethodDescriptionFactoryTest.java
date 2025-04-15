package org.bool.tostring.bytebuddy;

import net.bytebuddy.description.type.TypeDescription;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static net.bytebuddy.matcher.ElementMatchers.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MethodDescriptionFactoryTest {

    @Test
    void testRuntimeException() {
        assertThatThrownBy(this::throwsChecked)
            .isInstanceOf(IOException.class);
    }

    private void throwsChecked() {
        throw MethodDescriptionFactory.asRuntimeException(new IOException("test"));
    }

    @Test
    void testConstructor() {
        assertThat(MethodDescriptionFactory.constructor(StringBuilder.class, String.class))
            .isEqualTo(TypeDescription.ForLoadedType.of(StringBuilder.class)
                .getDeclaredMethods()
                .filter(isConstructor().and(takesArguments(String.class)))
                .getOnly())
            ;
    }

    @Test
    void testMethod() {
        assertThat(MethodDescriptionFactory.method(StringBuilder.class, "append", String.class))
            .isEqualTo(TypeDescription.ForLoadedType.of(StringBuilder.class)
                .getDeclaredMethods()
                .filter(named("append").and(takesArguments(String.class)).and(returns(StringBuilder.class)))
                .getOnly())
            ;
    }
}
