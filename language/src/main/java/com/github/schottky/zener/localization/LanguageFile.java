package com.github.schottky.zener.localization;

import com.github.schottky.zener.api.Tuple;
import com.google.common.base.Preconditions;
import org.apiguardian.api.API;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;

/**
 * A class that represents a File on the file-system that is used to represent a
 * {@link Language}
 */

@API(status = API.Status.STABLE)
public class LanguageFile {

    /**
     * creates a language-file from a {@link java.io.File}
     * <br>The file's path must have the format '/path/to/directory/[language-tag].[file-type]'
     * <br>see {@link Locale} for a list of supported language-tags and {@link StorageProvider} for
     * a list of possible file-types
     * @param file The file to generate the LanguageFile for
     * @return The LanguageFile representing the {@code java.io.File}
     * @throws InvalidLanguageDescription if the file's path does not conform to the format mentioned above
     */

    @NotNull
    @Contract("_ -> new")
    public static LanguageFile fromIOFile(@NotNull File file) throws InvalidLanguageDescription {
        Tuple<Locale,StorageProvider> components = componentsOf(file.getName());
        return new LanguageFile(file.getParentFile(), components.first(), components.second());
    }

    @NotNull
    // package-private so that Language.class may use this
    static Tuple<Locale,StorageProvider> componentsOf(@NotNull String resourceName) throws InvalidLanguageDescription {
        String[] components = resourceName.split("\\.");
        StorageProvider provider;
        if (components.length == 1) {
            provider = StorageProvider.JSON;
        } else if (components.length == 2) {
            provider = StorageProvider.fromFileEnding(components[1]).orElseThrow(() ->
                    new InvalidLanguageDescription("invalid language file-ending: " + components[1]));
        } else {
            throw new InvalidLanguageDescription("Invalid file-format");
        }
        return Tuple.of(Locale.forLanguageTag(components[0]), provider);
    }

    private final File parent;
    private final Locale locale;
    private final StorageProvider storageProvider;

    /**
     * Constructs a new language-file
     * @param parent The parent, or null if this file should not have a parent
     * @param locale The locale to use
     * @param storageProvider determines the file-ending and the way the contents of this file
     *                        are being interpreted
     */

    public LanguageFile(@Nullable File parent, Locale locale, StorageProvider storageProvider) {
        Preconditions.checkArgument(parent != null && parent.isDirectory(), "parent not a directory");
        this.parent = parent;
        this.locale = locale;
        this.storageProvider = storageProvider;
    }

    /**
     * generates a {@code java.io.File} from this LanguageFile
     * @return The File that this object represents
     */

    public File toIOFile() {
        return new File(parent, locale.toLanguageTag() + "." + storageProvider.fileEnding);
    }

    /**
     * returns the locale of this file
     * @return The locale
     */

    public Locale locale() {
        return locale;
    }

    /**
     * returns the StorageProvider for this file that determines the file-ending as well
     * how the contents of this file are being interpreted
     * @return The file-ending
     */

    public StorageProvider storageProvider() {
        return storageProvider;
    }

    public boolean exists() {
        return toIOFile().exists();
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        LanguageFile that = (LanguageFile) object;
        return Objects.equals(parent, that.parent) &&
                Objects.equals(locale, that.locale) &&
                storageProvider == that.storageProvider;
    }

    @Override
    public String toString() {
        return "LanguageFile{" +
                parent.toString() +
                locale.toLanguageTag() + '.' +
                storageProvider.fileEnding +
                '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(parent, locale, storageProvider);
    }

    /**
     * an enum containing all supported ways a file can be stored or read from from
     * the file-system
     */

    public enum StorageProvider {
        JSON("lang"),
        YAML("yaml"),
        YML("yml");

        /**
         * returns a StorageProvider, or an empty optional if the specified file-ending does not
         * represent a valid StorageProvider
         * @param fileEnding The ending to get the provider for
         * @return An optional containing the StorageProvider, if found
         */

        public static Optional<StorageProvider> fromFileEnding(String fileEnding) {
            for (StorageProvider provider: StorageProvider.values()) {
                if (provider.fileEnding.equals(fileEnding)) return Optional.of(provider);
            }
            return Optional.empty();
        }

        final String fileEnding;

        StorageProvider(String fileEnding) {
            this.fileEnding = fileEnding;
        }
    }
}
