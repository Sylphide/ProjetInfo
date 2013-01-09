package javabean;

import java.util.ArrayList;
import java.util.Random;

import object.Card;
import object.Deck;

public class Table {
	
	public enum Round{
		PLIS,COEURS,DAMES,PREMIERDERNIER,BARBU,SALADE,REUSSITE
	}
	
	private ArrayList<Player> players;
	private Deck deck;
	private int currentPlayer;
	private Round currentRound;
	
	
	public Table() {
		// TODO Auto-generated constructor stub
		deck=new Deck();
		players=new ArrayList<Player>();
	}
	
	public void addPlayer(Player player){
		players.add(player);
	}
	
	public void removePlayer(Player player){
		players.remove(player);
	}
	
	public void startGame(){
		initializeDeck();
		deal();
		currentPlayer=0;
		currentRound=Round.PLIS;
	}
	
	public void initializeDeck(){
		deck.initializeDeck(players.size());
	}
	
	public void deal(){
		while(!deck.isEmpty())
		{
			Random random=new Random();
			int rand=random.nextInt(deck.deckSize());
			Card card=deck.removeCard(rand);
			players.get(currentPlayer).getCard(card);
			currentPlayer=(currentPlayer+1)%players.size();
		}
	}
	
	public int getNumberOfPlayer(){
		return players.size();
	}
	
	public ArrayList<Card> getPlayerHand(int index){
		return players.get(index).getHand();
	}
	
	public Deck getDeck(){
		return deck;
	}
}
