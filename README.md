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
to enable this feature is the `Language`-class. You should usually create instances of this class using 
the many factory methods inside this class, for example
```java
Language language = Language.fromFile(new File(plugin.getDataFolder(), "en_us.lang"));
```
will attempt to create a language-object from the file '.../plugins/yourPlugin/en_us.lang'.
Have a look at the documentation to see what kind of language-files are supported.
Once you have a language, you can get your localized strings like so:
```java
String translated = language.translate("my.identifier");
```
