
public class TacticsCard extends Card {
	final Tactics kind; /* 0-9 */

	TacticsCard(Tactics kind) {
		this.kind = kind;
	}

	@Override
	public String toString() {
		return kind.name();
	}

	Boolean isMoral() {
		return kind == Tactics.Alexander ||
			kind == Tactics.Darius ||
			kind == Tactics.Companion ||
			kind == Tactics.Shield;
	}

	Boolean isEnvironment() {
		return kind == Tactics.Fog ||
			kind == Tactics.Mud;
	}

	Boolean isGuile() {
		return kind == Tactics.Scout ||
			kind == Tactics.Redeploy ||
			kind == Tactics.Deserter ||
			kind == Tactics.Traitor;
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
