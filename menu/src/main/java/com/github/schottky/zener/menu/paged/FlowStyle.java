package com.github.schottky.zener.menu.paged;

/**
 * Defines how the single {@code MenuItem}s inside a paged menu are to be
 * ordered. This has two implications. Firstly, it decides if the rows or the
 * columns are getting filled up first. The second priority is
 * to define whether the items are to be place from left-to right/right-to left
 * or from top-to bottom/bottom-to top.
 * @see #TOP_TO_BOTTOM
 * @see #BOTTOM_TO_TOP
 * @see #RIGHT_TO_LFT
 * @see #LEFT_TO_RIGHT
 */

public interface FlowStyle {

    int startX(int length);

    int startY(int length);

    int modX();

    int modY();

    boolean condX(int index, int length);

    boolean condY(int index, int length);

    boolean horizontalFirst();

    default boolean verticalFirst() { return !horizontalFirst(); }

    /**
     * A flow style that will first flow top-to bottom then
     * right-to left, filling up vertically first
     */
    Vertical TOP_TO_BOTTOM = Vertical.TOP_TO_BOTTOM;
    /**
     * A flow style that will first flow bottom-to top then
     * right-to left, filling up vertically first
     */
    Vertical BOTTOM_TO_TOP = Vertical.BOTTOM_TO_TOP;

    /**
     * A flow style that will first flow right-to left
     * then top-to bottom, filling up horizontally first
     */
    Horizontal RIGHT_TO_LFT = Horizontal.RIGHT_TO_LEFT;
    /**
     * A flow style that will first flow left-to right
     * then top-to bottom, filling up horizontally first
     */
    Horizontal LEFT_TO_RIGHT = Horizontal.LEFT_TO_RIGHT;

    enum Vertical implements FlowStyle {

        TOP_TO_BOTTOM,
        BOTTOM_TO_TOP;

        /**
         * returns a new {@link FlowStyle} that will first flow in vertical
         * direction then in horizontal direction, applying the given vertical flow-style
         * @param horizontalFlowStyle The vertical flow-style to pass; either {@link FlowStyle#LEFT_TO_RIGHT}
         *                          or {@link FlowStyle#RIGHT_TO_LFT}
         * @return The combined FlowStyle
         */

        public FlowStyle then(Horizontal horizontalFlowStyle) {
            return new Combined(horizontalFlowStyle, this, false);
        }

        @Override
        public int startY(int length) {
            return this == TOP_TO_BOTTOM ? 0 : length -1;
        }

        @Override
        public int modY() {
            return this == TOP_TO_BOTTOM ? 1 : -1;
        }

        @Override
        public boolean condY(int index, int length) {
            return this == TOP_TO_BOTTOM ? index < length : index >= 0;
        }

        @Override
        public int startX(int length) {
            return 0;
        }

        @Override
        public int modX() {
            return 1;
        }

        @Override
        public boolean condX(int index, int length) {
            return index < length;
        }

        @Override
        public boolean horizontalFirst() {
            return false;
        }
    }

    enum Horizontal implements FlowStyle {

        RIGHT_TO_LEFT,
        LEFT_TO_RIGHT;

        /**
         * returns a new {@link FlowStyle} that will first flow in horizontal
         * direction then in vertical direction, applying the given vertical flow-style
         * @param verticalFlowStyle The vertical flow-style to pass; either {@link FlowStyle#BOTTOM_TO_TOP}
         *                          or {@link FlowStyle#TOP_TO_BOTTOM}
         * @return The combined FlowStyle
         */

        public FlowStyle then(Vertical verticalFlowStyle) {
            return new Combined(this, verticalFlowStyle, true);
        }

        @Override
        public int startY(int length) {
            return 0;
        }

        @Override
        public int modY() {
            return 1;
        }

        @Override
        public boolean condY(int index, int length) {
            return index < length;
        }

        @Override
        public int startX(int length) {
            return this == LEFT_TO_RIGHT ? 0 : length - 1;
        }

        @Override
        public int modX() {
            return this == LEFT_TO_RIGHT ? 1 : -1;
        }

        @Override
        public boolean condX(int index, int length) {
            return this == LEFT_TO_RIGHT ? index < length : index >= 0;
        }

        @Override
        public boolean horizontalFirst() {
            return true;
        }
    }

    class Combined implements FlowStyle {

        public Combined(Horizontal horizontalFlowStyle, Vertical verticalFlowStyle, boolean horizontalFirst) {
            this.horizontalFlowStyle = horizontalFlowStyle;
            this.verticalFlowStyle = verticalFlowStyle;
            this.horizontalFirst = horizontalFirst;
        }

        Vertical verticalFlowStyle;

        @Override
        public int startY(int length) {
            return verticalFlowStyle.startY(length);
        }

        @Override
        public boolean condY(int index, int length) {
            return verticalFlowStyle.condY(index, length);
        }

        @Override
        public int modY() {
            return verticalFlowStyle.modY();
        }

        Horizontal horizontalFlowStyle;

        @Override
        public int startX(int length) {
            return horizontalFlowStyle.startX(length);
        }

        @Override
        public boolean condX(int index, int length) {
            return horizontalFlowStyle.condX(index, length);
        }

        @Override
        public int modX() {
            return horizontalFlowStyle.modX();
        }

        final boolean horizontalFirst;

        @Override
        public boolean horizontalFirst() {
            return horizontalFirst;
        }
    }
}
