pub fn is_even(i: u32) -> bool {
    i % 2 == 0
}

pub fn is_odd(i: u32) -> bool {
    !is_even(i)
}

pub fn dec_to_bin(mut decimal: u32) -> String {
    let mut binary_string = String::new();
    if decimal == 0 {
        return "0".to_string();
    }
    while decimal > 0 {
        let remainder = decimal % 2;
        binary_string.insert(0, char::from_digit(remainder, 10).unwrap());
        decimal /= 2;
    }
    binary_string
}

pub fn bin_to_dec(binary_string: String) -> u32 {
    u32::from_str_radix(&*binary_string, 2).unwrap()
}