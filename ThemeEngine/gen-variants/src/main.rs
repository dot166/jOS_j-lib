use std::*;
use image::*;

fn main() {
    let exe = env::current_exe().unwrap();
    let path = exe.parent().unwrap().to_path_buf();
    let resources = vec!["src/main/res/drawable-nodpi"];

    for resource in resources {
        generate_small_variants(&path, resource);
    }
}

fn generate_small_variants(base_path: &path::PathBuf, resource: &str) {
    let wallpapers_path = base_path.join(resource);
    clean(&wallpapers_path);
    let wallpapers = fs::read_dir(&wallpapers_path).unwrap();

    for wallpaper in wallpapers {
        let wallpaper = wallpaper.unwrap().path();
        let wallpaper_name = wallpaper.file_name().unwrap().to_str().unwrap();
        println!("{}", wallpaper_name);

        // Append _small.jpg to the wallpaper
        let wallpaper_small_name = format!("{}_small.jpg", wallpaper_name.trim_end_matches(".jpg"));
        let wallpaper_small_path = wallpapers_path.join(wallpaper_small_name);

        // Save the wallpaper with 1/4 size to wallpaper_small_path
        let img = open(&wallpaper).unwrap();
        let size = (img.width() / 4, img.height() / 4);
        let img_small = img.resize(size.0, size.1, imageops::FilterType::Lanczos3);
        img_small.save(wallpaper_small_path).unwrap();
    }
}

fn clean(wallpapers_path: &path::PathBuf) {
    let wallpapers = fs::read_dir(wallpapers_path).unwrap();

    for wallpaper in wallpapers {
        let wallpaper = wallpaper.unwrap().path();
        if wallpaper.file_name().unwrap().to_str().unwrap().ends_with("_small.jpg") {
            fs::remove_file(wallpaper).unwrap();
        }
    }
}

