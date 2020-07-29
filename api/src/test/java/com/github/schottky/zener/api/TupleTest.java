package com.github.schottky.zener.api;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class TupleTest {

    @Nested class A_Tuple {

        @Test
        public void equals_a_same_tuple() {
            Tuple<Integer, Integer> first = new Tuple<>(42, 1);
            Tuple<Integer, Integer> second = new Tuple<>(42, 1);
            assertEquals(first, second);
        }

        @Test
        public void when_permuted_the_order_changes() {
            Tuple<Integer, Integer> tuple = new Tuple<>(42, 1);
            assertEquals(42, tuple.first());
            assertEquals(1, tuple.second());
            tuple = tuple.permuted();
            assertEquals(1, tuple.first());
            assertEquals(42, tuple.second());
        }

        @Test
        public void can_be_combined_into_one_element() {
            Tuple<Integer, Double> tuple = new Tuple<>(42, 3.0);
            float fl = tuple.combine((i, d) -> (float) (i + d));
            assertEquals(fl, 45.0f);
        }

        @Test
        public void can_be_tested_for_two_elements() {
            Tuple<Integer, Integer> tuple = new Tuple<>(42, 1);
            assertTrue(tuple.testBoth((i1, i2) -> i1 == 42 && i2 == 1));
        }

        @Test
        public void can_be_transformed_into_a_map() {
            Tuple<Integer, Integer> tuple = new Tuple<>(42, 1);
            assertEquals(Collections.singletonMap(42, 1), tuple.asSingletonMap());
        }
    }

    @Nested class A_collection_of_tuples {

        @Test
        public void can_be_transformed_into_a_map() {
            Collection<Tuple<Integer, Integer>> collection = new ArrayList<>();
            collection.add(new Tuple<>(1, 1));
            collection.add(new Tuple<>(3, 4));
            Map<Integer, Integer> map = Tuple.appendToMap(collection, new HashMap<>());
            assertEquals(2, map.size());
            assertIterableEquals(Arrays.asList(1, 3), map.keySet());
            assertIterableEquals(Arrays.asList(1, 4), map.values());
        }
    }

}