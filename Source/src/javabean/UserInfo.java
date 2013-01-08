package javabean;

public class UserInfo {
	protected String nickName;
	protected int currentTable;
	protected String role;
	
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
	
	public String getRole(){
		return role;
	}
	
	public void setRole(String value){
		role=value;
	}
}
