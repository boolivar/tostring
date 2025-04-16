package org.bool.tostring.bytebuddy;

import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType.Builder;
import net.bytebuddy.dynamic.DynamicType.Builder.MethodDefinition.ImplementationDefinition;
import net.bytebuddy.dynamic.DynamicType.Builder.MethodDefinition.ReceiverTypeDefinition;
import net.bytebuddy.implementation.Implementation;
import net.bytebuddy.matcher.ElementMatchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ToStringTransformerTest {

    @Mock
    private Implementation implementation;

    @InjectMocks
    private ToStringTransformer transformer;

    @Test
    <T> void testTransform(@Mock Builder<T> builder, @Mock ImplementationDefinition<T> definition, @Mock ReceiverTypeDefinition<T> receiver) {
        when(builder.method(ElementMatchers.isToString()))
            .thenReturn(definition);
        when(definition.intercept(implementation))
            .thenReturn(receiver);

        assertThat(transformer.transform(builder, TypeDescription.ForLoadedType.of(String.class), null, null, null))
            .isSameAs(receiver);
    }

    @Test
    void testEquals() {
        assertThat(transformer)
            .isEqualTo(new ToStringTransformer(implementation));
        assertThat(transformer)
            .isNotEqualTo(new ToStringTransformer(null));
    }

    @Test
    void testHashCode() {
        assertThat(transformer.hashCode())
            .isEqualTo(new ToStringTransformer(implementation).hashCode());
        assertThat(new ToStringTransformer(null).hashCode())
            .isEqualTo(new ToStringTransformer(null).hashCode());
    }
}
