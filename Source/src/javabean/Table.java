package javabean;

import java.util.ArrayList;
import java.util.Random;

import object.Card;
import object.Card.Suit;
import object.Deck;
import object.Card.Rank;

public class Table {
	
	public enum Round{
		PLIS,COEURS,DAMES,PREMIERDERNIER,BARBU,SALADE,REUSSITE
	}
	
	private ArrayList<Player> players;
	private ArrayList<Card> board;
	private ArrayList<Integer> turnPoints;
	private Deck deck;
	private int currentPlayer;
	private int turnStarter;
	private Round currentRound;
	private boolean started;
	private boolean endTurn;
	
	
	public Table() {
		// TODO Auto-generated constructor stub
		deck=new Deck();
		players=new ArrayList<Player>();
		board=new ArrayList<Card>();
		turnPoints=new ArrayList<Integer>();
		started=false;
		endTurn=false;
	}
	
	public boolean isGameStarted(){
		return started;
	}
	
	public void setStarted(boolean value){
		started=value;
	}
	
	public void addPlayer(Player player){
		player.setPoints(0);
		turnPoints.add(0);
		players.add(player);
	}
	
	public void removePlayer(Player player){
		turnPoints.remove(0);
		players.remove(player);
	}
	
	public void removePlayer(int playerId){
		turnPoints.remove(0);
		players.remove(playerId);
	}
	
	public void startGame(){
		//A FAIRE TEST SUR DEMARRAGE ICI PLUTOT QUE CONTROLLERWEBSOCKET
		deal();
		started=true;
		turnStarter=0;
		currentPlayer=turnStarter;
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
	
	public boolean isEndTurn(){
		return endTurn;
	}
	
	public boolean playCard(int playerId, Card card){
		if(playerId==currentPlayer)
		{
			Player player=players.get(playerId);
			if(board.isEmpty())
			{
				endTurn=false;
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
		System.out.println(currentRound.toString());
		for(Player player: players){
			System.out.println("Player:");
			player.displayHand();
		}
		String string="";
		for(Card card:board){
			string+=card.getRank().toString()+"_"+card.getSuit().toString()+";";
		}
		System.out.println("Board:"+string);
		
		string="";
		for(Integer points:turnPoints){
			string+=points+";";
		}
		System.out.println("Points:"+string);
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
			attributePoints();
			board.clear();
			if(players.get(0).getHandSize()==0){
				if(!currentRound.equals(Round.REUSSITE)){
					currentRound=Round.values()[currentRound.ordinal()+1];
					for(int i=0; i<players.size(); i++){
						players.get(i).addPoints(turnPoints.get(i));
						turnPoints.set(i, 0);
						currentPlayer=0;
						endTurn=true;
						System.out.println("Player "+i+": "+players.get(i).getPoints());
					}
					deal();
				}
				else{
					
				}
			}
		}
	}
	
	public void attributePoints(){
		int turnWinnerId=getTurnWinnerId();
		currentPlayer=turnWinnerId;
		turnStarter=currentPlayer;
		if(currentRound.equals(Round.PLIS)){
			turnPoints.set(turnWinnerId,5+turnPoints.get(turnWinnerId));
		}
		else if(currentRound.equals(Round.COEURS)){
			int heartCount=0;
			for(Card card:board){
				if(card.getSuit().equals(Suit.HEARTS))
					heartCount++;
			}
			turnPoints.set(turnWinnerId,5*heartCount+turnPoints.get(turnWinnerId));
		}
		else if(currentRound.equals(Round.DAMES)){
			int queenCount=0;
			for(Card card:board){
				if(card.getRank().equals(Rank.QUEEN))
					queenCount++;
			}
			turnPoints.set(turnWinnerId,10*queenCount+turnPoints.get(turnWinnerId));
		}
		else if(currentRound.equals(Round.PREMIERDERNIER)){
			int numberOfPlayers=players.size();
			int cardsLeft=players.get(0).getHandSize()*numberOfPlayers;
			if((cardsLeft==30-numberOfPlayers && (numberOfPlayers==5 || numberOfPlayers==3)) || cardsLeft==32-numberOfPlayers || cardsLeft==0){
				turnPoints.set(turnWinnerId, 20+turnPoints.get(turnWinnerId));
			}
		}
		else if(currentRound.equals(Round.BARBU)){
			for(Card card:board){
				if(card.getRank().equals(Rank.KING) && card.getSuit().equals(Suit.HEARTS))
					turnPoints.set(turnWinnerId, 40+turnPoints.get(turnWinnerId));
			}
		}
		else if(currentRound.equals(Round.SALADE)){
			int points=5; //pli
			int heartCount=0;
			int queenCount=0;
			for(Card card:board){
				
				if(card.getSuit().equals(Suit.HEARTS))
					heartCount++;
				
				if(card.getRank().equals(Rank.QUEEN))
					queenCount++;
				
				int numberOfPlayers=players.size();
				int cardsLeft=players.get(0).getHandSize()*numberOfPlayers;
				if((cardsLeft==30-numberOfPlayers && (numberOfPlayers==5 || numberOfPlayers==3)) || cardsLeft==32-numberOfPlayers || cardsLeft==0)
					points+=20;
				
				if(card.getRank().equals(Rank.KING) && card.getSuit().equals(Suit.HEARTS))
					points+=40;
				
			}
			points+=heartCount*5;
			points+=queenCount*10;
			turnPoints.set(turnWinnerId, points+turnPoints.get(turnWinnerId));
		}
		//A FAIRE REUSSITE
	}
	
	public int getTurnWinnerId(){
		//Qui a commence? turnStarter?
		Rank bestCard=Rank.SEVEN;
		int turnWinnerId=0;
		for(int i=0; i<board.size(); i++){
			if(board.get(i).getSuit()==board.get(0).getSuit()){
				if(board.get(i).getRank().ordinal()>bestCard.ordinal()){
					turnWinnerId=i;
					bestCard=board.get(i).getRank();
				}
			}
		}
		turnWinnerId=(turnStarter+turnWinnerId)%players.size();
		return turnWinnerId;
	}
	
	public Deck getDeck(){
		return deck;
	}
}
