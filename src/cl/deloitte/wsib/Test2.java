package cl.deloitte.wsib;

import com.toedter.calendar.JDateChooser;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by jinostrozau on 2017-06-29.
 */
public class Test2 {
    private JButton button1;
    private JPanel panel1;
    private JDateChooser jDateChooser1;

    public Test2() {
        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null,"Testing");
            }
        });
    }

    public static void main(String[] args) {
        new Test2().initComponents();
    }

    public void initComponents(){
        JFrame frame = new JFrame("Test");
        frame.setContentPane(new Test2().panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}