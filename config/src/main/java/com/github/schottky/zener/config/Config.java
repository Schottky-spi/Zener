package com.github.schottky.zener.config;

import com.github.schottky.zener.api.Zener;
import com.github.schottky.zener.config.bind.Converters;
import com.github.schottky.zener.config.bind.Convertible;
import com.github.schottky.zener.messaging.Console;
import com.google.common.annotations.VisibleForTesting;
import org.apiguardian.api.API;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.function.Supplier;

/**
 * Represents a Configuration that is connected to a so called
 * "Options-class". The configuration is a simple {@link FileConfiguration}.
 * The Options-class is a class that contains static and public
 * attributes that are represented by this config. The purpose of this
 * is to map the contents of the file-configuration to an "objectified" configuration,
 * meaning a config that no longer contains primitive, resp. ConfigurationSerializable
 * objects but instead more sophisticated objects that are "ready-to-use".
 * This can also seen as an abbreviation of (for example)
 * <pre>{@code
 * plugin.getConfig().getString("my.path.to.a.name", "default");
 * }</pre>
 * to
 * <pre>{@code
 * Options.name
 * }</pre>
 * <br>
 * The only requirement to a class that holds configuration-objects
 * is that every object to be fetched from the config needs to be public
 * and static in the Options-class. The field may be final and may contain default-values.
 * <br>A static and public field will be ignored, if the {@link Ignore}-annotation is present.
 * <br>To enable custom conversion from a config-object, {@linkplain Convertible converters} are
 * required. There are two possibilities to register custom conversions:
 * <ol>
 *      <li>Every object that needs a custom conversion can have the annotation {@link ConvertWith}.
 *      This annotation requires the class that handles the conversion (and implements {@code Convertible}.
 *      <li>If every object of a certain type should be converted the same way, you can use
 *      {@link #registerCustomConvertible(Class, Convertible)} to enable this feature.
 * </ol>
 * <br>The order of conversion is as follows:
 * <ol>
 *     <li>If a {@code ConvertWith}-annotation is present, the algorithm will attempt to convert
 *     the config-object using the given convertible.
 *     <li>If this is not the case, the algorithm will try to find a registered convertible. The convertibles
 *     are static, meaning that all config-objects will use the same global resolution-policy
 *     <li>If all of the above methods fail, the algorithm will attempt to directly set the field of
 *     using the exact object that was given. This is, for example, the case for primitives (int, boolean,...) or strings
 * </ol>
 */

@API(status = API.Status.EXPERIMENTAL)
public class Config {

    private final Class<?> configClass;

    /**
     * returns the class that this object manages
     * @return The class
     */
    public Class<?> configClass() { return configClass; }

    private FileConfiguration yamlConfig;

    /**
     * returns the File-configuration-object that acts as a value-source
     * @return The file-configuration-object
     */
    public FileConfiguration fileConfiguration() { return yamlConfig; }

    private static final Map<Class<?>, Convertible<?>> customConvertibles = new HashMap<>();

    /**
     * register a custom convertible that converts all objects of the given type
     * to the type of the convertible
     * @param forType The type to convert
     * @param convertible The convertible to use
     * @param <T> The type of the type
     */
    public static <T> void registerCustomConvertible(Class<T> forType, Convertible<T> convertible) {
        customConvertibles.put(forType, convertible);
    }

    static {
        registerCustomConvertible(UUID.class, new Converters.ToUUID());
        registerCustomConvertible(Locale.class, new Converters.ToLocale());
        registerCustomConvertible(Material.class, new Converters.ToMaterial());
    }

    private final Supplier<FileConfiguration> configSupplier;

    /**
     * constructs a config-object that is able to reload config-options
     * @param configClass The class that this should manage
     * @param configSupplier The supplier to get a new config. This will be used
     *                       to reload the config as well
     */
    public Config(Class<?> configClass, Supplier<FileConfiguration> configSupplier) {
        this.configClass = configClass;
        this.configSupplier = configSupplier;
    }

    /**
     * constructs a config-object that cannot be reload (resp. will always
     * return the same config when reload)
     * @param configClass The class that this should manage
     * @param yamlConfig The config to use
     */

