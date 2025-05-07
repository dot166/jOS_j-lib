use std::io::stdin;

pub fn read_console_input() -> String {
    let mut input = String::new();
    stdin().read_line(&mut input).expect("error: unable to read user input");
    input
}