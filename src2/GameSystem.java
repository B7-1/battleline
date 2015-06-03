import java.util.*;


public class GameSystem {
	/* @NonNull */ Phase phase;
	/* @NonNull */ List<Player> players;
	/* @NonNull */ Stack<UnitCard> unitStack;
	/* @NonNull */ Stack<TacticsCard> tacticsStack;
	/* @NonNull */ Vector<Flag> flags;
	int turn; /* 0 or 1 */
	int s_waiting = 1;
	int c_waiting;

	GameSystem() {
		phase = new OrderPhase();		//phaseの初期化
		players = new ArrayList<Player>();
		unitStack = new Stack<UnitCard>();
		tacticsStack = new Stack<TacticsCard>();
		flags = new Vector<Flag>();
	}

	Player player(int i) {
		assert 0 <= i && i < 2;	//常に真となることを記述(開発者が何を想定しているかが分かる)
		return players.get(i);	//リストからデータを取得
	}

	Flag flag(int i) {
		assert 0 <= i && i < flags.size();
		return flags.get(i);
	}

	Area selectionArea = Area.None;	//初期化
	void selectCard(Card card) {
		Action act = new Action(selectionArea, card, null);
		System.out.println("card selected: " + card);
		BattleLineServer.out_box.println("card selected: " + card);
		phase.process(this, act);
	}

	void selectFlag(Flag flag) {
		Action act = new Action(selectionArea, null, flag);
		phase.process(this, act);
	}

	void selectStack(Stack stack) {
		Card c = (Card)stack.pop();
		selectCard(c);
	}

	void step() {
		Action act = new Action(Area.None, null, null);
		phase.process(this, act);
	}
}

//列挙
enum Area {
	None,
	CardStack,
	MyHand,
	OpponentHand,
	MySquads,
	OpponentSquads,
	Flags,
}
