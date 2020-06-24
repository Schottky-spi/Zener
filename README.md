# Zener
A Spigot Library

# Installation

To install this API using Maven add the dependency

```xml
<dependency>
    <groupId>com.github.Schottky-spi</groupId>
    <artifactId>zener</artifactId>
    <version>v1.1.1</version>
</dependency>
```
to your dependencies-list. You will also have to add the repository to your repositories-list:

```xml
<repository>
    <id>github</id>
    <url>https://maven.pkg.github.com/Schottky-spi/Zener</url>
</repository>
```

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
                    <exclude>org.jetbrains:annotations:jar:*</exclude> <!-- not necesairily applicable for any project, serves as an example
                </excludes>
            </artifactSet>
        </configuration>
    </execution>
</executions>
</plugin>
```

# Features

In work
