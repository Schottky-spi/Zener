package com.github.schottky.zener.command.resolver;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ParameterTest {

    void testMethod1(int a, int b, String c, Object d) {}

    @Test
    public void the_parameter_contains_the_correct_types() throws NoSuchMethodException {
        Parameter[] parameters = Parameter.fromMethod(getClass()
                .getDeclaredMethod("testMethod1", int.class, int.class, String.class, Object.class));
        assertEquals(4, parameters.length);
        assertEquals(int.class, parameters[0].type);
        assertEquals(int.class, parameters[1].type);
        assertEquals(String.class, parameters[2].type);
        assertEquals(Object.class, parameters[3].type);
    }

    void testMethod2() {}

    @Test
    public void the_parameters_contain_no_types_when_the_method_has_no_argument() throws NoSuchMethodException {
        Parameter[] parameters = Parameter.fromMethod(getClass().getDeclaredMethod("testMethod2"));
        assertEquals(0, parameters.length);
    }

    void testMethod3(@Unresolved int a, @Describe("b") int b, @Unresolved @Describe("c") double c, char d) {}

    @Test
    public void the_parameter_contains_the_correct_annotations() throws NoSuchMethodException {
        Parameter[] parameters = Parameter.fromMethod(getClass()
                .getDeclaredMethod("testMethod3", int.class, int.class, double.class, char.class));
        assertEquals(4, parameters.length);
        assertEquals(1, parameters[0].annotations.length);
        assertTrue(parameters[0].isAnnotationPresent(Unresolved.class));

        assertEquals(1, parameters[1].annotations.length);
        assertTrue(parameters[1].isAnnotationPresent(Describe.class));

        assertEquals(2, parameters[2].annotations.length);
        assertTrue(parameters[2].isAnnotationPresent(Describe.class));
        assertTrue(parameters[2].isAnnotationPresent(Unresolved.class));

        assertEquals(0, parameters[3].annotations.length);
    }


}