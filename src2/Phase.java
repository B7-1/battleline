import java.util.*;


public interface Phase {
    public void process(GameSystem s, Action act);
}

class OrderPhase implements Phase {
    public void process(GameSystem s, Action act) {
        System.out.print("Order Phase:");
        BattleLineServer.out_box.println("Order Phase");

        s.players.add(new Player());
        s.players.add(new Player());

        s.phase = new DealPhase();	//OrderPhaseの次はDealPhase
    }
}

class DealPhase implements Phase {
    public void process(GameSystem s, Action act) {
        System.out.println("Deal Phase:");
        BattleLineServer.out_box.println("Deal Phase:");
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
        Collections.shuffle(s.unitStack);	//unitカードの山札をシャッフル

        // create tactics card and shuffle stack
        for (Tactics t : Tactics.values()) {
            s.tacticsStack.add(new TacticsCard(t));
        }
        Collections.shuffle(s.tacticsStack);	//tacticsカードの山札をシャッフル

        // deal cards to players
        for (int i = 0; i < 7; i++) {
            UnitCard c = s.unitStack.pop();
            s.player(0).cards.add(c);
        }
        for (int i = 0; i < 7; i++) {
            UnitCard c = s.unitStack.pop();
            s.player(1).cards.add(c);
        }

        s.phase = new SelectPhase(s);	//DealPhaseの次はSelectPhase
    }
}

class SelectPhase implements Phase {
    SelectPhase(GameSystem s) {
        System.out.print("SelectPhase: ");
        BattleLineServer.out_box.print("SelectPhase");
        if (s.turn == 0)
            s.selectionArea = Area.MyHand;
        else
            s.selectionArea = Area.OpponentHand;
    }

    public void process(GameSystem s, Action act) {
        assert act.card != null;

        if (act.card instanceof UnitCard) {
            s.phase = new StationPhase(s, act.card);	//選んだカードがunitカードだったらStationPhaseへ
        } else if (act.card instanceof TacticsCard) {
            s.phase = new TacticsPhase();				//選んだカードがtacticsカードだったらTacticsPhaseへ
        }
    }
}

class StationPhase implements Phase {	//unitカードをどこのフラグに置くか決めるフェイズ
    final Card card;

    StationPhase(GameSystem s, Card card) {
        assert card != null;

        this.card = card;
        s.selectionArea = Area.Flags;
        System.out.print("StationPhase: " + card);
        BattleLineServer.out_box.print("StationPhase: " + card);
    }

    public void process(GameSystem s, Action act) {
        final Flag flag = act.flag;

        assert flag != null;
        if (flag.squads(s.turn).size() >= 3) return;
        if (flag.owner != -1) return;

        flag.squads(s.turn).add(card);
        s.player(s.turn).cards.remove(card);
        s.phase = new JudgePhase(s);		//StationPhaseの次はJudgePhase
    }
}

class TacticsPhase implements Phase {
    public void process(GameSystem s, Action act) {
        assert false;
        s.phase = new JudgePhase(s);		//TacticsPhaseの次はJudgePhase, JudgePhaseの次はDrawPhase
    }
}


class DrawPhase implements Phase {
    DrawPhase(GameSystem s) {
        System.out.print("DrawPhase: ");
        BattleLineServer.out_box.print("DrawPhase: ");
        s.selectionArea = Area.CardStack;
    }

    public void process(GameSystem s, Action act) {
        assert act.card != null;
        s.players.get(s.turn).cards.add(act.card);
        s.phase = new ChangePlayerPhase(s);	//DrawPhaseの次はChangePlayerPhase
    }
}

class ChangePlayerPhase implements Phase {
    ChangePlayerPhase(GameSystem s) {
        System.out.print("ChangePlayerPhase: ");
        BattleLineServer.out_box.println("ChangePlayerPhase: ");
        s.selectionArea = Area.None;
    }

    public void process(GameSystem s, Action act) {
        s.turn = (s.turn + 1) % 2;
        if ( s.turn == 1 ) s.s_waiting = 0;
        if ( s.turn == 0 ) s.c_waiting = 1;
        System.out.println("player" + (s.turn + 1));
        BattleLineServer.out_box.println("player" + (s.turn + 1));
        s.phase = new SelectPhase(s);		//ChangePlayerPhaseの次はSelectPhase
    }
}

class GameOverPhase implements Phase {
    public void process(GameSystem s, Action act) {

    }
}
