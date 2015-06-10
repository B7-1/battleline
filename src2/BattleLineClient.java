import java.io.*;
import java.net.*;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;


public class BattleLineClient {
	//static String str2 = "0";
	//static int box = 0;
	//static int turn_flag = 0;
	static BufferedReader in_box2;
	static PrintWriter out_box2;
	static BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
	
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
		System.out.print("ƒzƒXƒg–¼‚ð“ü—Í‚µ‚Ä‚Ë:");
		String Name = input.readLine();
		InetAddress addr = InetAddress.getByName(Name);
		System.out.println("addr = " + addr);
		Socket socket = new Socket(addr, BattleLineServer.PORT);
		try {
			System.out.println("socket = " + socket);
			in_box2 = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out_box2 = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
			while (true) {
				String str = in_box2.readLine();
				if (str.equals("Input")) {
					out_box2.println(String.valueOf(readInt(input)));
				} else {
				System.out.println(str);
				}
				if (str == "END") break;
			}
			
			//CUIclient ui = new CUIclient(new GameSystem());
			//ui.main();//launch(args);
		} finally {
			System.out.println("closing...");
			socket.close();
		}
	}
}