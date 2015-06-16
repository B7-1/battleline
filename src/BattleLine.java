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



class CUI /*implements ActionListener*/{
	GameSystem system;
	Integer selectedcard=-1;
	Integer selectedarea=-1;
	Integer selectcardstack=-1;

	CUI(GameSystem s) {
		system = s;
	}
	int readInt(BufferedReader in) throws IOException {
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

	void main() {
		try {
			GameSystem s = system;
			BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
			
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
			 	opponent_flag[i].setLayout(new GridLayout(0,3));
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
				flaglabel[i].setLayout(new GridLayout(0,3));
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


			while (true) {
				if (s.selectionArea == Area.None) {
					System.out.println();
					s.step();
				} else if (s.selectionArea == Area.MyHand || s.selectionArea == Area.OpponentHand) {

					if(s.turn == 0){	//server's turn
						for (int i = 0; i < s.player(s.turn).cards.size(); i++) {
							Card c = s.player(s.turn).cards.get(i);
							ImageIcon icon = new ImageIcon("./image/"+c.toString()+".png");
														
							btn[i].setIcon(icon);
							EtchedBorder border = new EtchedBorder(EtchedBorder.RAISED, Color.black, Color.black);
	    					btn[i].setBorder(border);
						}
					}

					if(s.turn == 1 && BattleLine.card_Flag == 0){	//client's turn
						BattleLine.out_box.println("handcards");// sign of giving hand cards
						for (int i = 0; i < s.player(s.turn).cards.size(); i++) {
							Card c = s.player(s.turn).cards.get(i);
							ImageIcon icon = new ImageIcon("./image/"+c.toString()+".png");
							
							BattleLine.out_box.println(c.toString());//give hand cards
							
							btn[i].setIcon(icon);
							EtchedBorder border = new EtchedBorder(EtchedBorder.RAISED, Color.black, Color.black);
	    					btn[i].setBorder(border);
						}
						BattleLine.card_Flag = 1;
					}


					if(selectedcard!=-1){
						System.out.println(selectedcard);
						final Player player = s.player(s.turn);
						int selectedIndex =selectedcard;
						if (s.turn == 1) {
							BattleLine.out_box.println("Input");
							selectedIndex = Integer.parseInt(BattleLine.in_box.readLine());
						}
						assert 0 <= selectedIndex && selectedIndex < player.cards.size();
						s.selectCard(player.cards.get(selectedIndex));
						System.out.println();
						selectedcard=-1;
						selectedarea=-1;
						selectcardstack=-1;
					}
				} else if (s.selectionArea == Area.Flags) {

					System.out.println("put card on one of the flags...");
					
					
					if(selectedarea!=-1){
						System.out.println(selectedarea);
						int selectedIndex = selectedarea;
						if (s.turn == 1) {
							BattleLine.out_box.println("Input");
							selectedIndex = Integer.parseInt(BattleLine.in_box.readLine());
						}
						assert 0 <= selectedIndex && selectedIndex < s.flags.size();
						s.selectFlag(s.flag(selectedIndex));
						selectedcard=-1;
						selectedarea=-1;
						selectcardstack=-1;

					}

				} else if (s.selectionArea == Area.CardStack) {
					
					System.out.println("draw a card from...");
					
					System.out.println("[0] unit, [1] tactics");

					BattleLine.out_box.println("fieldcard");
					for (int i = 0; i < s.flags.size(); i++) {
						Flag f = s.flag(i);
						opponent_flag[i].setText(f.cards.get(1).toString());
						
						BattleLine.out_box.println(f.cards.get(1).toString());//give client's fieldcards

						flaglabel[i].setText(f.cards.get(0).toString());
						BattleLine.out_box.println(f.cards.get(0).toString());//give server's fieldcards
					}
					if(selectcardstack!=-1){
						int selectedIndex = selectcardstack;
						assert 0 <= selectedIndex && selectedIndex <= 1;
						if(s.turn==0){
							if (selectedIndex == 0)
								s.selectStack(s.unitStack);
							else if (selectedIndex == 1)
								s.selectStack(s.tacticsStack);
						}else if(s.turn ==1){
							BattleLine.out_box.println("Input");
							selectedIndex = Integer.parseInt(BattleLine.in_box.readLine());
							if (selectedIndex == 0)
								s.selectStack(s.unitStack);
							else if (selectedIndex == 1)
								s.selectStack(s.tacticsStack);
						}
						selectedcard=-1;
						selectedarea=-1;
						selectcardstack=-1;

					}
					BattleLine.card_Flag = 0;// reset
				}
			}

		} catch (Exception e) {
			System.out.println(e);
		}
	}


}

public class BattleLine /* extends Application */ {
	public static final int PORT = 8080;
	static BufferedReader in_box;
	static PrintWriter out_box;
	static int card_Flag=0;//giving hand cards	0:not yet 1:already
	static int deck_Flag=0;//giving deck choices	0:not yet 1:already
	static int flag_Flag=0;//giving flag choices	0:not yet 1:already
	public static void main(String[] args) 
		throws IOException {
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
				CUI ui = new CUI(new GameSystem());
				ui.main();
			}finally{
				System.out.println("closing...");
				socket.close();
			}
		}finally{
			s.close();
		}
	}
}