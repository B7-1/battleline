import java.io.*;
import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.ImageIcon;

import java.awt.Container;
import java.awt.GridLayout;
import java.awt.FlowLayout;
import java.awt.Dimension;
import java.awt.Color;


class GUI extends JFrame{
	GUI(String title){
		setTitle(title);
		JButton mycard1= new JButton();
		JButton mycard2= new JButton();
		JButton mycard3= new JButton();
		JButton mycard4= new JButton();
		JButton mycard5= new JButton();

		
		setLayout(new GridLayout(5,0));
		// add(mycard1);
		// add(mycard2);
		// add(mycard3);
		// add(mycard4);
		// add(mycard5);

		setBounds(100, 100, 300, 250);		
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
	}
}