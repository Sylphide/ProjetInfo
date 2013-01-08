package javabean;

import java.util.ArrayList;

public class Lobby {

	private ArrayList<Table> tableList;
	
	public Lobby() {
		tableList=new ArrayList<Table>();
		// TODO Auto-generated constructor stub
	}
	
	public int getNumberOfTables() {
		return tableList.size();
	}
	
	public int addTable() {
		Table newTable=new Table();
		
		//Parametre de la nouvelle table a seter
		
		tableList.add(newTable);
		return tableList.size()-1; //Id of the new table
	}
	
	public void addPlayerAtTable(Player player,int tableId){
		tableList.get(tableId).addPlayer(player);
	}

}
