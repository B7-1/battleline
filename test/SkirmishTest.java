import static org.junit.Assert.*;
import org.junit.Test;
import java.util.*;

public class SkirmishTest {
	UnitCard c(int s, int n) {
		return new UnitCard(s, n);
	}

	List<Card> squad(Card... cards) {
		return Arrays.asList(cards);
	}

	JudgePhase phase;

	public SkirmishTest() {
		phase = new JudgePhase(new GameSystem());
	}

	@Test
	public void skirmish() {
		assertTrue(phase.isSkirmish(squad(c(0,1), c(0,2), c(0,3))));
		assertTrue(phase.isSkirmish(squad(c(0,5), c(0,6), c(0,7))));
		assertTrue(phase.isSkirmish(squad(c(0,8), c(0,9), c(0,10))));
	}

	@Test
	public void skirmish_leapnumber() {
		assertFalse(phase.isSkirmish(squad(c(0,2), c(0,4), c(0,6))));
	}

	@Test
	public void skirmish_shuffle() {
		assertTrue(phase.isSkirmish(squad(c(0,5), c(0,4), c(0,6))));
		assertTrue(phase.isSkirmish(squad(c(0,3), c(0,2), c(0,1))));
	}
}
