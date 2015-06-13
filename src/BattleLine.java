import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import java.util.*;

import java.io.*;
import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

import java.awt.Color;
import java.awt.event.*;


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
			gui.add(cards4);

			JButton[] flagbtn = new JButton[9];
			JButton[] btn = new JButton[7];
			JButton[] cardstack= new JButton[2];
			JLabel[] flag= new JLabel[9];

			for(Integer i=0;i<2;i++){
				cardstack[i]=new JButton();
				cardstack[i].addActionListener(
					e->selectcardstack=Integer.parseInt(e.getActionCommand()));
				cardstack[i].setActionCommand(i.toString());
				cardstack[i].setOpaque(true);
			}
			cardstack[0].setText("unit");
			cardstack[1].setText("tactics");

			cards3.add(cardstack[0]);
			for(Integer i=0;i<9;i++){
				flag[i]=new JLabel();
				cards3.add(flag[i]);
			}
			cards3.add(cardstack[1]);


			JLabel label1=new JLabel();
			cards4.add(label1);
			for(Integer i=0;i<9;i++){
				flagbtn[i]=new JButton();
				flagbtn[i].addActionListener(
					e->selectedarea=Integer.parseInt(e.getActionCommand()));
				flagbtn[i].setActionCommand(i.toString());
				flagbtn[i].setOpaque(true);
				cards4.add(flagbtn[i]);
			}
			JLabel label2=new JLabel();
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
						//ImageIcon icon = new ImageIcon("./AS-24-1S.jpg");
						btn[i].setText(c.toString());

					}


					if(selectedcard!=-1){
						System.out.println(selectedcard);
						final Player player = s.player(s.turn);
						final int selectedIndex =selectedcard;
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
					for (int i = 0; i < s.flags.size(); i++) {
						Flag f = s.flag(i);
						f.cards.get(1);
						

						flagbtn[i].setText(f.cards.get(0).toString());
					}
					
					if(selectedarea!=-1){
						System.out.println(selectedarea);
						final int selectedIndex = selectedarea;
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
					if(selectcardstack!=-1){
						final int selectedIndex = selectcardstack;
						assert 0 <= selectedIndex && selectedIndex <= 1;
						if (selectedIndex == 0)
							s.selectStack(s.unitStack);
						else if (selectedIndex == 1)
							s.selectStack(s.tacticsStack);
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
	public static void main(String[] args) {
		CUI ui = new CUI(new GameSystem());
		ui.main();
		//launch(args);
	}

	//@Override
	public void start(Stage stage) throws Exception {
		Label label = new Label("Hello JavaFX!");
		BorderPane pane = new BorderPane();
		pane.setCenter(label);
		
		Scene scene = new Scene(pane, 640, 480);
		stage.setScene(scene);

		stage.show();
	}
}