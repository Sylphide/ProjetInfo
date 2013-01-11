package javabean;

import java.util.ArrayList;

public class Lobby {

	private ArrayList<Table> tableList;
	private int tableIdCount;
	private ArrayList<Integer> realTableIds;
	
	public Lobby() {
		tableList=new ArrayList<Table>();
		tableIdCount=-1;
		realTableIds=new ArrayList<Integer>();
		// TODO Auto-generated constructor stub
	}
	
	public ArrayList<Integer> getRealTablesIds(){
		return realTableIds;
	}
	
	public int getNumberOfTables() {
		return tableList.size();
	}
	
	public int getCurrentTableIdCount(){
		return tableIdCount;
	}
	
	public int addTable() {
		tableIdCount++;
		realTableIds.add(tableIdCount);
		Table newTable=new Table();
		
		//Parametre de la nouvelle table a seter
		
		tableList.add(newTable);
		return tableIdCount; //Id of the new table
	}
	
	public void removeTable(int tableId){
		int realId=realTableIds.indexOf(tableId);
		realTableIds.remove(realId);
		tableList.remove(realId);
	}
	
	public Table getTable(int tableId){
		int realId=realTableIds.indexOf(tableId);
		return tableList.get(realId);
	}

}
