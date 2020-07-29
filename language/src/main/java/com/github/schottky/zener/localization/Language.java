package com.github.schottky.zener.localization;

import com.github.schottky.zener.api.Tuple;
import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.commons.lang.Validate;
import org.apiguardian.api.API;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.util.*;
import java.util.regex.Pattern;

/**
 * Represents a structure that maps identifiers to texts. This mapping
 * is bound to a certain locale.
 * <br>Two languages are seen as equivalent, if the two locales of the languages are equivalent.
 * Only the default-language may have the locale {@code null}, indicating that this is a special case
 * <br>Restrictions apply on the keys that an object of this class can hold. To check if a key is valid,
 * see {@link #isValidIdentifier(String)}
 */

@API(status = API.Status.STABLE)
public class Language {

    // --------------------------------------------------- Defaults ----------------------------------------------------

    private static final Language defaultLang = new Language(Collections.emptyMap(), null);

    /**
     * Adds the specified raw-map to the defaults.
     * These values will be taken if a certain language-value is not present
     * for a given identifier.
     * <br>see {@link Language#translate(String)} for details on how this class gets the value that will be translated,
     * given a certain identifier
     * @param rawMap The map containing the default-values. This must be in the correct format
     * @throws IllegalArgumentException if the map contains an invalid identifier
     * (as defined by {@link Language#isValidIdentifier(String)})
     */
    public static void addDefaults(Map<String,String> rawMap) {
        defaultLang.addAllOf(rawMap);
    }

    /**
     * convenience-method to add defaults as via {@link Language#addDefaults(Map)}
     * The String-array must contain (in order) a translation key, followed by the
     * value of that key
     * @param values The raw value to add to the default language
     * @throws IllegalArgumentException if the map contains an invalid identifier
     * (as defined by {@link Language#isValidIdentifier(String)}) or the input-array is not divisible by 2
     */

    @Contract(pure = true)
    public static void addDefaults(@NotNull String... values) {
        Preconditions.checkArgument(values.length % 2 == 0, "input-array odd");
        Map<String, String> rawMap = new HashMap<>();
        for (int i = 0; i < values.length; i += 2) {
            Validate.isTrue(isValidIdentifier(values[i]), "Invalid identifier");
            rawMap.put(values[i], values[i+1]);
        }
        addDefaults(rawMap);
    }

    /**
     * returns the default-value for a certain identifier.
     * Unlike {@link Language#translate(String)}, this method will throw if
     * the identifier cannot be found.
     * <br>In general, this method should not be used. The default-language is designed
     * to provide a suitable replacement, if a certain language does not contain a mapping
     * for a certain identifier. This is done automatically
     * @param identifier The identifier to get the default value for
     * @return the default value for a certain identifier
     * @throws NullPointerException if this identifier does not exist
     */

    @NotNull
    public static String getDefault(String identifier) {
        String value = defaultLang.find(identifier).orElseThrow(() ->
                new RuntimeException("identifier not in default language"));
        return ChatColor.translateAlternateColorCodes(colorChar, value);
    }

    // ---------------------------------------------------- Current ----------------------------------------------------

    private static Language current = defaultLang;

    /**
     * returns the language to be used in a static context.
     * <br>Per default, this will be the default-language
     * @return The language to use in a general context
     */
    public static Language current() {
        return current;
    }

    /**
     * Set the language to use and access statically
     * @param current The new language to use
     */

    public static void setCurrent(@NotNull Language current) { Language.current = current; }

    // -------------------------------------------------- Color-code ---------------------------------------------------

    private static char colorChar = '&';

    /**
     * Sets the color char that is used to colorize any translated strings.
     * <br>Per default, this is <i>'&amp;'</i>
     * <br>This color char is used by the method {@link ChatColor#translateAlternateColorCodes(char, String)}
     * @param colorChar The new color char to use
     * @throws IllegalArgumentException if the color char is not defined
     */

    public static void setColorChar(char colorChar) {
        Preconditions.checkArgument(Character.isDefined(colorChar));
        Language.colorChar = colorChar;
    }


    // ------------------------------------------------ Input-Validation -----------------------------------------------

    private static final Pattern VALID_INPUT = Pattern.compile("([a-z0-9]|_)+(?:\\.([a-z0-9]|_)+)*");
    private static final Pattern DOT = Pattern.compile("\\.");

