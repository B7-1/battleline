import static org.junit.Assert.assertEquals;
import org.junit.Test;
import java.util.*;

public class JudgePhaseTest {

	List<Card> squad(Object... cards) {
		List<Card> squad = new ArrayList<Card>();

		for (Object obj : cards) {
			if (obj instanceof Integer) {
				Integer i = (Integer)obj;
				squad.add(new UnitCard(i / 10, (i % 10) > 0 ? (i % 10) : 10));
			} else if (obj instanceof Tactics) {
				Tactics t = (Tactics)obj;
				squad.add(new TacticsCard(t));
			}
		}

		return squad;
	}

	JudgePhase phase;

	public JudgePhaseTest() {
		phase = new JudgePhase(new GameSystem());
	}

	@Test
	public void strengthOfHost() {
		assertEquals(70, phase.strengthOfCards(squad(11, 12, 34)));
		assertEquals(230, phase.strengthOfCards(squad(18, 10, 25)));
	}
}
