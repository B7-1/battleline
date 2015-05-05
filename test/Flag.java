import java.util.*;

class Flag {
	/* @NonNull */ List<List<Card>> cards;
	int forestaller;
	int owner;

	Flag() {
		cards = new ArrayList<List<Card>>();
		cards.add(new ArrayList<Card>());
		cards.add(new ArrayList<Card>());

		owner = -1;
	}

	List<Card> squads(int i) {
		assert 0 <= i && i < cards.size();
		return cards.get(i);
	}
}
