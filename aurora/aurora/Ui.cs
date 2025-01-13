using Gtk;

namespace aurora;

public class Ui
{
    public static int YesNoDialog()
    {

        var md = new MessageDialog(null,
                DialogFlags.DestroyWithParent, MessageType.Info,
                ButtonsType.YesNo, "oh i give up");

        Console.WriteLine(2);
        int response = md.Run();
            
        Console.WriteLine(3);
        md.Destroy();
        return response; //TODO: parse it properly
    }
}