# Zener
A Spigot Library

# Installation

To install this API using Maven add the dependency

```xml
<dependency>
    <groupId>com.github.Schottky-spi</groupId>
    <artifactId>zener</artifactId>
    <version>v1.2.1</version>
</dependency>
```
to your dependencies-list.

Since this library is pretty small, you probably want to shade it into your plugin using the Maven shade-plugin. You can take this template
as a hint

```xml
<plugin>
<groupId>org.apache.maven.plugins</groupId>
<artifactId>maven-shade-plugin</artifactId>
<version>3.2.1</version>
<executions>
    <execution>
        <phase>package</phase>
        <goals>
            <goal>shade</goal>
        </goals>
        <configuration>
            <artifactSet>
                <excludes>
                    <exclude>org.jetbrains:annotations:jar:*</exclude> <!-- not necesairily applicable for any project, serves as an example -->
                </excludes>
            </artifactSet>
        </configuration>
    </execution>
</executions>
</plugin>
```

# Features

## Localization

The main feature of this library is the ability too easily localize any messages. The main entry point
to enable this feature is the `Language`-class. In most cases, a `Language` can simply be created using
The static factory method
```java
Language language = Language.forPlugin(plugin, Locale.US, LanguageFile.StorageProvider.JSON);
```
This will attempt to create a language-object for the locale en-us for your plugin from a language-file using JSON as
storage-provider.
Once you have a language-object, you can get your localized strings like so:
```java
String translated = language.translate("my.identifier");
```
or, if extra values are needed, you can add them as well
```java
String translated = language.translateWithExtra("message.for.player", "player", player.getDisplayName());
```

## Utility-Functions
Other than this main-function, this library provides several utility-classes and functions aimed at making the
life of Spigot-developers better.

### Lore
A `Lore` simply represents the String-list of an Item-Stack's Item-Meta. This class simplifies operations on
lores like removing a range:
```
ItemMeta meta = ...;
Lore lore = Lore.of(meta);
lore.removeRange(1, 3);
```
removing a pre-compiled pattern
```
Pattern pattern = Pattern.quote("Sword of Asparagus");
lore.remove(pattern);
lore.add("Sword of Onions");
```
or simply adding a reset before every new line:
```
lore.add("Sword of Asparagus");
lore.add("Yields the power to cut asparagus");
```
printing this lore has the result
```
§rSword of Asparagus
§rYields the power to cut asparagus
```

### Item-storage

This utility-class is aimed to simplify calls like these:
```
ItemStack stack = ...;
Integer value = Objects.requireNonNull(stack.getItemMeta()).getPersistentDataContainer().get(namespacedKey, PersistentDataType.INTEGER);
if (value != null) {
    // do something
}
```
to something like
```
ItemStorage.getInt(stack.getItemMeta(), namespacedKey).ifPresent(value -> {
    // do something
});
```


### Random-chance collection

This can be used to get elements with a certian weight that might represent a chance. For example:
```
RandomChanceCollection<String> randomChanceCollection = RandomChanceCollection.of(initialElements);
for (int i = 0; i < 4; i++) {
    System.out.println(randomChanceCollection.randomElement());
}
```
could print out
```
foo
foo
foobar
bar
```
given that the initial elements are 
[(50, foo), (25, foobar), (25 bar)]
