import java.util.*;

public interface Phase {
	
}

class OrderPhase implements Phase {

}

class DealPhase implements Phase {
	DealPhase(GameSystem s) {
		// delete all cards
		s.unitStack.clear();
		s.tacticsStack.clear();
		s.players.get(0).cards.clear();
		s.players.get(1).cards.clear();
		for (Flag f : s.flags) {
			f.cards.get(0).clear();
			f.cards.get(1).clear();
		}

		// create unit card and shuffle stack
		for (int suit = 0; suit < 6; suit++) {
			for (int number = 1; number <= 10; number++) {
				s.unitStack.add(new UnitCard(suit, number));
			}
		}
		Collections.shuffle(s.unitStack);

		// create tactics card and shuffle stack
		for (Tactics t : Tactics.values()) {
			s.tacticsStack.add(new TacticsCard(t));
		}
		Collections.shuffle(s.tacticsStack);

		// deal cards to players
		for (int i = 0; i < 7; i++) {
			UnitCard c = s.unitStack.pop();
			s.players.get(0).cards.add(c);
		}
		for (int i = 0; i < 7; i++) {
			UnitCard c = s.unitStack.pop();
			s.players.get(1).cards.add(c);
		}
	}
}

class SelectPhase implements Phase {

}

class StationPhase implements Phase {

}

class TacticsPhase implements Phase {

}

class DrawPhase implements Phase {

}

class ChangePlayerPhase implements Phase {

}

class GameOverPhase implements Phase {

}
