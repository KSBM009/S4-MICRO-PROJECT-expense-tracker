import javax.swing.*;  
public class Simple extends JFrame{
    //Inheriting JFrame  
    JFrame f;  
    Simple(){
        //Create button  
        JButton b=new JButton("Login");  
        b.setBounds(130,100,100, 40);  
        //Adding button on frame
        add(b);
        setSize(400,500);  
        setLayout(null);  
        setVisible(true);  
    }  
    public static void main(String[] args) {  
        new Simple();
    }
}  