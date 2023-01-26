import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import se.vidstige.jadb.JadbConnection;
import se.vidstige.jadb.JadbDevice;

public class ADB_Clicker
{

    private CommandQueue commandQueue;
    private JadbConnection jadb;
    private JadbDevice device;

    public static void main (String[] args)
    {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        ADB_Clicker clicker = new ADB_Clicker();
        
        System.out.println("** Enter input: **");

        for (String input = readLine(reader); !"exit".equals(input); input = readLine(reader))
        {
            if ("".equals(input)) continue;

            String[] iStrings = input.split(" ");
            String param = "";
            for (int i = 1; i < iStrings.length; i++) param += iStrings[i] + (i == iStrings.length - 1 ? "" : " ");

            switch (iStrings[0])
            {
                case "insert":
                    try { clicker.insert(param); }
                    catch (InvalidFormatException e) { System.out.println(e.getMessage()); }
                break;

                case "wait":
                    try { clicker.wait(param); }
                    catch (InvalidFormatException e) { System.out.println(e.getMessage()); }
                break;

                case "remove":
                    try { clicker.remove(param); }
                    catch (InvalidFormatException e) { System.out.println(e.getMessage()); }
                break;

                case "run":
                    clicker.runQueue();
                break;

                case "list":
                    try { clicker.list(param); }
                    catch (InvalidFormatException e) { System.out.println(e.getMessage()); }
                break;

                case "select":
                    try { clicker.select(param); }
                    catch (InvalidFormatException e) { System.out.println(e.getMessage()); }
                break;
            }

            System.out.println("** Enter input: **");
        }

        System.out.println("Exiting");
    }

    private static String readLine (BufferedReader reader)
    {
        String ret;

        try
        {
            ret = reader.readLine().toLowerCase();
        }
        catch (IOException e)
        {
            ret = "exit";
        }

        return ret;
    }

    public ADB_Clicker ()
    {
        commandQueue = new CommandQueue();
        jadb = new JadbConnection();
    }

    public void insert (String param) throws InvalidFormatException
    {
        String[] parse = param.split(" ");
        int[] params = new int[5];

        if (parse.length != 5) throw new InvalidFormatException("Only 5 integer parameters are allowed.");

        for (int i = 0; i < 5; i++)
        {
            try 
            {
                params[i] = Integer.valueOf(parse[i]);
            }
            catch (NumberFormatException e)
            {
                throw new InvalidFormatException(String.format("Parameter %d is not an integer value", i+1));
            }
        }

        SwipeShellCommand ssc = new SwipeShellCommand(params[0], params[1], params[2], params[3], params[4]);
        commandQueue.insert(ssc);
        System.out.println("Inserted: " + ssc);
    }

    public void wait (String param) throws InvalidFormatException
    {

        int parse;
        try
        {
            parse = Integer.valueOf(param);
            commandQueue.insert(new WaitCommand(parse));
        }
        catch (NumberFormatException e)
        {
            throw new InvalidFormatException("Invalid paramter value. Expected a single decimal number.");
        }

    }

    public void remove (String param) throws InvalidFormatException
    {
        int parse;
        try
        {
            parse = Integer.valueOf(param);
            SwipeShellCommand ssc = commandQueue.remove(parse);
            System.out.println("Removed: " + ssc);
        }
        catch (IndexOutOfBoundsException e)
        {
            throw new InvalidFormatException("An invalid index was given");
        }
        catch (NumberFormatException e)
        {
            throw new InvalidFormatException("expected format: \"remove [integer]\"");
        }        
    }

    public void list (String param) throws InvalidFormatException
    {
        switch (param)
        {
            case "devices":
                list_devices();
            break;

            case "queue":
                list_queue();
            break;

            default:
                throw new InvalidFormatException("A parameter was not given or did not match \"devices\" or \"queue\"");
        }
    }

    private void list_queue ()
    {
        System.out.println("".equals(commandQueue.toString()) ? "Empty queue." : commandQueue.toString());
    }

    private void list_devices () throws InvalidFormatException
    {
        try
        {
            List<JadbDevice> devices = jadb.getDevices();
            for (int i = 0; i < devices.size(); i++)
            {
                System.out.println(String.format("%d: %s", i, devices.get(i).toString()));
            }
        }
        catch (Exception e)
        {
            throw new InvalidFormatException("An error occured: " + e.getMessage());
        }
    }

    public void select (String param) throws InvalidFormatException
    {
        try
        {
            int index = Integer.parseInt(param);
            List<JadbDevice> list = jadb.getDevices();
            device = list.get(index);
            System.out.println(String.format("Selected device: %s", device.toString()));
        }   
        catch (NumberFormatException e)
        {
            throw new InvalidFormatException("expected format: \"select [integer]\"");
        }
        catch (Exception e)
        {
            throw new InvalidFormatException("An error occured: " + e.getMessage());
        }
    }

    public void runQueue ()
    {
        if (commandQueue.getSize() <= 0)
        {
            System.out.println("Add Clicks to Queue to Run Queue!");
        }
        else if (device == null)
        {
            System.out.println("Select a device");
        }
        else
        {
            Thread thread = commandQueue.runQueueThread(device, "Command Thread");
            thread.run();
        }
    }

    private class InvalidFormatException extends Exception
    {
        public InvalidFormatException (String m)
        {
            super(m);
        }
    }
}
