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
	static int[] hcards = new int[8];
	static int[][] s_fcards = new int[9][3];
	static int[][] c_fcards = new int[9][3];
	static BufferedReader in_box2;
	static PrintWriter out_box2;
	static BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
	static Integer selectedcard=-1;
	static Integer selectedarea=-1;
	static Integer selectcardstack=-1;
	static Integer phase_counter=0;
	static Integer wait=0;


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
			JLabel[] flaglabel = new JLabel[9];
			JButton[] btn = new JButton[7];
			JButton[] cardstack= new JButton[2];
			JButton[] flag= new JButton[9];
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
				flag[i]=new JButton();

				flag[i].addActionListener(
					e->selectedarea=Integer.parseInt(e.getActionCommand()));
				flag[i].setActionCommand(i.toString());
				ImageIcon icon =new ImageIcon("./image/flag.png");
				flag[i].setIcon(icon);
				cards3.add(flag[i]);
			}
			cards3.add(cardstack[1]);


			cards4.add(label1);
			for(Integer i=0;i<9;i++){
				flaglabel[i]=new JLabel();
				//flagbtn[i].setLayout(new GridLayout(0,3));
				//flaglabel[i].setActionCommand(i.toString());
				flaglabel[i].setBackground(Color.GRAY);
				flaglabel[i].setOpaque(true);
				cards4.add(flaglabel[i]);
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
    		//GUI終了




		System.out.print("server's machine name;");
		String Name = input.readLine();
		InetAddress addr = InetAddress.getByName(Name);
		System.out.println("addr = " + addr);
		Socket socket = new Socket(addr, BattleLine.PORT);
		Integer phase_count=0;
		try {
			System.out.println("socket = " + socket);
			in_box2 = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out_box2 = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
			for(int i = 0; i < 8; i++) {
				hcards[i] = 0;	//initialize hcards
			}
			for(int i = 0; i < 9; i++) {
				for(int j = 0; j < 3; j++) {
					s_fcards[i][j] = 99;	//initialize s_fcards
					c_fcards[i][j] = 99;	//initialize c_fcards
				}
			}

			while (true) {
				System.out.println("aaaa\n");

				String str="-1";
				if(wait==0){
					str = in_box2.readLine();
				}

				if(str.equals("Input")){
					wait=1;
					System.out.println("wait\n");
				}

				if (wait==1) {
					System.out.println("in wait \n");
					if(phase_counter%3==0 && selectedcard!=-1){
						System.out.println("sendcard");
						out_box2.println(String.valueOf(selectedcard.toString()));//手札のカード入力
						System.out.println("send card");
						selectedcard=-1;
						selectedarea=-1;
						selectcardstack=-1;
						phase_counter++;
						wait=0;
					}else if(phase_counter%3==1 && selectedarea!=-1){
						out_box2.println(String.valueOf(selectedarea.toString()));//旗場所入力
						System.out.println("send flag");
						selectedcard=-1;
						selectedarea=-1;
						selectcardstack=-1;
						phase_counter++;
						wait=0;
					}else if(phase_counter%3==2 && selectcardstack!=-1){
						System.out.println("send cardstack");
						out_box2.println(String.valueOf(selectcardstack.toString()));//山札のカード入力
						selectedcard=-1;
						selectedarea=-1;
						selectcardstack=-1;
						phase_counter++;
						wait=0;
					}
					System.out.println("in wait 2 \n");
				} else if(str.equals("handcards")){
					for (int i = 0; i < 7; i++){
						String handcard = in_box2.readLine();
						//System.out.println("handcard:" + handcard);
						ImageIcon icon = new ImageIcon("./image/"+handcard+".png");
						btn[i].setIcon(icon);
					}
				} else if(str.equals("fieldcard")){
					for(int i = 0; i < 9; i++){
						String fieldcard0 = in_box2.readLine();
						String fieldcard1 = in_box2.readLine();
						if(fieldcard0.length() <= 4 && fieldcard0.length() > 2) {
							c_fcards[i][0] = Integer.parseInt(fieldcard0.substring(1,1+2));
						} else if(fieldcard0.length() >= 5 && fieldcard0.length() < 9){
							c_fcards[i][0] = Integer.parseInt(fieldcard0.substring(1,1+2));
							c_fcards[i][1] = Integer.parseInt(fieldcard0.substring(5,5+2));
						} else if(fieldcard0.length() >= 9 && fieldcard0.length() < 13){
							c_fcards[i][0] = Integer.parseInt(fieldcard0.substring(1,1+2));
							c_fcards[i][1] = Integer.parseInt(fieldcard0.substring(5,5+2));
							c_fcards[i][2] = Integer.parseInt(fieldcard0.substring(9,9+2));
						}
						if(fieldcard1.length() <= 4 && fieldcard1.length() > 2) {
							s_fcards[i][0] = Integer.parseInt(fieldcard1.substring(1,1+2));
						} else if(fieldcard1.length() >= 5 && fieldcard1.length() < 9){
							s_fcards[i][0] = Integer.parseInt(fieldcard1.substring(1,1+2));
							s_fcards[i][1] = Integer.parseInt(fieldcard1.substring(5,5+2));
						} else if(fieldcard1.length() >= 9 && fieldcard1.length() < 13){
							s_fcards[i][0] = Integer.parseInt(fieldcard1.substring(1,1+2));
							s_fcards[i][1] = Integer.parseInt(fieldcard1.substring(5,5+2));
							s_fcards[i][2] = Integer.parseInt(fieldcard1.substring(9,9+2));
						}
						flaglabel[i].setText("[" + c_fcards[i][0] + "," + c_fcards[i][1] + "," + c_fcards[i][2] + "]" );
						opponent_flag[i].setText("[" + s_fcards[i][0] + "," + s_fcards[i][1] + "," + s_fcards[i][2] + "]");
					}
				} else {
					System.out.println(str);
					System.out.println("bbbb\n");
				}
				if (str == "END") break;
			}
			
			
		} finally {
			System.out.println("closing...");
			socket.close();
		}
	}
}