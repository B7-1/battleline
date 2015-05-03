class Action {
	final Area selectedFrom;
	final Card card;
	final Flag flag;

	Action(Area area, Card card, Flag flag) {
		this.selectedFrom = area;
		this.card = card;
		this.flag = flag;
	}
}