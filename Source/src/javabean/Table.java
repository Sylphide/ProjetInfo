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
	private ArrayList<Card> board;
	private Deck deck;
	private int currentPlayer;
	private Round currentRound;
	
	
	public Table() {
		// TODO Auto-generated constructor stub
		deck=new Deck();
		players=new ArrayList<Player>();
		board=new ArrayList<Card>();
	}
	
	public void addPlayer(Player player){
		players.add(player);
	}
	
	public void removePlayer(Player player){
		players.remove(player);
	}
	
	public void startGame(){
		deal();
		currentPlayer=0;
		currentRound=Round.PLIS;
	}
	
	public void initializeDeck(){
		deck.initializeDeck(players.size());
	}
	
	public void deal(){
		initializeDeck();
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
	
	public int getCurrentPlayer(){
		return currentPlayer;
	}
	
	public ArrayList<Card> getPlayerHand(int index){
		return players.get(index).getHand();
	}
	
	public boolean playCard(int playerId, Card card){
		if(playerId==currentPlayer)
		{
			Player player=players.get(playerId);
			if(board.isEmpty())
			{
				board.add(card);
				player.playCard(card);
				currentPlayer=(currentPlayer+1)%players.size();
				displayAll();
				return true;
			}
			else
			{
				if(card.getSuit()==board.get(0).getSuit() || !player.gotSuit(board.get(0).getSuit())){
					board.add(card);
					player.playCard(card);
					currentPlayer=(currentPlayer+1)%players.size();
					checkStatus();
					displayAll();
					return true;
				}
				else
					return false;
			}
				
		}
		else
			return false;
	}
	
	public void displayAll(){
		for(Player player: players){
			System.out.println("Player:");
			player.displayHand();
		}
		String string="";
		for(Card card:board){
			string+=card.getRank().toString()+"_"+card.getSuit().toString()+";";
		}
		System.out.println("Board:"+string);
	}
	
	public boolean isBoardStarting(){
		if(board.size()==1){
			return true;
		}
		else
			return false;
	}
	
	public void checkStatus(){
		if(board.size()==players.size()){
			//PAUSE
			//attributePoints();
			board.clear();
//			if(players.get(0).getHand().isEmpty()){
//				currentRound=Round.values()[currentRound.ordinal()+1]; //a checker si la partie est fini (sinon debordement)
//				deal();
//			}
		}
	}
	
	public Deck getDeck(){
		return deck;
	}
}
