import javax.swing.*;
public class Simple{
    JFrame f;
    Simple(){
        //Creating a instance of JFrame
        f = new JFrame();

        //Creating a instance of JButton
        JButton b = new JButton("Login");
        b.setBounds(130, 100, 100, 40);
        //Adding button in JFrame
        f.add(b);
          
        f.setSize(400,500);  
        //Using no layout managers
        f.setLayout(null);
        //Making the frame visible
        f.setVisible(true);  
    }
    public static void main(String[] args){  
        new Simple();
    }  
}