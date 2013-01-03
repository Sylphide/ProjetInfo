package object;

public class Card {
	
	public enum Suit{
		SPADES,CLUBS,HEARTS,DIAMONDS
	}
	
	public enum Rank{
		SEVEN,EIGHT,NINE,TEN,JACK,QUEEN,KING,ACE
	}
	
	private Rank rank;
	private Suit suit;
	
	public Card() {
		// TODO Auto-generated constructor stub
	}
	
	public Card(Rank rankValue, Suit suitValue) {
		rank=rankValue;
		suit=suitValue;
	}
	
	public void setRank(Rank value){
		rank=value;
	}
	
	public Rank getRank(){
		return rank;
	}
	
	public void setSuit(Suit value){
		suit=value;
	}
	
	public Suit getSuit(){
		return suit;
	}

}
