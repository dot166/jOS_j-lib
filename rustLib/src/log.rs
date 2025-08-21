#[cfg(target_os = "android")]
use jni::objects::{JClass, JObject, JString, JValue};
#[cfg(target_os = "android")]
use crate::android::with_jni_env;
use crate::env_ext::is_desktop;

/// Internal helper (Android-only)
#[cfg(target_os = "android")]
fn call_android_log(level: &str, tag: &str, msg: &str) {
    with_jni_env(|env| {
        let log_class: JClass = env.find_class("android/util/Log").unwrap();
        let jtag: JString = env.new_string(tag).unwrap();
        let jmsg: JString = env.new_string(msg).unwrap();

        env.call_static_method(
            log_class,
            level,
            "(Ljava/lang/String;Ljava/lang/String;)I",
            &[
                JValue::Object(&JObject::from(jtag)),
                JValue::Object(&JObject::from(jmsg)),
            ],
        ).unwrap();
    });
}

/// Verbose log
pub fn log_verbose(tag: &str, msg: &str) {
    if !is_desktop() {
        #[cfg(target_os = "android")]
        return call_android_log("v", tag, msg);
    }
    println!("[VERBOSE][{}] {}", tag, msg);
}

/// Debug log
pub fn log_debug(tag: &str, msg: &str) {
    if !is_desktop() {
        #[cfg(target_os = "android")]
        return call_android_log("d", tag, msg);
    }
    println!("[DEBUG][{}] {}", tag, msg);
}

/// Info log
pub fn log_info(tag: &str, msg: &str) {
    if !is_desktop() {
        #[cfg(target_os = "android")]
        return call_android_log("i", tag, msg);
    }
    println!("[INFO][{}] {}", tag, msg);
}

/// Warn log
pub fn log_warn(tag: &str, msg: &str) {
    if !is_desktop() {
        #[cfg(target_os = "android")]
        return call_android_log("w", tag, msg);
    }
    println!("[WARN][{}] {}", tag, msg);
}

/// Error log
pub fn log_error(tag: &str, msg: &str) {
    if !is_desktop() {
        #[cfg(target_os = "android")]
        return call_android_log("e", tag, msg);
    }
    eprintln!("[ERROR][{}] {}", tag, msg);
}
