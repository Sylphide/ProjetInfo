package javabean;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class DBConnection {
	
    private String sqlQuery ="";
    private Statement query;
    
    public DBConnection() throws ClassNotFoundException, SQLException {
        // TODO Auto-generated constructor stub   	
    	super();	
		Class.forName("com.mysql.jdbc.Driver");
		Connection dbConnection = java.sql.DriverManager.getConnection("jdbc:mysql://localhost:3306/mysql", "root", "chepas");
		query=dbConnection.createStatement();
    }
    
    //Insert a new entry in the table Users from the database localBase
    public ArrayList<String> createNewUser(String firstName, String lastName, String nickName, String password) throws SQLException{
    	ArrayList<String> errors=new ArrayList<String>();
    	sqlQuery="USE localBase;";
    	query.executeUpdate(sqlQuery);
    	
    	//Check if nickname already used
    	sqlQuery="SELECT * FROM Users WHERE nickName='"+nickName+"';";
    	ResultSet rs = query.executeQuery(sqlQuery);
    	
    	//Go inside the loop if rs is not empty == if nickname is already taken
    	while(rs.next()){
    		errors.add("Pseudo deja utilisé");
    	}
    	
    	//Check if there are void entries
    	if(firstName.equals(""))
    		errors.add("Prenom manquant");
    	if(lastName.equals(""))
    		errors.add("Nom manquant");
    	if(nickName.equals(""))
    		errors.add("Pseudo manquant");
    	if(password.equals(""))
    		errors.add("Mot de passe manquant");
    	
    	//Insert new entry only if no errors
    	if(errors.isEmpty())
    	{
	    	sqlQuery="INSERT INTO Users VALUES(null,'"+firstName+"','"+lastName+"','"+nickName+"','"+password+"');";
	    	System.out.println(sqlQuery);
	    	query.executeUpdate(sqlQuery);
    	}
    	return errors;
    		
    }
    
    //Check nickName/password combination
    public boolean checkPasswordCombination(String nickName, String password) throws SQLException{
    	sqlQuery="USE localBase;";
    	query.executeUpdate(sqlQuery);
    	
    	sqlQuery="SELECT * FROM Users WHERE nickName='"+nickName+"';";
    	ResultSet rs = query.executeQuery(sqlQuery);
    	
    	while(rs.next()){
    		if(rs.getString("password").equals(password))
    			return true;
    	}
    	return false;
    		
    }
}
