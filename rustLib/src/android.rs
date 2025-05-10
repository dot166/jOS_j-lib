use regex::Regex;
use std::path::Path;
use std::process::Command;
use std::process::Output;
use std::process::Stdio;

pub fn update_android_array(content: &str, array_name: &str, items: &[String], verbose: bool) -> String {
    let updated = content.to_string();

    let array_re = Regex::new(&format!(
        r#"(?s)<array[^>]*\bname\s*=\s*"{0}"[^>]*>.*?</array>"#,
        regex::escape(array_name)
    )).unwrap();

    let items_str = items
        .iter()
        .map(|item| format!("        <item>{}</item>", item))
        .collect::<Vec<_>>()
        .join("\n");

    let replacement = format!(
        r#"<array
        name="{}"
        format="string"
    >
{}
    </array>"#,
        array_name, items_str
    );
    if verbose {
        println!("{}", replacement);
    }

    array_re.replace(&updated, replacement).to_string()
}

pub mod build {
    use super::*;

    pub fn get_env_from_envsetup_sh() -> Output {
        let envsetup_path = Path::new("build").join("envsetup.sh");
        Command::new("bash")
            .arg("-c")
            .arg(format!("source {} && env", envsetup_path.display()))
            .stdout(Stdio::piped())
            .spawn()
            .unwrap()
            .wait_with_output()
            .unwrap()
    }
}