package org.bool.tostring.agent;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;

class OptionsTest {

    @NullSource
    @EmptySource
    @ParameterizedTest
    void testEmptyArgs(String args) {
        assertThat(Options.parse(args))
            .usingRecursiveComparison()
            .isEqualTo(new Options())
            ;
    }

    @ValueSource(strings = {"--force", " --force  "})
    @ParameterizedTest
    void testForce(String args) {
        assertThat(Options.parse(args))
            .returns(true, Options::isForce)
            ;
    }

    @ValueSource(strings = {"org\\.bool\\..+", " org\\.bool\\..+  "})
    @ParameterizedTest
    void testRegex(String args) {
        assertThat(Options.parse(args))
            .returns("org\\.bool\\..+", Options::getRegex)
            ;
    }

    @ValueSource(strings = {".*.MyDto,--force", "--force,.*.MyDto", " --force , .*.MyDto "})
    @ParameterizedTest
    void testMultipleOptions(String args) {
        assertThat(Options.parse(args))
            .returns(".*.MyDto", Options::getRegex)
            .returns(true, Options::isForce)
            ;
    }
}