    public Config(Class<?> configClass, FileConfiguration yamlConfig) {
        this(configClass, () -> yamlConfig);
    }

    /**
     * returns a config that manages the default configuration of the providing
     * plugin
     * @param configClass The class that this should manage
     * @return The config-object that can be used to reload the config from disk
     */

    public static Config forDefault(Class<?> configClass) {
        return new Config(configClass, () -> {
            Zener.providingPlugin().reloadConfig();
            return Zener.providingPlugin().getConfig();
        });
    }

    public static void loadDefaultConfig(Class<?> configClass) {
        new Config(configClass, Zener.providingPlugin().getConfig()).reload();
    }

    public void reload() {
        this.yamlConfig = configSupplier.get();
        try {
            this.injectAll();
        } catch (MissingConfigEntry missingConfigEntry) {
            missingConfigEntry.printStackTrace();
        }
    }

    @VisibleForTesting
    public void injectInto(Field field) throws MissingConfigEntry {
        try {
            inject0(field);
        } catch (MissingConfigEntry e) {
            throw e;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    private void inject0(Field field) throws Exception {
        if (field.isAnnotationPresent(Ignore.class))
            return;
        final String path = getPath(field);
        final Class<?> type = field.getType();
        if (!yamlConfig.contains(path)) {
            if (requiresValue(field)) {
                throw new MissingConfigEntry();
            } else {
                return;
            }
        }
        final Object o = Objects.requireNonNull(yamlConfig.get(path));
        field.setAccessible(true);

        if (field.isAnnotationPresent(ConvertWith.class)) {
            Class<? extends Convertible<?>> cClass = field.getAnnotation(ConvertWith.class).value();
            Convertible<?> convertible = instantiateConvertible(cClass, type);
            Object converted = convertible.convertFrom(o);
            field.set(null, converted);
        } else if (customConvertibles.get(type) != null) {
            Object converted = customConvertibles.get(type).convertFrom(o);
            field.set(null, converted);
        } else {
            try {
                field.set(null, o);
            } catch (IllegalArgumentException exc) {
                Console.warning("Config-value %s is of wrong type (should be %s, but is %s)",
                        path,
                        type,
                        o.getClass().getSimpleName());
            }
        }
    }

    private Convertible<?> instantiateConvertible(Class<? extends Convertible<?>> cClass, Class<?> param) {
        Constructor<? extends Convertible<?>> constructor;
        try {
            constructor = cClass.getConstructor(Class.class);
            return instantiate(constructor, param);
        } catch (NoSuchMethodException e) {
            try {
                constructor = cClass.getConstructor();
                return instantiate(constructor);
            } catch (NoSuchMethodException noSuchMethodException) {
                Console.severe("Illegal Convertible: %s provides no valid constructor", cClass.getName());
                throw new Error();
            }
        }
    }

    private Convertible<?> instantiate(Constructor<? extends Convertible<?>> constructor, Object... params) {
        try {
            return constructor.newInstance(params);
        } catch (IllegalAccessException | InstantiationException | InvocationTargetException e) {
            Console.warning("An Unexpected error occurred while instantiating a convertible");
            throw new Error(e);
        }
    }

    private Object convert(
            Class<? extends Convertible<?>> convertibleClass,
            Object convertFrom,
            Class<?> type) throws Exception
    {
        Convertible<?> instance;
        try {
            Constructor<? extends Convertible<?>> constructor = convertibleClass.getConstructor(Class.class);
            instance = constructor.newInstance(type);
        } catch (NoSuchMethodException ignored) {
            instance = convertibleClass.newInstance();
        }
        return instance.convertFrom(convertFrom);
    }

    private void injectAll() throws MissingConfigEntry {
        for (Field field: configClass.getFields()) {
            injectInto(field);
        }
    }

    private String getPath(Field field) {
        if (field.isAnnotationPresent(Path.class)) {
            return field.getAnnotation(Path.class).value();
        } else {
            return field.getName();
        }
    }

    private boolean requiresValue(Field field) {
        return (field.isAnnotationPresent(Required.class));
    }
}
