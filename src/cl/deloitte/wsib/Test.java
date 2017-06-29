package cl.deloitte.wsib;

import javax.swing.*;


public class Test extends JFrame{
    private JPanel panel1;
    private JButton OKButton;

    public Test() {

        setSize(500,500);
        setContentPane(panel1);
        setLocationRelativeTo(null);
        panel1.setVisible(true);

    }

    public static void main(String[] args) {
        new Test();
    }
}
