import java.util.*;


public class GameSystem {
	/* @NonNull */ Phase phase;
	/* @NonNull */ List<Player> players;
	/* @NonNull */ Stack<UnitCard> unitStack;
	/* @NonNull */ Stack<TacticsCard> tacticsStack;

	GameSystem() {
		phase = new OrderPhase();
		players = new ArrayList<Player>();
		unitStack = new Stack<UnitCard>();
		tacticsStack = new Stack<TacticsCard>();
	}
}
