package com.github.schottky.zener.command;

import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;

@API(status = Status.INTERNAL)
public final class ArrayUtil {

    private ArrayUtil() {}

    /**
     * pops the first elements from a String-array, returning a String-array that will
     * contain the last (input.length - n) elements. The input-string will be copied and
     * not be altered in any way
     * @param in The string to remove the first elements from
     * @param n the amount of elements to remove
     * @return A copy of the string-array containing only the last (in.length - n) elements, or
     * the string-array, if <i>n</i> is 0
     */

    public static String[] popFirstN(String[] in, int n) {
        if (n == 0) return in;
        String[] out = new String[in.length - n];
        System.arraycopy(in, n, out, 0, out.length);
        return out;
    }
}
