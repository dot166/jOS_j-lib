use crate::io_ext::read_console_input;
use gtk::prelude::*;
use gtk::{glib, Application, ApplicationWindow};
use gtk4 as gtk;

pub fn supports_gui() -> bool {
    let app = Application::builder()
        .application_id("io.github.dot166.jlibrust.test-ui")
        .build();

    app.connect_activate(|app| {
        // We create the main window.
        let window = ApplicationWindow::builder()
            .application(app)
            .default_width(100)
            .default_height(100)
            .title("Test GUI Support")
            .resizable(false)
            .build();

        // Show the window.
        window.present();
        // get status code immediately
        app.quit();
    });

    app.run() == glib::ExitCode::SUCCESS
}

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