package com.github.schottky.zener.util;

import com.google.common.collect.Lists;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class RandomChanceCollectionTest {

    @Nested class A_new_random_chance_collection {

        @Test
        public void is_empty() {
            assertEquals(0, new RandomChanceCollection<>().size());
            assertEquals(0.0, new RandomChanceCollection<>().sumOfWeights());
        }
    }

    @Nested class An_empty_collection {

        @Test
        public void returns_null_if_quarried() {
            RandomChanceCollection<?> randomChanceCollection = new RandomChanceCollection<>();
            assertNull(randomChanceCollection.randomElement());
        }

        @Test
        public void has_nothing_to_iterate_over() {
            assertFalse(new RandomChanceCollection<>().iterator().hasNext());
            assertThrows(NoSuchElementException.class, () -> new RandomChanceCollection<>().iterator().next());
        }
    }

    List<Tuple<Double,String>> testValues = Lists.newArrayList(
            Tuple.of(50.0, "Hello"),
            Tuple.of(25.0, "World"),
            Tuple.of(25.0, "foo"),
            Tuple.of(75.0, "bar"));

    Map<String,Object> rawMap = setupRawMap();

    public Map<String,Object> setupRawMap() {
        Map<String,Object> rawMap = new HashMap<>();
        int sum = 0;
        for (Tuple<Double,String> tuple: testValues) {
            rawMap.put(String.valueOf(tuple.first() + sum), tuple.second());
            sum += tuple.first();
        }
        return rawMap;
    }

    @Nested public class A_non_empty_collection {

        @ParameterizedTest
        @ValueSource(ints = 1000)
        public void never_returns_null_for_non_null_elements(int repetitions) {
            RandomChanceCollection<?> randomChanceCollection = new RandomChanceCollection<>(testValues);
            for (int i = 0; i < repetitions; i++) {
                assertNotNull(randomChanceCollection.randomElement());
            }
        }

        @Test
        public void retains_its_external_view() {
            RandomChanceCollection<String> randomChanceCollection = RandomChanceCollection.of(testValues);
            assertIterableEquals(testValues, randomChanceCollection);
        }

        @Test
        public void permits_null_values() {
            RandomChanceCollection<String> randomChanceCollection = RandomChanceCollection.of(testValues);
            assertDoesNotThrow(() -> randomChanceCollection.add(30, null));
        }

        @ParameterizedTest
        @CsvSource(value = {"50.0, Hello", "25.0, World", "25.0, foo", "75.0, bar"})
        public void can_remove_values(double weight, String element) {
            RandomChanceCollection<String> randomChanceCollection = RandomChanceCollection.of(testValues);
            boolean changed = randomChanceCollection.remove(weight, element);
            Collection<Tuple<Double,String>> expected = new ArrayList<>(testValues);
            expected.remove(new Tuple<>(weight, element));

            assertEquals(expected.stream().mapToDouble(Tuple::first).sum(), randomChanceCollection.sumOfWeights());
            assertTrue(changed);
            assertIterableEquals(expected, randomChanceCollection);
        }

        @Test
        public void is_empty_when_cleared() {
            RandomChanceCollection<?> randomChanceCollection = new RandomChanceCollection<>(testValues);
            randomChanceCollection.clear();
            assertEquals(0, randomChanceCollection.size());
            assertEquals(0.0, randomChanceCollection.sumOfWeights());
            assertFalse(randomChanceCollection.iterator().hasNext());
        }

        @Test
        public void can_be_serialized() {
            RandomChanceCollection<String> randomChanceCollection = new RandomChanceCollection<>(testValues);
            assertEquals(rawMap, randomChanceCollection.serialize());
        }

        @Test
        public void can_be_deserialized() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
            Constructor<RandomChanceCollection> constructor = RandomChanceCollection.class.getDeclaredConstructor(Map.class);
            constructor.setAccessible(true);
            RandomChanceCollection<?> retrievedCollection = constructor.newInstance(rawMap);
            assertEquals(RandomChanceCollection.of(testValues), retrievedCollection);
        }

    }

    @Nested public class Random_elements {

        @ParameterizedTest
        @ValueSource(ints = 2000)
        public void are_approximately_randomly_distributed(int repetitions) {
            Map<String,Double> rate = Tuple.toMap(testValues, new HashMap<>(), tuple -> Tuple.of(tuple.second(), 0.0));

            RandomChanceCollection<String> randomChanceCollection = new RandomChanceCollection<>(testValues);
            for (int i = 0; i < repetitions; i++) {
                String randomElement = randomChanceCollection.randomElement();
                rate.computeIfPresent(randomElement, (ignored, d) -> d + 1);
            }
            rate.replaceAll((ignored, d) -> d / (double) repetitions);
            Map<String,Double> expected = Tuple.toMap(testValues, new HashMap<>(), Tuple::permuted);
            double sum = expected.values().stream().mapToDouble(d -> d).sum();
            expected.replaceAll((ignored, d) -> d / sum);

            for (Map.Entry<String,Double> entry: rate.entrySet()) {
                assertEquals(entry.getValue(), expected.get(entry.getKey()), 0.1);
            }
        }

        @ParameterizedTest
        @ValueSource(ints = {0, 5, 10})
        public void can_be_bulked(int values) {
            Collection<String> randomElements = new RandomChanceCollection<>(testValues).randomElements(values);
            assertEquals(values, randomElements.size());
            for (Object value: randomElements) {
                assertEquals(String.class, value.getClass());
            }
        }
    }

}