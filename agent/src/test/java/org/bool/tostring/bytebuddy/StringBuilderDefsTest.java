package org.bool.tostring.bytebuddy;

import net.bytebuddy.implementation.ToStringMethod;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class StringBuilderDefsTest extends ToStringMethod {

    StringBuilderDefsTest() {
        super(null);
    }

    @Test
    void testValueConsumer() {
        assertThat(StringBuilderDefs.append(String.class))
            .isSameAs(ValueConsumer.STRING);
        assertThat(StringBuilderDefs.append(StringBuilder.class))
            .isSameAs(ValueConsumer.CHARACTER_SEQUENCE);
        assertThat(StringBuilderDefs.append(byte.class))
            .isSameAs(ValueConsumer.INTEGER);
        assertThat(StringBuilderDefs.append(short.class))
            .isSameAs(ValueConsumer.INTEGER);
        assertThat(StringBuilderDefs.append(int.class))
            .isSameAs(ValueConsumer.INTEGER);
        assertThat(StringBuilderDefs.append(boolean[].class))
            .isSameAs(ValueConsumer.BOOLEAN_ARRAY);
        assertThat(StringBuilderDefs.append(Integer[].class))
            .isSameAs(ValueConsumer.REFERENCE_ARRAY);
        assertThat(StringBuilderDefs.append(int[][].class))
            .isSameAs(ValueConsumer.NESTED_ARRAY);
        assertThat(StringBuilderDefs.append(Object[][].class))
            .isSameAs(ValueConsumer.NESTED_ARRAY);
        assertThat(StringBuilderDefs.append(Integer.class))
            .isSameAs(ValueConsumer.OBJECT);
        assertThat(StringBuilderDefs.append(getClass()))
            .isSameAs(ValueConsumer.OBJECT);
    }
}