    /**
     * Returns true, if a certain identifier is valid, or not.
     * <br>A valid identifier is a dot-separated ('.') String that may
     * only contain lowercase letters, underscores and numbers. A dot may not occur at the very
     * beginning or end.
     * <br>Examples for valid identifiers:
     * <ul>
     *     <li>foo.bar
     *     <li>foo.foo_bar
     *     <li>foo.bar.foo_bar
     * </ul>
     * <br>Examples for invalid identifiers:
     * <ul>
     *     <li>foo.Bar
     *     <li>foo.&amp;bar
     *     <li>.foo.bar
     * </ul>
     * @param input the input to check
     * @return true if, and only if the input-string is a valid identifier
     */

    public static boolean isValidIdentifier(String input) {
        return VALID_INPUT.matcher(input).matches();
    }

    // ----------------------------------------------- Standard behavior -----------------------------------------------

    private final Tree<String,String> languageTree = new Tree<>();

    private final Locale locale;

    /**
     * returns the locale that this language represents
     * @return The locale
     */
    public Locale locale() {
        return locale;
    }

    /**
     * constructs a new Language with the given raw map.
     * <br>Usually, this constructor should not be called by an outside source.
     * Convenience method exist to load a language from a file, or to add
     * default language-values.
     * <br>The raw map must contain valid identifiers as keys
     * (all identifiers must return {@code true} if checked by {@link #isValidIdentifier(String)})
     * and the translated texts as values. No restrictions on the text apply
     * @param identValueMap The map to use as raw value
     * @param locale The locale that this language represents
     * @throws IllegalArgumentException if the identValueMap contains invalid identifiers
     * @see #addDefaults(String...)
     * @see #fromFile(File)
     */
    public Language(@NotNull Map<String,String> identValueMap, Locale locale) {
        this.addAllOf(identValueMap);
        this.locale = locale;
    }

    private void addAllOf(@NotNull Map<String,String> rawMap) {
        for (Map.Entry<String,String> entry: rawMap.entrySet()) {
            final String identifier = entry.getKey();
            final String value = entry.getValue();

            Validate.isTrue(isValidIdentifier(identifier), "Invalid identifier");
            String[] branches = DOT.split(identifier);
            languageTree.addLeafFor(branches, value);
        }
    }

    /**
     * returns the text that this identifier represents.
     * <br>If this language does not contain a mapping for the identifier,
     * the default language is used. If the default language also doesn't contain the
     * identifier, the identifier itself is returned unchanged.
     * @param identifier The identifier to get the translated text for
     * @return The translated text, if a mapping exists, or the identifier if none exists
     * @throws IllegalArgumentException if the identifier is invalid as defined by {@link #isValidIdentifier(String)}
     * @see #translateWithExtra(String, Map)
     */

    public String translate(@NotNull String identifier) {
        Preconditions.checkArgument(isValidIdentifier(identifier), "invalid input to translate");
        String translated = find(identifier).orElseSupply(() ->
                defaultLang.hasMappingFor(identifier) ? getDefault(identifier) : identifier);
        return ChatColor.translateAlternateColorCodes(colorChar, translated);
    }

    public String translate(@NotNull Localizable localizable) {
        return translate(localizable.identifier());
    }

    /**
     * returns true if, and only if this language contains a mapping for a given identifier,
     * in other words {@link Language#translate(String)} would return the mapped text.
     * <br> If the identifier is invalid as defined by {@link #isValidIdentifier(String)}, this method
     * returns false
     * @param identifier The identifier to check
     * @return true, if a mapping exists, false otherwise
     */

    public boolean hasMappingFor(String identifier) {
        if (!isValidIdentifier(identifier)) return false;
        return find(identifier).isSet();
    }

    private SearchResult<String> find(String identifier) {
        Deque<String> searchQuarry = enqueueAllOf(DOT.split(identifier));
        return languageTree.traceLeafStartingFromRoot(searchQuarry);
    }

    @NotNull
    private Deque<String> enqueueAllOf(@NotNull String[] strings) {
        Deque<String> deque = new ArrayDeque<>();
        for (String string: strings) {
            deque.addLast(string);
        }
        return deque;
    }

    /**
     * Translates the identifier as defined by {@link #translate(String)}. Then, replaces all occurrences
     * of every key of the given map inside the translated text with a String-representation of the value that belongs
     * to this key. The key has to occur in curly brackets inside the translated text. For example, the String
     * <br>"Hello, {what}!"
     * would be translated to
     * <br>"hello, World!"
     * if the Map contained the mapping {"what" -&gt; "World"}
     * @param identifier The identifier to translate
     * @param extra The extra values to apply to the translated string after translation
     * @return The translated and adjusted string
     * @throws IllegalArgumentException if the identifier is invalid as defined by {@link #isValidIdentifier(String)}
     * @see #translateWithExtra(String, String, Object)
     * @see #translateWithExtra(String, String, Object, String, Object)
     */

