package cl.deloitte.wsib;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

/**
 * Created by jinostrozau on 2017-02-07.
 */
public class JavaParserGUI {

    public static JButton OKButton;
    public static JButton button;
    public static JFileChooser fileChooser;

    public static void main(String[] args) {
        JFrame.setDefaultLookAndFeelDecorated(true);
        JDialog.setDefaultLookAndFeelDecorated(true);
        JFrame frame = new JFrame("JComboBox Test");
        frame.setLayout(new FlowLayout());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        button = new JButton("Select File");
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                fileChooser = new JFileChooser();
                int returnValue = fileChooser.showOpenDialog(null);
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    System.out.println(selectedFile.getPath());
                    System.out.println(selectedFile.getName());
                }
            }
        });

        OKButton = new JButton("TEST");

        frame.add(button);
        frame.add(OKButton);
        frame.pack();
        frame.setVisible(true);
    }
}
