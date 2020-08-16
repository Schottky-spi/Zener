package com.github.schottky.zener.menu.paged;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.provider.ArgumentsSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FlowStyleTest {

    @Nested class a_simple_flow_style_flows {

        @ParameterizedTest
        @ArgumentsSource(FlowStyleArgumentsProvider.class)
        public void left_to_right(int lengthX, int lengthY) {
            testFlowStyle(FlowStyle.LEFT_TO_RIGHT, true, true, lengthX, lengthY, true);
        }

        @ParameterizedTest
        @ArgumentsSource(FlowStyleArgumentsProvider.class)
        public void right_to_left(int lengthX, int lengthY) {
            testFlowStyle(FlowStyle.RIGHT_TO_LFT, false, true, lengthX, lengthY, true);
        }

        @ParameterizedTest
        @ArgumentsSource(FlowStyleArgumentsProvider.class)
        public void top_to_bottom(int lengthX, int lengthY) {
            testFlowStyle(FlowStyle.TOP_TO_BOTTOM, true, true, lengthX, lengthY, false);
        }

        @ParameterizedTest
        @ArgumentsSource(FlowStyleArgumentsProvider.class)
        public void bottom_to_top(int lengthX, int lengthY) {
            testFlowStyle(FlowStyle.BOTTOM_TO_TOP, true, false, lengthX, lengthY, false);
        }
    }

    @Nested class a_nested_flow_style_flows {

        @ParameterizedTest
        @ArgumentsSource(FlowStyleArgumentsProvider.class)
        public void left_to_right_then_top_to_bottom(int lengthX, int lengthY) {
            testFlowStyle(FlowStyle.LEFT_TO_RIGHT.then(FlowStyle.TOP_TO_BOTTOM), true, true, lengthX, lengthY, true);
        }

        @ParameterizedTest
        @ArgumentsSource(FlowStyleArgumentsProvider.class)
        public void left_to_right_then_bottom_to_top(int lengthX, int lengthY) {
            testFlowStyle(FlowStyle.LEFT_TO_RIGHT.then(FlowStyle.BOTTOM_TO_TOP), true, false, lengthX, lengthY, true);
        }

        @ParameterizedTest
        @ArgumentsSource(FlowStyleArgumentsProvider.class)
        public void right_to_left_then_top_to_bottom(int lengthX, int lengthY) {
            testFlowStyle(FlowStyle.RIGHT_TO_LFT.then(FlowStyle.TOP_TO_BOTTOM), false, true, lengthX, lengthY, true);
        }

        @ParameterizedTest
        @ArgumentsSource(FlowStyleArgumentsProvider.class)
        public void right_to_left_then_bottom_to_top(int lengthX, int lengthY) {
            testFlowStyle(FlowStyle.RIGHT_TO_LFT.then(FlowStyle.BOTTOM_TO_TOP), false, false, lengthX, lengthY, true);
        }

        @ParameterizedTest
        @ArgumentsSource(FlowStyleArgumentsProvider.class)
        public void top_to_bottom_then_left_to_right(int lengthX, int lengthY) {
            testFlowStyle(FlowStyle.TOP_TO_BOTTOM.then(FlowStyle.LEFT_TO_RIGHT), true, true, lengthX, lengthY, false);
        }

        @ParameterizedTest
        @ArgumentsSource(FlowStyleArgumentsProvider.class)
        public void bottom_to_top_then_left_to_right(int lengthX, int lengthY) {
            testFlowStyle(FlowStyle.BOTTOM_TO_TOP.then(FlowStyle.LEFT_TO_RIGHT), true, false, lengthX, lengthY, false);
        }

        @ParameterizedTest
        @ArgumentsSource(FlowStyleArgumentsProvider.class)
        public void top_to_bottom_then_right_to_left(int lengthX, int lengthY) {
            testFlowStyle(FlowStyle.TOP_TO_BOTTOM.then(FlowStyle.RIGHT_TO_LFT), false, true, lengthX, lengthY, false);
        }

        @ParameterizedTest
        @ArgumentsSource(FlowStyleArgumentsProvider.class)
        public void bottom_to_top_then_right_to_left(int lengthX, int lengthY) {
            testFlowStyle(FlowStyle.BOTTOM_TO_TOP.then(FlowStyle.RIGHT_TO_LFT), false, false, lengthX, lengthY, false);
        }

    }

    private void testFlowStyle(FlowStyle flow, boolean increaseX, boolean increaseY, int lengthX, int lengthY, boolean horizontalFirst) {
        if (horizontalFirst) assertTrue(flow.horizontalFirst());
        else assertTrue(flow.verticalFirst());

        int oldX = increaseX ? -1 : lengthX;
        int oldY = increaseY ? -1 : lengthY;
        int countOfIterations = 0;
        for (int x = flow.startX(lengthX); flow.condX(x, lengthX); x += flow.modX()) {
            for (int y = flow.startY(lengthY); flow.condY(y, lengthY); y += flow.modY()) {
                if (increaseX) assertCyclicIncrease(x, oldX, lengthX);
                else assertCyclicDecrease(x, oldX, lengthX);

                if (increaseY) assertCyclicIncrease(y, oldY, lengthY);
                else assertCyclicDecrease(y, oldY, lengthY);

                countOfIterations++;
                oldY = y;
            }
            oldX = x;
        }
        assertEquals(lengthX * lengthY, countOfIterations);
    }

    private void assertCyclicDecrease(int val, int oldVal, int length) {
        assertTrue((oldVal == val + 1) || (val == length - 1 && oldVal == 0),
                "Should decrease but increases (val=" + val + ", oldVal=" + oldVal+ ")");
    }

    private void assertCyclicIncrease(int val, int oldVal, int length) {
        assertTrue((oldVal == val - 1) || (val == 0 && oldVal == length - 1),
                "Should increase but decreases (val=" + val + ", oldVal=" + oldVal+ ")");
    }

    static class FlowStyleArgumentsProvider implements ArgumentsProvider {

        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext context) {
            return Stream.of(
                    args(0, 0),
                    args(0, 10),
                    args(10, 0),
                    args(1, 1),
                    args(3, 5),
                    args(10, 10));
        }

        private Arguments args(int lengthX, int lengthY) {
            return () -> new Object[] {lengthX, lengthY};
        }
    }
}