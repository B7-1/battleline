import java.util.*;
import java.util.stream.*;
import java.util.function.*;
import java.util.IntSummaryStatistics;
import java.util.Optional;
import java.util.function.Function;

public class JudgePhase implements Phase {
    JudgePhase(GameSystem s) {
        //System.out.print("JudgePhase: ");
        s.selectionArea = Area.None;
    }

    boolean isSkirmish(List<UnitCard> cards) {
        IntSummaryStatistics stat = cards.stream()
                .mapToInt(c -> c.number)
                .distinct()
                .summaryStatistics();

        return stat.getMax() - stat.getMin() + 1 == cards.size() &&
                stat.getCount() == cards.size();
    }

    boolean isBattalion(List<UnitCard> cards) {
        return cards.stream().map(c -> c.suit).distinct().count() == 1;
    }

    boolean isPhalanx(List<UnitCard> cards) {
        return cards.stream().map(c -> c.number).distinct().count() == 1;
    }

    Stream<List<Card>> substituteLeader(List<Card> cards) {
        List<List<Card>> result = new ArrayList<List<Card>>();

        Optional<TacticsCard> leader = cards.stream()
                .filter(c -> c instanceof TacticsCard)
                .map(c -> (TacticsCard)c)
                .filter(c -> c.kind == Tactics.Darius || c.kind == Tactics.Alexander)
                .findAny();

        if (!leader.isPresent()) {
            result.add(cards);
            return result.stream();
        }
        cards.remove(leader.get());

        for (int suit = 0; suit < UnitCard.NumberOfSuits; suit++) {
            for (int number = 1; number <= UnitCard.MaximumNumber; number++) {
                UnitCard c = new UnitCard(suit, number);
                cards.add(c);
                result.add(new ArrayList<Card>(cards));
                cards.remove(c);
            }
        }

        cards.add(leader.get());

        return result.stream();
    }

    Stream<List<Card>> substituteShield(List<Card> cards) {
        List<List<Card>> result = new ArrayList<List<Card>>();

        Optional<TacticsCard> shield = cards.stream()
                .filter(c -> c instanceof TacticsCard)
                .map(c -> (TacticsCard)c)
                .filter(c -> c.kind == Tactics.Shield)
                .findAny();

        if (!shield.isPresent()) {
            result.add(cards);
            return result.stream();
        }
        cards.remove(shield.get());

        for (int suit = 0; suit < UnitCard.NumberOfSuits; suit++) {
            for (int number = 1; number <= 3; number++) {
                UnitCard c = new UnitCard(suit, number);
                cards.add(c);
                result.add(new ArrayList<Card>(cards));
                cards.remove(c);
            }
        }

        cards.add(shield.get());

        return result.stream();
    }

    Stream<List<Card>> substituteCompanion(List<Card> cards) {
        List<List<Card>> result = new ArrayList<List<Card>>();

        Optional<TacticsCard> companion = cards.stream()
                .filter(c -> c instanceof TacticsCard)
                .map(c -> (TacticsCard)c)
                .filter(c -> c.kind == Tactics.Companion)
                .findAny();

        if (!companion.isPresent()) {
            result.add(cards);
            return result.stream();
        }
        cards.remove(companion.get());

        for (int suit = 0; suit < UnitCard.NumberOfSuits; suit++) {
            UnitCard c = new UnitCard(suit, 8);
            cards.add(c);
            result.add(new ArrayList<Card>(cards));
            cards.remove(c);
        }

        cards.add(companion.get());

        return result.stream();
    }

    int strengthOfSquad(List<UnitCard> squad) {
        int str = 0;

        for (UnitCard c : squad) {
            str += c.number * 10;
        }

        final int SkirmishBonus = 10000;
        final int BattalionBonus = 20000;
        final int PhalanxBonus = 25000;
        final int WedgeBonus = 30000;

        if (isSkirmish(squad) && isBattalion(squad)) str += WedgeBonus;
        else if (isPhalanx(squad)) str += PhalanxBonus;
        else if (isBattalion(squad)) str += BattalionBonus;
        else if (isSkirmish(squad)) str += SkirmishBonus;

        return str;
    }

    int alterTactics(List<Card> cards, ToIntFunction<List<UnitCard>> func) {
        return substituteCompanion(cards)
                .flatMap(this::substituteShield)
                .flatMap(this::substituteLeader)
                .map(s -> s.stream().map(c -> (UnitCard)c).collect(Collectors.toList()))
                .mapToInt(func)
                .max()
                .getAsInt();
    }

    int strengthOfCards(List<Card> cards) {
        return alterTactics(new ArrayList<Card>(cards), this::strengthOfSquad);
    }

    IntSummaryStatistics estimateStrengthOfCards(List<Card> cards, List<UnitCard> candidates, int rest, boolean fog) {
        IntSummaryStatistics result = new IntSummaryStatistics();

        if (rest == 0) {
            int score = strengthOfCards(cards);
            if (fog) score %= 1000;
            result.accept(score);
        } else {
            for (Card c : candidates) {
                if (cards.contains(c)) continue;
                cards.add(c);
                result.combine(estimateStrengthOfCards(cards, candidates, rest - 1, fog));
                cards.remove(c);
            }
        }

        return result;
    }

    public void process(GameSystem s, Action act) {
        List<UnitCard> remainder = s.remainderOfCards();
        IntSummaryStatistics emptyStat = null;

        for (Flag f : s.flags) {
            if (f.owner != -1) continue;

            int size = (f.isMuddy) ? 4 : 3;

            IntSummaryStatistics[] stats = new IntSummaryStatistics[2];
            for (int j = 0; j < 2; j++) {
                List<Card> squads = f.squads(j);
                int rest = size - squads.size();

                if (squads.isEmpty() && !f.isMuddy) {
                    if (emptyStat == null) emptyStat = estimateStrengthOfCards(squads, remainder, rest, f.isFogging);
                    stats[j] = emptyStat;
                } else {
                    stats[j] = estimateStrengthOfCards(squads, remainder, rest, f.isFogging);
                }
            }

            int p1 = (f.forestaller == 0) ? 1 : 0;
            int p2 = (f.forestaller == 1) ? 1 : 0;

            if (stats[0].getMin() + p1 > stats[1].getMax() + p2) {
                f.owner = 0;
            } else if (stats[0].getMax() + p1 < stats[1].getMin() + p2) {
                f.owner = 1;
            }
        }

        s.phase = new DrawPhase(s);
    }
}
