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

	static void nextPhase() {
		selectedcard=-1;
		selectedarea=-1;
		selectcardstack=-1;
		phase_counter++;
		wait=0;
	}

	static GUI gui;
	static void layoutComponents() {
		gui = new GUI("BattleLine");
		gui.layoutComponents();

		for (int i = 0; i < 2; i++) {
			gui.cardstack[i].addActionListener(e -> selectcardstack = Integer.parseInt(e.getActionCommand()));
		}

		for (int i = 0; i < 9; i++) {
			gui.flag[i].addActionListener(e -> selectedarea = Integer.parseInt(e.getActionCommand()));
		}

		for (int i = 0; i < 7; i++) {
			gui.btn[i].addActionListener(e -> selectedcard = Integer.parseInt(e.getActionCommand()));
		}
	}
	
	public static void main(String[] args) throws IOException {
		layoutComponents();

		System.out.print("server's machine name;");
		String Name = input.readLine();
		InetAddress addr = InetAddress.getByName(Name);
		System.out.println("addr = " + addr);
		Socket socket = new Socket(addr, BattleLine.PORT);
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
					System.out.println("stop");
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
						out_box2.println(selectedcard);//手札のカード入力
						System.out.println("send card");
						
						nextPhase();
					}else if(phase_counter%3==1 && selectedarea!=-1){
						out_box2.println(selectedarea);//旗場所入力
						System.out.println("send flag");
						
						nextPhase();
					}else if(phase_counter%3==2 && selectcardstack!=-1){
						System.out.println("send cardstack");
						out_box2.println(selectcardstack);//山札のカード入力
						
						nextPhase();
					}
					System.out.println("in wait 2 \n");
				} else if(str.equals("handcards")){
					for (int i = 0; i < 7; i++){
						System.out.println("handcard:");
						String handcard = in_box2.readLine();
						ImageIcon icon = new ImageIcon("./image/"+handcard+".png");
						gui.btn[i].setIcon(icon);
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

						for(int j=0;j<3;j++){
							ImageIcon icon1 = new ImageIcon("./image/s"+c_fcards[i][j]+".png");
							ImageIcon icon2 = new ImageIcon("./image/s"+s_fcards[i][j]+".png");
							gui.flaglabel_card[i][j].setIcon(icon1);
							gui.opponent_flag_card[i][j].setIcon(icon2);
						}
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