import static org.junit.Assert.*;
import org.junit.*;
import java.util.*;

public class PhalanxTest {
	List<UnitCard> squad(Integer... cards) {
		List<UnitCard> squad = new ArrayList<UnitCard>();

		for (Integer i : cards) {
			int suit = i / 10;
			int number = (i % 10) > 0 ? (i % 10) : 10;
			squad.add(new UnitCard(suit, number));
		}

		return squad;
	}

	JudgePhase phase;
	public PhalanxTest() {
		phase = new JudgePhase(new GameSystem());
	}

	@Test
	public void phalanx() {
		assertTrue(phase.isPhalanx(squad(11, 21, 31)));
		assertTrue(phase.isPhalanx(squad(00, 20, 50)));
	}

	@Test
	public void phalanx_diffnum() {
		assertFalse(phase.isPhalanx(squad(11, 22, 33)));
		assertFalse(phase.isPhalanx(squad(11, 12, 13)));
	}

	@Test
	public void phalanx_dup() {
		assertTrue(phase.isPhalanx(squad(11, 11, 21)));
		assertTrue(phase.isPhalanx(squad(33, 33, 33)));

		assertFalse(phase.isPhalanx(squad(11, 11, 12)));
	}

	@Test
	public void phalanx_forth() {
		assertTrue(phase.isPhalanx(squad(11, 11, 11, 21)));
		assertFalse(phase.isPhalanx(squad(11, 11, 22, 21)));
	}
}