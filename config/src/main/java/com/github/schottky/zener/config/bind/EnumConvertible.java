package com.github.schottky.zener.config.bind;

/**
 * Can be used to convert Strings to enums.
 * Since this method uses {@link Enum#valueOf(Class, String)}
 * to convert to the enum-type, this can be majorly in-performant
 * and is thus not present as a default converter. This should be used
 * as a simple converter, when the expected enum-size is fairly small.
 * However, for example when converting to a material, the already built-in
 * {@linkplain Converters.ToMaterial ToMaterial}-converter
 * should be used
 */
@SuppressWarnings({"unchecked", "rawtypes"}) // assume the user knows how to deal with this
public class EnumConvertible implements Convertible<Enum> {

    private final Class<Enum> clazz;

    public EnumConvertible(Class<Enum> optionsClass) { this.clazz = optionsClass; }

    @Override
    public Enum convertFrom(Object in) { return Enum.valueOf(clazz, ((String) in).toUpperCase()); }
}
