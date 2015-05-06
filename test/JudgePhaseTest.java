import static org.junit.Assert.assertEquals;
import org.junit.Test;
import java.util.*;

public class JudgePhaseTest {
	static final int SkirmishBonus = 10000;
	static final int BattalionBonus = 20000;
	static final int PhalanxBonus = 15000;

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

	@Test
	public void strengthOfLeader() {
		assertEquals("Host", 150, phase.strengthOfCards(squad(11, 24, Tactics.Alexander)));
		assertEquals("Skirmish", SkirmishBonus + 150, phase.strengthOfCards(squad(14, 26, Tactics.Darius)));
		assertEquals("Battalion", BattalionBonus + 170, phase.strengthOfCards(squad(21, 26, Tactics.Alexander)));
		assertEquals("Phalanx", PhalanxBonus + 120, phase.strengthOfCards(squad(24, 34, Tactics.Darius)));
		assertEquals("Wedge", SkirmishBonus + BattalionBonus + 150, phase.strengthOfCards(squad(24, 25, Tactics.Alexander)));
	}
}