    public String translateWithExtra(@NotNull String identifier, @NotNull Map<String,Object> extra) {
        String translated = translate(identifier);
        for (Map.Entry<String,Object> extraEntry: extra.entrySet()) {
            Object value = extraEntry.getValue();
            String translatedValue = value instanceof Localizable ? translate((Localizable) value) : Objects.toString(value);
            translated = translated.replace("{" + extraEntry.getKey() + "}", translatedValue);
        }
        return translated;
    }

    /**
     * convenience method for the replacement of a single target with a value
     * @param identifier The identifier to translate
     * @param replace The target to replace
     * @param with The value to replace the target with
     * @return The translated and adjusted string
     * @throws IllegalArgumentException if the identifier is invalid as defined by {@link #isValidIdentifier(String)}
     * @see #translateWithExtra(String, Map)
     *
     */

    public String translateWithExtra(@NotNull String identifier, String replace, Object with) {
        Map<String,Object> extra = Collections.singletonMap(replace, with);
        return translateWithExtra(identifier, extra);
    }

    /**
     * convenience method for the replacement of two targets with values
     * @param identifier The identifier to translate
     * @param replaceFirst The first target to replace
     * @param withFirstValue The first value to replace the target with
     * @param replaceSecond The second target to replace
     * @param withSecondValue The second value to replace the target with
     * @return The translated and adjusted string
     * @throws IllegalArgumentException if the identifier is invalid as defined by {@link #isValidIdentifier(String)}
     * @see #translateWithExtra(String, Map)
     */

    public String translateWithExtra(
            String identifier,
            String replaceFirst, Object withFirstValue,
            String replaceSecond, Object withSecondValue)
    {
        Map<String,Object> map = new HashMap<>();
        map.put(replaceFirst, withFirstValue);
        map.put(replaceSecond, withSecondValue);
        return translateWithExtra(identifier, map);
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Language language = (Language) object;
        return Objects.equals(locale, language.locale) &&
                languageTree.equals(language.languageTree);
    }

    @Override
    public int hashCode() {
        return Objects.hash(locale);
    }

    @Override
    public String toString() {
        Map<String,String> rawMap = this.asRawMap(10);
        StringJoiner joiner = new StringJoiner(",", locale + "{", "}");
        rawMap.forEach((identifier, value) -> joiner.add(identifier + ": " + value));
        if (rawMap.size() > languageTree.size()) { joiner.add("..."); }
        return joiner.toString();
    }

    // ------------------------------------------ Serializing/De-serializing -------------------------------------------

    public static Language readFrom(
            Reader reader,
            Locale locale,
            @NotNull LanguageFile.StorageProvider usedStorageProvider)
    {
        switch (usedStorageProvider) {
            case JSON:
                JsonObject object = new JsonParser().parse(reader).getAsJsonObject();
                return fromJson(object, locale);
            case YAML:
            case YML:
                return fromConfigurationSection(YamlConfiguration.loadConfiguration(reader), locale);
            default:
                throw new RuntimeException("Unimplemented storage-provider: " + usedStorageProvider);
        }
    }

    public static Language readFrom(
            Reader reader,
            @NotNull Tuple<Locale,LanguageFile.StorageProvider> components)
    {
        return readFrom(reader, components.first(), components.second());
    }

    public void writeTo(
            Writer writer,
            @NotNull LanguageFile.StorageProvider usedStorageProvider) throws IOException {
        switch (usedStorageProvider) {
            case JSON:
                String json = new GsonBuilder()
                        .setPrettyPrinting()
                        .create()
                        .toJson(toJson());
                writer.write(json);
                break;
            case YAML:
            case YML:
                writer.write(toYamlConfiguration().saveToString());
                break;
            default:
                throw new RuntimeException("Unimplemented storage-provider: " + usedStorageProvider);
        }
    }

    @NotNull
    @Contract("_, _ -> new")
    public static Language fromJson(@NotNull JsonObject json, Locale locale) {
        Map<String,String> rawMap = new HashMap<>();
        for (Map.Entry<String, JsonElement> jsonEntry: json.entrySet()) {
            JsonElement value = jsonEntry.getValue();
            rawMap.put(jsonEntry.getKey(), value.getAsString());
        }
        return new Language(rawMap, locale);
    }

