import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import java.util.*;

import java.io.*;
import javax.swing.JFrame;
import javax.swing.JButton;

import java.awt.Color;
import java.awt.event.*;


class CUI implements ActionListener{
	GameSystem system;
	Integer selectedcard=-1;
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

	public void actionPerformed(ActionEvent e){
    	String s= e.getActionCommand();
    	selectedcard= Integer.parseInt(s);
    	System.out.println(selectedcard);//印//
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
			//gui.add(cards5);
    		//gui.setVisible(true);
    		//GUI部分終了

			while (true) {
				if (s.selectionArea == Area.None) {
					System.out.println();
					s.step();

				} else if (s.selectionArea == Area.MyHand || s.selectionArea == Area.OpponentHand) {
					//カード選択部分
					System.out.println("select card from...");
					//Container contentPane = gameframe.getContentPane();
					//contentPane.add(panel, BorderLayout.CENTER);
					for (int i = 0; i < s.player(s.turn).cards.size(); i++) {
						Card c = s.player(s.turn).cards.get(i);
						System.out.print(" [" + i + "]" + c.toString());
						GUICardButton btn=new GUICardButton(c);
						btn.setForeground(Color.BLUE);
						btn.setBackground(Color.RED);
						btn.setName(c.toString());
						btn.addActionListener(this);
						btn.setActionCommand(c.toString());
						btn.setOpaque(true);
						cards5.add(btn);
					}

					gui.add(cards5);
					gui.setVisible(true);
					System.out.println();


					final Player player = s.player(s.turn);
					final int selectedIndex =selectedcard;
					selectedcard=-1;
					assert 0 <= selectedIndex && selectedIndex < player.cards.size();
					s.selectCard(player.cards.get(selectedIndex));
				} else if (s.selectionArea == Area.Flags) {
					//カード配置場所
					System.out.println("put card on one of the flags...");
					for (int i = 0; i < s.flags.size(); i++) {
						Flag f = s.flag(i);
						System.out.print(" " + f.cards.get(0));
						if (f.owner == -1) {
							System.out.print(":");
							System.out.print(i);
							System.out.print(":");
						} else {
							System.out.print((f.owner == 0) ? "|" : " ");
							System.out.print(" ");
							System.out.print((f.owner == 1) ? "|" : " ");
						}
						System.out.println(f.cards.get(1));
					}
					System.out.println();

					final int selectedIndex = readInt(in);//読み込み
					assert 0 <= selectedIndex && selectedIndex < s.flags.size();
					s.selectFlag(s.flag(selectedIndex));//s

				} else if (s.selectionArea == Area.CardStack) {
					//山札からカードを引く
					System.out.println("draw a card from...");
					//選択オプション
					System.out.println("[0] unit, [1] tactics");

					final int selectedIndex = readInt(in);
					assert 0 <= selectedIndex && selectedIndex <= 1;
					if (selectedIndex == 0)
						s.selectStack(s.unitStack);
					else if (selectedIndex == 1)
						s.selectStack(s.tacticsStack);
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