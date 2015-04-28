
public class UnitCard extends Card {
	final int suit; /* 0-5 */
	final int number; /* 1-10 */

	UnitCard(int suit, int number) {
		assert 0 <= suit && suit <= 5;
		assert 1 <= number && number <= 10;

		this.suit = suit;
		this.number = number;
	}

	@Override
	public String toString() {
		return "" + suit + number % 10;
	}
}
