import static org.junit.Assert.*;
import org.junit.*;
import java.util.*;

public class BattalionTest {
	UnitCard c(int s, int n) {
		return new UnitCard(s, n);
	}

	List<UnitCard> squad(UnitCard... cards) {
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
}
