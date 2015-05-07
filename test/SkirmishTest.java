import static org.junit.Assert.*;
import org.junit.Test;
import java.util.*;

public class SkirmishTest {
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

	public SkirmishTest() {
		phase = new JudgePhase(new GameSystem());
	}

	@Test
	public void skirmish() {
		assertTrue(phase.isSkirmish(squad(1, 2, 3)));
		assertTrue(phase.isSkirmish(squad(5, 6, 7)));
		assertTrue(phase.isSkirmish(squad(8, 9, 10)));
	}

	@Test
	public void skirmish_leapnumber() {
		assertFalse(phase.isSkirmish(squad(2, 4, 6)));
	}

	@Test
	public void skirmish_shuffle() {
		assertTrue(phase.isSkirmish(squad(5, 4, 6)));
		assertTrue(phase.isSkirmish(squad(3, 2, 1)));
	}

	@Test
	public void skirmish_duplicate() {
		assertFalse(phase.isSkirmish(squad(1, 2, 2)));
		assertFalse(phase.isSkirmish(squad(2, 2, 3)));
		assertFalse(phase.isSkirmish(squad(1, 1, 3)));
		assertFalse(phase.isSkirmish(squad(1, 3, 3)));
		assertFalse(phase.isSkirmish(squad(1, 1, 1)));
	}

	@Test
	public void skirmish_forth() {
		assertTrue(phase.isSkirmish(squad(1, 2, 3, 4)));
		assertTrue(phase.isSkirmish(squad(3, 4, 6, 5)));

		assertFalse(phase.isSkirmish(squad(1, 2, 3, 8)));
		assertFalse(phase.isSkirmish(squad(1, 2, 3, 3)));
	}
}
