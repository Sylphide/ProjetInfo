package object;

import java.util.ArrayList;
import object.Card.Rank;
import object.Card.Suit;

public class Deck {

	private ArrayList<Card> Cards;
	
	public Deck() {
		// TODO Auto-generated constructor stub
		Cards=new ArrayList<Card>();
	}
	
	public Deck(int numberOfPlayer){
		Cards=new ArrayList<Card>();
		for(Rank rank: Rank.values())
		{
			for(Suit suit: Suit.values())
			{
				if(numberOfPlayer==3)
				{
					if(!(rank.equals(Rank.SEVEN) && (suit.equals(Suit.CLUBS) || suit.equals(Suit.SPADES))))
					{
						Card card=new Card(rank,suit);
						Cards.add(card);	
					}
				}
				else //For now 2 or 4
				{
					Card card=new Card(rank,suit);
					Cards.add(card);
				}
			}
		}
	}
	
	public void displayCards(){
		for(int i=0; i<Cards.size(); i++)
		{
			Card card = Cards.get(i);
			System.out.println(card.getRank().toString() + card.getSuit().toString());
		}
	}

}
