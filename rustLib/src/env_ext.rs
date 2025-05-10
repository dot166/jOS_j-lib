use std::env;
use std::process::Output;

pub fn get_config_folder() -> Result<String, env::VarError> {
    if env::consts::OS == "macos" || env::consts::OS == "linux" {
        env::var("XDG_CONFIG_HOME")
    } else if env::consts::OS == "windows" {
        env::var("APPDATA")
    } else { 
        Err(env::VarError::NotPresent) // this only works on desktop platforms
    }
}

pub fn parse_env_from_output(output: Output) {
    let output_str = String::from_utf8(output.stdout).unwrap();
    for line in output_str.lines() {
        if let Some((var, value)) = line.split_once('=') {
            unsafe { env::set_var(var, value); }
        }
    }
}