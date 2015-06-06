import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.ImageIcon;

import java.awt.Container;
import java.awt.GridLayout;
import java.awt.GridLayout;
import java.awt.FlowLayout;
import java.awt.Dimension;
import java.awt.Color;

class GUIField extends JPanel{
	GUIField(){//Flag card[0]が自分で card[1]相手
		JLabel btn1 = new JLabel("1");
		JButton btn2 = new JButton("2");
		JButton btn3 = new JButton("3");
		JButton btn4 = new JButton("4");
		JButton btn5 = new JButton("5");
		JButton btn6 = new JButton("6");
		JButton btn7 = new JButton("7");
		JButton btn8 = new JButton("8");
		JButton btn9 = new JButton("9");
		JButton btn10 = new JButton("10");
		JLabel btn11 = new JLabel("11");

		setBackground(Color.BLACK);
		//btn11.setBackground(Color.ORANGE);
		//btn10.setBackground(Color.BLUE);
		setLayout(new GridLayout(1,11));


		add(btn1);
		add(btn2);
		add(btn3);
		add(btn4);
		add(btn5);
		add(btn6);
		add(btn7);
		add(btn8);
		add(btn9);
		add(btn10);
		add(btn11);

	}
}