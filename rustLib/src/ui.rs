use crate::io_ext::read_console_input;

pub fn console_yn_dialog(message: &str) -> bool {
    println!("{} [Y/N]", message);
    let input = read_console_input().trim().to_uppercase();
    if input == "Y" || input == "YES" {
        true
    } else if input == "N" || input == "NO" { 
        false
    } else {
        println!("invalid input");
        console_yn_dialog(message)
    }
}

pub fn console_ok_dialog(message: &str) {
    println!("{}", message);
    println!("press enter to continue");
    read_console_input(); // used to wait for enter key
}