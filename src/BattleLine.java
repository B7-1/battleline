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
	Integer selectedcard=-1;//どのカードを選択したか
	Integer selectedarea=-1;//どこにカードを置いたか
	Integer selectcardstack=-1;//引くカードを選択

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
				//flagbtn[i].setLayout(new GridLayout(0,3));
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

			while (true) {
				if (s.selectionArea == Area.None) {
					System.out.println();
					s.step();
				} else if (s.selectionArea == Area.MyHand || s.selectionArea == Area.OpponentHand) {
					//カード選択部分
					//System.out.println("select card from...");
					//Container contentPane = gameframe.getContentPane();
					//contentPane.add(panel, BorderLayout.CENTER);
					for (int i = 0; i < s.player(s.turn).cards.size(); i++) {
						Card c = s.player(s.turn).cards.get(i);
						ImageIcon icon = new ImageIcon("./image/"+c.toString()+".png");
						btn[i].setIcon(icon);
						EtchedBorder border = new EtchedBorder(EtchedBorder.RAISED, Color.black, Color.black);
    					btn[i].setBorder(border);
					}


					if(selectedcard!=-1){
						System.out.println(selectedcard);
						final Player player = s.player(s.turn);
						int selectedIndex =selectedcard;	//変更
						if (s.turn == 1) {							//ƒNƒ‰ƒCƒAƒ“ƒg‘¤‚Ìƒ^[ƒ“
							BattleLine.out_box.println("Input");	//ƒNƒ‰ƒCƒAƒ“ƒg‘¤‚©‚ç“ü—Í‚ð‹‚ß‚é
							selectedIndex = Integer.parseInt(BattleLine.in_box.readLine());
						}
						assert 0 <= selectedIndex && selectedIndex < player.cards.size();
						s.selectCard(player.cards.get(selectedIndex));
						System.out.println();
						selectedcard=-1;//どのカードを選択したか
						selectedarea=-1;//どこにカードを置いたか
						selectcardstack=-1;//引くカードを選択
					}
				} else if (s.selectionArea == Area.Flags) {
					//カード配置場所
					System.out.println("put card on one of the flags...");
					
					
					if(selectedarea!=-1){
						System.out.println(selectedarea);
						int selectedIndex = selectedarea; //変更
						if (s.turn == 1) {
							BattleLine.out_box.println("Input");	//ƒNƒ‰ƒCƒAƒ“ƒg‘¤‚©‚ç“ü—Í‚ð‹‚ß‚é
							selectedIndex = Integer.parseInt(BattleLine.in_box.readLine());
						}
						assert 0 <= selectedIndex && selectedIndex < s.flags.size();
						s.selectFlag(s.flag(selectedIndex));
						selectedcard=-1;//どのカードを選択したか
						selectedarea=-1;//どこにカードを置いたか
						selectcardstack=-1;//引くカードを選択

					}

				} else if (s.selectionArea == Area.CardStack) {
					//山札からカードを引く
					System.out.println("draw a card from...");
					//選択オプション
					System.out.println("[0] unit, [1] tactics");

					for (int i = 0; i < s.flags.size(); i++) {
						Flag f = s.flag(i);
						opponent_flag[i].setText(f.cards.get(1).toString());

						flagbtn[i].setText(f.cards.get(0).toString());
					}
					if(selectcardstack!=-1){
						int selectedIndex = selectcardstack;//変更
						assert 0 <= selectedIndex && selectedIndex <= 1;
						if(s.turn==0){
							if (selectedIndex == 0)
								s.selectStack(s.unitStack);
							else if (selectedIndex == 1)
								s.selectStack(s.tacticsStack);
						}else if(s.turn ==1){
							BattleLine.out_box.println("Input");	//ƒNƒ‰ƒCƒAƒ“ƒg‘¤‚©‚ç“ü—Í‚ð‹‚ß‚é
							selectedIndex = Integer.parseInt(BattleLine.in_box.readLine());
							if (selectedIndex == 0)
								s.selectStack(s.unitStack);
							else if (selectedIndex == 1)
								s.selectStack(s.tacticsStack);
						}
						selectedcard=-1;//どのカードを選択したか
						selectedarea=-1;//どこにカードを置いたか
						selectcardstack=-1;//引くカードを選択

					}
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
				in_box = new BufferedReader(new InputStreamReader(socket.getInputStream()));	//ŽóM—p
				out_box = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);	//‘—M—p
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