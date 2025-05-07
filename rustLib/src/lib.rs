pub mod ui;
pub mod android;
pub mod env_ext;
pub mod maths;
mod io_ext;

#[cfg(test)]
mod tests {
    use maths::*;
    use super::*;

    #[test]
    fn it_works() {
        assert_eq!(is_even(2), true);
        assert_eq!(is_odd(1), true);
        assert_eq!(dec_to_bin(8), "1000");
        assert_eq!(bin_to_dec("1000".parse().unwrap()), 8);
    }
}
