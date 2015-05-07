import static org.junit.Assert.assertEquals;
import org.junit.Test;
import java.util.*;

public class JudgePhaseTest {
	static final int SkirmishBonus = 10000;
	static final int BattalionBonus = 20000;
	static final int PhalanxBonus = 25000;
	static final int WedgeBonus = SkirmishBonus + BattalionBonus;

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
	public void strengthOfSkirmish() {
		assertEquals(SkirmishBonus + 60, phase.strengthOfCards(squad(11, 22, 33)));
		assertEquals(SkirmishBonus + 270, phase.strengthOfCards(squad(19, 28, 20)));
	}

	@Test
	public void strengthOfBattalion() {
		assertEquals(BattalionBonus + 120, phase.strengthOfCards(squad(11, 14, 17)));
		assertEquals(BattalionBonus + 80, phase.strengthOfCards(squad(21, 22, 25)));
	}

	@Test
	public void strengthOfPhalanx() {
		assertEquals(PhalanxBonus + 30, phase.strengthOfCards(squad(11, 21, 31)));
		assertEquals(PhalanxBonus + 210, phase.strengthOfCards(squad(37, 47, 17)));
	}

	@Test
	public void strengthOfWedge() {
		assertEquals(WedgeBonus + 60, phase.strengthOfCards(squad(11, 12, 13)));
		assertEquals(WedgeBonus + 180, phase.strengthOfCards(squad(25, 26, 27)));
	}

	@Test
	public void strengthOfLeader() {
		assertEquals("Host", 150, phase.strengthOfCards(squad(11, 24, Tactics.Alexander)));
		assertEquals("Skirmish", SkirmishBonus + 150, phase.strengthOfCards(squad(14, 26, Tactics.Darius)));
		assertEquals("Battalion", BattalionBonus + 170, phase.strengthOfCards(squad(21, 26, Tactics.Alexander)));
		assertEquals("Phalanx", PhalanxBonus + 120, phase.strengthOfCards(squad(24, 34, Tactics.Darius)));
		assertEquals("Wedge", WedgeBonus + 150, phase.strengthOfCards(squad(24, 25, Tactics.Alexander)));
	}

	@Test
	public void strengthOfShield() {
		assertEquals("Host", 140, phase.strengthOfCards(squad(18, 23, Tactics.Shield)));
		assertEquals("Skirmish", SkirmishBonus + 90, phase.strengthOfCards(squad(14, 23, Tactics.Shield)));
		assertEquals("Battalion", BattalionBonus + 100, phase.strengthOfCards(squad(21, 26, Tactics.Shield)));
		assertEquals("Phalanx", PhalanxBonus + 90, phase.strengthOfCards(squad(23, 33, Tactics.Shield)));
		assertEquals("Wedge", WedgeBonus + 90, phase.strengthOfCards(squad(22, 24, Tactics.Shield)));	
	}

	@Test
	public void strengthOfCompanion() {
		assertEquals("Host", 190, phase.strengthOfCards(squad(18, 23, Tactics.Companion)));
		assertEquals("Skirmish", SkirmishBonus + 210, phase.strengthOfCards(squad(17, 26, Tactics.Companion)));
		assertEquals("Battalion", BattalionBonus + 150, phase.strengthOfCards(squad(21, 26, Tactics.Companion)));
		assertEquals("Phalanx", PhalanxBonus + 240, phase.strengthOfCards(squad(28, 38, Tactics.Companion)));
		assertEquals("Wedge", WedgeBonus + 270, phase.strengthOfCards(squad(29, 20, Tactics.Companion)));
	}

	@Test
	public void strengthOfMoralTactics() {
		assertEquals("SLC", BattalionBonus + 210, phase.strengthOfCards(squad(Tactics.Alexander, Tactics.Shield, Tactics.Companion)));
		assertEquals("xLC", WedgeBonus + 210, phase.strengthOfCards(squad(Tactics.Darius, Tactics.Companion, 16)));
		assertEquals("xLC", PhalanxBonus + 240, phase.strengthOfCards(squad(Tactics.Darius, Tactics.Companion, 18)));
		assertEquals("xLC", BattalionBonus + 200, phase.strengthOfCards(squad(Tactics.Darius, Tactics.Companion, 12)));
		assertEquals("SxC", BattalionBonus + 210, phase.strengthOfCards(squad(Tactics.Shield, Tactics.Companion, 20)));
		assertEquals("SLx", WedgeBonus + 90, phase.strengthOfCards(squad(Tactics.Darius, Tactics.Shield, 12)));
		assertEquals("SLx", BattalionBonus + 200, phase.strengthOfCards(squad(Tactics.Darius, Tactics.Shield, 17)));
	}
}
