package object;

import java.util.ArrayList;
import object.Card.Rank;
import object.Card.Suit;

public class Deck {

	private ArrayList<Card> cards;
	
	public Deck() {
		// TODO Auto-generated constructor stub
		cards=new ArrayList<Card>();
	}
	
	public void initializeDeck(int numberOfPlayer){
		cards=new ArrayList<Card>();
		for(Rank rank: Rank.values())
		{
			for(Suit suit: Suit.values())
			{
				if(numberOfPlayer==3)
				{
					if(!(rank.equals(Rank.SEVEN) && (suit.equals(Suit.CLUBS) || suit.equals(Suit.SPADES))))
					{
						Card card=new Card(rank,suit);
						cards.add(card);	
					}
				}
				else //For now 2 or 4
				{
					Card card=new Card(rank,suit);
					cards.add(card);
				}
			}
		}
	}
	
	public boolean isEmpty(){
		return cards.isEmpty();
	}
	
	public Card removeCard(int index){
		Card card=cards.get(index);
		cards.remove(index);
		return card;
	}
	
	public int deckSize(){
		return cards.size();
	}
	
	public void displayCards(){
		for(int i=0; i<cards.size(); i++)
		{
			Card card = cards.get(i);
			System.out.println(card.getRank().toString() + card.getSuit().toString());
		}
	}

}
