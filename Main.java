import java.io.IOException;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.plaf.DimensionUIResource;

import se.vidstige.jadb.JadbDevice;
import se.vidstige.jadb.JadbConnection;
import se.vidstige.jadb.JadbException;

public class Main
{
    /**
     * 
     * 
     * 
     * @param args
     * @throws IOException
     * @throws JadbException
     * @throws InterruptedException
     */
    public static void main (String[] args) throws IOException, JadbException, InterruptedException
    {
        JadbConnection jadb = new JadbConnection();
        JadbDevice device = jadb.getDevices().get(0);

        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JTextField textField = new JTextField();
        textField.setText("");
        JButton button = new JButton();
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed (ActionEvent e)
            {
                String[] input = textField.getText().split(" ");
                
                if (input.length != 5) 
                {
                    String message = String.format("Expected 5 inputs, found %d. Don't use double spaces.", input.length);
                    JOptionPane.showMessageDialog(null, message);
                }

                int[] parse = new int[5];
                try
                {
                    for (int i = 0; i < 5; i++)
                    {
                        parse[i] = Integer.valueOf(input[i]);
                    }
                }
                catch (NumberFormatException ex)
                {
                    String message = String.format("Expected integer inputs. One of the inputs is not an integer input. Make sure there aren't double spaces");
                    JOptionPane.showMessageDialog(null, message);
                }

                String shell = String.format("input touchscreen swipe %d %d %d %d %d", parse[0], parse[1], parse[2], parse[3], parse[4]);
                try {
                    device.executeShell(shell);
                } catch (IOException | JadbException e1) {
                    JOptionPane.showMessageDialog(null, e1.getMessage(), "Error", JOptionPane.DEFAULT_OPTION);
                }
            }
        } );
        button.setText("Add Click");

        textField.setPreferredSize(new DimensionUIResource(400, 40));
        button.setPreferredSize(new DimensionUIResource(400, 40));
        frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));
        frame.add(textField);
        frame.add(button);
        frame.pack();
        frame.setVisible(true);

    }

}