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

class GUICard extends JPanel{
	GUICard(){
		setLayout(new GridLayout(0,11));
		JLabel label1=new JLabel();
		JLabel label2=new JLabel();
		add(label1);
		add(label2);
		// add(btn3);
		// add(btn4);
		// add(btn5);
		// add(btn6);
		// add(btn7);		
	}
}