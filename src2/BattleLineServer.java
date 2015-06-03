import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;

import java.io.*;
import java.net.*;

class CUIserver {
	GameSystem system;

	CUIserver(GameSystem s) {
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
					BattleLineServer.out_box.println("select card from...");	//送信
					for (int i = 0; i < s.player(s.turn).cards.size(); i++) {
						Card c = s.player(s.turn).cards.get(i);
						System.out.print(" [" + i + "]" + c.toString());
						BattleLineServer.out_box.print(" [" + i + "]" + c.toString());	//送信
					}
					BattleLineServer.out_box.println("");	//改行送信
					System.out.println();

					final Player player = s.player(s.turn);
					int selectedIndex = 0;	//強引に初期化
					if (s.turn == 0) selectedIndex = readInt(in);
					if (s.turn == 1) {
						BattleLineServer.out_box.println("Input");	//クライアント側から入力を求める
						selectedIndex = Integer.parseInt(BattleLineServer.in_box.readLine());
					}
					assert 0 <= selectedIndex && selectedIndex < player.cards.size();
					s.selectCard(player.cards.get(selectedIndex));

				} else if (s.selectionArea == Area.Flags) {
					System.out.println("put card on one of the flags...");
					BattleLineServer.out_box.println("put card on one of the flags...");	//送信
					for (int i = 0; i < s.flags.size(); i++) {
						Flag f = s.flag(i);
						System.out.print(" " + f.cards.get(0));
						BattleLineServer.out_box.print(" " + f.cards.get(0));
						System.out.print((f.owner == 0) ? "|" : ":");
						BattleLineServer.out_box.print((f.owner == 0) ? "|" : ":");
						System.out.print(i);
						BattleLineServer.out_box.print(i);
						System.out.print((f.owner == 1) ? "|" : ":");
						BattleLineServer.out_box.print((f.owner == 1) ? "|" : ":");
						System.out.println(f.cards.get(1));
						BattleLineServer.out_box.println(f.cards.get(1));
					}
					System.out.println();

					int selectedIndex = 0;	//強引に初期化
					if (s.turn == 0) selectedIndex = readInt(in);
					if (s.turn == 1) {
						BattleLineServer.out_box.println("Input");	//クライアント側から入力を求める
						selectedIndex = Integer.parseInt(BattleLineServer.in_box.readLine());
					}
					assert 0 <= selectedIndex && selectedIndex < s.flags.size();
					s.selectFlag(s.flag(selectedIndex));

				} else if (s.selectionArea == Area.CardStack) {
					System.out.println("draw a card from...");
					BattleLineServer.out_box.println("draw a card from...");
					System.out.println("[0] unit, [1] tactics");
					BattleLineServer.out_box.println("[0] unit, [1] tactics");

					int selectedIndex = 0;	//強引に初期化
					if (s.turn == 0) selectedIndex = readInt(in);
					if (s.turn == 1) {
						BattleLineServer.out_box.println("Input");	//クライアント側から入力を求める
						selectedIndex = Integer.parseInt(BattleLineServer.in_box.readLine());
					}
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

public class BattleLineServer {
	public static final int PORT = 8080;
	static BufferedReader in_box;
	static PrintWriter out_box;
	public static void main(String[] args)
				throws IOException {
		ServerSocket s = new ServerSocket(PORT);
		System.out.println("Started: " + s);
//		try {
			Socket socket = s.accept();
			try {
				System.out.println(socket.getInputStream());
				System.out.println(socket.getOutputStream());
				System.out.println("Connection accepted: " + socket);
				in_box = new BufferedReader(new InputStreamReader(socket.getInputStream()));	//受信用
				out_box = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);	//送信用
				CUIserver ui = new CUIserver(new GameSystem());
				ui.main();//launch(args);
			} finally {
			System.out.println("closing...");
				socket.close();
			}
//		} finally {
			s.close();
//		}
	}
}
