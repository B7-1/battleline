import java.util.*;


public interface Phase {
    public void process(GameSystem s, Action act);
}

class OrderPhase implements Phase {
    public void process(GameSystem s, Action act) {
        System.out.print("Order Phase:");

        s.players.add(new Player());
        s.players.add(new Player());

        s.phase = new DealPhase();
    }
}

class DealPhase implements Phase {
    public void process(GameSystem s, Action act) {
        System.out.println("Deal Phase:");
        // delete all cards
        s.unitStack.clear();
        s.tacticsStack.clear();
        s.players.get(0).cards.clear();
        s.players.get(1).cards.clear();
        s.flags.clear();
        for (int i = 0; i < 9; i++) {
            s.flags.add(new Flag());
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
            s.player(0).cards.add(c);
        }
        for (int i = 0; i < 7; i++) {
            UnitCard c = s.unitStack.pop();
            s.player(1).cards.add(c);
        }

        s.phase = new SelectPhase(s);
    }
}

class SelectPhase implements Phase {
    SelectPhase(GameSystem s) {
        System.out.print("SelectPhase: ");
        if (s.turn == 0)
            s.selectionArea = Area.MyHand;
        else
            s.selectionArea = Area.OpponentHand;
    }

    public void process(GameSystem s, Action act) {
        assert act.card != null;

        if (act.card instanceof UnitCard) {
            s.phase = new StationPhase(s, act.card);
        } else if (act.card instanceof TacticsCard) {
            s.phase = new TacticsPhase();
        }
    }
}

class StationPhase implements Phase {
    final Card card;

    StationPhase(GameSystem s, Card card) {
        assert card != null;

        this.card = card;
        s.selectionArea = Area.Flags;
        System.out.print("StationPhase: " + card);
    }

    public void process(GameSystem s, Action act) {
        final Flag flag = act.flag;

        assert flag != null;
        if (flag.squads(s.turn).size() >= 3) return;
        if (flag.owner != -1) return;

        flag.squads(s.turn).add(card);
        s.player(s.turn).cards.remove(card);
        s.phase = new JudgePhase(s);
    }
}

class TacticsPhase implements Phase {
    public void process(GameSystem s, Action act) {
        assert false;
        s.phase = new JudgePhase(s);
    }
}


class DrawPhase implements Phase {
    DrawPhase(GameSystem s) {
        System.out.print("DrawPhase: ");
        s.selectionArea = Area.CardStack;
    }

    public void process(GameSystem s, Action act) {
        assert act.card != null;
        s.players.get(s.turn).cards.add(act.card);
        s.phase = new ChangePlayerPhase(s);
    }
}

class ChangePlayerPhase implements Phase {
    ChangePlayerPhase(GameSystem s) {
        System.out.print("ChangePlayerPhase: ");
        s.selectionArea = Area.None;
    }

    public void process(GameSystem s, Action act) {
        s.turn = (s.turn + 1) % 2;
        System.out.println("player" + (s.turn + 1));
        s.phase = new SelectPhase(s);
    }
}

class GameOverPhase implements Phase {
    public void process(GameSystem s, Action act) {

    }
}
