using Gtk;

namespace aurora;

public class Ui
{
    internal static Window? _window = null;

    public bool IsGtk()
    {
        return _window != null;
    }

    public class Gtk
    {
        
    }

    public class ConsoleUi
    {
        
    }
}