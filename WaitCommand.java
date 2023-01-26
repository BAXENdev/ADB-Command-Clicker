import java.util.concurrent.TimeUnit;

import se.vidstige.jadb.JadbDevice;

public class WaitCommand extends SwipeShellCommand
{
    private int time;

    public WaitCommand (int time)
    {
        this.time = time;
    }

    @Override
    public boolean RunShellCommand (JadbDevice device)
    {
        try
        {
            TimeUnit.MILLISECONDS.sleep(time);
        }
        catch (Exception e)
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString ()
    {
        return String.format("Wait %dms", time);
    }
}
