import java.util.*;
import java.util.stream.*;
import java.util.function.*;


public class JudgePhase implements Phase {
    JudgePhase(GameSystem s) {
        System.out.print("JudgePhase: ");
        s.selectionArea = Area.None;
    }

    boolean isSkirmish(List<UnitCard> cards) {
        int min = 100;
        int max = -100;

        for (UnitCard c : cards) {
            if (min == c.number || max == c.number) return false;

            if (min > c.number) min = c.number;
            if (max < c.number) max = c.number;
        }

        return max - min + 1 == cards.size();
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

    int alterLeader(List<Card> cards, ToIntFunction<List<Card>> func) {
        Card leader = null;
        for (Card c : cards) {
            if (c instanceof UnitCard) continue;

            Tactics k = ((TacticsCard)c).kind;
            if (k == Tactics.Darius || k == Tactics.Alexander) {
                leader = (TacticsCard)c;
            }
        }
        if (leader == null) return func.applyAsInt(cards);
        cards.remove(leader);

        int max = 0;

        for (int suit = 0; suit < UnitCard.NumberOfSuits; suit++) {
            for (int number = 1; number <= UnitCard.MaximumNumber; number++) {
                UnitCard c = new UnitCard(suit, number);
                cards.add(c);
                int str = func.applyAsInt(cards);
                if (str > max) max = str;
                cards.remove(c);
            }
        }

        return max;
    }

    int alterCompanion(List<Card> cards, ToIntFunction<List<Card>> func) {
        Card companion = null;
        for (Card c : cards) {
            if (c instanceof UnitCard) continue;

            Tactics k = ((TacticsCard)c).kind;
            if (k == Tactics.Companion) {
                companion = (TacticsCard)c;
            }
        }
        if (companion == null) return func.applyAsInt(cards);
        cards.remove(companion);

        int max = 0;

        for (int suit = 0; suit < UnitCard.NumberOfSuits; suit++) {
            UnitCard c = new UnitCard(suit, 8);
            cards.add(c);
            int str = func.applyAsInt(cards);
            if (str > max) max = str;
            cards.remove(c);
        }

        return max;
    }

    int alterShield(List<Card> cards, ToIntFunction<List<Card>> func) {
        Card shield = null;
        for (Card c : cards) {
            if (c instanceof UnitCard) continue;

            Tactics k = ((TacticsCard)c).kind;
            if (k == Tactics.Shield) {
                shield = (TacticsCard)c;
            }
        }
        if (shield == null) return func.applyAsInt(cards);
        cards.remove(shield);

        int max = 0;

        for (int suit = 0; suit < UnitCard.NumberOfSuits; suit++) {
            for (int number = 1; number <= 3; number++) {
                UnitCard c = new UnitCard(suit, number);
                cards.add(c);
                int str = func.applyAsInt(cards);
                if (str > max) max = str;
                cards.remove(c);
            }
        }

        return max;
    }

    int strengthOfSquad(List<UnitCard> squad) {
        int str = 0;

        for (UnitCard c : squad) {
            str += c.number * 10;
        }

        if (isSkirmish(squad)) str += 10000;
        if (isBattalion(squad)) str += 20000;
        if (isPhalanx(squad)) str += 25000;

        return str;
    }

    int alterTactics(List<Card> cards, ToIntFunction<List<UnitCard>> func) {
        return alterLeader(cards, c1 ->
            alterCompanion(c1, c2 ->
                alterShield(c2, c3 ->
                    func.applyAsInt(c3.stream().map(c -> (UnitCard)c).collect(Collectors.toList()))
                    )
                )
            );
    }

    int strengthOfCards(List<Card> cards) {
        return alterTactics(new ArrayList<Card>(cards), this::strengthOfSquad);
    }

    public void process(GameSystem s, Action act) {
        for (Flag f : s.flags) {
            if (f.owner != -1) continue;
            if (f.squads(0).size() < 3) continue;
            if (f.squads(1).size() < 3) continue;

            int p1 = strengthOfCards(f.squads(0));
            int p2 = strengthOfCards(f.squads(1));
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