    public JsonObject toJson() {
        JsonObject object = new JsonObject();
        this.asRawMap(-1).forEach(object::addProperty);
        return object;
    }

    /**
     * Attempts to get a language for a plugin. In order to do this, this method will search in different locations to
     * find the desired language. The steps are as follows:
     * <br> First, the plugin will try to find the file inside the plugin's data-folder ({@link Plugin#getDataFolder()}
     * and any sub-folders if so desired by the option {@link Option#PARENT_DIR}
     * <br> Then, the plugin will try to find the language inside the {@code resources}-directory of the jar
     * (as if by calling {@link Plugin#getResource(String)} with the string being
     * <pre>{@code locale.toLanguageTag() + '.' + storageProvider.fileEnding}</pre>
     * A directory can also be specified using the
     * option {@link Option#RESOURCE_LOC}
     * <br>If the language couldn't be found in any of these locations, a fallback-resource will be taken. This has to
     * be present inside the resources-directory and defaults to "en-us.lang". This can be customized using the option
     * {@link Option#FALLBACK}. If this fallback does not exist, a {@code NullPointerException} will be thrown.
     * <br> Options, for example the parent-dir, can be specified using the syntax {@code Option.PARENT_DIR.set("/lang")}
     * @param plugin The plugin to get the language for
     * @param locale The desired locale
     * @param storageProvider The storage-provider to use (.lang, .yml,...)
     * @param options options to configure where to look for the language-files
     * @return A Language for the desired locale with the specified storage-provider, or the fallback if none was found
     * @throws NullPointerException if the fallback does not exist
     */


    @NotNull
    public static Language forPlugin(
            @NotNull Plugin plugin,
            @NotNull Locale locale,
            LanguageFile.@NotNull StorageProvider storageProvider,
            Language.Option @NotNull ... options)
    {
        File parentDirectory = plugin.getDataFolder();
        StringBuilder resourceLocation = new StringBuilder(locale.toLanguageTag()).append('.').append(storageProvider.fileEnding);
        String fallbackResource = "en-us.lang";
        for (Option option: options) {
            switch (option) {
                case PARENT_DIR:
                    parentDirectory = new File(parentDirectory, option.value);
                    break;
                case RESOURCE_LOC:
                    resourceLocation.insert(0, option.value + "/");
                    break;
                case FALLBACK:
                    fallbackResource = option.value;
                    break;
            }
        }
        LanguageFile languageFile = new LanguageFile(parentDirectory, locale, storageProvider);
        if (languageFile.exists()) {
            try {
                return Language.fromFile(languageFile);
            } catch (FileNotFoundException ignored) {}
        }

        try {
            String resource = resourceLocation.toString();
            Tuple<Locale, LanguageFile.StorageProvider> nameComponents = LanguageFile.componentsOf(resource);
            InputStream is = plugin.getResource(resource);
            if (is == null) {
                Bukkit.getConsoleSender().sendMessage(ChatColor.YELLOW + "Desired language '" + locale +
                        "' can neither be found inside the plugin-folder nor the resources-folder");
                Bukkit.getConsoleSender().sendMessage(ChatColor.YELLOW + "Falling back to locale " + fallbackResource);
                is = Objects.requireNonNull(plugin.getResource(fallbackResource));
                nameComponents = LanguageFile.componentsOf(fallbackResource);
            }
            return readFrom(new InputStreamReader(is), nameComponents.first(), nameComponents.second());
        } catch (InvalidLanguageDescription exception) {
            throw new Error(exception);
        }
    }

    /**
     * Valid options to specify where to look for a certain language-file in a certain context.
     * Options can be used like {@code Option opt = Option.<yourOption>.set("foo")}.
     * Every option has a default value of common use-cases. This will be used, if the option is
     * specified without setting it (calling it's {@link Option#set(String)} method).
     */

    public enum Option {
        /**
         * where to look for in the plugin-folder. Defaults to "lang"
         */
        PARENT_DIR("lang"),
        /**
         * where to look for in the resources-folder. Defaults to "lang"
         * A trailing File-separator ("/") must not be inserted
         */
        RESOURCE_LOC("lang"),
        /**
         * Where to find the fallback-resource, defaults to "en-us.lang)
         */
        FALLBACK("en-us.lang");

        String value;

