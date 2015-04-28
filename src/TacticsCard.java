
public class TacticsCard extends Card {
	final Tactics kind; /* 0-9 */

	TacticsCard(Tactics kind) {
		this.kind = kind;
	}

	@Override
	public String toString() {
		return kind.name();
	}
}

enum Tactics {
	Alexander,
	Darius,
	Companion,
	Shield,
	Fog,
	Mud,
	Scout,
	Redeploy,
	Deserter,
	Traitor
}
