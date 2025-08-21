package io.github.dot166.jlib.rusthelper;

public class Test {
    // Load the native library "libj_lib_rust.so".
    static {
        System.loadLibrary("j_lib_rust");
    }

    // Native function implemented in Rust.
    public static native void rustIntegrationTest();
}
