package javabean;

public class UserInfo {
	private String nickName;
	private int currentTable;
	
	public UserInfo(){
		
	}
	
	public String getNickName(){
		return nickName;
	}
	
	public void setNickName(String value){
		nickName=value;
	}
	
	public int getCurrentTable(){
		return currentTable;
	}
	
	public void setCurrentTable(int value){
		currentTable=value;
	}
}
