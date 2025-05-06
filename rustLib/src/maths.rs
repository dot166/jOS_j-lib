pub fn is_even(i: i64) -> bool {
    i % 2 == 0
}

pub fn is_odd(i: i64) -> bool {
    !is_even(i)
}