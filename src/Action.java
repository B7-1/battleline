import java.util.Optional;

class Action {
	final Optional<Area> selectedFrom;
	final Optional<Card> card;
	final Optional<Flag> flag;

	Action(Area area, Card card, Flag flag) {
		this.selectedFrom = Optional.ofNullable(area);
		this.card = Optional.ofNullable(card);
		this.flag = Optional.ofNullable(flag);
	}
}
