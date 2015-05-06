import static org.junit.Assert.*;
import org.junit.*;
import java.util.*;

public class BattalionTest {
	UnitCard c(int s, int n) {
		return new UnitCard(s, n);
	}
	TacticsCard t(Tactics tac) {
		return new TacticsCard(tac);
	}

	List<Card> squad(Card... cards) {
		return Arrays.asList(cards);
	}

	JudgePhase phase;

	public BattalionTest() {
		phase = new JudgePhase(new GameSystem());
	}

	@Test
	public void battalion() {
		assertTrue(phase.isBattalion(squad(c(0,1), c(0, 2), c(0, 3))));
		assertTrue(phase.isBattalion(squad(c(1,1), c(1, 3), c(1, 6))));
	}

	@Test
	public void battalion_diffsuit() {
		assertFalse(phase.isBattalion(squad(c(0,1), c(1, 1), c(0, 2))));
		assertFalse(phase.isBattalion(squad(c(0,1), c(1, 1), c(2, 1))));
	}

	@Test
	public void battalion_leader() {
		assertTrue(phase.isBattalion(squad(c(0, 1), c(0, 2), t(Tactics.Alexander))));
		assertTrue(phase.isBattalion(squad(c(0, 8), c(0, 1), t(Tactics.Darius))));

		assertFalse(phase.isBattalion(squad(c(0, 1), c(3, 1), t(Tactics.Alexander))));
		assertFalse(phase.isBattalion(squad(c(0, 1), c(2, 2), t(Tactics.Darius))));
	}

	@Test
	public void battalion_cavalry() {
		assertTrue(phase.isBattalion(squad(c(0, 1), c(0, 2), t(Tactics.Companion))));
		assertTrue(phase.isBattalion(squad(c(0, 7), c(0, 8), t(Tactics.Companion))));

		assertFalse(phase.isBattalion(squad(c(0, 1), c(1, 1), t(Tactics.Companion))));
		assertFalse(phase.isBattalion(squad(c(0, 8), c(1, 8), t(Tactics.Companion))));
	}

	@Test
	public void battalion_shield() {
		assertTrue(phase.isBattalion(squad(c(0, 1), c(0, 2), t(Tactics.Shield))));
		assertTrue(phase.isBattalion(squad(c(0, 7), c(0, 3), t(Tactics.Shield))));

		assertFalse(phase.isBattalion(squad(c(0, 1), c(1, 2), t(Tactics.Shield))));
		assertFalse(phase.isBattalion(squad(c(1, 3), c(0, 3), t(Tactics.Shield))));
	}

	@Test
	public void battalion_moral() {
		assertTrue(phase.isBattalion(squad(c(0, 1), t(Tactics.Alexander), t(Tactics.Companion))));
		assertTrue(phase.isBattalion(squad(c(1, 1), t(Tactics.Darius), t(Tactics.Shield))));
		assertTrue(phase.isBattalion(squad(c(2, 1), t(Tactics.Shield), t(Tactics.Companion))));
		assertTrue(phase.isBattalion(squad(t(Tactics.Shield), t(Tactics.Alexander), t(Tactics.Companion))));
	}
}
