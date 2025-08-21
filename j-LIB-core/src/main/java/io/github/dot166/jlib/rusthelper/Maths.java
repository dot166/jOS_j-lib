package io.github.dot166.jlib.rusthelper;

public class Maths {
    static {
        System.loadLibrary("j_lib_rust");
    }

    // Native function implemented in Rust.
    public static native String decToBin(String num);
    public static native int binToDec(int num);
    public static native boolean isOdd(int num);
    public static native boolean isEven(int num);
}
