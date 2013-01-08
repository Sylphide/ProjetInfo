package javabean;

import java.util.ArrayList;

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
			int rand=0;
			Card card=deck.removeCard(rand);
			players.get(currentPlayer).getCard(card);
			currentPlayer=(currentPlayer+1)%players.size();
		}
	}
	
	public Deck getDeck(){
		return deck;
	}
}
