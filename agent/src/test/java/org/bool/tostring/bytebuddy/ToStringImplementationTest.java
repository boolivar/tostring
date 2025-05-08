package org.bool.tostring.bytebuddy;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.dynamic.loading.ClassLoadingStrategy;
import net.bytebuddy.matcher.ElementMatchers;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ToStringImplementationTest {

    private final ToStringImplementation implementation = new ToStringImplementation("(", ")");

    @Test
    void testToString() throws Exception {
        Class<?> dynamicType = new ByteBuddy()
            .redefine(SimpleDto.class)
            .method(ElementMatchers.isToString())
            .intercept(implementation)
            .make()
            .load(getClass().getClassLoader(), ClassLoadingStrategy.Default.CHILD_FIRST)
            .getLoaded();

        assertThat(dynamicType.getDeclaredConstructor(String.class, int.class).newInstance("test", 22).toString())
            .isEqualTo("SimpleDto(name=test, value=22)");
    }

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
