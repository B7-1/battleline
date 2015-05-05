import static org.junit.Assert.*;
import org.junit.Test;
import java.util.*;

public class JudgePhaseTest {
	UnitCard c(int s, int n) {
		return new UnitCard(s, n);
	}

	List<Card> squad(Card... cards) {
		return Arrays.asList(cards);
	}

	JudgePhase phase;

	public JudgePhaseTest() {
		phase = new JudgePhase(new GameSystem());
	}

	@Test
	public void wedge() {
		assertTrue(phase.isWedge(squad(c(0,1), c(0,2), c(0,3))));
		assertTrue(phase.isWedge(squad(c(0,5), c(0,6), c(0,7))));
		assertTrue(phase.isWedge(squad(c(0,8), c(0,9), c(0,10))));
	}

	@Test
	public void wedge_diffsuit() {
		assertFalse(phase.isWedge(squad(c(0,1), c(1,2), c(1,3))));
		assertFalse(phase.isWedge(squad(c(0,1), c(0,2), c(1,3))));
		assertFalse(phase.isWedge(squad(c(1,1), c(0,2), c(1,3))));
	}

	@Test
	public void wedge_leapnumber() {
		assertFalse(phase.isWedge(squad(c(0,2), c(0,4), c(0,6))));
	}

	@Test
	public void wedge_shuffle() {
		assertTrue(phase.isWedge(squad(c(0,5), c(0,4), c(0,6))));
		assertTrue(phase.isWedge(squad(c(0,3), c(0,2), c(0,1))));
	}
}
