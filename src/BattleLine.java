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


public class BattleLine extends WindowAdapter {
	public static final int PORT = 8080;
	static BufferedReader in_box;
	static PrintWriter out_box;
	static int card_Flag=0;//giving hand cards	0:not yet 1:already
	static int[] hcards = new int[8];
	static int[][] s_fcards = new int[9][3];
	static int[][] c_fcards = new int[9][3];
	static InputMode inputmode = InputMode.Disabled;

	enum InputMode {
		Disabled,
		Stack,
		Card,
		Flag,
	}

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

	static ImageIcon cardIcon(Object obj) {
		return new ImageIcon("./image/" + obj + ".png");
	}

	static ImageIcon smallCardIcon(Object obj) {
		return new ImageIcon("./image/s" + obj + ".png");
	}

	static int readIntFromClient() throws IOException {
		out_box.println("Input");
		return Integer.parseInt(BattleLine.in_box.readLine());
	}

	static GameSystem system;
	static void start() {
		system = new GameSystem();

		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
			
			GUI gui = new GUI("BattleLine");
			for (int i = 0; i < 2; i++) {
				gui.cardstack[i].addActionListener(e -> {
					if (inputmode != InputMode.Stack) return;
					selectStack(Integer.parseInt(e.getActionCommand()));
					inputmode = InputMode.Disabled;
				});
			}

			for (int i = 0; i < 9; i++) {
				gui.flag[i].addActionListener(e -> {
					if (inputmode != InputMode.Flag) return;
					int selectedIndex = Integer.parseInt(e.getActionCommand());
					System.out.println(selectedIndex);
					assert 0 <= selectedIndex && selectedIndex < system.flags.size();
					system.selectFlag(system.flag(selectedIndex));
					inputmode = InputMode.Disabled;
				});
			}

			for (int i = 0; i < 7; i++) {
				gui.btn[i].addActionListener(e -> {
					if (inputmode != InputMode.Card) return;
					final Player player = system.player(system.turn);
					int selectedIndex = Integer.parseInt(e.getActionCommand());
					
					System.out.println(selectedIndex);
					assert 0 <= selectedIndex && selectedIndex < player.cards.size();
					system.selectCard(player.cards.get(selectedIndex));
					System.out.println();
					inputmode = InputMode.Disabled;
				});
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
					//server's turn
					if(system.turn == 0) {
						for (int i = 0; i < system.player(system.turn).cards.size(); i++) {
							Card c = system.player(system.turn).cards.get(i);

							gui.btn[i].setIcon(cardIcon(c));
							gui.btn[i].setBorder(new EtchedBorder(EtchedBorder.RAISED, Color.black, Color.black));
						}
					}

					//client's turn
					if(system.turn == 1 && card_Flag == 0) {
						// sign of giving hand cards
						out_box.println("handcards");

						for (int i = 0; i < system.player(system.turn).cards.size(); i++) {
							Card c = system.player(system.turn).cards.get(i);
							Card cs= system.player(0).cards.get(i);

							// give hand cards
							out_box.println(c);

							gui.btn[i].setIcon(cardIcon(cs));
							gui.btn[i].setBorder(new EtchedBorder(EtchedBorder.RAISED, Color.black, Color.black));
						}

						card_Flag = 1;
					}

					if (system.turn == 0) inputmode = InputMode.Card;
					final Player player = system.player(system.turn);
					if (system.turn == 1) {
						int selectedIndex = readIntFromClient();
						assert 0 <= selectedIndex && selectedIndex < player.cards.size();
						system.selectCard(player.cards.get(selectedIndex));
						System.out.println("system.turn=1\n");
					}
				} else if (system.selectionArea == Area.Flags) {
					System.out.println("put card on one of the flags...");

					if (system.turn == 0) inputmode = InputMode.Flag;
					if (system.turn==1) {
						int selectedIndex = readIntFromClient();
						assert 0 <= selectedIndex && selectedIndex < system.flags.size();
						system.selectFlag(system.flag(selectedIndex));
					}
				} else if (system.selectionArea == Area.CardStack) {
					System.out.println("draw a card from...");
					System.out.println("[0] unit, [1] tactics");

					out_box.println("fieldcard");

					for (Integer i = 0; i < system.flags.size(); i++) {
						Flag f = system.flag(i);

						// give client's fieldcards
						out_box.println(f.cards.get(1).toString());
						// give server's fieldcards
						out_box.println(f.cards.get(0).toString());

						String fieldcard0 = f.cards.get(1).toString();
						String fieldcard1 = f.cards.get(0).toString();

						assert fieldcard0.length() < 13;
						if (2 < fieldcard0.length()) {
							c_fcards[i][0] = Integer.parseInt(fieldcard0.substring(1,1+2));
						}
						if (4 < fieldcard0.length()) {
							c_fcards[i][1] = Integer.parseInt(fieldcard0.substring(5,5+2));
						}
						if (8 < fieldcard0.length()) {
							c_fcards[i][2] = Integer.parseInt(fieldcard0.substring(9,9+2));
						}

						assert fieldcard1.length() < 13;
						if (2 < fieldcard1.length()) {
							s_fcards[i][0] = Integer.parseInt(fieldcard1.substring(1,1+2));
						}
						if (4 < fieldcard1.length()) {
							s_fcards[i][1] = Integer.parseInt(fieldcard1.substring(5,5+2));
						}
						if (5 < fieldcard1.length()) {
							s_fcards[i][2] = Integer.parseInt(fieldcard1.substring(9,9+2));
						}

						for(int j = 0; j < 3; j++) {
							gui.flaglabel_card[i][j].setIcon(smallCardIcon(s_fcards[i][j]));
							gui.opponent_flag_card[i][j].setIcon(smallCardIcon(c_fcards[i][j]));
						}
					}

					if (system.turn == 0) inputmode = InputMode.Stack;
					if (system.turn == 1) {
						selectStack(readIntFromClient());
					}

					card_Flag = 0;
				}
			}

		} catch (Exception e) {
			System.out.println(e);
		}
	}

	static void selectStack(int index) {
		assert 0 <= index && index <= 1;
		if (index == 0)
			system.selectStack(system.unitStack);
		else if (index == 1)
			system.selectStack(system.tacticsStack);
	}
}