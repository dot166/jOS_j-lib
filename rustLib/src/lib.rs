pub mod ui;
pub mod android;
pub mod env_ext;
pub mod maths;

#[cfg(test)]
mod tests {
    use maths::*;
    use super::*;

    #[test]
    fn it_works() {
        assert_eq!(is_even(2), true);
        assert_eq!(is_odd(1), true);
    }
}
