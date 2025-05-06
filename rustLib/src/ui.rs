use gtk4 as gtk;
use gtk::prelude::*;
use gtk::{glib, Application, ApplicationWindow};

pub fn supports_gui() -> bool {
    let app = Application::builder()
        .application_id("io.github.dot166.jlibrust.test-ui")
        .build();

    app.connect_activate(|app| {
        // We create the main window.
        let window = ApplicationWindow::builder()
            .application(app)
            .default_width(320)
            .default_height(200)
            .title("Test GUI Support")
            .build();

        // Show the window.
        window.present();
        // get status code immediately
        app.quit();
    });

    app.run() == glib::ExitCode::SUCCESS
}