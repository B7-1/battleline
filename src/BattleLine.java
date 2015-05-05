import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;

import java.io.*;

class CUI {
	GameSystem system;

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

			while (true) {
				if (s.selectionArea == Area.None) {
					System.out.println();
					s.step();

				} else if (s.selectionArea == Area.MyHand || s.selectionArea == Area.OpponentHand) {
					System.out.println("select card from...");
					for (int i = 0; i < s.player(s.turn).cards.size(); i++) {
						Card c = s.player(s.turn).cards.get(i);
						System.out.print(" [" + i + "]" + c.toString());
					}
					System.out.println();

					final Player player = s.player(s.turn);
					final int selectedIndex = readInt(in);
					assert 0 <= selectedIndex && selectedIndex < player.cards.size();
					s.selectCard(player.cards.get(selectedIndex));

				} else if (s.selectionArea == Area.Flags) {
					System.out.println("put card on one of the flags...");
					for (int i = 0; i < s.flags.size(); i++) {
						Flag f = s.flag(i);
						System.out.print(" " + f.cards.get(0));
						System.out.print((f.owner == 0) ? "|" : ":");
						System.out.print(i);
						System.out.print((f.owner == 1) ? "|" : ":");
						System.out.println(f.cards.get(1));
					}
					System.out.println();

					final int selectedIndex = readInt(in);
					assert 0 <= selectedIndex && selectedIndex < s.flags.size();
					s.selectFlag(s.flag(selectedIndex));

				} else if (s.selectionArea == Area.CardStack) {
					System.out.println("draw a card from...");
					System.out.println("[0] unit, [1] tactics");

					final int selectedIndex = readInt(in);
					assert 0 <= selectedIndex && selectedIndex <= 1;
					if (selectedIndex == 0)
						s.selectStack(s.unitStack);
					else if (selectedIndex == 1)
						s.selectStack(s.unitStack);
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