use std::env;

pub fn get_config_folder() -> Result<String, env::VarError> {
    if env::consts::OS == "macos" || env::consts::OS == "linux" {
        env::var("XDG_CONFIG_HOME")
    } else if env::consts::OS == "windows" {
        env::var("APPDATA")
    } else { 
        Err(env::VarError::NotPresent) // this only works on desktop platforms
    }
}