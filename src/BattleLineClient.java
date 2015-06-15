import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import java.util.*;

import java.io.*;
import java.net.*;
import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.border.LineBorder;
import javax.swing.border.EtchedBorder;


import java.awt.Color;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.*;
import java.awt.GridLayout;

class BattleLineClient {
	//static String str2 = "0";
	//static int box = 0;
	//static int turn_flag = 0;
	static BufferedReader in_box2;
	static PrintWriter out_box2;
	static BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
	static Integer selectedcard=-1;//どのカードを選択したか
	static Integer selectedarea=-1;//どこにカードを置いたか
	static Integer selectcardstack=-1;//引くカードを選択


	static int readInt(BufferedReader in) throws IOException {
		try {
			Integer idx = null;
			while (idx == null) {
				idx = Integer.parseInt(in.readLine());
			}
			return idx;
		} catch(IOException e) {
			throw e;
		}
	}
	
	public static void main(String[] args)
					throws IOException {

		//GUI部分開始
			
	
			GUI gui = new GUI("BattleLine");
			List<Card> co = new ArrayList<Card>();
			GUICard cards1=new GUICard();

			GUIField cards2=new GUIField();
			GUICenter cards3=new GUICenter();
			GUIField cards4=new GUIField();
			GUICard cards5=new GUICard();
			gui.add(cards1);
			gui.add(cards2);
			gui.add(cards3);
			
			JLabel[] opponent_card=new JLabel[7]; 
			JButton[] flagbtn = new JButton[9];
			JButton[] btn = new JButton[7];
			JButton[] cardstack= new JButton[2];
			JLabel[] flag= new JLabel[9];
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
			 	opponent_flag[i].setBackground(Color.GRAY);
				opponent_flag[i].setOpaque(true);
				cards2.add(opponent_flag[i]);
			}
			cards2.add(label4);

			for(Integer i=0;i<2;i++){
				cardstack[i]=new JButton();
				cardstack[i].addActionListener(
					e->selectcardstack=Integer.parseInt(e.getActionCommand()));
				cardstack[i].setActionCommand(i.toString());
				ImageIcon icon =new ImageIcon("./image/cardimage.png");
			 	cardstack[i].setIcon(icon);
			 	EtchedBorder border = new EtchedBorder(EtchedBorder.RAISED, Color.white, Color.white);
    			cardstack[i].setBorder(border);

				cardstack[i].setOpaque(true);
			}
			cardstack[0].setText("unit");
			cardstack[1].setText("tactics");

			cards3.add(cardstack[0]);
			for(Integer i=0;i<9;i++){
				flag[i]=new JLabel();
				ImageIcon icon =new ImageIcon("./image/flag.png");
				flag[i].setIcon(icon);
				cards3.add(flag[i]);
			}
			cards3.add(cardstack[1]);


			cards4.add(label1);
			for(Integer i=0;i<9;i++){
				flagbtn[i]=new JButton();
				flagbtn[i].addActionListener(
					e->selectedarea=Integer.parseInt(e.getActionCommand()));
				flagbtn[i].setActionCommand(i.toString());
				flagbtn[i].setBackground(Color.GRAY);
				flagbtn[i].setOpaque(true);
				cards4.add(flagbtn[i]);
			}
			cards4.add(label2);

			for(Integer i=0;i<7;i++){
				btn[i]=new JButton();
				btn[i].addActionListener(
					e->selectedcard= Integer.parseInt(e.getActionCommand()));
				btn[i].setActionCommand(i.toString());
				btn[i].setOpaque(true);
				cards5.add(btn[i]);
			}

			gui.add(cards4);
			gui.add(cards5);
    		gui.setVisible(true);
    	//GUI部分終了
		System.out.print("初期入力:");
		String Name = input.readLine();
		InetAddress addr = InetAddress.getByName(Name);
		System.out.println("addr = " + addr);
		Socket socket = new Socket(addr, BattleLine.PORT);
		Integer phase_count=0;
		try {
			System.out.println("socket = " + socket);
			in_box2 = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out_box2 = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
			while (true) {
				String str = in_box2.readLine();
				
				if (str.equals("Input")) {
					out_box2.println(String.valueOf(readInt(input)));
				} else {
					System.out.println(str);//いつInputでいつその他が送られてくるか
				}
				if (str == "END") break;
			}
			
			//CUIclient ui = new CUIclient(new GameSystem());
			//ui.main();//launch(args);
		} finally {
			System.out.println("closing...");
			socket.close();
		}
	}
}