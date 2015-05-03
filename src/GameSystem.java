import java.util.*;


public class GameSystem {
	/* @NonNull */ Phase phase;
	/* @NonNull */ List<Player> players;
	/* @NonNull */ Stack<UnitCard> unitStack;
	/* @NonNull */ Stack<TacticsCard> tacticsStack;
	/* @NonNull */ List<Flag> flags;
	int turn; /* 0 or 1 */

	GameSystem() {
		phase = new OrderPhase();
		players = new ArrayList<Player>();
		unitStack = new Stack<UnitCard>();
		tacticsStack = new Stack<TacticsCard>();
		flags = new ArrayList<Flag>();
	}

	Area selectionArea = Area.None;
	void selectCard(Card card) {
		Action act = new Action(selectionArea, card, null);
		System.out.println("card selected: " + card);
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

enum Area {
	None,
	CardStack,
	MyHand,
	OpponentHand,
	MySquads,
	OpponentSquads,
	Flags,
}
