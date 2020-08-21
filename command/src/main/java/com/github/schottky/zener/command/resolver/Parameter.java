package com.github.schottky.zener.command.resolver;

import org.jetbrains.annotations.NotNull;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Optional;

public class Parameter {

    public static Parameter[] fromMethod(Method method) {
        final Class<?>[] parameterTypes = method.getParameterTypes();
        final Annotation[][] parameterAnnotations = method.getParameterAnnotations();
        final Parameter[] parameters = new Parameter[parameterTypes.length];
        for (int i = 0; i < parameterTypes.length; i++) {
            final Annotation[] presentAnnotations = Arrays.copyOf(parameterAnnotations[i], parameterAnnotations[i].length);
            final Parameter parameter = new Parameter(parameterTypes[i], presentAnnotations);
            parameters[i] = parameter;
        }
        return parameters;
    }

    public final Class<?> type;
    public final Annotation[] annotations;

    public Parameter(Class<?> type, Annotation[] annotations) {
        this.type = type;
        this.annotations = annotations;
    }

    public Parameter(Class<?> type) {
        this(type, new Annotation[0]);
    }

    public boolean isAnnotationPresent(Class<? extends Annotation> type) {
        for (Annotation annotation: annotations) {
            if (annotation.annotationType() == type) return true;
        }
        return false;
    }

    @SuppressWarnings("unchecked")
    public @NotNull <T extends Annotation> Optional<T> getAnnotation(Class<T> type) {
        for (Annotation annotation: annotations) {
            if (annotation.annotationType() == type) return Optional.of((T) annotation);
        }
        return Optional.empty();
    }
}
