import java.util.*;
import java.util.stream.*;


public class JudgePhase implements Phase {
    JudgePhase(GameSystem s) {
        System.out.print("JudgePhase: ");
        s.selectionArea = Area.None;
    }

    boolean isSkirmish(List<UnitCard> cards) {
        OptionalInt max = cards.stream()
                .mapToInt(c -> c.number)
                .max();
        OptionalInt min = cards.stream()
                .mapToInt(c -> c.number)
                .min();

        return max.getAsInt() - min.getAsInt() == 2;
    }

    boolean isBattalion(List<UnitCard> cards) {
        OptionalInt max = cards.stream()
                .mapToInt(c -> c.suit)
                .max();
        OptionalInt min = cards.stream()
                .mapToInt(c -> c.suit)
                .min();

        return max.getAsInt() == min.getAsInt();
    }

    boolean isPhalanx(List<UnitCard> cards) {
        OptionalInt max = cards.stream()
                .mapToInt(c -> c.number)
                .max();
        OptionalInt min = cards.stream()
                .mapToInt(c -> c.number)
                .min();

        return max.getAsInt() == min.getAsInt();
    }

    int strengthOfSquad(List<Card> cards) {
        int str = 0;
        List<UnitCard> unitcards = new ArrayList<UnitCard>();

        for (Card c : cards) {
            assert (c instanceof UnitCard);
            str += ((UnitCard)c).number * 10;
            unitcards.add((UnitCard)c);
        }

        if (isSkirmish(unitcards)) str += 10000;
        if (isBattalion(unitcards)) str += 20000;
        if (isPhalanx(unitcards)) str += 25000;

        return str;
    }

    public void process(GameSystem s, Action act) {
        for (Flag f : s.flags) {
            if (f.owner != -1) continue;
            if (f.squads(0).size() < 3) continue;
            if (f.squads(1).size() < 3) continue;

            int p1 = strengthOfSquad(f.squads(0));
            int p2 = strengthOfSquad(f.squads(1));
            if (f.forestaller == 0) {
                p1 += 1;
            } else {
                p2 += 1;
            }

            assert p1 != p2;
            if (p1 > p2) {
                f.owner = 0;
            } else {
                f.owner = 1;
            }
        }
        s.phase = new DrawPhase(s);
    }
}
