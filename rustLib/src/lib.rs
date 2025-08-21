pub mod ui;
pub mod android_util;
pub mod env_ext;
pub mod maths;
pub mod io_ext;
pub mod log;

#[cfg(target_os = "android")]
#[allow(non_snake_case)]
pub mod android {
    use crate::log::{log_debug, log_error, log_info, log_verbose, log_warn};
    use crate::maths::{bin_to_dec, dec_to_bin, is_even, is_odd};
    use jni::objects::JClass;
    use jni::objects::JString;
    use jni::{JNIEnv, JavaVM};
    use std::sync::OnceLock;
    use jni::sys::{jboolean, jint, jstring};

    static JAVAVM: OnceLock<JavaVM> = OnceLock::new();

    /// This function should be called once to store the JavaVM instance.
    /// It's typically called from the JNI_OnLoad entry point.
    pub fn set_jvm(jvm: JavaVM) {
        JAVAVM.set(jvm).ok();
    }

    /// A safe way to get a JNIEnv from any thread.
    /// It attaches the current thread to the JVM if it's not already attached.
    pub fn with_jni_env<F, R>(f: F) -> Option<R>
    where
        F: FnOnce(&mut JNIEnv) -> R,
    {
        let jvm = JAVAVM.get()?;

        let mut env_guard = jvm.attach_current_thread().ok()?;

        Some(f(&mut env_guard))
    }

    // The native function implemented in Rust.
    #[unsafe(no_mangle)]
    pub unsafe extern "system" fn Java_io_github_dot166_jlib_rusthelper_Test_rustIntegrationTest(
        env: JNIEnv,
        _: JClass,
    ) {
        set_jvm(env.get_java_vm().unwrap());
        log_error("test_aosp", "01189998819991197253");
        log_warn("test_aosp", "01189998819991197253");
        log_debug("test_aosp", "01189998819991197253");
        log_info("test_aosp", "01189998819991197253");
        log_verbose("test_aosp", "01189998819991197253");
        log_error("test_aosp", &*dec_to_bin(8));
        log_warn("test_aosp", &*dec_to_bin(8));
        log_debug("test_aosp", &*dec_to_bin(8));
        log_info("test_aosp", &*dec_to_bin(8));
        log_verbose("test_aosp", &*dec_to_bin(8));
    }

    #[unsafe(no_mangle)]
    pub unsafe extern "system" fn Java_io_github_dot166_jlib_rusthelper_Maths_decToBin(
        env: JNIEnv,
        _: JClass,
        num: jint,
    ) -> jstring {
        set_jvm(env.get_java_vm().unwrap());
        env.new_string(dec_to_bin(num as u32)).unwrap().into_raw()
    }

    #[unsafe(no_mangle)]
    pub unsafe extern "system" fn Java_io_github_dot166_jlib_rusthelper_Maths_binToDec(
        mut env: JNIEnv,
        _: JClass,
        num: JString,
    ) -> jint { unsafe {
        set_jvm(env.get_java_vm().unwrap());
        bin_to_dec(env.get_string(&num).unwrap().into_raw().as_ref().unwrap().to_string()) as jint
    }}

    #[unsafe(no_mangle)]
    pub unsafe extern "system" fn Java_io_github_dot166_jlib_rusthelper_Maths_isOdd(
        env: JNIEnv,
        _: JClass,
        num: jint,
    ) -> jboolean {
        set_jvm(env.get_java_vm().unwrap());
        is_odd(num as u32).into()
    }

    #[unsafe(no_mangle)]
    pub unsafe extern "system" fn Java_io_github_dot166_jlib_rusthelper_Maths_isEven(
        env: JNIEnv,
        _: JClass,
        num: jint,
    ) -> jboolean {
        set_jvm(env.get_java_vm().unwrap());
        is_even(num as u32).into()
    }
}


#[cfg(test)]
mod tests {
    use super::*;
    use log::*;
    use maths::*;

    #[test]
    fn it_works() {
        log_error("test_desktop", "01189998819991197253");
        log_warn("test_desktop", "01189998819991197253");
        log_debug("test_desktop", "01189998819991197253");
        log_info("test_desktop", "01189998819991197253");
        log_verbose("test_desktop", "01189998819991197253");
        log_error("test_desktop", &*dec_to_bin(8));
        log_warn("test_desktop", &*dec_to_bin(8));
        log_debug("test_desktop", &*dec_to_bin(8));
        log_info("test_desktop", &*dec_to_bin(8));
        log_verbose("test_desktop", &*dec_to_bin(8));
        assert_eq!(is_even(2), true);
        assert_eq!(is_odd(1), true);
        assert_eq!(dec_to_bin(8), "1000");
        assert_eq!(bin_to_dec("1000".parse().unwrap()), 8);
    }
}
