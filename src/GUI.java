import java.io.*;
import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.ImageIcon;
import javax.swing.border.EtchedBorder;

import java.awt.Container;
import java.awt.GridLayout;
import java.awt.FlowLayout;
import java.awt.Dimension;
import java.awt.Color;

import java.util.List;
import java.util.ArrayList;


class GUI extends JFrame {
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
		//setBackground(Color.GREEN);

		setBounds(100, 100, 1200, 750);		
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		layoutComponents();
	}

 	JLabel[][] flaglabel_card = new JLabel[9][3];
	JButton[] btn = new JButton[7];
	JLabel[][] opponent_flag_card=new JLabel[9][3];
	JButton[] cardstack= new JButton[2];
	JButton[] flag= new JButton[9];

	void layoutComponents() {
		List<Card> co = new ArrayList<Card>();
		GUICard cards1=new GUICard();

		GUIField cards2=new GUIField();
		GUICenter cards3=new GUICenter();
		GUIField cards4=new GUIField();
		GUICard cards5=new GUICard();
		this.add(cards1);
		this.add(cards2);
		this.add(cards3);

		JLabel[] opponent_card=new JLabel[7]; 
		JLabel[] flaglabel = new JLabel[9];

		JLabel[] opponent_flag=new JLabel[9];

		JLabel label1=new JLabel();
		JLabel label2=new JLabel();
		JLabel label3=new JLabel();
		JLabel label4=new JLabel();


		for(Integer i=0;i<7;i++){
			opponent_card[i]=new JLabel();
			ImageIcon icon =new ImageIcon("./image/cardimage.png");
			opponent_card[i].setIcon(icon);
			EtchedBorder border = new EtchedBorder(EtchedBorder.RAISED, Color.white, Color.white);
			opponent_card[i].setBorder(border);
			cards1.add(opponent_card[i]);
		}

		cards2.add(label3);
		for(Integer i=0;i<9;i++){
			opponent_flag[i]=new JLabel();
			opponent_flag[i].setBackground(Color.WHITE);
			opponent_flag[i].setLayout(new GridLayout(3,0));
			for(Integer j=0;j<3;j++){
				opponent_flag_card[i][j]=new JLabel();
				opponent_flag[i].add(opponent_flag_card[i][j]);
			}
			opponent_flag[i].setOpaque(true);
			cards2.add(opponent_flag[i]);
		}
		cards2.add(label4);

		for(Integer i=0;i<2;i++){
			ImageIcon icon = new ImageIcon("./image/cardimage.png");
			EtchedBorder border = new EtchedBorder(EtchedBorder.RAISED, Color.white, Color.white);

			cardstack[i] = new JButton();
			cardstack[i].setActionCommand(i.toString());
			cardstack[i].setIcon(icon);
			cardstack[i].setBorder(border);
			cardstack[i].setOpaque(true);
		}

		cardstack[0].setText("unit");
		cardstack[1].setText("tactics");

		cards3.add(cardstack[0]);
		for(Integer i=0;i<9;i++){
			ImageIcon icon = new ImageIcon("./image/flag.png");

			flag[i] = new JButton();
			flag[i].setActionCommand(i.toString());
			flag[i].setIcon(icon);
			cards3.add(flag[i]);
		}
		cards3.add(cardstack[1]);

		cards4.add(label1);
		for(Integer i=0;i<9;i++){
			flaglabel[i]=new JLabel();
			flaglabel[i].setLayout(new GridLayout(3,0));
				//flaglabel[i].setActionCommand(i.toString());
			for(Integer j=0;j<3;j++){
				flaglabel_card[i][j]=new JLabel();
				flaglabel[i].add(flaglabel_card[i][j]);
			}
			flaglabel[i].setBackground(Color.WHITE);
			flaglabel[i].setOpaque(true);
			cards4.add(flaglabel[i]);
		}
		cards4.add(label2);

		for(Integer i=0;i<7;i++){
			btn[i] = new JButton();
			btn[i].setActionCommand(i.toString());
			btn[i].setOpaque(true);
			cards5.add(btn[i]);
		}

		this.add(cards4);
		this.add(cards5);
		this.setVisible(true);
	}
}