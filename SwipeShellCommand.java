import java.lang.Thread;
import java.util.concurrent.TimeUnit;

import se.vidstige.jadb.JadbDevice;

/**
 * Data Structure for ADB Shell swipe command. <br>
 * 
 */
public class SwipeShellCommand
{
    /**
     * Returns a formatted shell swipe command.
     * 
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     * @param time Time in Milliseconds.
     * @return Shell swipe command.
     */
    public static String toString (int x1, int y1, int x2, int y2, long time)
    {
        return String.format("input touchscreen swipe %d %d %d %d %d", x1, y1, x2, y2, time);
    }

    private int x1, x2, y1, y2;
    private long time;

    /**
     * Default constructor. Initializes everything to 0;
     */
    public SwipeShellCommand ()
    {
        x1 = 0;
        y1 = 0;
        x2 = 0;
        y2 = 0;
        time = 0l;
    }

    /**
     * Parameterized Constructor. 
     * 
     * @param x1
     * @param x2
     * @param y1
     * @param y2
     * @param time
     */
    public SwipeShellCommand (int x1, int y1, int x2, int y2, long time) 
    {
        this.x1 = x1;
        this.x2 = x2;
        this.y1 = y1;
        this.y2 = y2;
        this.time = time;
    }

    /**
     * Gets x1.
     * 
     * @return
     */
    public int getX1 () 
    {
        return this.x1;
    }

    /**
     * Sets x1.
     * 
     * @param x1
     */
    public void setX1 (int x1) 
    {
        this.x1 = x1;
    }

    /**
     * Gets x2.
     * 
     * @return
     */
    public int getX2 () 
    {
        return this.x2;
    }

    /**
     * Sets x2.
     * 
     * @param x2
     */
    public void setX2 (int x2)
    {
        this.x2 = x2;
    }

    /**
     * Gets y1.
     * 
     * @return
     */
    public int getY1 () 
    {
        return this.y1;
    }

    /**
     * Sets y1.
     * 
     * @param y1
     */
    public void setY1 (int y1) 
    {
        this.y1 = y1;
    }

    /**
     * Gets y2.
     * 
     * @return
     */
    public int getY2 () 
    {
        return this.y2;
    }

    /**
     * Sets y2.
     * 
     * @param y2
     */
    public void setY2 (int y2) 
    {
        this.y2 = y2;
    }

    /**
     * Gets time.
     * 
     * @return
     */
    public long getTime() 
    {
        return this.time;
    }

    /**
     * Sets time.
     * @param time
     */
    public void setTime(long time) 
    {
        this.time = time;
    }

    /**
     * Creates an unsafe thread to run a Shell Swipe command on a given device.
     * 
     * @param device
     * @param threadName
     * @return Unsafe Thread
     */
    public Thread GetShellCommand (JadbDevice device, String threadName)
    {
        Thread ret = new Thread(threadName) 
        {
            @Override
            public void run ()
            {
                try
                {
                    device.executeShell(toString());
                    TimeUnit.MILLISECONDS.sleep(time);
                }
                catch (Exception e)
                {
                    //do nothing
                }
            }
        };
        
        return ret;
    }

    /**
     * Runs a shell command and waits until touch command finishes before returning.
     * Will return true if the method successfully ran. Will return false if an error was thrown. 
     * 
     * @param device
     * @return
     */
    public boolean RunShellCommand (JadbDevice device)
    {
        boolean ret = true;
        try
        {
            device.execute(toString());
            TimeUnit.MILLISECONDS.sleep(time);
        }
        catch (Exception e)
        {
            ret = false;
        }
        return ret;
    }

    /**
     * Returns a data string that is used for building scripts for ADB_Clicker.
     * 
     * @return
     */
    public String extractData ()
    {
        return String.format("%d|%d|%d|%d|%d");
    }

    /**
     * toString() returns a formatted Shell Swipe command for use with any device.
     * 
     * @return
     */
    public String toString ()
    {
        return String.format("input touchscreen swipe %d %d %d %d %d", x1, y1, x2, y2, time);
    }
}