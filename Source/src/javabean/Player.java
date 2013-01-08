package javabean;

import java.util.ArrayList;

import object.Card;

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
	
	public Card playCard(Card card){
		hand.remove(card);
		return card;
	}
	
	public ArrayList<Card> getHand(){
		return hand;
	}

}
