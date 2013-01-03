package javabean;

import java.util.ArrayList;

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
		currentPlayer=0;
		currentRound=Round.PLIS;
	}
	
	public void initializeDeck(){
		deck=new Deck(players.size());
	}
	
	public Deck getDeck(){
		return deck;
	}
}
