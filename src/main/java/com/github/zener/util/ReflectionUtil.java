package com.github.zener.util;

import java.lang.annotation.Annotation;
import java.util.Optional;

public final class ReflectionUtil {

    private ReflectionUtil() {}

    /**
     * returns the annotation that is present at a certain class
     * @param clazz The class that is annotated
     * @param annotationClass The class that represents a certain annotation
     * @param <T> The type of the annotation
     * @return The annotation
     */

    public static <T extends Annotation> Optional<T> annotationFor(Class<?> clazz, Class<T> annotationClass) {
        Class<?> current = clazz;
        do {
            if (current.isAnnotationPresent(annotationClass)) return Optional.of(current.getAnnotation(annotationClass));
            current = current.getSuperclass();
        } while (current != null);
        return Optional.empty();
    }
}