        Option(String defaultValue) {
            this.value = defaultValue;
        }

        /**
         * sets the value of this option; meaning where to look for when creating a file for a plugin
         * @param value The value to set
         * @return This option with modified state
         */

        public Option set(String value) {
            this.value = value;
            return this;
        }
    }

    @NotNull
    @Contract("_, _ -> new")
    public static Language fromConfigurationSection(@NotNull ConfigurationSection section, Locale locale) {
        final Map<String,String> rawMap = new HashMap<>();
        for (Map.Entry<String,Object> sectionEntry: section.getValues(true).entrySet()) {
            if (sectionEntry.getValue() instanceof ConfigurationSection) continue;
            rawMap.put(sectionEntry.getKey(), Objects.toString(sectionEntry.getValue()));
        }
        return new Language(rawMap, locale);
    }

    public YamlConfiguration toYamlConfiguration() {
        YamlConfiguration configuration = new YamlConfiguration();
        this.asRawMap(-1).forEach(configuration::set);
        return configuration;
    }

    public Map<String,String> asRawMap(int limit) {
        return languageTree.traverseAndFlatten(strings -> Joiner.on('.').join(strings), limit);
    }

    // --------------------------------------------------- File I/O ----------------------------------------------------

    @NotNull
    public static Language fromFile(File file) throws InvalidLanguageDescription, FileNotFoundException {
        return fromFile(LanguageFile.fromIOFile(file));
    }

    /**
     * Tries to find a certain file for a given locale in the parent-directory.
     * <br>A valid language-file is characterized by three conditions:
     * <ul>
     *     <li>The language-name must equal to the given locale's language-tag ({@link Locale#toLanguageTag()})
     *     <li>The file-ending must be a valid storage-provider (such valid storage-providers are all of
     *     {@link LanguageFile.StorageProvider}
     *     <li>From the conditions above, the file-name; if split by the character '.' may only contain
     *     one or two elements
     * </ul>
     * If the parent does not represent a directory, this will return an empty optional.
     *
     * @param locale The locale to find the language for
     * @param parent Where to look for the file
     * @return The language, if it was found, en empty optional otherwise
     */

    public static Optional<Language> findFileForLocale(Locale locale, @NotNull File parent) {
        if (!parent.isDirectory()) return Optional.empty();
        String[] files = Objects.requireNonNull(parent.list((dir, name) -> {
            String[] components = DOT.split(name);
            if (components.length == 1) return components[0].equals(locale.toLanguageTag());
            if (components.length == 2) return components[0].equals(locale.toLanguageTag()) &&
                    LanguageFile.StorageProvider.fromFileEnding(components[1]).isPresent();
            return false;
        }));
        if (files.length != 1) return Optional.empty();
        try {
            Language language = fromFile(new File(parent, files[0]));
            return Optional.of(language);
        } catch (InvalidLanguageDescription | FileNotFoundException ignored) {
            return Optional.empty();
        }
    }

    /**
     * returns a language from a resource (a resource can be found in the "resources"-directory inside the
     * compiled .jar-file). If the desired resource-name does not exist or doesn't represent a valid
     * language, returns an empty optional.
     *
     * @param plugin The plugin to get the language for
     * @param resourceName The name of the resource. This name must be fully written (e.g. "en-us.lang", not "en-us")
     * @return The Language, if it was present, or an empty optional
     */

    public static Optional<Language> fromResources(@NotNull Plugin plugin, String resourceName) {
        InputStream inputStream = plugin.getResource(resourceName);
        if (inputStream == null) return Optional.empty();
        try {
            Tuple<Locale, LanguageFile.StorageProvider> components = LanguageFile.componentsOf(resourceName);
            return Optional.of(readFrom(new InputStreamReader(inputStream), components.first(), components.second()));
        } catch (InvalidLanguageDescription exception) {
            return Optional.empty();
        }
    }

    public static Language fromFile(@NotNull LanguageFile file) throws FileNotFoundException {
        final FileReader reader = new FileReader(file.toIOFile());
        final Language language = readFrom(reader, file.locale(), file.storageProvider());
        try {
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return language;
    }

    public void saveToFile(@NotNull File file, LanguageFile.StorageProvider storageProvider) throws IOException {
        LanguageFile languageFile = new LanguageFile(file.getParentFile(), this.locale, storageProvider);
        final FileWriter writer = new FileWriter(languageFile.toIOFile());
        writeTo(writer, storageProvider);
        writer.close();
    }

}
