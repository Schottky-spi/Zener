package com.github.schottky.zener.util.test;

import org.junit.jupiter.params.converter.ArgumentConversionException;
import org.junit.jupiter.params.converter.SimpleArgumentConverter;

public class CSVToArray extends SimpleArgumentConverter {

    @Override
    protected Object convert(Object o, Class<?> aClass) throws ArgumentConversionException {
        String s = (String) o;
        return s.split("\\s*,\\s*");
    }
}
