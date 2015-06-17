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


public class BattleLine /* extends Application */ {
	public static final int PORT = 8080;
	static BufferedReader in_box;
	static PrintWriter out_box;
	static int card_Flag=0;//giving hand cards	0:not yet 1:already
	static int deck_Flag=0;//giving deck choices	0:not yet 1:already
	static int flag_Flag=0;//giving flag choices	0:not yet 1:already
	static int[] hcards = new int[8];
	static int[][] s_fcards = new int[9][3];
	static int[][] c_fcards = new int[9][3];
	static Integer selectedcard=-1;
	static Integer selectedarea=-1;
	static Integer selectcardstack=-1;

	public static void main(String[] args) throws IOException {
		ServerSocket s = new ServerSocket(PORT);
		System.out.println("Started: " + s);
		try {
			Socket socket = s.accept();
			try {
				System.out.println(socket.getInputStream());
				System.out.println(socket.getOutputStream());
				System.out.println("Connection accepted: " + socket);
				in_box = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				out_box = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
				start();
			}finally{
				System.out.println("closing...");
				socket.close();
			}
		}finally{
			s.close();
		}
	}

	static void start() {
		GameSystem system = new GameSystem();

		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
			
			GUI gui = new GUI("BattleLine");
			for (int i = 0; i < 2; i++) {
				gui.cardstack[i].addActionListener(e -> selectcardstack = Integer.parseInt(e.getActionCommand()));
			}

			for (int i = 0; i < 9; i++) {
				gui.flag[i].addActionListener(e -> selectedarea = Integer.parseInt(e.getActionCommand()));
			}

			for (int i = 0; i < 7; i++) {
				gui.btn[i].addActionListener(e -> selectedcard = Integer.parseInt(e.getActionCommand()));
			}

			for(int i = 0; i < 9; i++) {
				for(int j = 0; j < 3; j++) {
					s_fcards[i][j] = 99;	//initialize s_fcards
					c_fcards[i][j] = 99;	//initialize c_fcards
				}
			}

			while (true) {
				if (system.selectionArea == Area.None) {
					System.out.println();
					system.step();
				} else if (system.selectionArea == Area.MyHand || system.selectionArea == Area.OpponentHand) {

					if(system.turn == 0){	//server's turn
					for (int i = 0; i < system.player(system.turn).cards.size(); i++) {
						Card c = system.player(system.turn).cards.get(i);
						ImageIcon icon = new ImageIcon("./image/"+c.toString()+".png");

						gui.btn[i].setIcon(icon);
						EtchedBorder border = new EtchedBorder(EtchedBorder.RAISED, Color.black, Color.black);
						gui.btn[i].setBorder(border);
					}
				}

					if(system.turn == 1 && BattleLine.card_Flag == 0){	//client's turn
						BattleLine.out_box.println("handcards");// sign of giving hand cards
						for (int i = 0; i < system.player(system.turn).cards.size(); i++) {
							Card c = system.player(system.turn).cards.get(i);
							Card cs= system.player(0).cards.get(i);
							ImageIcon icon = new ImageIcon("./image/"+cs.toString()+".png");
							gui.btn[i].setIcon(icon);

							BattleLine.out_box.println(c.toString());//give hand cards
							
							
							EtchedBorder border = new EtchedBorder(EtchedBorder.RAISED, Color.black, Color.black);
							gui.btn[i].setBorder(border);
						}
						BattleLine.card_Flag = 1;
					}


					if(selectedcard!=-1 && system.turn==0){
						System.out.println(selectedcard);
						final Player player = system.player(system.turn);
						int selectedIndex =selectedcard;
						assert 0 <= selectedIndex && selectedIndex < player.cards.size();
						system.selectCard(player.cards.get(selectedIndex));
						System.out.println();
						selectedcard=-1;
						selectedarea=-1;
						selectcardstack=-1;
					}else if(system.turn==1){
						final Player player = system.player(system.turn);
						int selectedIndex ;
						BattleLine.out_box.println("Input");
						selectedIndex = Integer.parseInt(BattleLine.in_box.readLine());
						assert 0 <= selectedIndex && selectedIndex < player.cards.size();
						system.selectCard(player.cards.get(selectedIndex));
						System.out.println("system.turn=1\n");
					}
				} else if (system.selectionArea == Area.Flags) {

					System.out.println("put card on one of the flags...");
					
					
					if(selectedarea!=-1 && system.turn==0){
						System.out.println(selectedarea);
						int selectedIndex = selectedarea;
						assert 0 <= selectedIndex && selectedIndex < system.flags.size();
						system.selectFlag(system.flag(selectedIndex));
						selectedcard=-1;
						selectedarea=-1;
						selectcardstack=-1;

					}else if(system.turn==1){
						int selectedIndex ;
						BattleLine.out_box.println("Input");
						selectedIndex = Integer.parseInt(BattleLine.in_box.readLine());
						assert 0 <= selectedIndex && selectedIndex < system.flags.size();
						system.selectFlag(system.flag(selectedIndex));
					}

				} else if (system.selectionArea == Area.CardStack) {
					
					System.out.println("draw a card from...");
					
					System.out.println("[0] unit, [1] tactics");

					BattleLine.out_box.println("fieldcard");


					for (Integer i = 0; i < system.flags.size(); i++) {
						Flag f = system.flag(i);

						BattleLine.out_box.println(f.cards.get(1).toString());//give client's fieldcards
						BattleLine.out_box.println(f.cards.get(0).toString());//give server's fieldcards
						
						String fieldcard0 = f.cards.get(1).toString();
						String fieldcard1 = f.cards.get(0).toString();
						if(fieldcard0.length() <= 4 && fieldcard0.length() > 2) {
							BattleLine.c_fcards[i][0] = Integer.parseInt(fieldcard0.substring(1,1+2));
						} else if(fieldcard0.length() >= 5 && fieldcard0.length() < 9){
							BattleLine.c_fcards[i][0] = Integer.parseInt(fieldcard0.substring(1,1+2));
							BattleLine.c_fcards[i][1] = Integer.parseInt(fieldcard0.substring(5,5+2));
						} else if(fieldcard0.length() >= 9 && fieldcard0.length() < 13){
							BattleLine.c_fcards[i][0] = Integer.parseInt(fieldcard0.substring(1,1+2));
							BattleLine.c_fcards[i][1] = Integer.parseInt(fieldcard0.substring(5,5+2));
							BattleLine.c_fcards[i][2] = Integer.parseInt(fieldcard0.substring(9,9+2));
						}
						if(fieldcard1.length() <= 4 && fieldcard1.length() > 2) {
							BattleLine.s_fcards[i][0] = Integer.parseInt(fieldcard1.substring(1,1+2));
						} else if(fieldcard1.length() >= 5 && fieldcard1.length() < 9){
							BattleLine.s_fcards[i][0] = Integer.parseInt(fieldcard1.substring(1,1+2));
							BattleLine.s_fcards[i][1] = Integer.parseInt(fieldcard1.substring(5,5+2));
						} else if(fieldcard1.length() >= 9 && fieldcard1.length() < 13){
							BattleLine.s_fcards[i][0] = Integer.parseInt(fieldcard1.substring(1,1+2));
							BattleLine.s_fcards[i][1] = Integer.parseInt(fieldcard1.substring(5,5+2));
							BattleLine.s_fcards[i][2] = Integer.parseInt(fieldcard1.substring(9,9+2));
						}
						

						//flaglabel[i].setText("[" + BattleLine.s_fcards[i][0] + "," + BattleLine.s_fcards[i][1] + "," + BattleLine.s_fcards[i][2] + "]");
						//opponent_flag[i].setText("[" + BattleLine.c_fcards[i][0] + "," + BattleLine.c_fcards[i][1] + "," + BattleLine.c_fcards[i][2] + "]");
						for(int j=0;j<3;j++){
							//ImageIcon icon = new ImageIcon("./image/"+handcard+".png");
							//btn[i].setIcon(icon);
							ImageIcon icon1 = new ImageIcon("./image/s"+BattleLine.s_fcards[i][j]+".png");
							gui.flaglabel_card[i][j].setIcon(icon1);
							ImageIcon icon2 = new ImageIcon("./image/s"+BattleLine.c_fcards[i][j]+".png");
							gui.opponent_flag_card[i][j].setIcon(icon2);
						}
					}
					if(selectcardstack!=-1 && system.turn==0){
						int selectedIndex = selectcardstack;
						assert 0 <= selectedIndex && selectedIndex <= 1;
						
						if (selectedIndex == 0)
							system.selectStack(system.unitStack);
						else if (selectedIndex == 1)
							system.selectStack(system.tacticsStack);
						selectedcard=-1;
						selectedarea=-1;
						selectcardstack=-1;
					}else if(system.turn==1){
						BattleLine.out_box.println("Input");
						int selectedIndex;
						selectedIndex = Integer.parseInt(BattleLine.in_box.readLine());
						if (selectedIndex == 0)
							system.selectStack(system.unitStack);
						else if (selectedIndex == 1)
							system.selectStack(system.tacticsStack);

					}
					BattleLine.card_Flag = 0;// reset
				}
			}

		} catch (Exception e) {
			System.out.println(e);
		}
	}
}