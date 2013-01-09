package javabean;

import java.util.ArrayList;

import object.Card;
import object.Card.Suit;

public class Player extends UserInfo{

	private ArrayList<Card> hand;
	
	public Player() {
		// TODO Auto-generated constructor stub
		hand=new ArrayList<Card>();
	}
	
	public Player(UserInfo user) {
		// TODO Auto-generated constructor stub
		hand=new ArrayList<Card>();
		nickName=user.getNickName();
		currentTable=user.getCurrentTable();
		role=user.getRole();
	}
	
	public void getCard(Card card){
		hand.add(card);
	}
	
	public Card playCard(Card oldCard){
		for(Card card:hand){
			if(card.getRank()==oldCard.getRank() && card.getSuit()==oldCard.getSuit()){
				hand.remove(card);
				return card;
			}
		}	
		return null;
	}
	
	public boolean gotSuit(Suit suit){
		for(Card card:hand){
			System.out.println(card.getSuit().toString()+","+suit.toString());
			if(card.getSuit()==suit)
				return true;	
		}
		return false;
	}
	
	public ArrayList<Card> getHand(){
		return hand;
	}
	
	public void displayHand(){
		String string="";
		for(Card card:hand){
			string+=card.getRank().toString()+"_"+card.getSuit().toString()+";";
		}
		System.out.println(string);
	}

}
