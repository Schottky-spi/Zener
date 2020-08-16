package com.github.schottky.zener.config;

import com.github.schottky.zener.config.bind.Convertible;
import com.github.schottky.zener.config.bind.EnumConvertible;
import com.github.schottky.zener.config.bind.ListConvertible;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.util.Vector;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SuppressWarnings("unused")
public class ExampleOptions {

    // simple boolean value
    public static boolean enable;

    // value with some path
    @Path("some.random.path")
    public static double anInt;

    // converted using a simple converter
    @ConvertWith(Person.Converter.class)
    public static Person person;

    // Configuration serializable
    public static Vector vector;

    // Ignored value
    @Ignore
    public static Void x;

    // Required value that is not present
    @Required
    public static int forteTwo;

    // Non-required value that is also no present
    public static int forteOne;

    // using the prebuilt converter
    @ConvertWith(EnumConvertible.class)
    public static Material material;

    // converting a list to a set
    @ConvertWith(ListToSet.class)
    public static Set<String> list;

    // example class with custom converters
    public static class Person {
        private final String name;
        private final String surname;

        public Person(String name, String surname) {
            this.name = name;
            this.surname = surname;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Person person = (Person) o;
            return Objects.equals(name, person.name) &&
                    Objects.equals(surname, person.surname);
        }

        @Override
        public String toString() {
            return "Person{" +
                    "name='" + name + '\'' +
                    ", surname='" + surname + '\'' +
                    '}';
        }

        // Simple converter for a person
        static class Converter implements Convertible<Person> {
            @Override
            public Person convertFrom(Object in) {
                ConfigurationSection section = (ConfigurationSection) in;
                return new Person(section.getString("name"), section.getString("surname"));
            }
        }
    }

    public static class ListToSet implements ListConvertible<Set<String>> {

        @Override
        public Set<String> convertFromStream(Stream<?> stream) {
            return stream.map(String::valueOf).collect(Collectors.toSet());
        }
    }
}
