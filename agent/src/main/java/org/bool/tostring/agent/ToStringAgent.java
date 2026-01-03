package org.bool.tostring.agent;

import org.bool.tostring.bytebuddy.ToStringImplementation;
import org.bool.tostring.bytebuddy.ToStringTransformer;

import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.agent.builder.AgentBuilder.Transformer;
import net.bytebuddy.agent.builder.AgentBuilder.TypeStrategy;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.matcher.ElementMatcher;

import java.lang.instrument.Instrumentation;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static net.bytebuddy.matcher.ElementMatchers.*;

public class ToStringAgent {

    public static void premain(String agentArgs, Instrumentation i13n) {
        List<String> options = Arrays.stream(Objects.toString(agentArgs, "").split(","))
            .map(String::trim)
            .collect(Collectors.toList());
        new ToStringAgent().install(i13n, options);
    }

    public void install(Instrumentation i13n, List<String> options) {
        new AgentBuilder.Default()
            .with(TypeStrategy.Default.REDEFINE)
            .type(typeMatcher(options.stream().filter(o -> !o.startsWith("--")).findFirst().orElse(null), options.contains("--force")))
            .transform(transformer())
            .installOn(i13n);
    }

    private ElementMatcher<? super TypeDescription> typeMatcher(String regex, boolean force) {
        return not(force ? none() : declaresMethod(isToString()))
            .and(regex != null ? nameMatches(regex) : any());
    }

    private Transformer transformer() {
        return new ToStringTransformer(new ToStringImplementation());
    }
}