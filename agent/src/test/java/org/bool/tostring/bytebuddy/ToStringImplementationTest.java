package org.bool.tostring.bytebuddy;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ToStringImplementationTest {

    private final ToStringImplementation implementation = new ToStringImplementation("(", ")");

    @Test
    void testEquals() {
        assertThat(implementation)
            .isEqualTo(new ToStringImplementation("(", ")"))
            .isNotEqualTo(new ToStringImplementation("[", "]"))
            ;
        assertThat(new ToStringImplementation("[", "]"))
            .isEqualTo(new ToStringImplementation("[", "]"))
            .isNotEqualTo(implementation)
            ;
    }

    @Test
    void testHashCode() {
        assertThat(implementation.hashCode())
            .isEqualTo(new ToStringImplementation("(", ")").hashCode())
            ;
        assertThat(new ToStringImplementation().hashCode())
            .isEqualTo(new ToStringImplementation().hashCode())
            ;
    }
}
