package com.github.schottky.zener.util.test;

import com.github.schottky.zener.util.version.Release;
import org.junit.jupiter.params.converter.ArgumentConversionException;
import org.junit.jupiter.params.converter.SimpleArgumentConverter;

public class StringToRelease extends SimpleArgumentConverter {

    @Override
    protected Object convert(Object o, Class<?> aClass) throws ArgumentConversionException {
        String value = (String) o;
        return Release.valueOf(value);
    }
}
