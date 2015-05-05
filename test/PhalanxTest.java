import static org.junit.Assert.*;
import org.junit.*;
import java.util.*;

public class PhalanxTest {
	UnitCard c(int s, int n) {
		return new UnitCard(s, n);
	}

	List<Card> squad(Card... cards) {
		return Arrays.asList(cards);
	}

	JudgePhase phase;
	public PhalanxTest() {
		phase = new JudgePhase(new GameSystem());
	}

	@Test
	public void phalanx() {
		assertTrue(phase.isPhalanx(squad(c(1,1),  c(2,1),  c(3,1))));
		assertTrue(phase.isPhalanx(squad(c(0,10), c(2,10), c(5,10))));
	}

	@Test
	public void phalanx_diffnum() {
		assertFalse(phase.isPhalanx(squad(c(1,1),  c(2,2),  c(3,3))));
		assertFalse(phase.isPhalanx(squad(c(1,1), c(1,2), c(1,3))));
	}
}